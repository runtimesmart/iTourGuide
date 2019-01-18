package com.itg.ui.activity;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import com.itg.bean.City;
import com.itg.bean.UpdateAPKBean;
import com.itg.httpRequest.DownloadService;
import com.itg.httpRequest.WebServiceUtil;
import com.itg.httpRequest.asynctask.CityListTask;
import com.itg.iguide.R;
import com.itg.ui.view.Loading;
import com.itg.ui.view.contact.ContactListAdapter;
import com.itg.ui.view.contact.ContactListViewImpl;
import com.itg.util.AppConfig;
import com.itg.util.DES_Encrypt;
import com.itg.util.MyApplication;
import com.itg.util.SDFileUtil;
import com.slidingmenu.lib.SlidingMenu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author tong.han;
 * 
 * @date 2015年8月7日下午2:08:31
 */

public class HomeActivity extends BaseActivity implements OnClickListener {
	private RelativeLayout relative_geren, relative_xiaoxi, relative_yijian,
			relative_guanyu, relative_exit_login, login, nologin, relative_gengxin, view;
	private ImageView head_image;
	private ImageView search_image, left_image, center_image;
	private TextView sliding_login, sliding_usernames, version_text, popupTitle, dialog_title;
	private EditText center_text;
	private Button dialog_ok, dialog_no;
	
	private LinearLayout localCityLayout;
	private SlidingMenu mSlidingMenu;
	private PopupWindow popupWindow;
	private List<City> cityList;
	private SharedPreferences shared, version;
	private String id, name, size, type, description;
	private Editor edit;
	private Dialog dialog;
	private String versionId;
	
	long cTime = 0;
	
