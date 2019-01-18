/**
 * @FileName HotPotDistrictSearchTask.java
 * @Package com.itg.httpRequest.asynctask
 * @Description TODO
 * @Author Alpha
 * @Date 2015-8-21 下午5:13:56 
 * @Version V1.0

 */
package com.itg.httpRequest.asynctask;

import java.util.ArrayList;
import java.util.List;

import com.itg.adapter.DistrictSearchAdapter;
import com.itg.bean.HotPotDistrict;
import com.itg.httpRequest.WebServiceUtil;
import com.itg.ui.activity.SearchActivity;
import com.itg.ui.view.Loading;
import com.itg.util.AppConfig;
import com.itg.util.DES_Encrypt;
import com.itg.util.MyApplication;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class HotPotDistrictSearchTask extends
		AsyncTask<String, Loading, List<HotPotDistrict>> {

	public DistrictSearchAdapter adapter;
	private List<HotPotDistrict> districtList;
	private List<HotPotDistrict> newList = new ArrayList<HotPotDistrict>();
	private Context mContext;

	public HotPotDistrictSearchTask(DistrictSearchAdapter adapter,
			List<HotPotDistrict> districtList, Context mContext) {
		this.adapter = adapter;
		this.districtList = districtList;
		this.mContext = mContext;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		Loading.startLoading(mContext);
	}

	@Override
	protected List<HotPotDistrict> doInBackground(String... params) {
		if (params[1].equals(SearchActivity.class.toString())) {
			String wsUrl = AppConfig.HOTPOT_SERVICE_URL + "HotPotDistrictService/HpDistrictAutoComplete.svc?wsdl";
			String wsSoapAction = "http://tempuri.org/IHotPotDistrictAutoComplete/HotPotDistrictNameAutoComplete";
			String wsMethod = "HotPotDistrictNameAutoComplete";
			String wsPsString[] = { "inputName" };
			String param[] = { params[0] };
			String resultString = WebServiceUtil.GetWsMsg(wsUrl, wsSoapAction, wsMethod, wsPsString, param);
			String jsonResult = "";
			try {
				
				jsonResult = DES_Encrypt.decryptDES(resultString, AppConfig.DES_KEY);
				newList = (List<HotPotDistrict>) MyApplication.parseCollection(jsonResult,List.class,HotPotDistrict.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			districtList.clear();
			districtList.addAll(newList);
			return districtList;
		}
		return null;
	}

	@Override
	protected void onPostExecute(List<HotPotDistrict> result) {
		super.onPostExecute(result);
		try {
			if(result.size() != 0){
				adapter.notifyDataSetChanged();
				Loading.stopLoading();
			} else {
				Toast.makeText(mContext, "您搜索的数据为空", Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onProgressUpdate(Loading... values) {
		super.onProgressUpdate(values);
	}

}
