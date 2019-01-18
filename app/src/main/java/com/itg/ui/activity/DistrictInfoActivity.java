/**
* @FileName DistrictInfoActivity.java
* @Package com.itg.ui.activity
* @Description TODO
* @Author Alpha
* @Date 2015-9-8 下午2:47:25 
* @Version V1.0

*/
package com.itg.ui.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;



import com.itg.adapter.HotpotListAdapter;
import com.itg.bean.District;
import com.itg.bean.HotPot;
import com.itg.httpRequest.DownloadService;
import com.itg.httpRequest.DownloadService.Downbinder;
import com.itg.httpRequest.IDistrictAndHotPotCallback;
import com.itg.httpRequest.IOfflineVersionCallback;
import com.itg.httpRequest.IOfflineZipCheck;
import com.itg.httpRequest.asynctask.DistrictAndHotPotInfoTask;
import com.itg.httpRequest.asynctask.HotpotDistrictOfflineZipCheckTask;
import com.itg.httpRequest.asynctask.ImageTask;
import com.itg.httpRequest.asynctask.OfflineZipVersionCheckTask;
import com.itg.httpRequest.asynctask.offlineline.HotpotDistrictInfoCallback;
import com.itg.httpRequest.asynctask.offlineline.HotpotDistrictInfoTask;
import com.itg.iguide.R;
import com.itg.ui.view.GridViewWithHeaderAndFooter;
import com.itg.ui.view.Loading;
import com.itg.ui.view.NumberCircleBar;
import com.itg.ui.view.rotate.Constants;
import com.itg.ui.view.rotate.RotationHelper;
import com.itg.util.AppConfig;
import com.itg.util.MyApplication;
import com.itg.util.SDFileUtil;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DistrictInfoActivity extends BaseActivity implements IDistrictAndHotPotCallback
,OnClickListener,OnItemClickListener,IOfflineVersionCallback,IOfflineZipCheck,HotpotDistrictInfoCallback{

	//private TextView txtControl;
	/*返回按钮*/
	private ImageView backImageView;
	/*景区图片*/
	private ImageView districtImageView;
    /*	景区介绍*/
	private TextView districtDescriptionView;
	/*景区门票*/
	private TextView districtTicketView;
	/*景区交通*/
	private TextView districtTrafficView;
	/*内容显示容器*/
	private TextView districtStuffView;
	private ImageView mapNaviBtn;
	private WebView stuffPanel ;
	private GridViewWithHeaderAndFooter gridView;
	private boolean isOfflineLine=false;
	
	//private com.itg.ui.view.MoreTextView districtStuffView;
	/*景区内部路线*/
	private TextView districtTipView;
	/*离线包下载按钮*/
	private ImageView offlineDownloadBtn;
	/*下载动画*/
	public LinearLayout districtHotpotLayout;
	private NumberCircleBar numCircle;
	private TextView districtTitleView;
	private TextView districtInnerLineView;
	private District currentDistrict;
	private HotpotListAdapter hotpotListAdapter;
	private List<HotPot> hotpotList;
	private int districtId;
	private final int OFF_SET=3;
	
	//private HotpotOfflineZipDownloadTask downloadTask;
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.iguide_district_info_layout);
	initComponment();
	continueDownload();
	
}
   
/**
 * 检查是否继续执行下载进度条
 * */
