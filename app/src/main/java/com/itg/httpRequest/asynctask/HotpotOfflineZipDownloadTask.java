/**
* @FileName HotpotOfflineMapDownloadTask.java
* @Package com.itg.httpRequest.asynctask
* @Description TODO
* @Author Alpha
* @Date 2015-10-15 上午11:18:00 
* @Version V1.0

*/
package com.itg.httpRequest.asynctask;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipException;
import com.itg.bean.District;
import com.itg.bean.DownloadInfo;
import com.itg.db.DownloadDBUtil;
import com.itg.ui.view.NumberCircleBar;
import com.itg.util.AppConfig;
import com.itg.util.MyApplication;
import com.itg.util.SDFileUtil;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class HotpotOfflineZipDownloadTask extends Handler implements Runnable
		 {
	private NumberCircleBar circleBar;
	private boolean isSetToal=false;
	private Context context; 
	private DownloadDBUtil dbUtil;
	private  String offlinePathString;
	public int downloadLength=0;
	private DownloadInfo info;
	private District temDistrict;
	private HotPotMapZipTask mapZipTask;
	private ExecutorService executorServiceDownload=Executors.newFixedThreadPool(3);

	public HotpotOfflineZipDownloadTask(NumberCircleBar  circleBar,Context context
			,District district)
	{
		this.circleBar=circleBar;
		this.context=context;
		this.temDistrict=district;
		offlinePathString=AppConfig.OFFLINE_ZIP_STRING+district.getDistrictId()+".zip";
		initHandler();
		startThread();
	}
	
	public void startThread()
	{
		if(mapZipTask==null)
			if(temDistrict.getDistrictMapName().contains(".zip"))
			 mapZipTask= new HotPotMapZipTask(null, null, true,temDistrict.getDistrictMapName());
		executorServiceDownload.execute(this);	
	}
	
	private void databaseExector()
	{		

		executorServiceDownload.execute(new Runnable() {	
			@Override
			public void run() {
				info= dbUtil.query(temDistrict.getDistrictId());

				if(info==null)
				{
					synchronized (DownloadDBUtil.locker) {
						dbUtil.insert(offlinePathString,temDistrict.getDistrictName(),temDistrict.getDistrictId() ,downloadLength,contentLength,temDistrict.getTimeStamp());
					}
				}
				else {
				      if(downloadLength<=contentLength || info.getVersion()<temDistrict.getTimeStamp())
				      {
						synchronized (DownloadDBUtil.locker) {
						dbUtil.update(offlinePathString, temDistrict.getDistrictId(), downloadLength,contentLength,temDistrict.getTimeStamp());
					  }
					}
				}
			}
		});
	
	}
	
	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		switch (msg.what){
		case 1:
			if(!isSetToal)
			{					
				circleBar.setMax(contentLength);
				isSetToal=true;
			}
			else {
				
				int step=(int) Math.ceil((double)(2048*100.0d/contentLength*100.0d))==0.0 ? 1 :(int) Math.ceil((double)(2048*10000.0d/contentLength*100.0d)) ;
				circleBar.incrementProgressBy(step);
				if(downloadLength>=contentLength)
				{
					downloadLength=contentLength;
				}
				else if( downloadLength<contentLength)
				{
					downloadLength+=1024;
				}
				
				if(info!=null &&info.getDownloadLength()==contentLength && info.getVersion()==temDistrict.getTimeStamp())
				{
					circleBar.incrementProgressBy(step);
					executorServiceDownload.shutdown();
					return;
				}
				if(!executorServiceDownload.isShutdown())
				databaseExector();

			}					
			break;	
		case -1:
			//circleBar.incrementProgressBy(contentLength);
			if(downloadLength!=contentLength)
			downloadLength=contentLength;
			executorServiceDownload.shutdown();
			circleBar.clearAnimation();
			break;
		}
	}
	
	private void initHandler()
	{
		dbUtil=new DownloadDBUtil(context.getApplicationContext());
	}
	
	@Override
	public void run() {
		zipDownloader();
	}

	/**
	 * @return 0 网络流错误 1 下载未完成 2 解压失败 3 完成
	 * **/

	private Integer zipDownloader() {
		int ret=0;
		if(temDistrict.getDistrictId()!=0)
		{		
			
			InputStream inputStream=getHttpInputStream(offlinePathString);
			if(inputStream==null)
			{
				ret= 0;
			}
			else {
				SDFileUtil fileUtil= new SDFileUtil();
				String storePath=fileUtil.getSDCard()+AppConfig.WIDGET_OFFLINE_PATH+temDistrict.getDistrictId()+"/";
				if(!fileUtil.IsFileExists(storePath))
					fileUtil.doMakeDir(storePath);
				String filePathString=storePath+temDistrict.getDistrictId()+".zip";

				
			    new Timer().schedule(new TimerTask() {
					
					@Override
					public void run() {
						if(circleBar.isFinished() && downloadLength>=contentLength)
						{
							HotpotOfflineZipDownloadTask.this.sendEmptyMessage(-1);
							HotpotOfflineZipDownloadTask.this.removeCallbacks(this);		
						}
						else {
							HotpotOfflineZipDownloadTask.this.sendEmptyMessage(1);			
						}
					}
				},0,1);
				
				boolean isFinish=new MyApplication().downloadFile(inputStream, filePathString);
				if(isFinish)
				{
					int result = 0;
					try {
						result =new MyApplication().upZipFile(new File(filePathString), storePath);
					} catch (ZipException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(result==1)
					{
						MyApplication.deleteFile(new File(filePathString));
						ret=3;
					}
					else {
						ret=2;
					}
				}
				else 
				{
					ret=1;
				}
			}
		}
	
		return ret;
	}


	private int contentLength;
	private InputStream getHttpInputStream(String path) {
		InputStream inputStream = null;
		HttpURLConnection httpUrlConnection = null;
		try {
			URL url = new URL(path);
			httpUrlConnection = (HttpURLConnection) url.openConnection();
			httpUrlConnection.setRequestMethod("GET");
			httpUrlConnection.setReadTimeout(20000);
			httpUrlConnection.setConnectTimeout(20000);
			httpUrlConnection.setDoInput(true);
			httpUrlConnection.connect();
			int code = httpUrlConnection.getResponseCode();
			if (code == HttpURLConnection.HTTP_OK) {
					 contentLength = httpUrlConnection.getContentLength();

				inputStream = httpUrlConnection.getInputStream();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
		return inputStream;

	}

}
