package com.itg.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class ZipUncompress {

	public int upZipFile(File zipFile, String folderPath) throws ZipException,
	IOException {
ZipFile zfile = new ZipFile(zipFile);
Enumeration zList = zfile.entries();
ZipEntry ze = null;
byte[] buf = new byte[1024];
while (zList.hasMoreElements()) {
	ze = (ZipEntry) zList.nextElement();
	if (ze.isDirectory()) {

		String dirstr = folderPath + ze.getName();
		dirstr = new String(dirstr.getBytes("UTF-8"), "GB2312");
		File f = new File(dirstr);
		f.mkdir();
		continue;
	}
	OutputStream os = new BufferedOutputStream(new FileOutputStream(
			getRealFileName(folderPath, ze.getName())));
	InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
	int readLen = 0;
	while ((readLen = is.read(buf)) != -1) {
		os.write(buf, 0, readLen);
	}
	is.close();
	os.close();
}
zfile.close();
return 1;
}

	    public File getRealFileName(String baseDir, String absFileName){
			String newAbsFileName = absFileName.replace("\\", "/");
	        String[] dirs=newAbsFileName.split("/");
	        File ret=new File(baseDir);
	        String substr = null;
	        if(dirs.length>1){
	            for (int i = 0; i < dirs.length-1;i++) {
	                substr = dirs[i];
	                try {
	                    //substr.trim();
	                    substr = new String(substr.getBytes("UTF-8"), "GB2312");
	                    
	                } catch (UnsupportedEncodingException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }
	                ret=new File(ret, substr);

	            }
	            if(!ret.exists()){
	                ret.mkdirs();
	    		}
	            substr = dirs[dirs.length-1];
	            try {
	                //substr.trim();
	                substr = new String(substr.getBytes("UTF-8"), "GB2312");
	            } catch (UnsupportedEncodingException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	            
	            ret=new File(ret, substr);

	            return ret;
	        }else if(dirs.length == 1){
                ret=new File(ret, dirs[0]);

	        }

	        return ret;
	    }
	

	public void deleteFile(File file) {
		if (file.exists()) { 
			if (file.isFile()) { 
				file.delete();
			}
		} else {

		}
	}
}
