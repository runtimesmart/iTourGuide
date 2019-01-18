/**
* @FileName OfflineZipVersionCheckTask.java
* @Package com.itg.httpRequest.asynctask
* @Description TODO
* @Author Alpha
* @Date 2015-10-27 上午10:44:09 
* @Version V1.0

*/
package com.itg.httpRequest.asynctask;

import com.itg.httpRequest.IOfflineVersionCallback;
import com.itg.httpRequest.WebServiceUtil;
import com.itg.util.AppConfig;
import com.itg.util.DES_Encrypt;

import android.os.AsyncTask;

public class OfflineZipVersionCheckTask extends
		AsyncTask<Integer, Void, Integer> {
	private IOfflineVersionCallback callback;
	public OfflineZipVersionCheckTask(IOfflineVersionCallback callback) {
		this.callback=callback;
	}
	@Override
	protected Integer doInBackground(Integer... params) {
		if(params[0]==0){
			return 0;
		}
		String wsUrl = AppConfig.HOTPOT_SERVICE_URL + "HotPotDistrictService/getOfflineVersion.svc?wsdl";
		String wsSoapAction = "http://tempuri.org/IgetOfflineVersion/getOfflineZipVersion";
		String wsMethod = "getOfflineZipVersion";
		String wsPsString[] = { "distrcitId" };
		String param[] = {  params[0].toString() };
		String resultString = WebServiceUtil.GetWsMsg(wsUrl, wsSoapAction, wsMethod, wsPsString, param);
		try {
			String result=DES_Encrypt.decryptDES(resultString,AppConfig.DES_KEY);
			return Integer.parseInt(result) ;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		if(result!=null)
		callback.setVersion(result);
	}

}
