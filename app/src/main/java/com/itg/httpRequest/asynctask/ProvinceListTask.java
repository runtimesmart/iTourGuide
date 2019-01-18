/**
 * @FileName ProvinceListTask.java
 * @Package com.itg.httpRequest.asynctask
 * @Description TODO
 * @Author Alpha
 * @Date 2015-8-24 下午6:05:14 
 * @Version V1.0

 */
package com.itg.httpRequest.asynctask;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.itg.bean.HotPotDistrict;
import com.itg.bean.Province;
import com.itg.httpRequest.WebServiceUtil;
import com.itg.ui.activity.SearchActivity;
import com.itg.util.AppConfig;
import com.itg.util.DES_Encrypt;
import com.itg.util.JsonUtil;
import com.itg.util.MyApplication;

import android.os.AsyncTask;

public class ProvinceListTask extends AsyncTask<String, Void, List<Province>> {

	private List<Province> newList;
	private List<Province> provinceList = new ArrayList<Province>();

	@Override
	protected List<Province> doInBackground(String... params) {
		if (params[1].equals(SearchActivity.class.toString())) {
			String wsUrl = AppConfig.HOTPOT_SERVICE_URL
					+ "HotPotDistrictService/getAllCities.svc?wsdl";
			;
			String wsSoapAction = "http://tempuri.org/IgetAllCities/getAllCity.svc/getAllProvinces";
			String wsMethod = "getAllCities";
			String wsPsString[] = { "null" };
			String param[] = { "null" };
			String resultString = WebServiceUtil.GetWsMsg(wsUrl, wsSoapAction,
					wsMethod, wsPsString, param);
			String jsonResult = "";
			try {
				jsonResult = DES_Encrypt.decryptDES(resultString,
						AppConfig.DES_KEY);
				newList = (List<Province>) MyApplication.parseCollection(jsonResult,List.class,Province.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			provinceList.clear();
			provinceList.addAll(newList);
			return provinceList;
		}
		return null;
	}

	protected void onPostExecute(List<Province> result) {

	};

}
