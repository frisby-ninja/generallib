package net.ninjaworks.generallib;

import net.ninjaworks.generallib.util.Logger;
import org.apache.commons.io.IOUtils;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

/**
 * A simple web utility class.
 */
public class NetUtils {

    /**
     * Opens the given url in the user's default browser.
     * @param url The url to open
     */
    public static void openURLInBrowser(URL url) {
        OS os = OS.determine();
        if (os.equals(OS.WINDOWS) && Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(url.toURI());
            } catch (Exception e) {
                Logger.GENERAL_LIB_LOGGER.printException(e);
            }
        } else if (os.equals(OS.MACOS)) {
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(new String[]{"open", url.toString()});
            } catch (Exception e) {
                Logger.GENERAL_LIB_LOGGER.printException(e);
            }
        } else if (os.equals(OS.LINUX)) {
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(new String[]{"xdg-open", url.toString()});
            } catch (Exception e) {
                Logger.GENERAL_LIB_LOGGER.printException(e);
            }
        }
    }

    /**
     * Parses any data found on the given webpage.
     * @param url The url to get the data from
     * @return The parsed data, null if it fails
     */
    public static String getDataFromWebpage(URL url) {
        String content = null;
        try {
            content = IOUtils.toString(url.openStream(), Charset.defaultCharset());
        } catch (Exception e) {
            Logger.GENERAL_LIB_LOGGER.printException(e);
        }
        return content;
    }

    /**
     * Downloads a file from a given url, but doesn't put it into the destination folder
     * until it's finished.
     * @param url The url of the file to download
     * @param pathToWriteTo The path to which the file should be written to
     * @param fileName The outputted file's name
     * @return The file downloaded, or null if it fails
     */
    public static File downloadFileFromURLWithTMP(URL url, Path pathToWriteTo, String fileName) {
        File file = null;
        Path fullPath = Path.of(pathToWriteTo.toString() + "\\" +
                fileName);
        String folderName = "goatclient-" + System.nanoTime();
        Path tempPath = Path.of(System.getProperty("java.io.tmpdir")
                .replace("\\", "/") + "/" + folderName +
                "/" + fileName);
        FileOutputStream fos = null;
        try {
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            file = tempPath.toFile();
            file.mkdirs();
            file.delete();
            Files.createFile(file.toPath());
            fos = new FileOutputStream(file);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (Exception e) {
            Logger.GENERAL_LIB_LOGGER.printException(e);
        }
        try {
            if(fos != null) {
                fos.close();
            }
            Files.copy(tempPath, fullPath);
            if (file != null) {
                boolean bl = file.delete();
                if(!bl) {
                    Logger.GENERAL_LIB_LOGGER.error("Could not delete file : " + file);
                }
            }
            File dir = new File(System.getProperty("java.io.tmpdir").replace("\\", "/")
                    + "/" + folderName);
            if(dir.isDirectory()) {
                boolean bl = dir.delete();
                if (!bl) {
                    Logger.GENERAL_LIB_LOGGER.error("Could not delete directory : " + dir);
                }
            }
        } catch (Exception e) {
            Logger.GENERAL_LIB_LOGGER.printException(e);
        }
        return file;
    }

    /**
     * Downloads a file from a given url, but doesn't put it into the destination folder
     * until it's finished.
     * @param url The url of the file to download
     * @param pathToWriteTo The path to which the file should be written to
     * @return The file downloaded, or null if it fails
     */
    public static File downloadFileFromURLWithTMP(URL url, Path pathToWriteTo) {
        File file = null;
        String urlString = url.toString();
        //Split the url with regex "/" to later get the last 'part' of the url, example : "test.txt"
        String[] splitUrlString = urlString.split("/");
        String fileName = splitUrlString[splitUrlString.length - 1];
        Path fullPath = Path.of(pathToWriteTo.toString() + "\\" +
                fileName);
        String folderName = "goatclient-" + System.nanoTime();
        Path tempPath = Path.of(System.getProperty("java.io.tmpdir")
                .replace("\\", "/") + "/" + folderName +
                "/" + fileName);
        FileOutputStream fos = null;
        try {
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            file = tempPath.toFile();
            file.mkdirs();
            file.delete();
            Files.createFile(file.toPath());
            fos = new FileOutputStream(file);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (Exception e) {
            Logger.GENERAL_LIB_LOGGER.printException(e);
        }
        try {
            if(fos != null) {
                fos.close();
            }
            try {
                File newFile = fullPath.toFile();
                newFile.mkdirs();
                newFile.delete();
                Files.copy(tempPath, fullPath);
            } catch (NoSuchFileException e) {
                Logger.GENERAL_LIB_LOGGER.printException(e);
            }
            if (file != null) {
                boolean bl = file.delete();
                if(!bl) {
                    Logger.GENERAL_LIB_LOGGER.error("Could not delete file : " + file);
                }
            }
            File dir = new File(System.getProperty("java.io.tmpdir").replace("\\", "/")
                    + "/" + folderName);
            if(dir.isDirectory()) {
                boolean bl = dir.delete();
                if (!bl) {
                    Logger.GENERAL_LIB_LOGGER.error("Could not delete directory : " + dir);
                }
            }
        } catch (IOException e) {
            Logger.GENERAL_LIB_LOGGER.printException(e);
        }
        return file;
    }

    /**
     * Downloads a file from a given url.
     * @param url The url of the file to download
     * @param pathToWriteTo The path to which the file should be written to
     * @param fileName The outputted file's name
     * @return The file downloaded, or null if it fails
     */
    public static File downloadFileFromURL(URL url, Path pathToWriteTo, String fileName) {
        File file = null;
        String urlString = url.toString();
        //Split the url with regex "/" to later get the last 'part' of the url, example : "test.txt"
        String[] splitUrlString = urlString.split("/");
        Path fullPath = Path.of(pathToWriteTo.toString() + "\\" +
                fileName);
        try {
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            file = fullPath.toFile();
            file.mkdirs();
            file.delete();
            Files.createFile(file.toPath());
            FileOutputStream fos = new FileOutputStream(fullPath.toFile());
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (Exception e) {
            Logger.GENERAL_LIB_LOGGER.printException(e);
        }
        return file;
    }

    /**
     * Downloads a file from a given url.
     * @param url The url of the file to download
     * @param pathToWriteTo The path to which the file should be written to
     * @return The file downloaded, or null if it fails
     */
    public static File downloadFileFromURL(URL url, Path pathToWriteTo) {
        File file = null;
        String urlString = url.toString();
        //Split the url with regex "/" to later get the last 'part' of the url, example : "test.txt"
        String[] splitUrlString = urlString.split("/");
        Path fullPath = Path.of(pathToWriteTo.toString() + "\\" + splitUrlString[splitUrlString.length - 1]);
        try {
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            file = new File(pathToWriteTo.toString().replace("\\", "/"));
            FileOutputStream fos = new FileOutputStream(fullPath.toFile());
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (Exception e) {
            Logger.GENERAL_LIB_LOGGER.printException(e);
        }
        return file;
    }
}