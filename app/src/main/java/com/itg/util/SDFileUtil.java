/**
* @FileName SDFileUtil.java
* @Package com.itg.util
* @Description TODO
* @Author Alpha
* @Date 2015-9-28 下午3:15:22 
* @Version V1.0

*/
package com.itg.util;

import java.io.File;
import java.io.IOException;

import android.os.Environment;
import android.util.Log;

public class SDFileUtil {

	private String SDCARDPATH;
	/**
	 * 得到手机的存贮目录
	 */
	public SDFileUtil() {
		
         boolean sdCardExist = Environment.getExternalStorageState()   
                             .equals(android.os.Environment.MEDIA_MOUNTED);   //判断sd卡是否存在 
         if(sdCardExist) { 
        	//获取SD卡根目录
         	SDCARDPATH = Environment.getExternalStorageDirectory().toString() + "/";
         } else {
        	 SDCARDPATH = Environment.getExternalStorageDirectory().toString();//获取手机根目录
         }
		
	}

	/**
	 * 得到手机的存贮目录
	 */ 
	public String getSDCard() {
		return SDCARDPATH;
	}

	/**
	 * 在sd卡上创建文件
	 * 
	 * @param strFileName 要创建的文件名
	 * @return
	 */
	public File doCreateFile(String strFileName) {
		File file = new File(strFileName);
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}
	
	/**
	 * 在sd卡上创建文件夹
	 * 
	 * @param strDirName
	 *            要创建的文件夹名
	 * @return
	 */
	public File doMakeDir(String dirName) {
		File file = new File(dirName);
		if (!file.exists()) {
			boolean isCreateDir = false;
			if (dirName.contains("/")) {
				isCreateDir = file.mkdirs();
			} else {
				isCreateDir = file.mkdir();
			}		
		} 
		return file;
	}
	/**
	 * 判断sd卡上是否存在某个文件
	 * 
	 * @param strFileName
	 *            要判断的文件名
	 * @return
	 */
	public boolean IsFileExists(String strFileName) {
		File file = new File(strFileName);
		return file.exists();
	}
	/**
	 * 判断文件是否大小一样
	 * */
	public boolean IsFileExists(String fileName, long len) {
		File file = new File(SDCARDPATH + fileName);
		return file.length() >= len - 10;
	}
	
	public boolean sceneryIsDownload(){
		String downloadfilepath = getSDCard()+"SceneryOnline/ScenicAreaOffline/";
		File sceneryDir = new File(downloadfilepath);
		if(sceneryDir.exists()){
			return true;
		}
		return false;
	}
	
}
