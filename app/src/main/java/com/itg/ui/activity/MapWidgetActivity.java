/**
* @FileName MapWeidgetActivity.java
* @Package com.itg.ui.activity
* @Description TODO
* @Author Alpha
* @Date 2015-9-1 下午3:44:43 
* @Version V1.0

*/
package com.itg.ui.activity;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.itg.bean.HotpotForMap;
import com.itg.bean.mapwidget.MapObjectContainer;
import com.itg.bean.mapwidget.MapObjectModel;
import com.itg.httpRequest.IHotpotMapCallback;
import com.itg.httpRequest.IMapZipCallback;
import com.itg.httpRequest.asynctask.HotPotMapZipTask;
import com.itg.httpRequest.asynctask.HotpotMapTask;
import com.itg.iguide.R;
import com.itg.ui.view.FloatWindowService;
import com.itg.ui.view.FloatWindowService.Floatbinder;
import com.itg.ui.view.mapwidget.Popup;
import com.itg.ui.view.rotate.Constants;
import com.itg.ui.view.rotate.RotationHelper;
import com.itg.util.AppConfig;
import com.itg.util.MyApplication;
import com.itg.util.SDFileUtil;
import com.ls.widgets.map.MapWidget;
import com.ls.widgets.map.config.GPSConfig;
import com.ls.widgets.map.config.MapGraphicsConfig;
import com.ls.widgets.map.config.OfflineMapConfig;
import com.ls.widgets.map.events.MapScrolledEvent;
import com.ls.widgets.map.events.MapTouchedEvent;
import com.ls.widgets.map.events.ObjectTouchEvent;
import com.ls.widgets.map.interfaces.Layer;
import com.ls.widgets.map.interfaces.MapEventsListener;
import com.ls.widgets.map.interfaces.OnLocationChangedListener;
import com.ls.widgets.map.interfaces.OnMapScrollListener;
import com.ls.widgets.map.interfaces.OnMapTouchListener;
import com.ls.widgets.map.model.MapObject;
import com.ls.widgets.map.utils.PivotFactory;
import com.ls.widgets.map.utils.PivotFactory.PivotPosition;

public class MapWidgetActivity extends BaseActivity implements OnClickListener
,OnMapTouchListener,MapEventsListener,IHotpotMapCallback,IMapZipCallback,OnMapScrollListener
{
	private ImageView image_back;
	private TextView title;
//	private LinearLayout linear;
	private View view;
	private MapWidget mapWidget;
	private RelativeLayout widgetLayout;
	private Popup mapObjectInfoPopup;

	private Location[] points;
	private int currentPoint=0;
	private MapObjectContainer model;
	private static final Integer LAYER1_ID = 0;
	private static final Integer LAYER2_ID = 1;
	private static final int MAP_ID = 23;
public LinearLayout mapLayout;
private ImageView rightListBtn;
private Integer districtId;
	private List<HotpotForMap> mapList;
	private  String mapName;
	private int nextObjectId=0;
	private int pinHeight;
	private int touchCount=0;
	//private Handler mHandler;
	private Bundle instanceState;
	private RelativeLayout layout;
	//地图popup
	private RelativeLayout popuMapLayout ;


@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.iguide_mapweidget_layout);
	initView();
	model = new MapObjectContainer();
	nextObjectId = 0;


	 instanceState=savedInstanceState;
	// initMapWidget(savedInstanceState);
	 
	 Intent intent= new Intent(MapWidgetActivity.this, FloatWindowService.class);
		
	 getApplicationContext().bindService(intent, connection, Context.BIND_AUTO_CREATE);

}


