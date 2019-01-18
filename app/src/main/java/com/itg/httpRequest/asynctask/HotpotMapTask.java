/**
* @FileName HotpotMapTask.java
* @Package com.itg.httpRequest.asynctask
* @Description TODO
* @Author Alpha
* @Date 2015-9-25 下午3:10:05 
* @Version V1.0

*/
package com.itg.httpRequest.asynctask;

import java.util.List;
import com.itg.bean.HotpotForMap;
import com.itg.httpRequest.IHotpotMapCallback;
import com.itg.httpRequest.WebServiceUtil;
import com.itg.util.AppConfig;
import com.itg.util.DES_Encrypt;
import com.itg.util.MyApplication;

import android.os.AsyncTask;

public class HotpotMapTask extends AsyncTask<String, Void, List<HotpotForMap>> {

	private List<HotpotForMap> hotpotMapList;
	private IHotpotMapCallback callBack;
	public HotpotMapTask(List<HotpotForMap> hotpotMapList,IHotpotMapCallback callBack) {
		this.hotpotMapList=hotpotMapList;
		this.callBack=callBack;	
	}
	@Override
	protected List<HotpotForMap> doInBackground(String... params) {
		if(!params[0].equals(""))
		{
			String url = AppConfig.HOTPOT_SERVICE_URL + "HotPotService/HotPotInfoById.svc?wsdl";
			String soapAction = "http://tempuri.org/IHotPotInfoById/HotPotList";
			String methodName = "HotPotList";
			String pare[] = new String[] { "districtId" };
			String ps[] = new String[] { params[0] };
			String result =  WebServiceUtil.GetWsMsg(url, soapAction, methodName, pare, ps);
			try {
				String jsonResult = DES_Encrypt.decryptDES(result,
						AppConfig.DES_KEY);
				List<HotpotForMap> newHotpotMapList = (List<HotpotForMap>) MyApplication.parseCollection(
						jsonResult, List.class, HotpotForMap.class);
				hotpotMapList.clear();
				hotpotMapList.addAll(newHotpotMapList);
				return hotpotMapList;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(List<HotpotForMap> result) {
		super.onPostExecute(result);
		if(result!=null)
		{
			callBack.setHotpotMap(result);
		}
	}
	
	

}
