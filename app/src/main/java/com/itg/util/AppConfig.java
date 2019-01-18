/**
* @FileName AppConfig.java
* @Package com.itg.util
* @Description TODO
* @Author Alpha
* @Date 2015-8-7 下午4:47:20 
* @Version V1.0

*/
package com.itg.util;


public class AppConfig {

	/**
	 * 服务端接口主机头
	 * */
	public static final String HOTPOT_SERVICE_URL = "http://103.10.85.178:88/Service/";
//	public static final String IGUIDE_SERVICE_PHOTO = "http://192.168.1.52:81/";
//	public static final String IGUIDE_SERVICE_HOST = "http://192.168.1.52:81/Service/";
	public static final String IMAGE_URL = "http://103.10.85.178:81/";
	/**
	 * 根据景点id查询景点的详细信息
	 * */
	public static final String URL_GETHOTPOTDETAILBYID=HOTPOT_SERVICE_URL+"HotPotService/HotPotDetailProvider.svc";
	

	public static final String SERVICE_URL = "http://103.10.85.178:8080/cgManage/";
	
	/*景区图片--缩略图*/
	public static final String DISTRICT_IMAGE_THUMB_URL=SERVICE_URL+"UploadFiles/hotpotimg/Thumb/";
	/*景区图片--原图*/
	public static final String DISTRICT_IMAGE_URL=SERVICE_URL+"UploadFiles/hotpotimg/";

	/*景点图片--缩略图*/
	public static final String HOTPOT_IMAGE_THUMB_URL=SERVICE_URL+"UploadFiles/hotpotimg/Thumb/";

	public static final String HOTPOT_VOICE_URL=SERVICE_URL+"UploadFiles/hotpotvoice/";
	public static final String OFFLINE_DISTRICT_MAP_ZIP_URL_STRING=SERVICE_URL+"UploadFiles/hopotplatchart/";
	
	public static final String WIDGET_IMAGE_PATH="iguide/maps/";
	public static final String WIDGET_OFFLINE_PATH="iguide/offline/";
	
	//安装包路径
	public static final String APK_URL = SERVICE_URL + "UploadFiles/HotPotSoft/";
	
	//离线包路径
	public static final String OFFLINE_ZIP_STRING=SERVICE_URL+"HotPotDistrictPackage/zip/";
	
	// 日期格式
			public final static String SIMPLE_DATA_FORMATE = "yyyy-MM-dd hh:mm:ss";
	/**
	 * soapaction名称
	 */
	public static final String SAOPACTION = "";
	/**
	 * 方法名
	 * 
	 */
	public static final String METHOD_NAME = "";
	/**
	 * des解密密钥
	 */
	public static final String DES_KEY = "j7Fe8Dt9";
	public static final String IFLY_TECK="=55d58fe0";//"="记得加上
	/**
	 * 百度地图的key
	 */
	public static final String MAP_KEY = "YXjXjihMckFtF9kmhA74QzYk";
	// public static final String MAP_KEY="qxKWfuWwIxr5lRZiMk43Df1s";

	public static final String SD_VOICE_PATH = "SceneryOnLine/Voice/";
	
	public static final String NETWORK_BROADCAST_FILTER="itg.network.deteceted";

}
