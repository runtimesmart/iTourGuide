/**
* @FileName HttpUtil.java
* @Package com.itg.util
* @Description TODO
* @Author Alpha
* @Date 2015-9-6 下午2:49:56 
* @Version V1.0

*/
package com.itg.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HttpUtil {


//	public static boolean downloadFile(InputStream inputStream,String outputPath)
//	{
//		SDFileUtil sdFileUtil=new SDFileUtil();
//		File file = sdFileUtil.doCreateFile(outputPath);
//		FileOutputStream outputStream;
//		try {
//			outputStream = new FileOutputStream(file);
//			byte[] buffer = new byte[1024];
//			int length = 0;
//			while ((length = inputStream.read(buffer)) != -1) {
//				outputStream.write(buffer,0,length);
//			}
//			return true;
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			return false;
//		}
//	}
//	
//	
//	 public static int upZipFile(File zipFile, String folderPath)throws ZipException,IOException {
//		        ZipFile zfile=new ZipFile(zipFile);
//		        Enumeration zList=zfile.entries();
//		        ZipEntry ze=null;
//		        byte[] buf=new byte[1024];
//		        while(zList.hasMoreElements()){
//		            ze=(ZipEntry)zList.nextElement();  
//		            if(ze.isDirectory()){
//
//		                String dirstr = folderPath + ze.getName();
//		                dirstr = new String(dirstr.getBytes("UTF-8"), "GB2312");
//		                File f=new File(dirstr);
//		                f.mkdir();
//		                continue;
//		            }
//		            OutputStream os=new BufferedOutputStream(new FileOutputStream(getRealFileName(folderPath, ze.getName())));
//		            InputStream is=new BufferedInputStream(zfile.getInputStream(ze));
//		            int readLen=0;
//		            while ((readLen=is.read(buf))!=-1) {
//		                os.write(buf, 0, readLen);
//		            }
//		            is.close();
//		            os.close();
//		        }
//		        zfile.close();
//		        return 1;
//		    }
//
//
//		    public static File getRealFileName(String baseDir, String absFileName){
//				String newAbsFileName = absFileName.replace("\\", "/");
//		        String[] dirs=newAbsFileName.split("/");
//		        File ret=new File(baseDir);
//		        String substr = null;
//		        if(dirs.length>1){
//		            for (int i = 0; i < dirs.length-1;i++) {
//		                substr = dirs[i];
//		                try {
//		                    //substr.trim();
//		                    substr = new String(substr.getBytes("UTF-8"), "GB2312");
//		                    
//		                } catch (UnsupportedEncodingException e) {
//		                    // TODO Auto-generated catch block
//		                    e.printStackTrace();
//		                }
//		                ret=new File(ret, substr);
//
//		            }
//		            if(!ret.exists()){
//		                ret.mkdirs();
//		    		}
//		            substr = dirs[dirs.length-1];
//		            try {
//		                //substr.trim();
//		                substr = new String(substr.getBytes("UTF-8"), "GB2312");
//		            } catch (UnsupportedEncodingException e) {
//		                // TODO Auto-generated catch block
//		                e.printStackTrace();
//		            }
//		            
//		            ret=new File(ret, substr);
//
//		            return ret;
//		        }else if(dirs.length == 1){
//	                ret=new File(ret, dirs[0]);
//
//		        }
//
//		        return ret;
//		    }
//		
//	
//		public static boolean deleteFile(File file) {
//			if (file.exists()) { 
//				if (file.isFile()) { 
//					return file.delete();
//				}
//			} else {
//				return false;
//			}
//			return false;
//		}
//// 
////	public static Map<String,Object> unZipFromInputStream(String outputDirectory,InputStream in)
////	{
////		  if(in == null)   
////	            return null;  
////	          
////	        ZipEntry zipEntry = null;  
////	        FileOutputStream out = null;  
////	        String uniqueName,iconUrl = null;  
////	        Map<String,Object> map = new HashMap<String,Object>();  
////	        ZipInputStream zipIn = new ZipInputStream(in);
////	        try{  
////	            while ((zipEntry = zipIn.getNextEntry()) != null) {  
////	                //如果是文件夹路径方式，本方法内暂时不提供操作  
////	            	zipEntry.g
////	                if (zipEntry.isDirectory()) {  
////	                  String name = zipEntry.getName();  
////	                  name = name.substring(0, name.length() - 1);  
////	                  File file = new File(outputDirectory + File.separator + name);  
////	                  file.mkdir();  
////	                }   
////	                else {  
////	                    //如果是文件，则直接在对应路径下生成   
////	                    uniqueName = (zipEntry.getName());  
////	                    File path = new File(outputDirectory + File.separator);  
////	                    if(!path.exists())  
////	                        path.mkdirs();  
////	                      
////	                    iconUrl = outputDirectory + File.separator + uniqueName;  
////	                    File file = new File(iconUrl);  
////	                    file.createNewFile();  
////	                    out = new FileOutputStream(file);  
////	                    int b = 0;  
////	                    while ((b = zipIn.read()) != -1){  
////	                        out.write(b);  
////	                    }  
////	                    out.close();  
////	                    map.put(zipEntry.getName(),iconUrl);  
////	                }  
////	            }  
////	            return map;  
////	        } catch(Exception ex){  
////	           // log.error("in unZip(InputStream in,String outputDirectory) has an error,e is " + ex);  
////	            return null;  
////	        }  
////	        finally{  
//////	            IOUtils.closeQuietly(zipIn);  
//////	            IOUtils.closeQuietly(in);  
//////	            IOUtils.closeQuietly(out);  
////	        }  }
////
////	        	FileOutputStream outputStream=null;
////	       
////	            ZipInputStream zis = null;
////	            BufferedOutputStream bos = null;
////	            try {
////	                zis = new ZipInputStream(isInputStream);
////	                SDFileUtil sdFileUtil=new SDFileUtil();
////	                ZipEntry zipEntry = null;
////	                while ((zipEntry = zis.getNextEntry()) != null ) {
////	                	if(zipEntry.isDirectory())
////	                	{
////	                		String name = zipEntry.getName();
////	                		name = name.substring(0, name.length() - 1);
////	                		File file = new File(outputDirectory + File.separator + name);
////	                		file.mkdir();
////	                	}
////	                	else {
////	                		  // 写入文件
////	                		File file = new File(outputDirectory + File.separator + zipEntry.getName());
////	                		file.createNewFile();
////	                		outputStream = new FileOutputStream(file);
////	                		//普通解压缩文件
////	                	
////	                		int b;
////	                		while ((b = zis.read()) != -1){
////	                		outputStream.write(b);
////		                
////		              
////						}
////	                  
////	                	}
////	                	
////	        }
////	            }
////	            catch(Exception e)
////	            {
////	            	
////	            }
////	            
////	                return null;
////	            }
//	            
//		
//		
//		
//		
//		
//		
//	
//	public static Bitmap getBitMap(String path)
//	{
//		InputStream is=getHttpInputStream(path);
//		Bitmap bitMap=BitmapFactory.decodeStream(is) ;
//		return bitMap;
//	}
//
//	public static InputStream getHttpInputStream(String path)
//	{
//		InputStream inputStream = null;
//		HttpURLConnection httpUrlConnection = null;
//		try {
//			URL url=new URL(path);
//			 httpUrlConnection=(HttpURLConnection) url.openConnection();
//			httpUrlConnection.setRequestMethod("GET");
//			httpUrlConnection.setReadTimeout(20000);
//			httpUrlConnection.setConnectTimeout(20000);
//			httpUrlConnection.setDoInput(true);
//			httpUrlConnection.connect();
//			int code=httpUrlConnection.getResponseCode();
//			if(code==HttpURLConnection.HTTP_OK)
//			{
//				inputStream=httpUrlConnection.getInputStream();
//			}
//		
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			return null;
//		}
//		return inputStream;
//		
//	}

}
