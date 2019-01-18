/**
* @FileName FetchGaussImageTask.java
* @Package com.itg.httpRequest.asynctask
* @Description TODO
* @Author Alpha
* @Date 2015-9-16 下午3:07:24 
* @Version V1.0

*/
package com.itg.httpRequest.asynctask;

import com.itg.httpRequest.IGaussBgCallback;
import com.itg.ui.activity.HotpotInfoActivity;
import com.itg.util.AppConfig;
import com.itg.util.MyApplication;
import com.itg.util.SDFileUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class FetchGaussImageTask extends AsyncTask<String, Void, Bitmap> {

	private IGaussBgCallback callBack;
	private boolean isOffline;
	public FetchGaussImageTask(IGaussBgCallback callBack,boolean isOffline) {
		this.callBack=callBack;
		this.isOffline=isOffline;
	}
	@Override
	protected Bitmap doInBackground(String... params) {
		if(params[1].equals(HotpotInfoActivity.class.toString()))
		{
			if(!params[0].equals(""))
			{
				Bitmap bitmap=null;
			
				try {
					if(isOffline)
					{
						bitmap=BitmapFactory.decodeFile(new SDFileUtil().getSDCard()+AppConfig.WIDGET_OFFLINE_PATH+params[0]);
					}
					else {
						bitmap =new MyApplication().getBitMap(AppConfig.DISTRICT_IMAGE_URL+params[0]);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return bitmap;
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);
		if(result!=null)
		{
			callBack.setGaussBg(result);
		}
	}
}
