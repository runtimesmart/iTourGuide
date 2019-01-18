package com.itg.httpRequest.asynctask;

import com.itg.httpRequest.WebServiceUtil;
import com.itg.ui.activity.LoginActivity;
import com.itg.util.AppConfig;


import android.os.AsyncTask;

public class RequestGetSMSTask extends AsyncTask<String, String, String>{

	private String codeString;
	@Override
	protected String doInBackground(String... param) {
		// TODO Auto-generated method stub
		String SMSurl = AppConfig.HOTPOT_SERVICE_URL + "NewAccount/UserMobileVerify.svc?wsdl";
		String SMSSoapAction = "http://tempuri.org/IUserMobileVerify/verifyMobileByCode";
		String SMSmethod = "verifyMobileByCode";
		String SMSstring[] = {"mobileNo", "deviceId"};
		String params[] = {param[0],param[1]};
		//获取数据
		String msgJson = WebServiceUtil.GetWsMsg(SMSurl, SMSSoapAction, SMSmethod, SMSstring, params);
		return msgJson;
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(!result.equals("")){
			codeString=result;
		}
	}
}
