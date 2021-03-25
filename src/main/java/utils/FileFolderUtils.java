package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.lang3.StringUtils;

import alm.qc.restapi.MF_Assert;
import alm.qc.restapi.UtilsQc;
import utils.customException.CustomException;

public class FileFolderUtils {

	public static void main(String[] args) {

		String inputFolderPath = FileFolderUtils
				.convertRelToAbsPath(".//src//test//java//codeExamples//JDBC//SapHana//Output//Before");
		;

		// SAP_Hana_Cloudera_Integration_ResultSet_10022020_112018
		String matchContainsI = "SAP_Hana_Cloudera_Integration_";
		String matchExtension = "txt";

//		File searchedFile = findLastModified(inputFolderPath, matchContainsI);

		File searchedFile = findLastModified(inputFolderPath, matchContainsI, matchExtension);
		if (searchedFile != null) {

			System.out.println("File found");
			System.out.println(searchedFile.getAbsolutePath());
			System.out.println("");

		} else {

			System.out.println("NOT Found");

		}

		// System.out.println(lastModPath);

	}

	// TODO Check out : FilenameUtils. methods

	
	
	public static String buildPath(String BasePath, String AppendPath) {
		
		return (BasePath + System.getProperty("file.separator") + AppendPath).replace("//", System.getProperty("file.separator")).replace("/", System.getProperty("file.separator"));
		
	}
	
	
	/**
	 * Copies the SourceFile to Destination Folder. If destination Folder does not
	 * exists, it creates the folder
	 * 
	 * @author Pankaj Sharma
	 * @modified 12-02-2020
	 * @return Void
	 * 
	 */
	public static void copyFolderContent(String SourceFilePath, String destinationPath) throws IOException {
		try {
			
			System.out.println("[DirectoryUtils ln 26] SourceFilePath = " + SourceFilePath);
			
			CreateDirectory(destinationPath);
			// Source directory which you want to copy to new location
			File sourceFolder = new File(SourceFilePath);

			// Target directory where files should be copied
			File destinationFolder = new File(destinationPath);

			// Call Copy function
			copyFolder(sourceFolder, destinationFolder);

		} catch (Exception e) {
			
			System.out.println("[DirectoryUtils ln 41] Could not move the file from  " + SourceFilePath + " to " + destinationPath);
		}

	}
	
