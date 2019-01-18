/**
 * @FileName HotPotDistrictMap.java
 * @Package com.itg.httpRequest.asynctask
 * @Description TODO
 * @Author Alpha
 * @Date 2015-8-31 上午9:51:32 
 * @Version V1.0

 */
package com.itg.httpRequest.asynctask;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.itg.bean.DistrictMap;
import com.itg.httpRequest.IDistrictMapCallback;
import com.itg.httpRequest.WebServiceUtil;
import com.itg.util.AppConfig;
import com.itg.util.DES_Encrypt;
import com.itg.util.MyApplication;
import android.os.Handler;
import android.os.Message;

public class HotPotDistrictMapTask extends Handler implements Runnable {

	private List<DistrictMap> dMapList;
    private IDistrictMapCallback iCallBack;
    private String cityName;
    private ExecutorService executorService=Executors.newFixedThreadPool(1);
	public HotPotDistrictMapTask(IDistrictMapCallback iCallBack,String cityName) {
		this.iCallBack=iCallBack;
		this.cityName =cityName;
		startThread();
	}
	
	private void startThread()
	{
		executorService.execute(this);
	}
	@Override
	public void run() {
		dMapList= doInBackground();
		if(dMapList!=null) this.sendEmptyMessage(0);
	}
	
	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		if(msg.what==0) 
			{
			onPostExecute(dMapList);
			executorService.shutdown();
			this.removeCallbacks(this);
			}
	}

	protected List<DistrictMap> doInBackground() {
		if (!cityName.equals("")) {
			String wsUrl = AppConfig.HOTPOT_SERVICE_URL
					+ "HotPotDistrictService/HpDistrictMapListByCity.svc?wsdl";
			String wsSoapAction = "http://tempuri.org/IHpDistrictMapListByCity/getDistrictOnMapByCity";
			String wsMethod = "getDistrictOnMapByCity";
			String wsPsString[] = { "cityName" };
			String param[] = { cityName };
			String resultString = WebServiceUtil.GetWsMsg(wsUrl, wsSoapAction,
					wsMethod, wsPsString, param);
			String jsonResult = "";

			try {
				jsonResult = DES_Encrypt.decryptDES(resultString,
						AppConfig.DES_KEY);
				dMapList = (List<DistrictMap>) MyApplication.parseCollection(
						jsonResult, List.class, DistrictMap.class);
				return dMapList;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	}

	protected void onPostExecute(List<DistrictMap> result) {
		if (result != null && result.size()>0) {
			iCallBack.getDistrictMapInfo(result);
		}
	}




}
