/**
* @FileName HotPotDistrictSearchAdapter.java
* @Package com.itg.adapter
* @Description TODO
* @Author Alpha
* @Date 2015-8-21 下午7:02:30 
* @Version V1.0

*/
package com.itg.adapter;

import java.util.List;

import com.baidu.location.e.r;
import com.itg.bean.HotPotDistrict;
import com.itg.iguide.R;
import com.itg.ui.activity.DistrictInfoActivity;

import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DistrictSearchAdapter extends BaseAdapter {

	private List<HotPotDistrict> districtList;
	private Context context;
	public DistrictSearchAdapter(List<HotPotDistrict> dristrictList,Context context) {
		this.districtList=dristrictList;
		this.context=context;
	}

	@Override
	public int getCount() {
		return districtList.size();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewCache viewCache;
		final HotPotDistrict district;
		if(convertView==null)
		{
			convertView=LayoutInflater.from(context).inflate(R.layout.iguide_search_result, null);
			viewCache=new ViewCache();
			viewCache.distrctNameTextView=(TextView) convertView.findViewById(R.id.iguide_search_districtname);
			viewCache.provinceNameTextView=(TextView) convertView.findViewById(R.id.iguide_search_province);
			district=districtList.get(position);
			viewCache.distrctNameTextView.setText(district.getDistrictName());
			viewCache.provinceNameTextView.setText(district.getProvinceName());
			convertView.setTag(viewCache);
			return convertView;
		} else {
		viewCache=(ViewCache)convertView.getTag();
		}
		district = districtList.get(position);
		viewCache.distrctNameTextView.setText(district.getDistrictName());
		viewCache.provinceNameTextView.setText(district.getProvinceName());
		return convertView;
	}
	private class ViewCache
	{
		private TextView distrctNameTextView,provinceNameTextView;
		
	}

}
