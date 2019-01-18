/**
* @FileName HotPotDistrictListTask.java
* @Package com.itg.httpRequest.asynctask
* @Description TODO
* @Author Alpha
* @Date 2015-9-2 上午11:40:58 
* @Version V1.0

*/
package com.itg.httpRequest.asynctask;

import java.util.ArrayList;
import java.util.List;
import com.itg.adapter.DistrictListAdapter;
import com.itg.bean.DistrictMap;
import com.itg.httpRequest.WebServiceUtil;
import com.itg.ui.fragment.IguideListFragment;
import com.itg.util.AppConfig;
import com.itg.util.DES_Encrypt;
import com.itg.util.MyApplication;

import android.os.AsyncTask;
import android.view.ViewGroup;

public class HotPotDistrictListTask extends AsyncTask<String, Void,List<DistrictMap> > {

	private List<DistrictMap> newDistrictList=new ArrayList<DistrictMap>();
	private List<DistrictMap> districtMapList;
	private DistrictListAdapter adapter;
	private ViewGroup headViewGroup;
	private int bouceHeight;
	public HotPotDistrictListTask(List<DistrictMap> districtMapList,DistrictListAdapter adapter,ViewGroup viewGroup) {
		this.districtMapList=districtMapList;
		this.adapter=adapter;
		this.headViewGroup=viewGroup;
	}
	
	@Override
	protected List<DistrictMap> doInBackground(String... params) {
		if (params[0].equals(IguideListFragment.class.toString())
				&& !params[1].equals("")) {
			String wsUrl = AppConfig.HOTPOT_SERVICE_URL
					+ "HotPotDistrictService/HpDistrictMapListByCity.svc?wsdl";
			String wsSoapAction = "http://tempuri.org/IHpDistrictMapListByCity/getDistrictOnMapByCity";
			String wsMethod = "getDistrictOnMapByCity";
			String wsPsString[] = { "cityName" };
			String param[] = { params[1] };
			String resultString = WebServiceUtil.GetWsMsg(wsUrl, wsSoapAction,
					wsMethod, wsPsString, param);
			String jsonResult = "";

			try {
				jsonResult = DES_Encrypt.decryptDES(resultString,
						AppConfig.DES_KEY);
				newDistrictList = (List<DistrictMap>) MyApplication.parseCollection(
						jsonResult, List.class, DistrictMap.class);
				districtMapList.clear();
				districtMapList.addAll(newDistrictList);
				if(!params[2].equals("")) bouceHeight=Integer.parseInt(params[2]);
				return districtMapList;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	}
	
	@Override
	protected void onPostExecute(List<DistrictMap> result) {
		super.onPostExecute(result);
		
		if(result!=null && result.size()>0)
		{
			adapter.notifyDataSetChanged();
		}
		if(headViewGroup!=null)
		{
			headViewGroup.setPadding(0, -bouceHeight, 0, 0);
		}
	}

}
