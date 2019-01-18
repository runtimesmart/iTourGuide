/**
* @FileName ImageBlur.java
* @Package com.itg.jni
* @Description TODO
* @Author Alpha
* @Date 2015-9-16 上午11:42:24 
* @Version V1.0

*/
package com.itg.jni;

import android.graphics.Bitmap;

public class ImageBlur {

public static native void blurIntArray(int[] pImg, int w, int h, int r);
public static native void blurBitMap(Bitmap bitmap, int r);
 static{
	 System.loadLibrary("JNI_ImageBlur");
 }

}