private void continueDownload()
{
	final MyApplication application= ((MyApplication) getApplication());
	if(application.getCircleBar()!=null && districtId==application.getTemDistrict().getDistrictId())
	{
	
		if(!application.getCircleBar().isFinished())
		{
			offlineDownloadBtn.setVisibility(View.GONE);
			numCircle.setVisibility(View.VISIBLE);
			numCircle.setProgress(application.getCircleBar().getProgress());
		      final Handler handler;
		      final TimerTask task;
		  	final Timer timer= new Timer();
		      handler = new Handler()
		        {
		        	@Override
		        	public void handleMessage(Message msg) {
		        		if(msg.what==0)
		        		{
		        			if(numCircle.isFinished())
		        			{
		        				application.getCircleBar().incrementProgressBy(100);//设置最大
		        				numCircle.clearAnimation();
		        				timer.cancel();
		        				
		        			//	this.removeCallbacks(task);
		        			}
		        			numCircle.incrementProgressBy(2);
		        		}
		        	}
		        };
		        
			
				
				 task=new TimerTask() {
					
					@Override
					public void run() {
						handler.sendEmptyMessage(0);
					}
				};
				timer.schedule(task, 100,100);
		  
				
		}

	}	
}
/**
 * 初始化控件
 * **/
private void initComponment()
{
	districtHotpotLayout=(LinearLayout)findViewById(R.id.iguide_district_hotpot_layout);
	 gridView=(GridViewWithHeaderAndFooter) findViewById(R.id.iguide_district_hotpot_info);
	 mapNaviBtn=(ImageView) findViewById(R.id.iguide_title_right_image_map);
	 mapNaviBtn.setOnClickListener(this);
	View headerView=LayoutInflater.from(this).inflate(R.layout.iguide_district_info_header_layout, null);
	gridView.addHeaderView(headerView,null,false);
//	View footView=LayoutInflater.from(this).inflate(R.layout.iguide_title, null);
//	gridView.addFooterView(footView);
	backImageView=(ImageView) this.findViewById(R.id.iguide_title_left_image);
	backImageView.setOnClickListener(backClickListener);
	hotpotList=new ArrayList<HotPot>();
	hotpotListAdapter=new HotpotListAdapter(hotpotList,this);
	gridView.setAdapter(hotpotListAdapter);
	 gridView.setOnItemClickListener(this);
	initDistrictView(headerView);
	Intent intent=getIntent();
	districtId= intent.getIntExtra("districtId", 0);
	new OfflineZipVersionCheckTask(this).execute(districtId);

}

//获取回调版本号
@Override
public void setVersion(int versionId) {
	new HotpotDistrictOfflineZipCheckTask(offlineDownloadBtn,this,versionId,this).execute(districtId);
}
//检测zip包是否下载回调
@Override
public void isOfflineZipDownloaded(boolean isDownloaded) {
	//new DistrictAndHotPotInfoTask(this,hotpotListAdapter).execute(String.valueOf(districtId),DistrictInfoActivity.class.toString());
	isOfflineLine=isDownloaded;
	if(isDownloaded)
	{
		loadOfflineData();
	}
	else {
		new DistrictAndHotPotInfoTask(this,hotpotListAdapter).execute(String.valueOf(districtId),DistrictInfoActivity.class.toString());
	}
}

 private void loadOfflineData()
 {
	 new HotpotDistrictInfoTask(this).execute(String.valueOf(districtId));
 }
 
 //离线-景区和景点回调
 @Override
 public void setHotpotDistrictInfo(List<District> district,
 		List<HotPot> hotpot) {
	 initOfflineDistrictData(district.get(0));
	 hotpotList.clear();
	 hotpotList.addAll(hotpot);
	 hotpotListAdapter.notifyDataSetChanged();
 }
 
 /*初始化-离线景区信息*/
 @SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
