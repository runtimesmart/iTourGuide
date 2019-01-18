/**
* @FileName DistrictAndHotPotInfoTask.java
* @Package com.itg.httpRequest.asynctask
* @Description TODO
* @Author Alpha
* @Date 2015-9-9 上午9:31:37 
* @Version V1.0

*/
package com.itg.httpRequest.asynctask;

import com.itg.adapter.HotpotListAdapter;
import com.itg.bean.District;
import com.itg.httpRequest.IDistrictAndHotPotCallback;
import com.itg.httpRequest.WebServiceUtil;
import com.itg.ui.activity.DistrictInfoActivity;
import com.itg.util.AppConfig;
import com.itg.util.DES_Encrypt;
import com.itg.util.JsonParser;
import android.os.AsyncTask;

public class DistrictAndHotPotInfoTask extends
		AsyncTask<String, Void, District> {

	private IDistrictAndHotPotCallback callBack;
	private HotpotListAdapter adapter;
	public DistrictAndHotPotInfoTask(IDistrictAndHotPotCallback callBack,HotpotListAdapter adapter) {
		this.callBack=callBack;
		this.adapter=adapter;
	}
	
	
	@Override
	protected District doInBackground(String... params) {
		if(params[1].equals(DistrictInfoActivity.class.toString()))
		{
		String wsUrl = AppConfig.HOTPOT_SERVICE_URL
				+ "HotPotDistrictService/getHdDistrictInfoById.svc?wsdl";
		String wsSoapAction = "http://tempuri.org/IgetHdDistrictInfoById/getDistrictAndHotPotInfoByDid";
		String wsMethod = "getDistrictAndHotPotInfoByDid";
		String wsPsString[] = { "districtId" };
		String param[] = {params[0]};
		String resultString = WebServiceUtil.GetWsMsg(wsUrl, wsSoapAction,
				wsMethod, wsPsString, param);
		String jsonResult = "";
		try {
			jsonResult = DES_Encrypt.decryptDES(resultString,
					AppConfig.DES_KEY);
			//Log.i("info", "--->>"+jsonResult);
		District district=	JsonParser.ParserToList(jsonResult);
		return district;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		return null;
	}
	@Override
	protected void onPostExecute(District result) {
		super.onPostExecute(result);
		if(result!=null)
		{
			callBack.setDistrictPotInfo(result);			
			adapter.notifyDataSetChanged();
		}
	}
	

}
