/**
* @FileName HotPotMapZipTask.java
* @Package com.itg.httpRequest.asynctask
* @Description TODO
* @Author Alpha
* @Date 2015-9-25 下午4:05:04 
* @Version V1.0

*/
package com.itg.httpRequest.asynctask;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipException;
import com.itg.httpRequest.IMapZipCallback;
import com.itg.ui.view.Loading;
import com.itg.util.AppConfig;
import com.itg.util.MyApplication;
import com.itg.util.SDFileUtil;

import android.content.Context;
import android.os.Handler;
import android.os.Message;


public class HotPotMapZipTask extends Handler  implements Runnable  {

	private IMapZipCallback callback;
	private Context mContext;
	private boolean isPassived;
	private String mapName;
	private ExecutorService executorServiceDownload=Executors.newFixedThreadPool(3);
	public HotPotMapZipTask(IMapZipCallback callback, Context mContext,boolean isPassived,String mapName) {
		this.callback=callback;
		this.mContext = mContext;
		this.isPassived=isPassived;
		this.mapName=mapName;
		onPreExecute();
		startThread();
	}
	
	private void startThread()
	{
		executorServiceDownload.execute(this);
	}
@Override
public void run() {
	boolean result= doInBackground();
	if(result)
	{		
		this.sendEmptyMessage(9);
	}
}


	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		if(msg.what==9)
		{
			onPostExecute(true);
			executorServiceDownload.shutdown();
			this.removeCallbacks(this);
		}
	}
	
	
	
	
	
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		if(!isPassived)
		Loading.startLoading(mContext);
	}
	
	protected Boolean doInBackground(String... params) {
		
		if(!mapName.equals(""))
		{	
			String pathString=AppConfig.OFFLINE_DISTRICT_MAP_ZIP_URL_STRING+mapName;
			InputStream inputStream = null;
			try {
				inputStream = MyApplication.getHttpInputStream(pathString);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}		
			if(inputStream!=null)
			{	
				//handlerSendMsg(0);
				SDFileUtil fileUtil= new SDFileUtil();
				String storePath=fileUtil.getSDCard()+AppConfig.WIDGET_IMAGE_PATH;
				String filePath=storePath+mapName;
				if(!fileUtil.IsFileExists(storePath))
					fileUtil.doMakeDir(storePath);
				boolean result= new MyApplication().downloadFile(inputStream, filePath);
				 if(result)
				 {
					 //handlerSendMsg(1);
					 int ur = 0;
					try {
						if(!filePath.contains(".zip")) return false;
						ur =  new MyApplication().upZipFile(new File(filePath), storePath);
					} catch (ZipException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(ur==1)
					{
						//handlerSendMsg(2);
						boolean dr= MyApplication.deleteFile(new File(filePath));
//						if(dr)handlerSendMsg(3);
//						else handlerSendMsg(-3);
						return true;
					}
					else
					{
						//handlerSendMsg(-2);
						return false;
					}
				 }
				 else {
					// handlerSendMsg(-1);
					return false;
				}

			}
		}
		return true;
		
	}
	
//	private void handlerSendMsg(int code)
//	{
//		if(mHandler!=null)
//		 mHandler.sendEmptyMessage(code);
//	}

	protected void onPostExecute(Boolean result) {
		//super.onPostExecute(result);
		if(result && !isPassived)
		{
			Loading.stopLoading();
			callback.finishHandleMapZip();
		}
		else if(!result) {
			
		}
	}

}