	/**
	 * This function recursively copy all the sub folder and files from sourceFolder
	 * to destinationFolder
	 */
	private static void copyFolder(File sourceFolder, File destinationFolder) throws IOException {

		// Check if sourceFolder is a directory or file
		// If sourceFolder is file; then copy the file directly to new location
		if (sourceFolder.isDirectory()) {
			// Verify if destinationFolder is already present; If not then create it
			if (!destinationFolder.exists()) {
				destinationFolder.mkdirs();
//                            System.out.println("Directory created [DirectoryUtils Ln 50] =  " + destinationFolder);
			}

			// Get all files from source directory
			String files[] = sourceFolder.list();

			// Iterate over all files and copy them to destinationFolder one by one
			for (String file : files) {
				File srcFile = new File(sourceFolder, file);
				File destFile = new File(destinationFolder, file);

				// Recursive function call
				copyFolder(srcFile, destFile);
			}
		} else {
			// Copy the file content from one place to another

			Files.copy(sourceFolder.toPath(), destinationFolder.toPath(), StandardCopyOption.REPLACE_EXISTING);
//			System.out.println("[DirectoryUtils ln 85] File copied :: " + destinationFolder);
		}
	}

	
	public static void CreateDirectory(String path) {
		try {
			File file = new File(path);
			file.mkdirs();
			boolean flag = file.mkdir();
			System.out.print("Directory created? " + flag);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void moveFilesFromDirToDir(String srcDirPath, String destDirPath) {
		try {
			/* Apache commons io StringUtilsExamples class provide isNoneBlank() method. */
			if (StringUtils.isNoneBlank(srcDirPath) && StringUtils.isNoneBlank(destDirPath)) {
				/* Create source directory instance. */
				File srcDirFile = new File(srcDirPath);

				/* Create target directory instance. */
				File destDirFile = new File(destDirPath);

				/* Move directory to directory. */
				/* The third parameter is true means create target directory if not exist. */
				FileUtils.moveDirectoryToDirectory(srcDirFile, destDirFile, true);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void deleteDirectoryContents(String directoryPath) throws IOException {

		File sourceFolder = new File(directoryPath);
		deleteDirectory(sourceFolder);
	}

	public static void deleteDirectory(File directoryToBeDeleted) throws IOException {
		if (directoryToBeDeleted.isDirectory()) {
			// Verify if destinationFolder is already present; If not then create it
			if (directoryToBeDeleted.exists()) {
				FileUtils.deleteDirectory(directoryToBeDeleted);
			}

		}

	}
	

	@SuppressWarnings("unused")
	private static void currentFolderName(String sCurrentFolderPath) throws IOException {
		
		
		File currentDirFile = new File(sCurrentFolderPath);
		
		
		
		
		
		// NEEDS to exist 
		System.out.println("currentDirFile.isDirectory() = " + currentDirFile.isDirectory());
		System.out.println("currentDirFile.isFile() = " + currentDirFile.isFile());

		System.out.println("currentDirFile.currentDirFile.getParent() = " + currentDirFile.getParent());
		System.out.println("currentDirFile.currentDirFile.getCanonicalFile() = " + currentDirFile.getCanonicalFile());
		System.out.println("currentDirFile.getCanonicalPath() = " + currentDirFile.getCanonicalPath());
		System.out.println("");
		
	}

	
	
	
	public static String convertRelToAbsPath(String RelativeFilePath) {
		
		// Craig Adam
		// 30-01-2020
		// Example of use
		// RelativeFilePath = ".//src//test//java//codeExamples//JDBC//SapHana//Output//Prior//"
		// convertRelToAbsPath(RelativeFilePath)
		
		// Starts with "." then is a relative path
		if(RelativeFilePath.substring(0,1).contentEquals(".")) {
			
//			return System.getProperty("user.dir") + System.getProperty("file.separator") + RelativeFilePath.substring(1,RelativeFilePath.length());
			// Remove file separator - included at the start of the relative path eg .//results
			// Convert // to \
			String sAbsPath = (System.getProperty("user.dir") + RelativeFilePath.substring(1,RelativeFilePath.length()).replaceFirst(".", "").replace("//", System.getProperty("file.separator"))).replace(System.getProperty("file.separator"), "/");
					
			return sAbsPath;
		}
		
		// Already an absolute path return
		return RelativeFilePath;
		
	}	
	
	
	@SuppressWarnings("unused")
	private static void currentDrive() throws IOException {
		
		// create file in Current working directory
		File currentDirFile = new File(".");
		String helper = currentDirFile.getAbsolutePath();
		String currentDrive = helper.substring(0, helper.length() - currentDirFile.getCanonicalPath().length());
		System.out.println("current drive = " + currentDrive);
		
	}
	
	@SuppressWarnings("unused")
	private static void absolutePath() {
		
		// create file in Current working directory
		File currentDirFile = new File(".");
		System.out.println("currentDirFile.getAbsolutePath() = " + currentDirFile.getAbsolutePath());
		System.out.println("");

		
	}
	
	@SuppressWarnings("unused")
	private static void canonicalPath() throws IOException {
		
		// create file in Current working directory
		File currentDirFile = new File(".");
		System.out.println("currentDirFile.getCanonicalPath() = " + currentDirFile.getCanonicalPath());
		System.out.println("");
		
	}
	
	
	@SuppressWarnings("unused")
	private static void userDirectory() {
		
		// Get user directory
		System.out.println("System.getProperty(\"user.dir\") = " + System.getProperty("user.dir"));
		System.out.println("");
		
	}
	
	@SuppressWarnings("unused")
	private static void systemProperties() {
		
		// All System properties
		Properties properties = System.getProperties();
		Enumeration<Object> enumeration = properties.keys();
		for (int i = 0; i < properties.size(); i++) {
		    Object obj = enumeration.nextElement();
		    System.out.println("Key: "+obj+"\tOutPut= "+System.getProperty(obj.toString()));
		}
		
	}
	
	
	public static File findLastModified(String inputFolderPath, String matchContainsI, String endingWith) {

		// Craig Adam
		// 30/01/2020

		// TODO ADD counter warning or and additional parameters to contain or by regex

		File dir = new File(inputFolderPath);

		// check dir exists
		if (!dir.exists()) {

			CustomException
					.throwException("Directory : " + inputFolderPath + " does NOT exist" + System.lineSeparator());

		}

		File[] files = dir.listFiles();

//	    Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR); 
		Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE); // last modified 1st

		// check dir exists
		if (files.length < 1) {

			CustomException.throwException(
					"Directory : " + inputFolderPath + " does NOT contain any files" + System.lineSeparator());

		}

		// Iterate over reverse sorted file list and look for 1st match
		File matchedFile = null;
		for (int i = 0; i < files.length; i++) {

			if (files[i].getName().contains(matchContainsI)) {

				if (files[i].getName().endsWith(endingWith)) {

					matchedFile = files[i];
					break;

				}

			}

			if (i == files.length - 1) {

				CustomException.throwException("[FileFolderUtils Ln 105] Iterated through all files "
						+ System.lineSeparator() + " no file with name containing : " + matchContainsI
						+ System.lineSeparator() + " ending with : " + endingWith + System.lineSeparator()
						+ " was found in directory : " + inputFolderPath + System.lineSeparator());

			}

		}

		// Check file found
		if (matchedFile == null) {

			CustomException.throwException("[FileFolderUtils Ln 114] matchedFile = null " + System.lineSeparator()
					+ " No file with name containing : " + matchContainsI + System.lineSeparator() + " ending with : "
					+ endingWith + System.lineSeparator() + " was found in directory : " + inputFolderPath
					+ System.lineSeparator());

		}

		return matchedFile;

	}

	public static File findLastModified(String inputFolderPath, String matchContains) {

		// Craig Adam
		// 30/01/2020

		// TODO ADD counter warning or and additional parameters to contain or by regex

		File dir = new File(inputFolderPath);
		File[] files = dir.listFiles();

//	        Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR); 
		Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE); // last modified 1st

//	        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

		File matchedFile = null;
		for (int i = 0; i < files.length; i++) {

			// System.out.println(files[i].getName() + " : " +
			// sdf.format(files[i].lastModified()));

			if (files[i].getName().contains(matchContains)) {

//					System.out.println(files[i].getName());
				matchedFile = files[i];
				break;

			}

			if (i == files.length - 1) {

				System.out.println("NO MATCH ln 125");

			}

		}

		return matchedFile;

	}

	public static LinkedHashMap<String, String> getFileList_ByName(String inputFolderPath) {

		// Craig Adam
		// 30-01-2020
		// Return details of all files in a folder

		List<String> fileNames = FileFolderUtils.fileWalk_isRegularFile(inputFolderPath);
		PrintUtils.printObject(fileNames);

		List<String> folderNames = FileFolderUtils.fileWalk_isDirectory(inputFolderPath);
		PrintUtils.printObject(folderNames);

		List<String> filterdFileNames = null;
		filterdFileNames = FileFolderUtils.fileWalk_endsWith(inputFolderPath, ".java");
		PrintUtils.printObject(filterdFileNames);

		filterdFileNames = FileFolderUtils.fileWalk_matchesRegEx(inputFolderPath, ".*\\.java");
		filterdFileNames = FileFolderUtils.fileWalk_matchesRegEx(inputFolderPath, ".*info.*");
		PrintUtils.printObject(filterdFileNames);

		filterdFileNames = FileFolderUtils.fileWalk_IsHidden(inputFolderPath);
//			System.out.println("Size = " + filterdFileNames.size());	
//			PrintUtils.printObject(filterdFileNames);

		filterdFileNames = FileFolderUtils.fileWalk_IsHidden(inputFolderPath, ".*.hidden.*");
//			System.out.println("Size name contains 'hidden' = " + filterdFileNames.size());	
//			PrintUtils.printObject(filterdFileNames);

		filterdFileNames = FileFolderUtils.fileWalk_IsHidden(inputFolderPath, ".*.do not find.*");
//			System.out.println("Size name contains 'do not find' = " + filterdFileNames.size());	
//			PrintUtils.printObject(filterdFileNames);

		File matchedFile = findLastModified(inputFolderPath, "WebRowSetConverted");

		System.out.println("Matched name =  " + matchedFile.getName());

		return null;

	}

	public static void sortbydate() {

		// http://www.java2s.com/Code/Java/File-Input-Output/Sortfilesbaseontheirlastmodifieddate.htm

		File dir = new File("c:\\");
		File[] files = dir.listFiles();

		Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			System.out.printf("File %s - %2$tm %2$te,%2$tY%n= ", file.getName(), file.lastModified());
		}

		Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			System.out.printf("File %s - %2$tm %2$te,%2$tY%n= ", file.getName(), file.lastModified());
		}
	}

	// https://javaconceptoftheday.com/list-all-files-in-directory-in-java/
	// Files.list() traverses only the current directory. Let’s see them in detail.

	public static List<String> fileWalk_isRegularFile(String inputFolderPath) {

		// https://mkyong.com/java/java-how-to-list-all-files-in-a-directory/
		// after 1.8
		// https://javaconceptoftheday.com/list-all-files-in-directory-in-java/
		// Files.walk() recursively traverses the given directory and it’s
		// sub-directories to get list of all the files and folders in the hierarchy

		List<String> result = null;
		try (Stream<Path> walk = Files.walk(Paths.get(inputFolderPath))) {

			result = walk.filter(Files::isRegularFile) //
					.map(x -> x.toString()) //
					.collect(Collectors.toList()); //

			// result.forEach(System.out::println);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;

	}

	public static List<String> fileWalk_isDirectory(String inputFolderPath) {

		// https://mkyong.com/java/java-how-to-list-all-files-in-a-directory/
		// after 1.8

		List<String> result = null;
		try (Stream<Path> walk = Files.walk(Paths.get(inputFolderPath))) {

			result = walk.filter(Files::isDirectory).map(x -> x.toString()).collect(Collectors.toList());

			// result.forEach(System.out::println);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;

	}

	public static List<String> fileWalk_IsHidden(String inputFolderPath) {

		// https://javaconceptoftheday.com/list-all-files-in-directory-in-java/

		List<String> result = null;
		try (Stream<Path> walk = Files.walk(Paths.get(inputFolderPath))) {

			result = walk //
					.filter(file -> {
						try {
							return Files.isHidden(file);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return false;
					}) //
					.map(x -> x.toString()) //
					.collect(Collectors.toList()); //

		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;

	}

	public static List<String> fileWalk_IsHidden(String inputFolderPath, String matchRegEx) {

		// https://javaconceptoftheday.com/list-all-files-in-directory-in-java/

		List<String> result = null;
		try (Stream<Path> walk = Files.walk(Paths.get(inputFolderPath))) {

			result = walk //
					.filter(file -> {
						try {
							return Files.isHidden(file);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return false;
					}) //
					.map(x -> x.toString()) //
					.filter(f -> f.matches(matchRegEx)) //
					.collect(Collectors.toList()); //

		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;

	}

	public static List<String> fileWalk_endsWith(String inputFolderPath, String matchEndsWith) {

		// https://mkyong.com/java/java-how-to-list-all-files-in-a-directory/
		// after 1.8

		List<String> result = null;
		try (Stream<Path> walk = Files.walk(Paths.get(inputFolderPath))) {

			result = walk.map(x -> x.toString()).filter(f -> f.endsWith(matchEndsWith)) //
					.collect(Collectors.toList());

			// result.forEach(System.out::println);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;

	}

	public static List<String> fileWalk_matchesRegEx(String inputFolderPath, String matchRegEx) {

		// https://mkyong.com/java/java-how-to-list-all-files-in-a-directory/
		// after 1.8

		List<String> result = null;
		try (Stream<Path> walk = Files.walk(Paths.get(inputFolderPath))) {

			result = walk.map(x -> x.toString()).filter(f -> f.matches(matchRegEx)) //
					.collect(Collectors.toList());

			// result.forEach(System.out::println);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;

	}

	public static List<String> fileWalk_matchEquals(String inputFolderPath, String matchContentEquals) {

		// https://mkyong.com/java/java-how-to-list-all-files-in-a-directory/
		// after 1.8

		List<String> result = null;
		try (Stream<Path> walk = Files.walk(Paths.get(inputFolderPath))) {

			result = walk.map(x -> x.toString()).filter(f -> f.contentEquals(matchContentEquals)) //
					.collect(Collectors.toList());

			// result.forEach(System.out::println);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;

	}

	public static List<String> fileWalk_contains(String inputFolderPath, String matchEndsWith) {

		// https://mkyong.com/java/java-how-to-list-all-files-in-a-directory/
		// after 1.8

		List<String> result = null;
		try (Stream<Path> walk = Files.walk(Paths.get(inputFolderPath))) {

			result = walk.map(x -> x.toString()).filter(f -> f.endsWith(matchEndsWith)) //
					.collect(Collectors.toList());

			// result.forEach(System.out::println);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;

	}

	public static void zipFolder(String inputFolder, String outputFolder, String outputFilename) {
		// https://www.mkyong.com/java/how-to-compress-files-in-zip-format/

		// Check that folder exists
		MF_Assert.assertPathExists("Folder " + inputFolder + " does not exit - UtilsQc Ln40", inputFolder);

		UtilsQc appZip = new UtilsQc();
		appZip.generateFileList(new File(inputFolder), inputFolder);

// create output folder if does not exist
		// https://stackoverflow.com/questions/28947250/create-a-directory-if-it-does-not-exist-and-then-create-the-files-in-that-direct
		File directory = new File(outputFolder);
		if (!directory.exists()) {
			directory.mkdir();
			// If you require it to make the entire directory path including parents,
			// use directory.mkdirs(); here instead.
		}

		appZip.zipIt(outputFolder + "\\" + outputFilename, inputFolder);

	}

	/**
	 * Generate a string based on LocalTimeDate.now() - From year to seconds For
	 * example to use as a unique filename or to append to string to create unique
	 * filename
	 * 
	 * @author Craig Adam
	 * @modified 31-01-2020
	 * @return String = "_ddMMyyyy_HHmmss"
	 * 
	 */
	public static String uniqueSuffixDTN() {

		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("_ddMMyyyy_HHmmss")).toString();
	}

	/**
	 * Traverse a directory and get all files, and add the file into fileList
	 * 
	 * @param node          file or directory
	 * @param SOURCE_FOLDER
	 * @return
	 */
	public static ArrayList<String> generateFileList(File node, String SOURCE_FOLDER) {

		// example
		// appZip.generateFileList(new File(inputFolder), inputFolder);

		ArrayList<String> fileList = new ArrayList<String>();

		// https://www.mkyong.com/java/how-to-compress-files-in-zip-format/

		// add file only
		if (node.isFile()) {
			fileList.add(formatFilePath_zip(node.getAbsoluteFile().toString(), SOURCE_FOLDER));
		}

		if (node.isDirectory()) {
			String[] subNote = node.list();
			for (String filename : subNote) {
				generateFileList(new File(node, filename), SOURCE_FOLDER);
			}
		}

		return fileList;

	}

	/**
	 * 
	 * Saves a string into a file
	 * 
	 * @param InputString    - in memory string to save
	 * @param OutputFilePath - absolute file name to save as
	 * @author Craig Adam - Modified 14-02-2020
	 * 
	 */
	public static void saveStringToFile(String InputString, String OutputFilePath) {

		// Craig Adam
		// 30-11-2019

//		System.out.println("[JDBCSupportUtility ln 2163]");
//		System.out.println("File saved to : " + System.lineSeparator() + OutputFilePath);

		try {

			FileWriter fw;
			fw = new FileWriter(OutputFilePath);
			fw.write(InputString);
			fw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("");

	}

	/**
	 * Format the file path for zip
	 * 
	 * @param file file path
	 * @return Formatted file path
	 */
	private static String formatFilePath_zip(String file, String SOURCE_FOLDER) {
		// https://www.mkyong.com/java/how-to-compress-files-in-zip-format/

		return file.substring(SOURCE_FOLDER.length() + 1, file.length());
	}

}
