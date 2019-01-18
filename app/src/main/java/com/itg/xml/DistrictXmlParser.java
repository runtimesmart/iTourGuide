/**
* @FileName DistrictXmlParser.java
* @Package com.itg.xml
* @Description TODO
* @Author Alpha
* @Date 2015-10-28 上午9:26:26 
* @Version V1.0

*/
package com.itg.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.itg.bean.District;
import com.itg.bean.HotPotDistrictConfig;

public class DistrictXmlParser {

	public DistrictXmlParser() {
	}
	public  List<District> districtConfigParser(InputStream inputStream) throws Exception {
		XmlPullParserFactory pullFactory=XmlPullParserFactory.newInstance();
		
			 XmlPullParser pullParser= pullFactory.newPullParser();
			 pullParser.setInput(inputStream, "UTF-8");
			 List<District> districtConfigs = null;
			 District districtConfig = null;
			 int eventType= pullParser.getEventType();
			 while (eventType!=XmlPullParser.END_DOCUMENT) {
			 switch (eventType) {
			 case XmlPullParser.START_DOCUMENT:
				 districtConfigs=new ArrayList<District>();
			 break;
			case XmlPullParser.START_TAG:
				
				if("title".equalsIgnoreCase(pullParser.getName()))
				{
					districtConfig=new District();
					districtConfig.setDistrictName(pullParser.nextText());
				}
				else if("latitude".equalsIgnoreCase(pullParser.getName()))
				{
					districtConfig.setLatitude(pullParser.nextText());
				}
				else if("longitude".equalsIgnoreCase(pullParser.getName()))
				{
					districtConfig.setLongtitude(pullParser.nextText());
				}
				else if("item1".equalsIgnoreCase(pullParser.getName()))
				{
					String desTextString=pullParser.nextText().replace("HotPotDistrictPackage/ScenicAreaOffline/", "");
					districtConfig.setDistrictDescription(desTextString);
					districtConfig.setDistrictId( Integer.parseInt(desTextString.substring(0,desTextString.indexOf("/"))));
				
				}
				else if("item2".equalsIgnoreCase(pullParser.getName()))
				{
					districtConfig.setDistrictInnerline(pullParser.nextText().replace("HotPotDistrictPackage/ScenicAreaOffline/", ""));
				}
//				else if("item3".equalsIgnoreCase(pullParser.getName()))
//				{
//					districtConfig.setdi(pullParser.nextText().replace("HotPotDistrictPackage/ScenicAreaOffline/", ""));
//				}
				else if("item4".equalsIgnoreCase(pullParser.getName()))
				{
					districtConfig.setDistrictTicket(pullParser.nextText().replace("HotPotDistrictPackage/ScenicAreaOffline/", ""));
				}
				else if("item5".equalsIgnoreCase(pullParser.getName()))
				{
					districtConfig.setDistirctTraffic(pullParser.nextText().replace("HotPotDistrictPackage/ScenicAreaOffline/", ""));
				}
				else if("mapname".equalsIgnoreCase(pullParser.getName()))
				{
					districtConfig.setDistrictMapName(pullParser.nextText());
				}
				else if("originalImage".equalsIgnoreCase(pullParser.getName()))
				{
					districtConfig.setDistrictImage(pullParser.nextText().replace("HotPotDistrictPackage/ScenicAreaOffline/", ""));
				}
				else if("voice".equalsIgnoreCase(pullParser.getName()))
				{
					districtConfig.setVoice(pullParser.nextText().replace("HotPotDistrictPackage/ScenicAreaOffline/", ""));
				}
				else if("hotPotXmlPath".equalsIgnoreCase(pullParser.getName()))
				{
				    districtConfig.setHotpotPath(pullParser.nextText().replace("HotPotDistrictPackage/ScenicAreaOffline/", ""));
				}
				
				break;
			case XmlPullParser.END_TAG:
				if("scenic".equalsIgnoreCase(pullParser.getName()))
				{
					districtConfigs.add(districtConfig);
					districtConfig=null;

				}
				break;
			}
				eventType=pullParser.next();
		}	
			inputStream.close();
			return districtConfigs;
	}

}
