/**
* @FileName NetWorkDetected.java
* @Package com.itg.util
* @Description TODO
* @Author Alpha
* @Date 2015-8-10 下午4:02:01 
* @Version V1.0

*/
package com.itg.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;

@SuppressLint("ServiceCast")
public class NetWorkDetected {
	public final static int NONET=-1;
	public final static int WIFI=1;
	public final static int MOBILE=2;
	public final static int UNKNOW=0;

public static int  getNetWorkInfo(Context context) {
	ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	NetworkInfo networkInfo= connectivityManager.getActiveNetworkInfo();
	if (networkInfo==null) {
		return NONET;
	}
	int netState=networkInfo.getType();
	if (netState==ConnectivityManager.TYPE_WIFI) {
		return WIFI;
	}
	else if(netState==ConnectivityManager.TYPE_MOBILE)
	{
	  return MOBILE;
	}
	return UNKNOW;
}


/**
 * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
 * @param context
 * @return true 表示开启
 */
public static final boolean isOPen(final Context context) {
	LocationManager locationManager 
	                         = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
	// 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
	boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	// 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
//	boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	if (gps) {
		return true;
	}

	return false;
}

/**
 * 强制帮用户打开GPS
 * @param context
 */
public static void turnGPSOn(final Context ctx)
{
     Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
     intent.putExtra("enabled", true);
     ctx.sendBroadcast(intent);

    String provider = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
    if(!provider.contains("gps"))
        { 
        //if gps is disabled
        final Intent poke = new Intent();
        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
        poke.setData(Uri.parse("3")); 
         ctx.sendBroadcast(poke);
    }
}
/**
 * 强制帮用户关闭GPS
 * @param context
 */
@SuppressWarnings("deprecation")
public static void turnGPSOff(final Context ctx)
{
    //String provider = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
  //  if(provider.contains("gps")){ //if gps is enabled
//        final Intent poke = new Intent();
//        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
//        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
//        poke.setData(Uri.parse("3")); 
//        ctx.sendBroadcast(poke);
   // }
	
	 Settings.Secure.setLocationProviderEnabled( ctx.getContentResolver(), LocationManager.GPS_PROVIDER, false );
}

public  static void openGPSSetting(final Context ctx) {
	 Intent intent = new Intent();
     intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	 ctx.startActivity(intent); 
         // General settings activity
         intent.setAction(Settings.ACTION_SETTINGS);
         try {
        	 ctx.startActivity(intent);
         } catch (Exception e) {
         }
     
}

}