private FloatWindowService floatService;
private ServiceConnection connection =new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			floatService=null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Floatbinder binder=(Floatbinder) service;
			 floatService=binder.getService();
			 FloatWindowService.view.setVisibility(View.GONE);
		}
	};

	private void initWidgetView() {
		initMapWidget(instanceState);
	
		initMapListeners();

	}
	
	private boolean isDownload()
	{
		 SDFileUtil util=new SDFileUtil();
		 String folderString=mapName.replace(".zip", "/");
		 String xmlFileString=mapName.replace(".zip", ".xml");
		return util.IsFileExists(util.getSDCard()+AppConfig.WIDGET_IMAGE_PATH+folderString+xmlFileString);
	}
	public void initView(){
		Intent intent=getIntent();
		 districtId=intent.getIntExtra("did", 0);
		String name = intent.getStringExtra("name");
		 mapName=intent.getStringExtra("mapName");
		 mapList=new ArrayList<HotpotForMap>();
		 layout = (RelativeLayout) findViewById(R.id.iguide_map_widget);
		new HotpotMapTask(mapList,this).execute(districtId.toString());
	
		mapLayout=(LinearLayout) findViewById(R.id.linearlayout);
		rightListBtn=(ImageView) findViewById(R.id.iguide_title_right_image);
		rightListBtn.setImageResource(R.drawable.list_menu);
		rightListBtn.setOnClickListener(this);
		image_back = (ImageView) findViewById(R.id.iguide_title_left_image);
		title = (TextView) findViewById(R.id.iguide_title_center_text);
	//	linear = (LinearLayout) findViewById(R.id.linearlayout);
		view = findViewById(R.id.include_layout);
		image_back.setImageResource(R.drawable.login_jitou);
		image_back.setOnClickListener(this);
		title.setText(name);
	}

	private void mapZipDownload()
	{
		if(mapName.equals("") || mapName.contains(".jpg") || mapName.contains(".png") || mapName.equals("null"))
		{
			addDefautMap();
			Toast.makeText(this, "暂无地图", 0).show();
		}
		else if(isDownload())
		{
			initWidgetView();
			initMapObjects();
		}
		else {
			//mHandler= new handler(MapWidgetActivity.this);
			
			new HotPotMapZipTask(this, MapWidgetActivity.this,false,mapName);
		}

	}
	
