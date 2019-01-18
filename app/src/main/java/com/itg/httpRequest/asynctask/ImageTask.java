/**
 * @FileName HotPotDistrctImageTask.java
 * @Package com.itg.httpRequest.asynctask
 * @Description TODO
 * @Author Alpha
 * @Date 2015-9-6 下午2:26:00 
 * @Version V1.0

 */
package com.itg.httpRequest.asynctask;

import com.itg.adapter.DistrictListAdapter;
import com.itg.adapter.HotpotListAdapter;
import com.itg.ui.activity.DistrictInfoActivity;
import com.itg.ui.view.mapwidget.Popup;
import com.itg.util.AppConfig;
import com.itg.util.LRUCache;
import com.itg.util.MyApplication;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

public class ImageTask extends AsyncTask<String, Void, Bitmap> {

	private ImageView hdImage;
	private LRUCache bitMapCache;
	private MyApplication mApplication=new MyApplication();
	public ImageTask(ImageView hdImage, LRUCache bitMapCache) {
		this.hdImage = hdImage;
		this.bitMapCache = bitMapCache;
	}

	@Override
	protected Bitmap doInBackground(String... params)  {
		Bitmap bitmap = null;
		if (params[0].equals("")) {
			return null;
		}
		//景区详细中景区图片
		if(bitMapCache==null)
		{
			if (params[1].equals(DistrictInfoActivity.class.toString())) {
				try {
					bitmap = mApplication.getBitMap(AppConfig.DISTRICT_IMAGE_URL
							+ params[0]);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
			else if(params[1].equals(Popup.class.toString()))
			{
				try {
					bitmap = mApplication.getBitMap(AppConfig.HOTPOT_IMAGE_THUMB_URL
							+ params[0]);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else {
			//景区列表中景区图片
			if (params[1].equals(DistrictListAdapter.class.toString())) {
				
				if (bitMapCache.getCache(params[0]) == null) {
					try {
						bitmap = mApplication.getBitMap(AppConfig.DISTRICT_IMAGE_THUMB_URL
								+ params[0]);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					bitMapCache.putCache(params[0], bitmap);
				} else {
					bitmap = bitMapCache.getCache(params[0]);
				}

			}
			//景点列表景点图片
			else if(params[1].equals(HotpotListAdapter.class.toString())) {
				if (bitMapCache.getCache(params[0]) == null) {
					try {
						bitmap = mApplication.getBitMap(AppConfig.HOTPOT_IMAGE_THUMB_URL
								+ params[0]);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					bitMapCache.putCache(params[0], bitmap);
				} else {
					bitmap = bitMapCache.getCache(params[0]);
				}
			}
		}
	
		return bitmap;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);
		if (result != null) {
			hdImage.setImageBitmap(result);
		}
	}

}
