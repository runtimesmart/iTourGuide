package com.itg.ui.fragment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.itg.bean.DistrictMap;
import com.itg.broadcastreceiver.GPSChangeReceiver;
import com.itg.broadcastreceiver.GPSChangeReceiver.GPSChangeListener;
import com.itg.httpRequest.IDistrictMapCallback;
import com.itg.httpRequest.asynctask.HotPotDistrictMapTask;
import com.itg.iguide.R;
import com.itg.ui.activity.MapWidgetActivity;
import com.itg.ui.view.ToggleButtonView;
import com.itg.ui.view.ToggleButtonView.OnSwitchChangedListener;
import com.itg.util.NetWorkDetected;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class MapFragment extends BaseFragment implements OnClickListener,
		BDLocationListener, OnSwitchChangedListener, GPSChangeListener,
		LocationListener, Listener, IDistrictMapCallback,TextWatcher {
	private BaiduMap baiduMap;
	private MapView mapView;
	private ImageView focusImageView, settingImageView;
	private LocationMode locationMode = LocationMode.NORMAL;
	private PopupWindow popupWindow;
	BitmapDescriptor layerMarker;
	private ToggleButtonView toggleGPS;
	private ToggleButtonView toggleAutoPlay;
	private TextView gpsTextViewTip, gpsTextViewNum;
	// 定位相关
	LocationClient mLocClient;
	boolean isFirstLoc = true;
	private Context context;
	LocationManager lm;
	private View popupView;
	private EditText center_text;
	/* 地理搜索 */
	private GeoCoder mGeoCoder;
	private MapStatusUpdate mStatusUpdate;
	public List<DistrictMap> mapList;
	private LatLng currentLatlan;
	private TextView tvLatLanTextView;;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		context = getActivity().getApplicationContext();
		SDKInitializer.initialize(context);// Map SDK初始化 必须使用applicationContext

		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.iguide_map_fragment, null);
		initMapComponent(view);
		return view;
	}

	/* 初始化地图组件 */
	private void initMapComponent(View view) {
		mapView = (MapView) view.findViewById(R.id.iguide_mapview);
		if (baiduMap == null)
			baiduMap = mapView.getMap();
		focusImageView = (ImageView) view
				.findViewById(R.id.btn_map_focuslocation);
		settingImageView = (ImageView) view.findViewById(R.id.btn_map_setting);
		center_text = (EditText) getActivity().findViewById(
				R.id.iguide_title_center_text);
		center_text.addTextChangedListener(this);	
		focusImageView.setOnClickListener(this);
		settingImageView.setOnClickListener(this);
		baiduMap.setMyLocationEnabled(true);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		// option.setScanSpan(5000);
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(5000);
		option.setAddrType("all");
		option.setIsNeedAddress(true);
		// 定位初始化
		mLocClient = new LocationClient(context);
		mLocClient.registerLocationListener(this);
		mLocClient.setLocOption(option);
		mLocClient.start();
	}
/**
 * 加载marker
 * **/
	private View initMarkerView(String districtName) {
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.iguide_map_marker, null);
		TextView tView = (TextView) view
				.findViewById(R.id.iguide_map_district_marker);
		tView.setText(districtName);
		return tView;
	}
	
/**
 * 初始化地图覆盖物
 * **/
	public void initOverlay(List<DistrictMap> mapList) {
		baiduMap.clear();
		for (DistrictMap districtMap : mapList) {
			if (districtMap.getXCoordinate().equalsIgnoreCase("null"))
				continue;
			LatLng laln = new LatLng(Double.valueOf(districtMap
					.getXCoordinate()), Double.valueOf(districtMap
					.getYCoordinate()));
			OverlayOptions overlayOptions = new MarkerOptions()
					.position(laln)
					.icon(BitmapDescriptorFactory
							.fromView(initMarkerView(districtMap.getHd_Title())));
			Marker marker = (Marker) baiduMap.addOverlay(overlayOptions);
			Bundle bundle = new Bundle();
			// bundle.putCharSequence("dName", districtMap.getHd_Title());
			bundle.putInt("dId", districtMap.getId());
			if(districtMap.getMapName()==null)
				bundle.putCharSequence("mapName","");
			else bundle.putCharSequence("mapName",districtMap.getMapName());
			bundle.putString("name", districtMap.getHd_Title());
			marker.setExtraInfo(bundle);

		}
		DistrictMap mapInfo = mapList.get((mapList.size()/3==0?1: mapList.size()/3)- 1);
		if (!mapInfo.getXCoordinate().equals("null"))
			setMapFocusCurrent(new LatLng(Double.valueOf(mapInfo
					.getXCoordinate()),
					Double.valueOf(mapInfo.getYCoordinate())));
		baiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				if(marker.getExtraInfo()==null)
				{
					Toast.makeText(getActivity().getApplicationContext(), "数据缓冲中..", Toast.LENGTH_SHORT).show();
				return false;
				}
				int id = (Integer) marker.getExtraInfo().get("dId");
				Intent intent = new Intent(getActivity().getApplicationContext(),MapWidgetActivity.class);
				intent.putExtra("did", id);
				intent.putExtra("name", (String) marker.getExtraInfo().get("name"));
				intent.putExtra("mapName", (String) marker.getExtraInfo().get("mapName"));
				startActivity(intent);
				
				return true;
			}
		});

	}

	/**
	 * 获取景区地图数据后回调方法
	 * **/
	@Override
	public void getDistrictMapInfo(List<DistrictMap> inputMapList) {
		initOverlay(inputMapList);

	}
