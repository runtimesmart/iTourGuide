/**
 * @FileName HotpotXmlParser.java
 * @Package com.itg.xml
 * @Description TODO
 * @Author Alpha
 * @Date 2015-10-28 下午1:51:53 
 * @Version V1.0

 */
package com.itg.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.itg.bean.HotPot;
import com.itg.bean.HotpotConfig;

public class HotpotXmlParser {


	public List<HotPot> hotpotXmlParser(InputStream inputStream) throws Exception {
		XmlPullParserFactory pullFactory = XmlPullParserFactory.newInstance();
		XmlPullParser xmlParser = pullFactory.newPullParser();
		xmlParser.setInput(inputStream, "UTF-8");
		List<HotPot> hotpotConfigs=null;
		HotPot hotConfig=null;
		int eventType = xmlParser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				hotpotConfigs=new ArrayList<HotPot>();
				break;
			case XmlPullParser.START_TAG:
				if("hotPot".equalsIgnoreCase(xmlParser.getName()))
				{
					hotConfig=new HotPot();
					hotConfig.setHotpotId(Integer.parseInt(xmlParser.getAttributeValue(0)));
				}
				else if("title".equalsIgnoreCase(xmlParser.getName()))
				{
					hotConfig.setHotpotName(xmlParser.nextText());
				}
				else if("thumbImage".equalsIgnoreCase(xmlParser.getName()))
				{
					hotConfig.setHotpotImageName(xmlParser.nextText().replace("HotPotDistrictPackage/ScenicAreaOffline/", ""));
				}
				else if("voice".equalsIgnoreCase(xmlParser.getName()))
				{
					hotConfig.setHotPotVoiceName(xmlParser.nextText().replace("HotPotDistrictPackage/ScenicAreaOffline/", ""));
				}
//				else if("description".equalsIgnoreCase(xmlParser.getName()))
//				{
//					hotConfig.set(xmlParser.nextText());
//				}
				else if("latitude".equalsIgnoreCase(xmlParser.getName()))
				{
					hotConfig.setLantitude(xmlParser.nextText());
				}
				else if("longitude".equalsIgnoreCase(xmlParser.getName()))
				{
					hotConfig.setLongtitude(xmlParser.nextText());
				}
				break;
			case XmlPullParser.END_TAG:
				if("hotPot".equalsIgnoreCase(xmlParser.getName()))
				{
				hotpotConfigs.add(hotConfig);
				hotConfig=null;
				}

				break;
		
			}
			eventType = xmlParser.next();
		}
		inputStream.close();
		return hotpotConfigs;

	}

}
