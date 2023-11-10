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
        //creates new channel to read content from url connection
        try(ReadableByteChannel rbch = Channels.newChannel(url.openStream());
            //variable pointing to save directory of downloaded file
            FileOutputStream fos = new FileOutputStream(saveDir)) {
            //transfers bytes into this channel's file from the given readable byte channel
            //parametres are: src – the source channel, position = the file position at which the transfer is to begin
            //count – the maximum number of bytes to be transferred
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
        //writes data in bytes using another OutputStream
        try(BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
            byte[] bytesIn = new byte[4096];
            int read;
            //until there are bytes
            while((read = zipInput.read(bytesIn)) != -1) {
                //writes given length of bytes from the specified byte array starting at given offset to the buffered output stream
                bos.write(bytesIn, 0, read);
            }
        }
    }
}
