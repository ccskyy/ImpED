package com.yorker.imped.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.yorker.imped.exceptions.XINEException;
public class FileUtil {
	private static final Logger logger = Logger.getLogger(FileUtil.class);

	/**
	 * Compares two data files
	 * @param source file
	 * @param target file
	 * @return true if given files are identical
	 */
	public static boolean isSameContent(String src,String target) {
		boolean flag = true;
		try (BufferedReader br1 = new BufferedReader(new FileReader(src)); 
			 BufferedReader br2 = new BufferedReader(new FileReader(target)))
		{
			String strLine;
			while ((strLine = br1.readLine()) != null) {
				if (!strLine.equals( br2.readLine())){
					flag = false;
					break;
				}
			}

		} catch (IOException e) {
			logger.error("Exception occured while comparing files .."+ XINEUtil.getErrorStackTrace(e));
		}
		return flag;
	}
	
	/**
	 * Move file from one directory to another directory
	 * @param process id file
	 * @param destination file with path
	 */
	public static void moveFile(String processIdFile, String destDir) {
		File file = new File(processIdFile);
		String fileName = file.getName();
		file.renameTo(new File(file.getParentFile().getPath()+ destDir + fileName));
	}

	/**
	 * Rename the given file
	 * @param source file with path
	 * @param destination file with path
	 */
	public static void renameFile(String srcFile, String destFile) {
		File file = new File(srcFile);
		file.renameTo(new File(destFile));
	}

	/**
	 * Deletes checkpoint file
	 * @param process id file
	 * @param destination directory
	 * @param group name
	 */
	public static void deleteCheckPointFile(String processIdFile, String destDir,String groupName) {
		File file = new File(processIdFile);
		File checkPointFile = new File(file.getParentFile().getPath()+ destDir + groupName);
		checkPointFile.delete();
	}

	/**
	 * Removes given file permanently
	 * @param file name with path
	 */
	public static void removeFile(String fileName) {
		File file = new File(fileName);
		if (file.exists()) {
			file.delete();
		}
	}
	
	/**
	 * Append to existing file.
	 *
	 * @param processfile the path where you want to create file
	 * @param fileContent the file content
	 * @throws XINEException the XINE exception
	 */
	public static void append2File(String processfile, String fileContent) throws XINEException {
		try {
			File file = new File(processfile);
			if (!file.exists()) {
				file.createNewFile();
			}
			//true = append file
			FileWriter fileWritter = new FileWriter(file,true);
	        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
	        bufferWritter.write(fileContent);
	        bufferWritter.close();
		} catch (IOException e) {
			throw new XINEException(e.getMessage(),XINEConstants.ERROR_CODES.get("ERROR-500"));
		}
	}
	
}
