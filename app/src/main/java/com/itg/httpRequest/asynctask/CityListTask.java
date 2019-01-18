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

import android.content.Context;
import android.os.AsyncTask;

import com.itg.bean.City;
import com.itg.httpRequest.WebServiceUtil;
import com.itg.ui.activity.HomeActivity;
import com.itg.ui.view.Loading;
import com.itg.ui.view.contact.ContactListAdapter;
import com.itg.util.AppConfig;
import com.itg.util.DES_Encrypt;
import com.itg.util.JsonUtil;
import com.itg.util.MyApplication;

public class CityListTask extends AsyncTask<String, Void, List<City>> {

	private List<City> newList=new ArrayList<City>();
	private List<City> cityList ;
    private ContactListAdapter adapter;
	public CityListTask(List<City> cityList,ContactListAdapter adapter)
	{
		this.cityList=cityList;
		this.adapter=adapter;
	}
	
	
	@Override
	protected List<City> doInBackground(String... params) {
		if (params[0].equals(HomeActivity.class.toString())) {
			String wsUrl = AppConfig.HOTPOT_SERVICE_URL
					+ "HotPotDistrictService/getAllCity.svc?wsdl";
			;
			String wsSoapAction = "http://tempuri.org/IGetAllCity/getAllCities";
			String wsMethod = "getAllCities";
			String wsPsString[] = { "null" };
			String param[] = { "null" };
			String resultString = WebServiceUtil.GetWsMsg(wsUrl, wsSoapAction,
					wsMethod, wsPsString, param);
			String jsonResult = "";
			try {
				jsonResult = DES_Encrypt.decryptDES(resultString,
						AppConfig.DES_KEY);
				newList = (List<City>) MyApplication.parseCollection(jsonResult,List.class,City.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cityList.clear();
			cityList.addAll(newList);
			Loading.stopLoading();
			return cityList;
		}
		return null;
	}

	protected void onPostExecute(List<City> result) {
		adapter.notifyDataSetChanged();
	};

}