private void initOfflineDistrictData(District district)
 {
		this.currentDistrict=district;
 	districtTitleView.setText(district.getDistrictName());

	districtImageView.setImageBitmap(BitmapFactory.decodeFile(new SDFileUtil().getSDCard()+AppConfig.WIDGET_OFFLINE_PATH+district.getDistrictImage()));

	
	//districtStuffView.setVisibility(View.GONE);
	stuffPanel.getSettings().setDefaultFontSize(14);
//	stuffPanel.getSettings().setJavaScriptEnabled(true);
//	stuffPanel.addJavascriptInterface(new InJavascriptObject(), "stuff_content");
	stuffPanel.loadUrl("file:/sdcard/"+AppConfig.WIDGET_OFFLINE_PATH+district.getDistrictDescription());
//	stuffPanel.setWebViewClient(new WebViewClient(){
//		@Override
//		public void onPageFinished(WebView view, String url) {
//		
//			view.loadUrl("javascript:window.stuff_content.showHtml('<head>'+"  
//                        + "document.documentElement.innerText+'</head>');");
//			super.onPageFinished(view, url);
//		}
//	});

	stuffPanel.setVisibility(View.VISIBLE);
	stuffPanel.setOnClickListener(this);
	stuffPanel.setOnTouchListener(new OnTouchListener() {	
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int indexY=(int) event.getY();
			int indexX=(int) event.getX();
			if(event.getAction()==MotionEvent.ACTION_UP)
			{
				if(Math.abs(indexY-event.getY())<OFF_SET && Math.abs(indexX-event.getX())<OFF_SET)
				redirectToDetail();
			}
	
			return true;
		}
	});
	 //禁止复制
	stuffPanel.setOnLongClickListener(new OnLongClickListener() {
		@Override
		public boolean onLongClick(View v) {
			return true;
		}
	});
 }
 
 final class InJavascriptObject
 {
	public void showHtml(String html)
	 {
		 districtStuffView.setText(html);
	 }
 }


/*初始化头部景区view*/
@SuppressLint("NewApi")
private void initDistrictView(View view)
{
	districtImageView=(ImageView) view.findViewById(R.id.iguide_district_image);
	districtTipView=(TextView) view.findViewById(R.id.iguide_district_tip);
	districtTitleView=(TextView) this.findViewById(R.id.iguide_local_district);
	
	districtDescriptionView=(TextView) view.findViewById(R.id.iguide_district_description);
	districtDescriptionView.setOnClickListener(this);
	districtTicketView=(TextView) view.findViewById(R.id.iguide_district_ticket);
	districtTicketView.setOnClickListener(this);
	districtTrafficView=(TextView) view.findViewById(R.id.iguide_district_traffic);
	districtTrafficView.setOnClickListener(this);
	districtInnerLineView=(TextView) view.findViewById(R.id.iguide_district_innerline);
	districtInnerLineView.setOnClickListener(this);
	districtStuffView=(TextView) view.findViewById(R.id.iguide_district_stuff);

	districtDescriptionView.setBackground(getResources().getDrawable(R.drawable.list_xq_dh_left_hv));
	stuffPanel=(WebView) view.findViewById(R.id.iguide_district_stuff_wv);
	//下载按钮
	offlineDownloadBtn=(ImageView) view.findViewById(R.id.iguide_district_offline_download);
	offlineDownloadBtn.setOnClickListener(this);
	
	numCircle=(NumberCircleBar) view.findViewById(R.id.iguide_district_down_circle);
}
/*初始化背景*/
@SuppressLint("NewApi")
private void initStuffBackground()
{
	districtDescriptionView.setBackground(getResources().getDrawable(R.drawable.list_xq_dh_left));
	districtTicketView.setBackground(getResources().getDrawable(R.drawable.list_xq_dh));
	districtTrafficView.setBackground(getResources().getDrawable(R.drawable.list_xq_dh));
	districtInnerLineView.setBackground(getResources().getDrawable(R.drawable.list_xq_dh_right));
}
/*返回上一级*/
private OnClickListener backClickListener=new OnClickListener() {
	@Override
	public void onClick(View v) {
		//DistrictInfoActivity.this.finish();
		DistrictInfoActivity.this.exitAnimation();
	}
};

