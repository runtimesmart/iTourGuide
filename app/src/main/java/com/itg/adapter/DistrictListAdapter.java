/**
 * @FileName DistrictListAdapter.java
 * @Package com.itg.adapter
 * @Description TODO
 * @Author Alpha
 * @Date 2015-9-1 下午4:45:28 
 * @Version V1.0

 */
package com.itg.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itg.bean.DistrictMap;
import com.itg.httpRequest.asynctask.ImageTask;
import com.itg.iguide.R;
import com.itg.ui.fragment.IguideListFragment;
import com.itg.util.LRUCache;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DistrictListAdapter extends BaseAdapter {

	private List<DistrictMap> districtList;
	private Context context;
	private String currentLocation;
	private LRUCache bitMapCache = new LRUCache();
	private ViewCache viewCache;

	public DistrictListAdapter(List<DistrictMap> districtList, Context context,
			String currentLocation) {
		this.districtList = districtList;
		this.context = context;
		this.currentLocation = currentLocation;
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

	@SuppressLint("UseSparseArrays")
	private HashMap<Integer, View> mapCache = new HashMap<Integer, View>();

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (mapCache.get(position) == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.iguide_list_fragment_item, null);
			viewCache = new ViewCache();
			viewCache.ivDistrictImage = (ImageView) convertView
					.findViewById(R.id.iguide_list_image);
			viewCache.tvDistrictName = (TextView) convertView
					.findViewById(R.id.iguide_list_districtname);
//			viewCache.tvLocateMap = (TextView) convertView
//					.findViewById(R.id.iguide_list_linear_maplocation);
//			viewCache.tvLocateWidgetMap = (TextView) convertView
//					.findViewById(R.id.iguide_list_linear_mapwidget);
			
			viewCache.tvDistrictDescription=(TextView) convertView.findViewById(R.id.iguide_list_linear_description);
			viewCache.tvDistance=(TextView) convertView.findViewById(R.id.iguide_list_linear_distance);
			viewCache.tvDistrictLevel=(TextView) convertView.findViewById(R.id.iguide_list_linear_class);
			convertView.setTag(viewCache);
			mapCache.put(position, convertView);
		} else {
			convertView = mapCache.get(position);
			viewCache = (ViewCache) convertView.getTag();
		}
		// viewCache= (ViewCache) convertView.getTag();

		DistrictMap district = districtList.get(position);
		String dImageName = district.getHd_ImageName();
		if (!dImageName.equals("")) {
			new ImageTask(viewCache.ivDistrictImage, bitMapCache)
					.execute(dImageName, DistrictListAdapter.class.toString());
		}
		if(!district.getXCoordinate().equals("null"))
		{
		double dis= getDistanceFromTwoPoint(district.getXCoordinate(),district.getYCoordinate());
		String dis_=convertFriendlyDistance(dis);
	
		viewCache.tvDistance.setText(dis_);
		}
		viewCache.tvDistrictDescription.setText(district.getHd_Description());
		setDistrictLevel(district.getHd_Level());
		viewCache.tvDistrictName.setText(district.getHd_Title());
//		viewCache.tvLocateMap.setOnClickListener(this);
//		viewCache.tvLocateWidgetMap.setOnClickListener(this);
		return convertView;
	}
	
	private void setDistrictLevel(String level)
	{
		int nLevel=Integer.valueOf(level);
		switch (nLevel) {
		case 0:
			viewCache.tvDistrictLevel.setText("无");
		case 1:
			viewCache.tvDistrictLevel.setText("A");
			break;
		case 2:
			viewCache.tvDistrictLevel.setText("AA");
			break;
		case 3:
			viewCache.tvDistrictLevel.setText("AAA");
			break;
		case 4:
			viewCache.tvDistrictLevel.setText("AAAA");
			break;
		case 5:
			viewCache.tvDistrictLevel.setText("AAAAA");
			break;

		default:
			break;
		}
	}
	
	public String convertFriendlyDistance(double distances)
	{
		int currentD=(int) Math.round(distances);
		if(currentD<1000)
		{
			return currentD+"m";
		}
		else if(currentD>=1000)
		{
			return Math.round(currentD/1000)+"km";
		}
		return "";
	}

	public double getDistanceFromTwoPoint(String x,String y) {
//		double DEF_PI = 3.14159265359; // PI
//		double DEF_2PI = 6.28318530712; // 2*PI
		double DEF_PI180 = 0.01745329252; // PI/180.0
		double DEF_R = 6370693.5; // radius of earth

		 String[] curStrings=currentLocation.split(",");
		 float lat=Float.valueOf(curStrings[0]);
		 float lng=Float.valueOf(curStrings[1]);
		
		 float latX=Float.valueOf(x);
		 float lngY=Float.valueOf(y);
		
		double ew1, ns1, ew2, ns2;
		double distance;
		// 角度转换为弧度
		ew1 = lng * DEF_PI180;
		ns1 = lat * DEF_PI180;
		ew2 = lngY * DEF_PI180;
		ns2 = latX * DEF_PI180;
		// 求大圆劣弧与球心所夹的角(弧度)
		distance = Math.sin(ns1) * Math.sin(ns2) + Math.cos(ns1)
				* Math.cos(ns2) * Math.cos(ew1 - ew2);
		// 调整到[-1..1]范围内，避免溢出
		if (distance > 1.0)
			distance = 1.0;
		else if (distance < -1.0)
			distance = -1.0;
		// 求大圆劣弧长度
		distance = DEF_R * Math.acos(distance);
		return distance;
	}

	class ViewCache {
		private ImageView ivDistrictImage;
		private TextView tvDistrictName, tvLocateMap, tvLocateWidgetMap,
				tvDistance,tvDistrictLevel,tvDistrictDesc,tvDistrictDescription;
	}

//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.iguide_list_linear_maplocation:
//			FragmentTransaction transaction = manager.beginTransaction();
//			transaction.show(fragment)
//			break;
//		case R.id.iguide_list_linear_mapwidget:
//			Toast.makeText(context, "2", 0).show();
//			break;
//
//		}
//	}

}
