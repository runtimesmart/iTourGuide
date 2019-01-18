/**
* @FileName HotpotDistrictInfoTask.java
* @Package com.itg.httpRequest.asynctask.offlineline
* @Description TODO
* @Author Alpha
* @Date 2015-10-28 上午11:29:38 
* @Version V1.0

*/
package com.itg.httpRequest.asynctask.offlineline;

import java.io.InputStream;
import java.util.List;

import com.itg.bean.District;
import com.itg.bean.HotPot;
import com.itg.util.AppConfig;
import com.itg.util.MyApplication;
import com.itg.util.SDFileUtil;
import com.itg.xml.DistrictXmlParser;
import com.itg.xml.HotpotXmlParser;

import android.os.AsyncTask;

public class HotpotDistrictInfoTask extends AsyncTask<String, Void, Boolean> {

	private HotpotDistrictInfoCallback callback;
	private  List<District> districtConfigs;
	private List<HotPot> hotpotConfigs;
	
	public HotpotDistrictInfoTask(HotpotDistrictInfoCallback callback) {
		this.callback=callback;
	}
	@Override
	protected Boolean doInBackground(String... params) {
		if(!"".equals(params[0]))
		{
			 DistrictXmlParser dParser=new DistrictXmlParser();
			 HotpotXmlParser hParser=new HotpotXmlParser();
			 String sdPathString=new SDFileUtil().getSDCard()+AppConfig.WIDGET_OFFLINE_PATH;
			
			 try {
				 InputStream inputStream=MyApplication.getLocalInputStream(sdPathString+params[0]+"/config.xml");
				 districtConfigs= dParser.districtConfigParser(inputStream);
				 InputStream hotpotInputStream=MyApplication.getLocalInputStream(sdPathString+districtConfigs.get(0).getHotpotPath());
				  hotpotConfigs=hParser.hotpotXmlParser(hotpotInputStream);
				 if(null!=districtConfigs && null!=hotpotConfigs) return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	@Override
	protected void onPostExecute(Boolean result) {
		if(result)
		{
			callback.setHotpotDistrictInfo(districtConfigs, hotpotConfigs);
		}
		
	}

}