@Override
public void setDistrictPotInfo(District district) {
	this.currentDistrict=district;
//	isDistrictZipDownloaded(district.getDistrictId(),district.getTimeStamp());
	initDistrictData(district);
	hotpotList.clear();
	hotpotList.addAll(district.getHotPot());

}
/*初始化景区信息*/
private void initDistrictData(District district)
{
	districtTitleView.setText(district.getDistrictName());
	new ImageTask(districtImageView, null).execute(district.getDistrictImage(),DistrictInfoActivity.class.toString());
    districtStuffView.setText(Html.fromHtml(district.getDistrictDescription()) );
    districtStuffView.setVisibility(View.VISIBLE);
	districtStuffView.setOnClickListener(this);
	//stuffPanel.setVisibility(View.GONE);
    // if(district.districtOfflineId)
}

/*景区介绍/门票/交通/路线 显示/隐藏  --展开隐藏*/
@SuppressLint("NewApi")
@Override
public void onClick(View v) {
	
	switch (v.getId()) {
	case R.id.iguide_district_description:
		 initStuffBackground();
		districtTipView.setText(getResources().getString(R.string.district_des));
		districtDescriptionView.setBackground(getResources().getDrawable(R.drawable.list_xq_dh_left_hv));
		if(isOfflineLine) {	stuffPanel.loadUrl("file:/sdcard/"+AppConfig.WIDGET_OFFLINE_PATH+currentDistrict.getDistrictDescription());
		districtStuffView.setText(currentDistrict.getDistrictDescription());
		}
		else
		 districtStuffView.setText(Html.fromHtml(currentDistrict.getDistrictDescription()));
		break;
	case R.id.iguide_district_ticket:
		initStuffBackground();
		districtTipView.setText(getResources().getString(R.string.district_ticket));
		districtTicketView.setBackground(getResources().getDrawable(R.drawable.list_xq_dh_hv));
		if(isOfflineLine) {	stuffPanel.loadUrl("file:/sdcard/"+AppConfig.WIDGET_OFFLINE_PATH+currentDistrict.getDistrictTicket()); 
		districtStuffView.setText(currentDistrict.getDistrictTicket());
		}
		else districtStuffView.setText(Html.fromHtml(currentDistrict.getDistrictTicket()));	
		break;
	case R.id.iguide_district_traffic:
		initStuffBackground();
		districtTipView.setText(getResources().getString(R.string.district_traffic));
		districtTrafficView.setBackground(getResources().getDrawable(R.drawable.list_xq_dh_hv));
		if(isOfflineLine) {	stuffPanel.loadUrl("file:/sdcard/"+AppConfig.WIDGET_OFFLINE_PATH+currentDistrict.getDistirctTraffic());
		districtStuffView.setText(currentDistrict.getDistirctTraffic());
		}
		else 
		districtStuffView.setText(Html.fromHtml(currentDistrict.getDistirctTraffic()));
		break;
	case R.id.iguide_district_innerline:
		initStuffBackground();
		districtTipView.setText(getResources().getString(R.string.district_innerline));
		districtInnerLineView.setBackground(getResources().getDrawable(R.drawable.list_xq_dh_right_hv));
		if(isOfflineLine) {	stuffPanel.loadUrl("file:/sdcard/"+AppConfig.WIDGET_OFFLINE_PATH+currentDistrict.getDistrictInnerline()); 
		districtStuffView.setText(currentDistrict.getDistrictInnerline());
		}
		else 
		districtStuffView.setText(Html.fromHtml(currentDistrict.getDistrictInnerline()));
		break;
	/*展开隐藏*/	
	case R.id.iguide_district_stuff:
		//districtStuffView.setLines(20);
		redirectToDetail();
		break;
	case R.id.iguide_district_offline_download:
		offlineDownloadBtn.setVisibility(View.GONE);
		numCircle.setVisibility(View.VISIBLE);
		//new HotpotOfflineZipDownloadTask(numCircle,DistrictInfoActivity.this).execute(String.valueOf(district.districtOfflineId) ,DistrictInfoActivity.class.toString());
	//	Handler handler =new Handler();
		//new HotpotOfflineZipDownloadTask(numCircle,DistrictInfoActivity.this,currentDistrict);
//		Intent intent=new Intent(DistrictInfoActivity.this,DownloadService.class);
		bindDownloadService();
		break;
	case R.id.iguide_title_right_image_map://跳转地图
		showView();
		rotateHelper = new RotationHelper(DistrictInfoActivity.this, Constants.KEY_FIRST_INVERSE);
		rotateHelper.applyFirstRotation(districtHotpotLayout, 0, -90);
		//ActivityConfig.rotateAnimation(DistrictInfoActivity.this);
		
		break;

	}
}
private void bindDownloadService()
{
	((MyApplication)getApplication()).setCircleBar(numCircle);
	((MyApplication)getApplication()).setTemDistrict(currentDistrict);
	Intent intent= new Intent("com.itg.download");
//	getApplicationContext().bindService(intent, connection,  Context.BIND_AUTO_CREATE);	
	startService(intent);
}
private DownloadService downloadService;
private boolean boundToService=false;
private ServiceConnection connection =new ServiceConnection() {
	
	@Override
	public void onServiceDisconnected(ComponentName name) {
		downloadService=null;
		boundToService=false;
	}
	
	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		Downbinder binder=(Downbinder) service;
		downloadService=binder.getService();
		boundToService=true;

	}
};