	private MyApplication mApplication=new MyApplication();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.iguide_home_layout);
		initActionBar();
		slidingMenuInitView(HomeActivity.this);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 判断是否登录
					if (isLogin()) {
						// 登录
						sliding_usernames.setText(shared.getString("phone", ""));
						requestImage(head_image);
						nologin.setVisibility(View.GONE);
						login.setVisibility(View.VISIBLE);
						relative_exit_login.setVisibility(View.VISIBLE);
					} else {
						// 未登录
						login.setVisibility(View.GONE);
						nologin.setVisibility(View.VISIBLE);
						relative_exit_login.setVisibility(View.GONE);
					}
	}

	/**
	 * 初始化自定义actionbar
	 * 
	 **/
	private void initActionBar() {
		shared = getSharedPreferences("UserMsg", Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
		search_image = (ImageView) HomeActivity.this
				.findViewById(R.id.iguide_title_right_image);
		left_image = (ImageView) HomeActivity.this
				.findViewById(R.id.iguide_title_left_image);
		center_image = (ImageView) HomeActivity.this
				.findViewById(R.id.iguide_title_center_image);
		center_text = (EditText) HomeActivity.this
				.findViewById(R.id.iguide_title_center_text);
		localCityLayout = (LinearLayout) HomeActivity.this
				.findViewById(R.id.iguide_local_city);
		localCityLayout.setOnClickListener(this);
		center_image.setVisibility(View.VISIBLE);
		search_image.setImageResource(R.drawable.sosuo);
		left_image.setImageResource(R.drawable.dl);
		search_image.setOnClickListener(this);
		left_image.setOnClickListener(this);
		center_text.setOnClickListener(this);
	}

	// actionbar左右单击事件
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iguide_title_right_image:
			initSearch();
			break;
		case R.id.iguide_title_left_image:
			mSlidingMenu.showMenu();
			break;
		case R.id.iguide_local_city:
			initPopupWindow();
			break;
		case R.id.iguide_title_center_text:
			initPopupWindow();
			break;
		case R.id.iguide_dialog_no_button: //下载更新包
			downloadAPK(versionId);
		break;

		}
	}
	
	private void downloadAPK(String versionId)
	{
		edit.putString("version", versionId);
		version_text.setText("v" + version.getString("version", ""));
		dialog.dismiss();//关闭对话框  
    	//要下载的apk路径
    	String path = AppConfig.SERVICE_URL + "UploadFiles/soft/" + name;
//    	Log.i("tag", "path = " + path);
    	//存储apk的路径
    	String file = new SDFileUtil().getSDCard() + name;
//    	Log.i("tag", "file = " + file);
    	//将下载路径转化成inputStream
    	InputStream stram=null;
		try {
			stram = MyApplication.getHttpInputStream(path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//下载apk
		Handler handler=new Handler();
//		TimerTask mTask=new TimerTask() {
//			
//			@Override
//			public void run() {
//			}
//		};
    	Boolean boo = mApplication.downloadFile(stram, file);
    	//判断是否下载成功
    	if(boo){
    		Toast.makeText(getApplicationContext(), "下载成功", Toast.LENGTH_SHORT).show();
    		defalutInstall(file);
    	}
	}

	@SuppressLint("NewApi")
	private void initPopupWindow() {
		View contentView = LayoutInflater.from(this).inflate(
				R.layout.iguide_actionbar_cityfilter, null);
		ImageView iv_back = (ImageView) contentView
				.findViewById(R.id.iguide_title_left_image);
		popupTitle = (TextView) contentView
				.findViewById(R.id.iguide_title_popup);
		popupTitle.setVisibility(View.VISIBLE);
		ContactListViewImpl containerView = (ContactListViewImpl) contentView
				.findViewById(R.id.iguide_contact_listview);
		cityList = new ArrayList<City>();
		ContactListAdapter adapter = new ContactListAdapter(this,
				R.layout.iguide_city_item, cityList);
		containerView.setFastScrollEnabled(true);
		containerView.setAdapter(adapter);
		containerView.setOnItemClickListener(adapterViewListener);
		new CityListTask(cityList, adapter).execute(HomeActivity.class
				.toString());
		iv_back.setImageResource(R.drawable.arrow_down_);
		popupTitle.setText("选择城市");
		iv_back.setOnClickListener(popupBack);
		popupWindow = new PopupWindow(contentView);
		popupWindow.setTouchable(true);
		popupWindow.setClippingEnabled(false);// 禁止popup超出屏幕，允许精确设置
		popupWindow.setAnimationStyle(R.style.iguide_citylist_anim);
		popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
		popupWindow.setFocusable(true); /*
										 * 4.1以上版本默认popupwindow没有焦点导致期内
										 * 布局元素点击事件无效
										 */
		popupWindow.showAtLocation(localCityLayout, Gravity.CENTER, 0,
				getStatusBarHeight(this));
	}

	/**
	 * popupwindow 城市列表选择
	 * **/
	private OnItemClickListener adapterViewListener = new OnItemClickListener() {
		public void onItemClick(android.widget.AdapterView<?> parent,
				View view, int position, long id) {
			center_text.setText(cityList.get(position).getCityName());
			closePopupWindow();
			// Toast.makeText(getApplicationContext(),cityList.get(position).getCityName()
			// , 2).show();
		};
	};

	// popupWindow返回
	View.OnClickListener popupBack = new OnClickListener() {

		@Override
		public void onClick(View v) {
			closePopupWindow();
		}
	};

	private void closePopupWindow() {
		popupWindow.dismiss();
		popupTitle.setVisibility(View.GONE);
	}

	// 获取通知栏高度
	public static int getStatusBarHeight(Activity activity) {
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);

		return frame.top;
	}

	private void initSearch() {
		Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.iguide_push_right_left_in,
				R.anim.iguide_push_left_left_out);
	}

	private void slidingMenuInitView(Context context) {
		if (mSlidingMenu == null) {
			// 实例化Slidmenu
			mSlidingMenu = new SlidingMenu(context);
			// 设置Slidmenu为左侧拉出
			mSlidingMenu.setMode(SlidingMenu.LEFT);
			// 设置Slidmenu使用的布局使用 R.layout.slidmenu获得
			mSlidingMenu.setMenu(R.layout.iguide_slidingmenu);
			// 设置全屏可以触摸
			mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
			// 设置Sliddmenu的宽度为屏幕的1/2
			mSlidingMenu.setBehindWidth(getWindowManager().getDefaultDisplay().getWidth() / 3);
			// 指定附在某个activity中
			mSlidingMenu.attachToActivity(HomeActivity.this, SlidingMenu.SLIDING_CONTENT);
			// SlidingMenu划出时主页面显示的剩余宽度
			mSlidingMenu.setBehindOffset(100);
			// 得到Slidmenu布局的控件id 并设置点击事件
			slidingInitMenu(mSlidingMenu);
			//得到头像名称
			String user_name = shared.getString("phone", "");
//			Log.i("tag", "user_name = " + user_name);
			sliding_usernames.setText(shared.getString("phone", ""));
			// 判断是否登录
			if (isLogin()) {
				// 登录
				login.setVisibility(View.VISIBLE);
				relative_exit_login.setVisibility(View.VISIBLE);
				nologin.setVisibility(View.GONE);
				requestImage(head_image);  //获取头像
			} else {
				// 未登录
				login.setVisibility(View.GONE);
				nologin.setVisibility(View.VISIBLE);
				relative_exit_login.setVisibility(View.GONE);
			}
		} else {
			mSlidingMenu.showMenu();
		}
	}

	public void slidingInitMenu(SlidingMenu mSlidingMenu) {
		version = getSharedPreferences("version", MODE_PRIVATE);
		edit = version.edit();
		relative_geren = (RelativeLayout) mSlidingMenu
				.findViewById(R.id.iguide_sliding_geren);
		relative_yijian = (RelativeLayout) mSlidingMenu
				.findViewById(R.id.iguide_sliding_yijian);
		relative_guanyu = (RelativeLayout) mSlidingMenu
				.findViewById(R.id.iguide_sliding_guanyu);
		relative_exit_login = (RelativeLayout) mSlidingMenu
				.findViewById(R.id.iguide_sliding_login);
		relative_xiaoxi = (RelativeLayout) mSlidingMenu.findViewById(R.id.iguide_sliding_xiaoxi);
		sliding_login = (TextView) mSlidingMenu
				.findViewById(R.id.iguide_sliding_login_button);
		login = (RelativeLayout) mSlidingMenu.findViewById(R.id.iguide_login);
		nologin = (RelativeLayout) mSlidingMenu
				.findViewById(R.id.iguide_noLogin);
		relative_gengxin = (RelativeLayout) mSlidingMenu.findViewById(R.id.iguide_sliding_gengxin);
		head_image = (ImageView) mSlidingMenu.findViewById(R.id.iguide_sliding_touxiang);
		sliding_usernames = (TextView) mSlidingMenu.findViewById(R.id.iguide_sliding_usernames);
		version_text = (TextView) mSlidingMenu.findViewById(R.id.iguide_sliding_gengxin_version);
		relative_geren.setOnClickListener(listener);
		relative_yijian.setOnClickListener(listener);
		relative_guanyu.setOnClickListener(listener);
		relative_xiaoxi.setOnClickListener(listener);
		relative_exit_login.setOnClickListener(listener);
		sliding_login.setOnClickListener(listener);
		relative_gengxin.setOnClickListener(listener);
		edit.putString("version", "1.0");
		edit.commit();
		version_text.setText("v" + version.getString("version", ""));
	}

	View.OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {
			//登录Button
			case R.id.iguide_sliding_login_button:
				startActivity(new Intent(getApplicationContext(),LoginActivity.class));
				break;
				//进入消息中心
			case R.id.iguide_sliding_xiaoxi:
				startActivity(new Intent(getApplicationContext(), MessageActivity.class));
				break;
				//进入个人信息     
			case R.id.iguide_sliding_geren:
				if(!isLogin()){
					startActivity(new Intent(getApplicationContext(), LoginActivity.class));
				} else {
					startActivity(new Intent(getApplicationContext(),SettingActivity.class));
				}
				break;
				//进入意见反馈
			case R.id.iguide_sliding_yijian:
				startActivity(new Intent(getApplicationContext(),
						FankuiActivity.class));
				break;
				//进入关于我们
			case R.id.iguide_sliding_guanyu:
				startActivity(new Intent(getApplicationContext(), AboutWeActivity.class));
				break;
				//版本更新
			case R.id.iguide_sliding_gengxin:
				Loading.startLoading(HomeActivity.this);
				new UpdateAPKTask().execute(HomeActivity.class.toString());
				break;
				//退出登录
			case R.id.iguide_sliding_login:
				view = (RelativeLayout) LayoutInflater.from(HomeActivity.this).inflate(R.layout.iguide_dialog, null);
				dialog = new Dialog(HomeActivity.this, R.style.add_dialog);
				dialog.setContentView(view);
				dialog.show();
				tuiChuDialogView(view);
				//添加点击事件
				dialog_title.setText("您确定要退出登录吗?");
				dialog_no.setText("否");
				dialog_ok.setText("是");
				dialog_no.setOnClickListener(dialogListener);
				dialog_ok.setOnClickListener(dialogListener);
				break;
			}
		}
	};
	
	View.OnClickListener dialogListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.iguide_dialog_ok_button:
				 dialog.dismiss();//关闭对话框  
               //清空Sharedprefarance
               Editor editor = shared.edit();
               editor.putString("phone", "");
               editor.commit();
               reFresh();
				break;
			case R.id.iguide_dialog_no_button:
				dialog.dismiss();//关闭对话框  
				break;
			}
		}
	};
	
	public void reFresh(){
		Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
		startActivity(intent);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (System.currentTimeMillis() - cTime > 2000) {
				if (popupWindow!=null && popupWindow.isShowing()) {
					popupWindow.dismiss();
					return true;
				}
				Toast toast = Toast.makeText(this,
						getString(R.string.exit_tip), Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				cTime = System.currentTimeMillis();
				return true;
			} else {
				this.finish();
				Intent serviceIntent=new Intent("com.itg.download");
				stopService(serviceIntent);
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				startActivity(intent);
				System.exit(0);
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	class UpdateAPKTask extends AsyncTask<String, UpdateAPKBean, UpdateAPKBean> {
		private UpdateAPKBean bean;
		
		@Override
		protected UpdateAPKBean doInBackground(String... params) {
			// TODO Auto-generated method stub
			String UDUrl = AppConfig.HOTPOT_SERVICE_URL + "OtherService/VersionUpdate.svc?wsdl";
			String UDSoapAction = "http://tempuri.org/IVersionUpdate/VersionCheck";
			String UDMethod = "VersionCheck";
			String UDPsString[] = { "null" };
			String param[] = { "null" };
			String resultString = WebServiceUtil.GetWsMsg(UDUrl, UDSoapAction,
					UDMethod, UDPsString, param);
			String jsonResult = "";
			JSONObject jsonObject = null;
			try {
				jsonResult = DES_Encrypt.decryptDES(resultString,
						AppConfig.DES_KEY);
				JSONArray jsonArray = new JSONArray(jsonResult);
				for(int i = 0; i < jsonArray.length(); i++){
					jsonObject = jsonArray.getJSONObject(i);
					id = jsonObject.getString("VersionId");
					name = jsonObject.getString("SoftName");
					size = jsonObject.getString("FileSize");
					description = jsonObject.getString("Description");
				}
				 bean = new UpdateAPKBean();
				bean.setFileSize(size);
				bean.setSoftName(name);
				bean.setVersionId(id);
				bean.setDescription(description);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return bean;
		}
		@Override
		protected void onPostExecute(UpdateAPKBean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result != null){
				Loading.stopLoading(); //等待页面停止
				//判断版本号
				if(version.getString("version", "") != "" || version.getString("version", "") != null){
					if(Float.parseFloat(version.getString("version", "")) < Float.parseFloat(result.getVersionId())){
						type = result.getDescription();
						String name = result.getSoftName();
						String id = result.getVersionId();
						updateDialog(type, name);
//						Log.i("tag", "name = " + name);
						versionId=id;
					}
					if(Float.parseFloat(version.getString("version", "")) == Float.parseFloat(result.getVersionId())){
						Toast.makeText(HomeActivity.this, "已经是最新版本", Toast.LENGTH_SHORT).show();
					}
				}
				edit.commit();
			}
		}
	}
	
	//版本更新对话框
		public void updateDialog(String type, final String name){
			view = (RelativeLayout) LayoutInflater.from(HomeActivity.this).inflate(R.layout.iguide_dialog, null);
			dialog = new Dialog(HomeActivity.this, R.style.add_dialog);
			dialog.setContentView(view);
			dialog.show();
			tuiChuDialogView(view);
			//添加点击事件
			dialog_title.setText(type);
			dialog_no.setText("立即更新");
			dialog_ok.setText("以后再说");
			dialog_no.setOnClickListener(this);
			dialog_ok.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();//关闭对话框  
				}
			});
		}
		private void defalutInstall(String file) {
            Intent i = new Intent(Intent.ACTION_VIEW); 
            i.setDataAndType(Uri.fromFile(new File(file)), "application/vnd.android.package-archive"); 
            startActivity(i);
    }
		
		//实例化
		public void tuiChuDialogView(View view){
			//实例化
			dialog_title = (TextView) view.findViewById(R.id.text2);
			dialog_ok = (Button) view.findViewById(R.id.iguide_dialog_ok_button);
			dialog_no = (Button) view.findViewById(R.id.iguide_dialog_no_button);
		}
}