/**
 * 网络获取景区地图信息
 * **/
	public void districtMapExector(String cityName) {
		new HotPotDistrictMapTask(this,cityName);
	}

	/**
	 * 根据城市获取坐标，获取景区地图数据后回调显示
	 * **/
	public void setupMapData(CharSequence s)
	{
		//getCoordinateByCityName(s.toString());
		districtMapExector(s.toString());
	}
	
	/**
	 * actionbar 文字改变监听
	 * **/

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (!getActivity().getFragmentManager().findFragmentByTag("map").isHidden())
			{
			setupMapData(s);
			}

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
		}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_map_focuslocation:
			setMapFocusCurrent(currentLatlan);
			break;

		case R.id.btn_map_setting:
			initPopupWindow();
			break;
		case R.id.iguide_map_control_down:
			popupWindow.dismiss();
			break;
		case R.id.iguide_map_control_up:
			popupWindow.dismiss();
			break;
		}

	}

	private void initPopupWindow() {
		if (popupView == null) {
			popupView = LayoutInflater.from(getActivity()).inflate(
					R.layout.iguide_map_popwindow_dialog, null);
		}
		if (popupWindow == null)
			popupWindow = new PopupWindow(popupView,
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT, true);
		popupWindow.setTouchable(true);
		popupWindow.setAnimationStyle(R.drawable.iguide_hide_show_pop);
		ColorDrawable colorDrawable = new ColorDrawable(0x90000000);
		popupWindow.setBackgroundDrawable(colorDrawable);
		popupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);
		LinearLayout touchControlUp = (LinearLayout) popupView
				.findViewById(R.id.iguide_map_control_up);
		LinearLayout touchControlDown = (LinearLayout) popupView
				.findViewById(R.id.iguide_map_control_down);
		toggleGPS = (ToggleButtonView) popupView
				.findViewById(R.id.iguide_map_gps_switcher);
		toggleAutoPlay = (ToggleButtonView) popupView
				.findViewById(R.id.iguide_map_auto_play);
		gpsTextViewTip = (TextView) popupView
				.findViewById(R.id.iguide_pop_open_gps_tip);
		gpsTextViewNum = (TextView) popupView
				.findViewById(R.id.iguide_pop_open_gps_tip_num);

		// gps 按钮状态设置
		if (NetWorkDetected.isOPen(getActivity().getApplicationContext()))
			toggleGPS.mSwitchOn = true;
		else
			toggleGPS.mSwitchOn = false;
		toggleAutoPlay.mSwitchOn = true;
		toggleGPS.setOnChangeListener(this);
		toggleAutoPlay.setOnChangeListener(this);
		// 触摸空白去关闭Popup
		touchControlUp.setOnClickListener(this);
		touchControlDown.setOnClickListener(this);
		// 设置gps状态变化监听广播
		GPSChangeReceiver.callbackListener.add(this);
		// 获取LocationManager
		lm = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);
		// 设置gps状态监听--GPSStatus
		lm.addGpsStatusListener(this);
		// 设置位置更新监听
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);

	}

	@Override
	public void onReceiveLocation(BDLocation location) {
		// map view 销毁后不在处理新接收的位置
		if (location == null || mapView == null)
			return;
		MyLocationData locData = new MyLocationData.Builder()
				.accuracy(location.getRadius())
				// 此处设置开发者获取到的方向信息，顺时针0-360
				.direction(100).latitude(location.getLatitude())
				.longitude(location.getLongitude()).build();
		baiduMap.setMyLocationData(locData);
		if (isFirstLoc) {
			isFirstLoc = false;
			currentLatlan = new LatLng(location.getLatitude(),
					location.getLongitude());
			// 设置中心点图标
			baiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
					locationMode, true, null));
			mStatusUpdate = MapStatusUpdateFactory.newLatLng(currentLatlan);
			mStatusUpdate = MapStatusUpdateFactory.zoomTo(15.0f);
			baiduMap.animateMapStatus(mStatusUpdate);
			mStatusUpdate = null;

			tvLatLanTextView=(TextView) getActivity().findViewById(R.id.iguide_latlng);
			tvLatLanTextView.setText(location.getLatitude()+","+location.getLongitude());
			if (location.getCity() != null)
				center_text.setText(location.getCity());
			
		
		}
	}

	/* 根据城市名解析Geo坐标 */