//显示景区stuff详细
private void redirectToDetail()
{
	Intent intent=new Intent(DistrictInfoActivity.this,DistrictIntroduce.class);
	intent.putExtra("introduce", districtStuffView.getText().equals("")?currentDistrict.getDistrictDescription():districtStuffView.getText());
	intent.putExtra("title", districtTitleView.getText());
	startActivity(intent);
	//ActivityConfig.enterAnimation(DistrictInfoActivity.this);	
	this.enterAnimation();
}

String tag;
RotationHelper rotateHelper;
public void showView() {
	/* 取得Intent中的Bundle对象 */
	Intent intent = this.getIntent();

		/* 取得Bundle对象中的数据 */
		tag = intent.getStringExtra("second");
		if (tag==null || tag.equals("Second")) {
			rotateHelper = new RotationHelper(this,
					Constants.KEY_FIRST_CLOCKWISE);
			rotateHelper.applyLastRotation(districtHotpotLayout, -90, 0);
		}
	
}
public void jumpToSecond() {
	Intent mapIntent = new Intent(this,MapWidgetActivity.class);
	mapIntent.putExtra("did", currentDistrict.getDistrictId());
	mapIntent.putExtra("name", currentDistrict.getDistrictName());
	mapIntent.putExtra("mapName", currentDistrict.getDistrictMapName());
	mapIntent.putExtra("front", "First");
	startActivity(mapIntent);
	finish();
}

//景点item点击事件
@SuppressLint("NewApi")
@Override
public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
Intent intent =new Intent(DistrictInfoActivity.this,HotpotInfoActivity.class);
HotPot hotPot=hotpotList.get(position);
intent.putExtra("hotpotid", hotPot.getHotpotId());
intent.putExtra("hotpotname", hotPot.getHotpotName());
intent.putExtra("hotpotimage", hotPot.getHotpotImageName());
intent.putExtra("hotpotvoiece", hotPot.getHotPotVoiceName());
startActivity(intent);
Loading.startLoading(DistrictInfoActivity.this);
//ActivityConfig.enterAnimation(DistrictInfoActivity.this);
this.enterAnimation();
}

@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
if(event.getKeyCode()==KeyEvent.KEYCODE_BACK)
	//ActivityConfig.exitAnimation(DistrictInfoActivity.this);
	this.exitAnimation();
	return super.onKeyDown(keyCode, event);
}

@Override
protected void onDestroy() {
//	if(boundToService)
//	getApplicationContext().unbindService(connection);
	super.onDestroy();
	//boundToService=false;

}






}
