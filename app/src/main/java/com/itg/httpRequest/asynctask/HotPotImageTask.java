/**
* @FileName HotPotImageTask.java
* @Package com.itg.httpRequest.asynctask
* @Description TODO
* @Author Alpha
* @Date 2015-9-18 下午4:56:23 
* @Version V1.0

*/
package com.itg.httpRequest.asynctask;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import com.itg.httpRequest.IHotPotImageCallback;
import com.itg.ui.view.Loading;
import com.itg.util.AppConfig;
import com.itg.util.MyApplication;
import com.itg.util.SDFileUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class HotPotImageTask extends AsyncTask<String, Void, List<Bitmap>> {

	private IHotPotImageCallback callBack;
	//private InfiniteLooperPageAdapterContainer adapter;
	private  List<ImageView> imageViews;
	private boolean isOffline;
	private String path;
	public HotPotImageTask(List<ImageView> imageViews, IHotPotImageCallback callBack,boolean isOffline,String path) {
		this.imageViews=imageViews;
		this.callBack=callBack;
		this.isOffline=isOffline;
		this.path=path;
	//	this.adapter=adapter;
	}
	@Override
	protected List<Bitmap> doInBackground(String... params) {
		if(params!=null)
		{
			 List<Bitmap> bitmapList = new ArrayList<Bitmap>();
			 WeakReference<List<Bitmap>> bitMapListWeakReference=new WeakReference<List<Bitmap>>(bitmapList);
			for (int i = 0; i < params.length; i++) {
				String pathString=AppConfig.DISTRICT_IMAGE_URL+params[i];
				try {
					if(isOffline)
					{
						bitMapListWeakReference.get().add(BitmapFactory.decodeFile(new SDFileUtil().getSDCard()+AppConfig.WIDGET_OFFLINE_PATH+path+params[i]));
					}
					else {
						bitMapListWeakReference.get().add(new MyApplication().getBitMap(pathString));
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//Bitmap bitmap=HttpUtil.getBitMap(AppConfig.DISTRICT_IMAGE_URL+params[i]);
				
			}
			return bitMapListWeakReference.get();
		}
		return null;
	}

	@Override
	protected void onPostExecute(List<Bitmap> result) {
		super.onPostExecute(result);
		
		if(result!=null && result.size()>0)
		{
//			imageViews.clear();
//			for (int i = 0; i < result.size(); i++) {
//				ImageView iView=new ImageView(context);
//				iView.setImageBitmap(result.get(i));
//				imageViews.add(iView);
//			}
			callBack.setHotPotImage(result);
			Loading.stopLoading();
	//	adapter.notifyDataSetChanged();
		}
	}
}
