/**
 * @FileName HotpotListAdapter.java
 * @Package com.itg.adapter
 * @Description TODO
 * @Author Alpha
 * @Date 2015-9-9 下午7:01:58 
 * @Version V1.0

 */
package com.itg.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itg.bean.HotPot;
import com.itg.httpRequest.asynctask.ImageTask;
import com.itg.iguide.R;
import com.itg.util.AppConfig;
import com.itg.util.LRUCache;
import com.itg.util.SDFileUtil;
import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class HotpotListAdapter implements ListAdapter {

	private List<HotPot> hotpotList;
	private DataSetObservable mDataSetObservable = new DataSetObservable();
	private Context context;
	private LRUCache lruCache = new LRUCache();
	private LRUCache offCache=new LRUCache();
	private Map<Integer, View> cacheMap = new HashMap<Integer, View>();

	public HotpotListAdapter(List<HotPot> hotpotList, Context context) {
		this.hotpotList = hotpotList;
		this.context = context;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		mDataSetObservable.registerObserver(observer);
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		mDataSetObservable.unregisterObserver(observer);
	}

	@Override
	public int getCount() {
		return hotpotList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewCache viewCache = null;
		if (cacheMap.get(position) == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.iguide_district_info_hotpot_item, null);
			viewCache = new ViewCache();
			viewCache.hotpotTitle = (TextView) convertView
					.findViewById(R.id.iguide_hotpot_title);
			viewCache.hotpotImage = (ImageView) convertView
					.findViewById(R.id.iguide_hotpot_image);
			viewCache.hotpotVoiceIcon=(ImageView) convertView.findViewById(R.id.iguide_hotpot_voice);
			convertView.setTag(viewCache);
			cacheMap.put(position, convertView);
		} else {
			/*两个对象必须实例化*/
			convertView = cacheMap.get(position);
			viewCache=(ViewCache) convertView.getTag();
		}
		HotPot hotpot = hotpotList.get(position);
		if(!hotpot.getHotPotVoiceName().equals(""))
		{
			viewCache.hotpotVoiceIcon.setImageResource(R.drawable.list_xq_horn);
		}
		String imageString=hotpot.getHotpotImageName();
		if(imageString.contains("/"))//来自本地
		{
			if(offCache.getCache(imageString)==null)
			{
				Bitmap bitmap=BitmapFactory.decodeFile(new SDFileUtil().getSDCard()+AppConfig.WIDGET_OFFLINE_PATH+imageString);
				viewCache.hotpotImage.setImageBitmap(bitmap);
				offCache.putCache(imageString, bitmap);
			}
			else {
				viewCache.hotpotImage.setImageBitmap(offCache.getCache(imageString));
			}
		}
		else if(imageString.contains(","))
		{
		String[] images=imageString.split(",");
		imageString=images[1];
		new ImageTask(viewCache.hotpotImage, lruCache).execute(imageString,HotpotListAdapter.class.toString());
		}	
		else 
		{
			new ImageTask(viewCache.hotpotImage, lruCache).execute(imageString,HotpotListAdapter.class.toString());
		}
		
		viewCache.hotpotTitle.setText(hotpot.getHotpotName());

		return convertView;
	}

	private class ViewCache {
		private ImageView hotpotImage,hotpotVoiceIcon;
		private TextView hotpotTitle;
	}

	@Override
	public int getItemViewType(int position) {
		return 0;
	}

	@Override
	public int getViewTypeCount() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}

	public void notifyDataSetChanged() {
		mDataSetObservable.notifyChanged();
	}

}
