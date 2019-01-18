package com.itg.httpRequest.asynctask;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.itg.bean.City;
import com.itg.bean.UpdateAPKBean;
import com.itg.httpRequest.WebServiceUtil;
import com.itg.ui.view.Loading;
import com.itg.util.AppConfig;
import com.itg.util.DES_Encrypt;
import com.itg.util.JsonUtil;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class UpdateAPKTask extends AsyncTask<String, Void, UpdateAPKBean> {
	private Context mContext;
	private String id;
	private String name;
	private String size;
	private UpdateAPKBean bean;
	
	public UpdateAPKTask(Context mContext) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
	}
	
	@Override
	protected UpdateAPKBean doInBackground(String... params) {
		// TODO Auto-generated method stub
		String UDUrl = AppConfig.HOTPOT_SERVICE_URL + "OtherService/VersionUpdate.svc?wsdl";
		String UDSoapAction = "http://tempuri.org/IVersionUpdate/VersionCheck";
		String UDMethod = "VersionCheck";
		String UDPsString[] = { "null" };
		String param[] = { "null" };
		String resultString = WebServiceUtil.GetWsMsg(UDUrl, UDSoapAction,
				UDMethod, UDPsString, param);
		String jsonResult = "";
		JSONObject jsonObject = null;
		try {
			jsonResult = DES_Encrypt.decryptDES(resultString,
					AppConfig.DES_KEY);
			JSONArray jsonArray = new JSONArray(jsonResult);
			for(int i = 0; i < jsonArray.length(); i++){
				jsonObject = jsonArray.getJSONObject(i);
				id = jsonObject.getString("VersionId");
				name = jsonObject.getString("SoftName");
				size = jsonObject.getString("FileSize");
			}
			 bean = new UpdateAPKBean();
			bean.setFileSize(size);
			bean.setSoftName(name);
			bean.setVersionId(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		Log.i("tag", bean.getFileSize());
//		Log.i("tag", bean.getSoftName());
//		Log.i("tag", bean.getVersionId());
		return bean;
	}
	@Override
	protected void onPostExecute(UpdateAPKBean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(result != null){
		}
	}
}