//	static class handler extends Handler
//	{
//		private  WeakReference<MapWidgetActivity> weakMapWidget;
//		public handler(MapWidgetActivity mapWidget) {
//			weakMapWidget=new WeakReference<MapWidgetActivity>(mapWidget);
//		}
//	
//		@Override
//		public void handleMessage(Message msg) {
//			MapWidgetActivity activity=weakMapWidget.get();
//		switch (msg.what) {
//		case 0:
//			Toast.makeText(activity, "开始下载", Toast.LENGTH_SHORT).show();
//			break;
//		case 1:
//			Toast.makeText(activity, "下载完成，开始解压", Toast.LENGTH_SHORT).show();
//			break;
//		case 2:
//			Toast.makeText(activity, "解压完成，等待处理", Toast.LENGTH_SHORT).show();
//		
//			break;
//		case 3:
//			Toast.makeText(activity, "处理完成", Toast.LENGTH_SHORT).show();
//
//			break;
//		case -1:
//			Toast.makeText(activity, "下载失败", Toast.LENGTH_SHORT).show();
//
//			break;
//		case -2:
//			Toast.makeText(activity, "解压失败", Toast.LENGTH_SHORT).show();
//			break;
//		case -3:
//			Toast.makeText(activity, "处理未完成", Toast.LENGTH_SHORT).show();
//			break;
//		}
//		}
//		
//	};
	
	
	
	/**
	 * 地图压缩包下载解压完成后回调
	 * **/
	@Override
	public void finishHandleMapZip() {
		initWidgetView();
		initMapObjects();
		mapWidget.centerMap();
	}
	/**
	 * 无地图默认图片
	 * **/
	private void addDefautMap()
	{
		RelativeLayout.LayoutParams layoutParams =new RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT, android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
			//layout.setLayoutParams(layoutParams);
		
			ImageView iView=new ImageView(this);
			WeakReference<ImageView> iReference=new WeakReference<ImageView>(iView);
			iReference.get().setImageResource(R.drawable.kt);
			iReference.get().setLayoutParams(layoutParams);
			layout.addView(iReference.get());
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapWidget.saveState(outState);
	}
	/**
	 * 景点信息执行后回调
	 * **/
	@Override
	public void setHotpotMap(List<HotpotForMap> mapList) {
		points = new Location[mapList.size()];
		for (int i = 0; i < mapList.size(); i++) {
			HotpotForMap map=mapList.get(i);
			points[i] = new Location("test");
			if(mapList.get(i).getXCoordinate().equals("null"))
			{
				//Toast.makeText(MapWidgetActivity.this, mapList.get(i).getHName()+"景点暂无坐标。", 0).show();
//				addDefautMap();
//				return;
			}
			else {
				points[i].setLatitude(Double.valueOf(map.XCoordinate));
				points[i].setLongitude(Double.valueOf(mapList.get(i).YCoordinate));
				MapObjectModel objectModel = new MapObjectModel();
				objectModel.setId(map.getHid());
				objectModel.setCaption(map.getHName());
				objectModel.setX(map.XCoordinate);
				objectModel.setY(map.YCoordinate);
				objectModel.setVoiceName(map.getHVoiceName());
				objectModel.setImageName(map.getHImage());
				model.addObject(objectModel);
			}
		}
		//下载地图并解压
		mapZipDownload();
			
	}
	//去掉水印
	private void removeLogo()
	{
		Class<?> c = null;
		try {
			// 反射找到Resources类
			c = Class.forName("com.ls.widgets.map.utils.Resources");
			Object obj = c.newInstance();
			// 找到Logo属性，是一个数组
			Field field = c.getDeclaredField("LOGO");
			field.setAccessible(true);
			// 将LOGO字段设置为null
			field.set(obj, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@SuppressLint("ResourceAsColor")
	private void initMapWidget(Bundle savedInstanceState)
	{
		removeLogo();
		// In order to display the map on the screen you will need 
		// to initialize widget and place it into layout.
		DisplayMetrics displayMetrics=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		float ddd=displayMetrics.density;
		File res=new File(Environment.getExternalStorageDirectory()+"/"+AppConfig.WIDGET_IMAGE_PATH+mapName.replace(".zip", ""));
		mapWidget = new MapWidget(savedInstanceState, this, 
				res, // root name of the map under assets folder.
				11); // initial zoom level

        mapWidget.setId(MAP_ID);
        OfflineMapConfig config = mapWidget.getConfig();
        config.setZoomBtnsVisible(false); // Sets embedded zoom buttons visible
        config.setPinchZoomEnabled(true); // Sets pinch gesture to zoom
        config.setFlingEnabled(true);    // Sets inertial scrolling of the map
        config.setMapCenteringEnabled(true);
        
        // Configuration of GPS receiver
        GPSConfig gpsConfig = config.getGpsConfig();
        gpsConfig.setPassiveMode(false);
        gpsConfig.setGPSUpdateInterval(500, 5);
        
        // Configuration of position marker
        MapGraphicsConfig graphicsConfig = config.getGraphicsConfig();
        graphicsConfig.setAccuracyAreaColor(0x550000FF); // Blue with transparency
        graphicsConfig.setAccuracyAreaBorderColor(R.color.hotpot_map_bgcolor); // Blue without transparency

         
        // Adding the map to the layout
        layout.addView(mapWidget, 0);
        layout.setBackgroundColor(Color.parseColor("#f2f2f2"));
        
        // Adding layers in order to put there some map objects
        mapWidget.createLayer(LAYER1_ID); // you will need layer id's in order to access particular layer
		
	}
		
		private void initMapObjects() 
		{	
			//初始化pop布局
			 popuMapLayout =  (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.iguide_hotpot_map_poup, null);
	
			//初始化mAppWidget布局
			 mapObjectInfoPopup = new Popup(this, popuMapLayout,(RelativeLayout)findViewById(R.id.iguide_map_widget));
			
			Layer layer1 = mapWidget.getLayerById(LAYER1_ID);
			if(model.size()>0)
			{
			for (int i=0; i<model.size(); ++i) {	
				addNotScalableMapObject(model.getObject(i), layer1);
			}	
			}
			// Adding two map objects to the second layer
		
		
		}
		
		
		private void addNotScalableMapObject(String x, String y,  Layer layer) 
		{
			if(x.equals("") || x.equals("null"))return;
			// Getting the drawable of the map object
			Drawable drawable = getResources().getDrawable(R.drawable.pot_icon);
			WeakReference<Drawable> weakReference=new WeakReference<Drawable>(drawable);
			pinHeight = weakReference.get().getIntrinsicHeight();
			// Creating the map object
			
			MapObject object1 = new MapObject(Integer.valueOf(nextObjectId), // id, will be passed to the listener when user clicks on it 
					weakReference.get(),  
											  new Point((int) Math.round(Double.parseDouble(x)),(int)Math.round(Double.parseDouble(y)-22)), // coordinates in original map coordinate system.
											  // Pivot point of center of the drawable in the drawable's coordinate system.
											  PivotFactory.createPivotPoint(weakReference.get(), PivotPosition.PIVOT_CENTER),
											  true, // This object will be passed to the listener
											  false); // is not scalable. It will have the same size on each zoom level

			// Adding object to layer
			layer.addMapObject(object1);
			nextObjectId += 1;
		}
		
		
		private void addNotScalableMapObject(MapObjectModel objectModel,  Layer layer) 
		{
			if (objectModel.getLocation() != null) {
				addNotScalableMapObject(objectModel.getLocation(), layer);
			} else {
				addNotScalableMapObject(objectModel.getX(), objectModel.getY(),  layer);
			}
		}

		
		private void addNotScalableMapObject(Location location, Layer layer) {
			if (location == null)
				return;
			
			// Getting the drawable of the map object
			Drawable drawable = getResources().getDrawable(R.drawable.pot_icon);
			WeakReference<Drawable> weakReference=new WeakReference<Drawable>(drawable);
			// Creating the map object
			MapObject object1 = new MapObject(Integer.valueOf(nextObjectId), // id, will be passed to the listener when user clicks on it 
					weakReference.get(),  
											  new Point(0, 0), // coordinates in original map coordinate system.
											  // Pivot point of center of the drawable in the drawable's coordinate system.
											  PivotFactory.createPivotPoint(weakReference.get(), PivotPosition.PIVOT_CENTER),
											  true, // This object will be passed to the listener
											  true); // is not scalable. It will have the same size on each zoom level
			layer.addMapObject(object1);
			
			// Will crash if you try to move before adding to the layer. 
			object1.moveTo(location);
			nextObjectId += 1;
		}
		//设置图标随手势滑动而滑动
		private void handleOnMapScroll(MapWidget v, MapScrolledEvent event) 
		{	
			// When user scrolls the map we receive scroll events
			// This is useful when need to move some object together with the map
			
			int dx = event.getDX(); // Number of pixels that user has scrolled horizontally
			int dy = event.getDY(); // Number of pixels that user has scrolled vertically

			if (mapObjectInfoPopup.isVisible()) {
				mapObjectInfoPopup.moveBy(dx, dy);
			}
		}
		
		private void initMapListeners() 
		{
			// In order to receive MapObject touch events we need to set listener
			mapWidget.setOnMapTouchListener(this);
			
			// In order to receive pre and post zoom events we need to set MapEventsListener
			mapWidget.addMapEventsListener(this); 
			mapWidget.setOnMapScrolledListener(this);
	        // In order to receive map scroll events we set OnMapScrollListener
//			mapWidget.setOnMapScrolledListener(new OnMapScrollListener()
//	        {
//	            public void onScrolledEvent(MapWidget v, MapScrolledEvent event)
//	            {
//	                handleOnMapScroll(v, event);
//	            }
//	        });
			
			
	               
			mapWidget.setOnLocationChangedListener(new OnLocationChangedListener() {
				@Override
				public void onLocationChanged(MapWidget v, Location location) {
					// You can handle location change here.
					// For example you can scroll to new location by using v.scrollMapTo(location)
				}
			});
		}
		//mapWidget滚动事件
		@Override
		public void onScrolledEvent(MapWidget v, MapScrolledEvent event) {
			handleOnMapScroll(v, event);
		}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.iguide_title_left_image:
			if(MyApplication.isPlayActivited)
				FloatWindowService.view.setVisibility(View.VISIBLE);
			instanceState=null;
			this.exitAnimation();
			break;
		case R.id.iguide_title_right_image:
			showView();
			rotateHelper = new RotationHelper(this,
					Constants.KEY_SECOND_CLOCKWISE);
			rotateHelper.applyFirstRotation(mapLayout, 0, 90);
			break;
			
		}
	}
	String tag;
	RotationHelper rotateHelper;
	public void showView() {
		/* 取得Intent中的Bundle对象 */
		Intent intent = getIntent();

			/* 取得Bundle对象中的数据 */
			tag = intent.getStringExtra("front");

		if (tag==null || tag.equals("First")) {
			rotateHelper = new RotationHelper(this, Constants.KEY_SECOND_INVERSE);
			rotateHelper.applyLastRotation(mapLayout, 90, 0);
		}
	}

	public void jumpToFirst() {
		Intent listIntent = new Intent(this,DistrictInfoActivity.class);
		listIntent.putExtra("districtId", districtId);
		listIntent.putExtra("second", "Second");
		startActivity(listIntent);
		finish();
		
	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {		
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
		{
			instanceState=null;
			this.exitAnimation();
			if(MyApplication.isPlayActivited)
			FloatWindowService.view.setVisibility(View.VISIBLE);
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
//		instanceState=null;
//		mapWidget=null;
		instanceState=null;
		finish();
		
	}
	
	@Override
	public void onTouch(MapWidget arg0, MapTouchedEvent event) {
		// 得到触摸的对象
				ArrayList<ObjectTouchEvent> touchedObjs = event.getTouchedObjectIds();
				// 判断是否点击景点对象
				if (touchedObjs.size() > 0) {

//					int xInMapCoords = event.getMapX();
//					int yInMapCoords = event.getMapY();
					int xInScreenCoords = event.getScreenX();
					int yInScreenCoords = event.getScreenY();

					ObjectTouchEvent objectTouchEvent = event.getTouchedObjectIds().get(0);
					// 的到图层对象
					long layerId = objectTouchEvent.getLayerId();
					// 得到地图对象id
					Integer objectId = (Integer) objectTouchEvent.getObjectId();
				
//					String message = "You touched the object with id: " + objectId
//							+ " on layer: " + layerId + " mapX: " + xInMapCoords
//							+ " mapY: " + yInMapCoords + " screenX: " + xInScreenCoords
//							+ " screenY: " + yInScreenCoords;

					// 根据id找到地图对象
					MapObjectModel objectModel = model.getObject(objectId
							.intValue());
					Log.i("pop", "objectModel:" + objectModel);

					if (objectModel != null) {
						// 的到屏幕密度
						float density = getResources().getDisplayMetrics().density;
						int imgHeight = (int) (pinHeight / density / 2);

						int x = xToScreenCoords(Integer.parseInt(objectModel.getX()));
						int y = yToScreenCoords(Integer.parseInt(objectModel.getY())
								- imgHeight);

						// 显示气泡
						showLocationsPopup(objectModel, x, y);
					} else {
						// 如果同时触摸的是多个 判断用户点的是哪个
						showLocationsPopup(objectModel, xInScreenCoords,
								yInScreenCoords);
					}

				} else {
					if (mapObjectInfoPopup != null) {
						mapObjectInfoPopup.hide();
						//隐藏标题
						if(touchCount%2==0)
							view.setVisibility(View.GONE);
							else view.setVisibility(View.VISIBLE);
							touchCount++;
					}
				}
	}
	
	//显示景点提示
	private void showLocationsPopup(MapObjectModel objectModel, int x, int y) {
	

		if (mapObjectInfoPopup != null) {
			mapObjectInfoPopup.hide();
		}
		//有地图无语音，则设置不可点击
		if(objectModel.getVoiceName().equals("null") )
		{
		RelativeLayout playPanelLayout= (RelativeLayout) popuMapLayout.findViewById(R.id.iguide_voice_panel);
		playPanelLayout.setBackgroundColor(Color.GRAY);
		playPanelLayout.setClickable(false);
		Toast.makeText(this, "暂无语音", 0).show();
		}
		
		mapObjectInfoPopup.loadHotpotPopInfo(objectModel);
		mapObjectInfoPopup.show(x, y);
	}

	private int xToScreenCoords(int mapCoord) {
		return (int) (mapCoord * mapWidget.getScale() - mapWidget.getScrollX());
	}

	private int yToScreenCoords(int mapCoord) {
		return (int) (mapCoord * mapWidget.getScale() - mapWidget.getScrollY());
	}
	@Override
	public void onPostZoomIn() {
	}
	@Override
	public void onPostZoomOut() {
	}
	@Override
	public void onPreZoomIn() {
	}
	@Override
	public void onPreZoomOut() {
	}







}
