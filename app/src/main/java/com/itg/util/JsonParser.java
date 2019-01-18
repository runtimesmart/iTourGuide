package com.itg.util;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.itg.bean.District;
import com.itg.bean.HotPot;

import android.util.Log;

/**
 * Json结果解析类
 */
public class JsonParser {

	public static String parseIatResult(String json) {
		StringBuffer ret = new StringBuffer();
		try {
			JSONTokener tokener = new JSONTokener(json);
			JSONObject joResult = new JSONObject(tokener);

			JSONArray words = joResult.getJSONArray("ws");
			for (int i = 0; i < words.length(); i++) {
				// 转写结果词，默认使用第一个结果
				JSONArray items = words.getJSONObject(i).getJSONArray("cw");
				JSONObject obj = items.getJSONObject(0);
				ret.append(obj.getString("w"));
				// 如果需要多候选结果，解析数组其他字段
				// for(int j = 0; j < items.length(); j++)
				// {
				// JSONObject obj = items.getJSONObject(j);
				// ret.append(obj.getString("w"));
				// }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret.toString();
	}

	public static District ParserToList(String json) {
		District district = new District();
		try {
			JSONArray jArray = new JSONArray(json);
			JSONObject jObject = (JSONObject) jArray.opt(0);
			JSONObject jObject2 = (JSONObject) jObject.opt("Key");
			
			district.setDistrictId(jObject2.getInt("districtId"));
			district.setDistrictName(jObject2.getString("districtName"));
			district.setDistrictMapName(jObject2.getString("districtMapName"));
			district.setDistrictImage(jObject2.getString("districtImage"));
			district.setDistrictDescription(jObject2
					.getString("districtDescription"));
			district.setDistrictInnerline(jObject2
					.getString("districtInnerline"));
			district.setDistrictTicket(jObject2.getString("districtTicket"));
			district.setDistirctTraffic(jObject2.getString("districtTraffic"));
			district.setDistrictOfflineId(jObject2.getInt("districtOfflineId"));
			district.setTimeStamp(jObject2.getInt("timeStamp"));

			JSONArray jArray2 = (JSONArray) jObject.opt("Value");
			List<HotPot> hotpotList = new ArrayList<HotPot>();
			for (int i = 0; i < jArray2.length(); i++) {
				JSONObject hotpotObject = (JSONObject) jArray2.get(i);
				HotPot hotPot = new HotPot();
				hotPot.setHotpotId(hotpotObject.getInt("hotpotId"));
				hotPot.setHotpotName(hotpotObject.getString("hotpotName"));
				hotPot.setHotpotImageName(hotpotObject
						.getString("hotpotImageName"));
				hotPot.setHotPotVoiceName(hotpotObject
						.getString("hotpotVoiceName"));
				hotpotList.add(hotPot);
			}
			district.setHotPot(hotpotList);
			return district;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return district;

	}

}
