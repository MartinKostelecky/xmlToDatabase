package cz.martinkostelecky.service;

import cz.martinkostelecky.model.CityEntity;
import cz.martinkostelecky.model.CityPartEntity;
import cz.martinkostelecky.repository.CityPartRepository;
import cz.martinkostelecky.repository.CityRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * service class providing xml parsing method
 * and save to database method for both City and City parts
 */
@Service
public class xmlToDatabase {
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private CityPartRepository cityPartRepository;
    @Autowired
    private DownloadAndUnzipFileService downloadAndUnzipFileService;
    /**
     * initializes and processes download, unzip, parsing and saving City and City parts in database
     * @param fileURL url of zipped xml file on the internet
     * @param saveDir directory where zipped xml file is downloaded
     */
    @Transactional
    public void processAndSaveCitiesAndCityParts(String fileURL, String saveDir) {
        try {
            downloadAndUnzipFileService.downloadZippedFile(fileURL, saveDir);
            String resourcesPath = new File("src/main/resources").getAbsolutePath();
            File xmlFile = downloadAndUnzipFileService.unzip(saveDir, resourcesPath);

            List<CityEntity> parsedCities = parseXML(xmlFile);
            cityRepository.saveAll(parsedCities);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * parse given xml file
     * @param xmlFile xml file to parse
     * @return list of cities
     */
    private List<CityEntity> parseXML(File xmlFile) {
        List<CityEntity> cities = new ArrayList<>();

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();

            // Use getClass().getResourceAsStream() to get the InputStream of the XML file in resources directory
            InputStream inputStream = getClass().getResourceAsStream("/20210331_OB_573060_UZSZ.xml");

            // Use InputStreamReader to specify the encoding (UTF-8 in this case)
            Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            InputSource is = new InputSource(reader);
            is.setEncoding("UTF-8");

            Document doc = docBuilder.parse(is);
            doc.getDocumentElement().normalize();

            NodeList cityNodes = doc.getElementsByTagName("vf:Obec");
            for (int i = 0; i < cityNodes.getLength(); i++) {
                Node cityNode = cityNodes.item(i);

                if (cityNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element cityElement = (Element) cityNode;

                    int cityCode = Integer.parseInt(cityElement.getElementsByTagName("obi:Kod").item(0).getTextContent());
                    String cityName = cityElement.getElementsByTagName("obi:Nazev").item(0).getTextContent();

                    CityEntity city = new CityEntity();
                    city.setCode(cityCode);
                    city.setName(cityName);

                    // For CityParts
                    List<CityPartEntity> cityParts = new ArrayList<>();
                    NodeList cityPartNodes = doc.getElementsByTagName("vf:CastObce");
                    for (int j = 0; j < cityPartNodes.getLength(); j++) {
                        Node cityPartNode = cityPartNodes.item(j);

                        if (cityPartNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element cityPartElement = (Element) cityPartNode;

                            int cityPartCode = Integer.parseInt(cityPartElement.getElementsByTagName("coi:Kod").item(0).getTextContent());
                            int cityCodeOfCityPart = Integer.parseInt(cityPartElement.getElementsByTagName("obi:Kod").item(0).getTextContent());
                            String cityPartName = cityPartElement.getElementsByTagName("coi:Nazev").item(0).getTextContent();

                            CityPartEntity cityPart = new CityPartEntity();
                            cityPart.setCode(cityPartCode);
                            cityPart.setName(cityPartName);
                            cityPart.setCityCode(cityCodeOfCityPart);
                            cityPart.setCity(city);
                            cityParts.add(cityPart);
                        }
                    }
                    city.setCityParts(cityParts);
                    cities.add(city);
                }
            }
            return cities;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }
}