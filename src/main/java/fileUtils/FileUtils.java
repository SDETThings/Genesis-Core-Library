package fileUtils;

import LogUtils.LibraryLoggingUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.Zip;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/*
 * FileUtils class contains the methods for performing file related operations
 */
public class FileUtils {

    LibraryLoggingUtils libraryLoggingUtils;
    String className = Thread.currentThread().getStackTrace()[1].getClassName();
    ThreadLocal<WebDriver> tl;
    /**
     * writeFileToLocation method is used to write a file to a specific location.
     * @param filePathToBeWritten Actual existing file path which is to be written in another folder.
     * @param destinationFolderPath folder in which existing file is to be written into.
     * @return newlyWrittenFilePath This is the path of the newly written file ( if written successfully else returns null).
     */
    public synchronized String writeFileToLocation(String filePathToBeWritten,String destinationFolderPath)
    {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        libraryLoggingUtils = new LibraryLoggingUtils();
        String newlyWrittenFilePath = null;
        try
        {
            Path destinationFolder = Paths.get(destinationFolderPath);
            Path existingFile = Paths.get(filePathToBeWritten);
            Path destinationFilePath = destinationFolder.resolve(existingFile.getFileName());
            Files.copy(existingFile,destinationFilePath);
            File file = new File(String.valueOf(destinationFilePath));
            if(file.exists())
            {
                newlyWrittenFilePath = String.valueOf(destinationFilePath);
            }
            else
            {
                newlyWrittenFilePath =null;
            }
        }
        catch (IOException e)
        {
            libraryLoggingUtils.getCurrentMethodException(className, methodName, e);
        }
        return newlyWrittenFilePath;
    }
    /**
     * findFileFromNestedFolderStructure method is used to find and return the path of the file in nested folder structure.
     * @param parentFolderToStartSearch Folder path from where the method begins the search.
     * @param fileName Name of the actual file ( may or may not include the extension ).
     * @param extension Name of the extension .
     * @return filePath This is the path of the searched file ( if present else returns null).
     */
    public synchronized String findFileFromNestedFolderStructure(File parentFolderToStartSearch,String fileName,String... extension) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        libraryLoggingUtils = new LibraryLoggingUtils();
        String filePath = null;
        try {
            if(extension.length>0)
            {
                if (!parentFolderToStartSearch.getParent().contains(fileName + extension[0]))
                {
                    File[] files = parentFolderToStartSearch.listFiles();
                    if (files != null)
                    {
                        for (File file : files) {
                            if (extension.length == 0)
                            {
                                if (file.isDirectory()) {
                                    // Recursive call if the file is a directory
                                    findFileFromNestedFolderStructure(file, fileName);
                                } else if (file.getName().equals(fileName)) {
                                    // File found, print the absolute path
                                    filePath = file.getParent() + "/" + file.getName();
                                    break;
                                } else {
                                    filePath = null;
                                }
                            } else {
                                if (file.isDirectory()) {
                                    // Recursive call if the file is a directory
                                    findFileFromNestedFolderStructure(file, fileName, extension[0]);
                                } else if (file.getName().equals(fileName + extension[0])) {
                                    // File found, print the absolute path
                                    filePath = file.getParent() + "/" + file.getName();
                                    break;
                                } else {
                                    filePath = null;
                                }
                            }
                        }
                    }
                }
            }else{

            }
        } catch (Exception e) {
            libraryLoggingUtils.getCurrentMethodException(className,methodName,e);
        }
        return filePath;
    }

    /**
     * createZipFolder Method is used to create zip file of existing folder
     * @param testCaseResultsFolder The path of the actual test case folder which needs to be zipped ( Example : ./src/test/resources/TestCaseResults/TC002_20231018125321 )
     * @param outputZipFile The path of the destination zip folder ( Example :./src/test/resources/TestCaseResultsZipFiles/TC002_20231018125321.zip)
     * @return Returns the newly created zip file path as String
     * @see #zipFile(File, String, ZipOutputStream)
     */
    public synchronized String createZipFolder(String testCaseResultsFolder, String outputZipFile) throws IOException {
        FileOutputStream fos = new FileOutputStream(outputZipFile);
        ZipOutputStream zipOutputStream = new ZipOutputStream(fos);
        File fileToZip = new File(testCaseResultsFolder);
        zipFile(fileToZip, fileToZip.getName(), zipOutputStream);
        zipOutputStream.close();
        fos.close();
        return outputZipFile;
    }
    /**
     * zipFile Method is used to create zip file of existing folder , It is called from method * {@link #createZipFolder(String, String)} to perform the zip file creation.
     * @param fileToZip The path of the actual folder which needs to be zipped ( Example : ./src/test/resources/TestCaseResults/TC002_20231018125321 )
     * @param fileName The name of the destination zip folder
     * @param zipOut The path of the destination zip folder ( Example :./src/test/resources/TestCaseResultsZipFiles/TC002_20231018125321.zip)
     *
     */
    public synchronized void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            String[] children = fileToZip.list();
            for (String child : children) {
                zipFile(new File(fileToZip, child), fileName + "\\" + child, zipOut);
            }
        } else {
            FileInputStream fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileName);
            zipOut.putNextEntry(zipEntry);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) >= 0) {
                zipOut.write(buffer, 0, length);
            }
            fis.close();
            zipOut.closeEntry();
        }
    }

    /**
     * convertAndWriteFileToBase64 Method is used to convert zip file to Base64 encoded format and write to new location     *
     * @param testCaseResultsZipFolder This will accept folder path for the zip file to be encoded
     * @param resultsFolder            This will accept file name for the zip file to be encoded
     * @param Base64EncodedFolderPath  This will accept folder path where encoded files will be kept
     */
    public synchronized String convertAndWriteFileToBase64(String testCaseResultsZipFolder, String resultsFolder, String Base64EncodedFolderPath) {
        String targetFilePath = null;
        try {
            String ziplocationFile = new File(resultsFolder).getName();
            if (!resultsFolder.isEmpty()) {
                String inputFile = createZipFolder(resultsFolder, testCaseResultsZipFolder + ziplocationFile + ".zip");
                System.out.println("path of zipped folder : " + inputFile);
                FileInputStream fis = new FileInputStream(inputFile);
                byte[] bytes = new byte[(int) new File(inputFile).length()];
                fis.read(bytes);
                fis.close();
                String base64Data = Base64.getEncoder().encodeToString(bytes);
                targetFilePath = Paths.get(Base64EncodedFolderPath, new File(inputFile).getName()).toString();
                Files.write(Paths.get(targetFilePath), base64Data.getBytes());
            } else {
                System.out.println("Test result folder is empty");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return targetFilePath;
    }
    /**
     * createTestCaseResultsFolder This method is used to create a test case result folder based on naming convention testcaseid_dateTimestamp based on provided date and timestamp patter.
     * @param destinationPath This will accept folder path where the base64 encoded file is to be created.
     * @param testCaseName    This will accept file name for the base64 encoded file to be encoded.
     * @param datePattern     This will accept pattern of the date and timestamp.
     */
    public synchronized String createTestCaseResultsFolder(String destinationPath, String testCaseName, String datePattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
        String timestamp = dateFormat.format(new Date());
        String folderName = testCaseName + "_" + timestamp;
        File folder = new File(destinationPath, folderName);
        String testCaseFolderPath = null;
        try {
            if (!folder.exists() && folder.mkdirs()) {
                Path absolutePath = Paths.get(folder.getAbsolutePath());
                Path basePath = Paths.get("").toAbsolutePath();
                Path relativePath = basePath.relativize(absolutePath);
                testCaseFolderPath = relativePath.toString();
                String replaced = testCaseFolderPath.replace("\\", "/").replace("//", "/");

            }
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
        return testCaseFolderPath;
    }
    /**
     * createZipFolderAndReturnPath This method is used to create a zip folder.
     * @param folderTobeZipped This will accept folder path where the base64 encoded file is to be created.
     * @param zippedFileDestinationPath This will accept destination file path where the zip folder is to be created.
     * @return zippedFileDestinationPath This will return the destination file path.
     */
    public synchronized String createZipFolderAndReturnPath(String folderTobeZipped, String zippedFileDestinationPath) {

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(zippedFileDestinationPath);
            ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
            File sourceFolder = new File(folderTobeZipped);
            addFolderToZip(sourceFolder, sourceFolder.getName(), zipOutputStream);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return zippedFileDestinationPath;
    }
    /**
     * addFolderToZip This method is used to add create a zip folder.
     * @param folder This will accept folder path where file is to be created.
     * @param parentFolder This will accept name of the parent folder.
     * @param zos This will accept Zipoutputstream object.
     */
    public synchronized void addFolderToZip(File folder, String parentFolder, ZipOutputStream zos) {
        System.out.println("path of folder " + folder.getAbsolutePath());
        System.out.println("parentFolder " + parentFolder);
        System.out.println("number of files in parent folder" + folder.listFiles().length);
        for (File file : folder.listFiles()) {
            System.out.println("absolute path : " + file.getAbsoluteFile());
            if (file.isDirectory()) {
                addFolderToZip(file, parentFolder + File.separator + file.getName(), zos);
            } else {
                addFileToZip(file, parentFolder + File.separator + file.getName(), zos);
            }
        }

    }
// used in above function addFolderToZip
    public synchronized void addFileToZip(File file, String entryName, ZipOutputStream zos) {
        System.out.println("entryName:" + entryName);
        try (
                FileInputStream fis = new FileInputStream(file);
        ) {
            ZipEntry zipEntry = new ZipEntry("./src/test/resources/TestCaseResults/" + entryName);
            zos.putNextEntry(zipEntry);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }
            zos.closeEntry();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
