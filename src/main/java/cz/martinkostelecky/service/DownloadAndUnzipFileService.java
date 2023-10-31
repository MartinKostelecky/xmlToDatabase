package cz.martinkostelecky.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * service class providing download,
 * unyip and extract logic
 */
@Service
public class DownloadAndUnzipFileService {
    /**
     * @param fileURL url of zipped xml file on the internet
     * @param saveDir directory where zipped xml file is downloaded
     * @throws IOException
     */
    public void downloadZippedFile(String fileURL, String saveDir) throws IOException {
        URL url = new URL(fileURL);
        try(ReadableByteChannel rbch = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream(saveDir)) {
            fos.getChannel().transferFrom(rbch, 0, Long.MAX_VALUE);
        }
    }
    /**
     * @param zipFilePath directory where zipped file is located
     * @param destDirectory directory where unzipped file is saved
     * @throws IOException
     */
    public File unzip(String zipFilePath, String destDirectory) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        try(ZipInputStream zipInput = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry = zipInput.getNextEntry();
            while(entry != null) {
                String filePath = destDirectory + File.separator + entry.getName();
                if(!entry.isDirectory()) {
                    extractFile(zipInput, filePath);
                } else {
                    File dir = new File(filePath);
                    dir.mkdir();
                }
                zipInput.closeEntry();
                entry = zipInput.getNextEntry();
            }
        }
        return destDir;
    }
    /**
     * extracts data from file using array of bytes as a buffer
     * @param zipInput ZipInputStream containing the file to be extracted.
     * @param filePath directory where the extracted file will be saved.
     * @throws IOException
     */
    public static void extractFile(ZipInputStream zipInput, String filePath) throws IOException {
        try(BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
            byte[] bytesIn = new byte[4096];
            int read;
            while((read = zipInput.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
        }
    }
}
