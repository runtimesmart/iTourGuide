/**
* @FileName HotpotDistrictOfflineZipCheckTask.java
* @Package com.itg.httpRequest.asynctask
* @Description TODO
* @Author Alpha
* @Date 2015-10-23 下午2:37:04 
* @Version V1.0

*/
package com.itg.httpRequest.asynctask;


import com.itg.bean.DownloadInfo;
import com.itg.db.DownloadDBUtil;
import com.itg.httpRequest.IOfflineZipCheck;
import com.itg.iguide.R;
import com.itg.ui.activity.DistrictInfoActivity;
import com.itg.util.AppConfig;
import com.itg.util.SDFileUtil;



import android.os.AsyncTask;
import android.widget.ImageView;

public class HotpotDistrictOfflineZipCheckTask extends
		AsyncTask<Integer, Void, Integer> {
	private ImageView downloadBtn;
	private DistrictInfoActivity context;
	private int newVersion;
	private IOfflineZipCheck zipCheckCallback;

	public HotpotDistrictOfflineZipCheckTask(ImageView downloadBtn,DistrictInfoActivity context,int newVersion,IOfflineZipCheck zipCheckCallback) {
		this.downloadBtn=downloadBtn;
		this.context=context;
		this.newVersion=newVersion;
		this.zipCheckCallback=zipCheckCallback;
	}
	@Override
	protected Integer doInBackground(Integer... params) {
		if(params[0]==0)
			return 0;
		else {
			DownloadDBUtil dbUtil=new DownloadDBUtil(context.getApplicationContext());
			SDFileUtil fileUtil=new SDFileUtil();
			boolean isConfigExist =fileUtil.IsFileExists(fileUtil.getSDCard()+ AppConfig.WIDGET_OFFLINE_PATH+params[0]+"/config.xml");
			DownloadInfo info=dbUtil.query(params[0]);
			if(info==null)
			return -1;
			else {
				if(info.getDownloadLength()<info.getFilelength())
				{
					return 1;//未下载完整
				}
				else if(info.getDownloadLength()==info.getFilelength() && isConfigExist)
				{
					if(info.getVersion()<newVersion)
					{
						return 3; //zip有更新
					}
					return 2;
				}
			}
		}
		return -1;
	}
	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		if(result==null)
			return;
		switch (result) {
		case 0:
			downloadBtn.setImageResource(R.drawable.list_xq_download);
			break;

		case -1:
			zipCheckCallback.isOfflineZipDownloaded(false);
			downloadBtn.setImageResource(R.drawable.list_xq_download);
			break;
		case 1:
			zipCheckCallback.isOfflineZipDownloaded(false);
			downloadBtn.setImageResource(R.drawable.list_xq_download);
			break;
		case 2:
			zipCheckCallback.isOfflineZipDownloaded(true);
			downloadBtn.setImageResource(R.drawable.download_conpeleted);
			downloadBtn.setClickable(false);
			break;
		case 3:
			zipCheckCallback.isOfflineZipDownloaded(true);
			downloadBtn.setImageResource(R.drawable.downloat_update);
			break;
		}
	}

}
