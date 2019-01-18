/**
* @FileName DownloadService.java
* @Package com.itg.httpRequest
* @Description TODO
* @Author Alpha
* @Date 2015-11-11 上午9:20:51 
* @Version V1.0

*/
package com.itg.httpRequest;



import com.itg.httpRequest.asynctask.HotpotOfflineZipDownloadTask;

import com.itg.util.MyApplication;

import android.R.integer;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class DownloadService extends Service {

	public int currentSize;
	private Downbinder binder;
	public HotpotOfflineZipDownloadTask downloadTask;
	public class Downbinder extends Binder
	{
		public DownloadService getService()
		{
			return DownloadService.this;
		}
	}
	@Override
	public void onCreate() {
		super.onCreate();
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	MyApplication application=(MyApplication)getApplication();
		
	downloadTask=new HotpotOfflineZipDownloadTask(application.getCircleBar(), getApplicationContext(), application.getTemDistrict());
//		if(binder==null)
//			return new Downbinder();
//		return null;
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		MyApplication application=(MyApplication)getApplication();
		
		new HotpotOfflineZipDownloadTask(application.getCircleBar(), getApplicationContext(), application.getTemDistrict());
		if(binder==null)
			return new Downbinder();
	return null;
	}

}
