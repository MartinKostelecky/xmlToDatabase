package cz.martinkostelecky.controller;

import cz.martinkostelecky.service.xmlToDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * controller class provides endpoint for processing and managing xml information
 */
@RestController
@RequestMapping("/api/cities")
public class CityController {
    @Autowired
    private xmlToDatabase xmlToDatabase;

    /**
     * @param fileURL url of zipped xml file on the internet
     * @param saveDir directory where zipped xml file is downloaded
     */
    @PostMapping("/process")
    public void processCitiesAndCityParts(
            @Value("${city.fileURL}") String fileURL,
            @Value("${city.saveDir}") String saveDir) {
        xmlToDatabase.processAndSaveCitiesAndCityParts(fileURL, saveDir);
    }
}

