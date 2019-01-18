package com.itg.util;

import java.io.File;
import java.io.IOException;

import android.os.Environment;

public class FileUtils {
	private String SDPath;

	public String getSDPath() {
		return SDPath;
	}

	public FileUtils() {

		SDPath = Environment.getExternalStorageDirectory() + "/";
	}

	public File CreateFile(String FileName) {
		File file = new File(SDPath + FileName);
		try {
			boolean isCreate = file.createNewFile();
			return file;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public File CreateSDDir(String SDDir) {
		System.out.println("dirdirdirdirdiridrdirdirdirdir");
		File file = new File(SDPath + SDDir);
		boolean isCreate = file.mkdir();
		return file;
	}

	public boolean IsFileExists(String FileName) {
		File file = new File(SDPath + FileName);
		return file.exists();

	}
	
	public boolean IsFile(String FileName){
		File file = new File(FileName);
		if(file.isDirectory()){
			return true;
		}
		return false;
	}
}