//	private void getCoordinateByCityName(String cityName) {
//		mGeoCoder = GeoCoder.newInstance();
//		mGeoCoder.geocode(new GeoCodeOption().city(cityName).address(cityName));
//		/* 初始化搜索监听事件 */
//	//	mGeoCoder.setOnGetGeoCodeResultListener(geoPoiSearchResultListener);
//
//	}

	/* 在地图上定位到当前坐标位置并marker */
	private void setMapFocusCurrent(LatLng latLng) {
		mStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
		baiduMap.animateMapStatus(mStatusUpdate);
		baiduMap.addOverlay(new MarkerOptions().position(latLng).icon(
				BitmapDescriptorFactory.fromResource(R.drawable.ditu_dw_biao)));
	}

	/* 地理位置搜索监听 */
//	private OnGetGeoCoderResultListener geoPoiSearchResultListener = new OnGetGeoCoderResultListener() {
//
//		@Override
//		public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
//		}
//
//		@Override
//		public void onGetGeoCodeResult(GeoCodeResult result) {
//			LatLng latLng = result.getLocation();
//			// setMapFocusCurrent(latLng);
//			if (latLng != null) {
//				CharSequence charSequence = latLng.latitude + ","
//						+ latLng.longitude;
//				Toast.makeText(getActivity(), charSequence, 3).show();
//			}
//		}
//	};

	public void onReceivePoi(BDLocation poiLocation) {
	}

	/**
	 * 滑动按钮滑动时执行
	 * */
	@Override
	public void onSwitchChange(ToggleButtonView switchView, boolean isChecked) {

		Intent toGPSSetting = new Intent(
				Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivityForResult(toGPSSetting, 0);
		popupWindow.dismiss();
		initPopupWindow();

	}

	@Override
	public void onLocationChanged(Location location) {
		gpsTextViewNum.setText(" ±" + Math.round(location.getAccuracy()) + "m");
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	private List<GpsSatellite> numSatelliteList = new ArrayList<GpsSatellite>();

	/**
	 * 通过卫星信噪比计算信号强度
	 * **/
	@Override
	public void onGpsStatusChanged(int event) {
		GpsStatus gpsStatus = lm.getGpsStatus(null);
		if (gpsStatus == null) {
			gpsTextViewTip.setText(satelliteSignal(0));
			return;
		} else if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {
			int maxSatellites = gpsStatus.getMaxSatellites();
			Iterator<GpsSatellite> it = gpsStatus.getSatellites().iterator();
			numSatelliteList.clear();
			int count = 0;
			float sgnr = 0f;
			while (it.hasNext() && count <= maxSatellites) {
				GpsSatellite s = it.next();
				sgnr += s.getSnr();
				numSatelliteList.add(s);
				count++;
			}
			gpsTextViewTip.setText(satelliteSignal(sgnr
					/ numSatelliteList.size()));
			gpsTextViewNum.setText(" ±40m");
		}
	}

	/**
	 * 设置信号强度提示 **
	 */
	private String satelliteSignal(float sgnr) {
		String tipString = "";
		if (sgnr < 10) {
			tipString = "信号较弱";
		} else if (sgnr >= 10 && sgnr < 20) {
			tipString = "信号较好";
		} else if (sgnr >= 20 && sgnr < 30) {
			tipString = "信号非常好";
		} else if (sgnr >= 30 && sgnr < 40) {
			tipString = "信号较强";
		} else if (sgnr >= 40) {
			tipString = "信号非常强";
		}
		return tipString;
	}

	/*
	 * GPS状态发生改变时执行
	 */
	@Override
	public void stateChangeCallback(GPSChangeReceiver receiver, boolean state) {
		popupWindow.dismiss();
		initPopupWindow();

	}

	@Override
	public void onResume() {
		mapView.onResume();
		super.onResume();
	}

	@Override
	public void onPause() {
		mapView.onPause();
		super.onPause();
	}

	@Override
	public void onDestroyView() {
		 mapView.onDestroy();
		super.onDestroyView();
	}

	// 每次切换fragment如果会销毁的话是执行了 FragmentTranstion.replace，这个会重新调用fragment生命周期
	@Override
	public void onDestroy() {
		mLocClient.stop();
		// 关闭定位图层
		baiduMap.setMyLocationEnabled(false);
		mapView.onDestroy();
		mapView = null;
		super.onDestroy();
	}

}
