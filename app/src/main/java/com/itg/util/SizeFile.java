package com.itg.util;

import java.io.File;
import java.text.DecimalFormat;

import android.widget.Toast;

public class SizeFile {
	static long dirlength;//保存目录大小的变量

    static void sdl(String dirname) {
        File dir = new File(dirname);
        System.out.println(dirname);
        String f[] = dir.list();
        File f1;
        for (int i = 0; i < f.length; i++) {
            f1 = new File(dirname + "/" + f[i]);
            if (!f1.isDirectory())
                dirlength += f1.length();
            else
                sdl(dirname + "/" + f[i]);//如果是目录,递归调用
        }
    }

    public String seekSize(String args[]) {
    	DecimalFormat df = new DecimalFormat("#.00");
        if (args.length != 1)//判断是否只带一个参数,参数是目录名
        {
            System.out.println("Parameter error!");
            System.exit(0);
        }
        dirlength = 0;
        String dirname = args[0];
        File dir = new File(dirname);
        if (dir.isDirectory())//判断是否是目录,如果不是退出程序
        {
            sdl(dirname);//计算目录大小
            System.out.println("Length is " + dirlength + " bytes.");
        } else
            System.out.println(dir + " isn't a directory!");
        if(dirlength <= 0){
        	return null;
        }
        String fileSizeString = "";
        if (dirlength < 1024) {
        	   fileSizeString = df.format((double) dirlength) + "B";
        	  } else if (dirlength < 1048576) {
        	   fileSizeString = df.format((double) dirlength / 1024) + "KB";
        	  } else if (dirlength < 1073741824) {
        	   fileSizeString = df.format((double) dirlength / 1048576) + "MB";
        	  } else {
        	   fileSizeString = df.format((double) dirlength / 1073741824) + "GB";
        	  }
        return fileSizeString;
    }
    
  //清除缓存   删除文件夹下所有文件
  	public boolean deleteFile(File file){
          if(file.isFile()){ //是否是目录
              file.delete();
              return false;
          } else if(file.exists()){  //判断文件是否存在
	          if(file.isDirectory()){
	              File[] childFile = file.listFiles();
	              if(childFile == null || childFile.length == 0){
	                  file.delete();
	                  return true;
	              }
	              for(File f : childFile){
	                  deleteFile(f);
	              }
	              file.delete();
	              return true;
	          }
          } 
          return false;
          
      }
}
