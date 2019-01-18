package com.itg.ui.activity;
import java.io.File;

import com.itg.broadcastreceiver.NetWorkBroadCastReceiver;
import com.itg.broadcastreceiver.NetWorkBroadCastReceiver.NetWorkBroadCastReceiverCallBack;
import com.itg.httpRequest.asynctask.RequestImageTask;
import com.itg.iguide.R;
import com.itg.util.AppConfig;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.Toast;

public class BaseActivity extends Activity implements NetWorkBroadCastReceiverCallBack {

	Toast toast;
	private SharedPreferences shared;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		shared = getSharedPreferences("UserMsg", MODE_PRIVATE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	
	public void setVoiceMax()
	{
		final AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 12, 0);
		audioManager.setStreamVolume(AudioManager.STREAM_ALARM,
				audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM),
				AudioManager.FLAG_PLAY_SOUND);
	}
	public void exitAnimation()
	{
		finish();
		overridePendingTransition(R.anim.iguide_push_left_righ_in, R.anim.iguide_push_right_left_out);
		
	}

	public void enterAnimation()
	{
		overridePendingTransition(R.anim.iguide_push_right_left_in, R.anim.iguide_push_left_left_out);
	}
	
	//通过手机号码判断是否登录
	public boolean isLogin(){
		String phone = requestPhone();
		if(phone != "" || phone.length() != 0){
			return true;
		}
		return false;
	}
	
	//拿到注册的手机号码
	public String requestPhone(){
		SharedPreferences isLogin = getSharedPreferences("UserMsg", MODE_PRIVATE);
		String phone = isLogin.getString("phone", "");
		return phone;
	}
	
	//拿到本地存储的头像
	public void requestImage(ImageView imageview){
		Bitmap image = null;
		String userName = shared.getString("phone", "");
		String userImage = shared.getString("userImage", "");
		String fileName = "/sdcard/itourguide/UserPhoto/" + userImage ;
		
		String fileImage = AppConfig.IMAGE_URL + "UploadFiles/UserPhoto/" + userName;
		File imageFile = new File(fileName);
		image = BitmapFactory.decodeFile(fileName);
		//判断imageName在Sharedprefarance里面存储为空
		if(userImage != ""){
			if(userImage.contains(userName)){
//				Log.i("tag", "image = " + userName);
				if(imageFile.exists()){
					imageview.setImageBitmap(image);  
				} 
			} else {
				imageview.setImageResource(R.drawable.menu_touxiang);
			}
		} else {
			new RequestImageTask(imageview).execute(userName);
		}
	}
	
	@Override
	public void onResume() {	
		super.onResume();
		//注入继承NetWorkBroadCastReceiverCallBack 的类对象，有两层
		NetWorkBroadCastReceiver.netCallBacks.add(this);
		//首次进入，先检测一次网络，必须有NetWorkBroadCastReceiverCallBack，因此放后边。
		NetWorkBroadCastReceiver.getNetWorkInfo(BaseActivity.this);
	}
	@Override
	public void onPause() {
		super.onPause();
		toast=null;
	}
	@Override
	public void onDisconnected() {
		if(toast==null)
		{
		toast=	Toast.makeText(this,getString(R.string.network_nonet_tip) ,Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		}
	}
	@Override
	public void onConnected(int conState) {
		if(toast==null)
		{
		if (conState==NetWorkBroadCastReceiver.WIFI) {
			toast=	Toast.makeText(this,getString(R.string.network_wifi_tip) , Toast.LENGTH_SHORT);
		}
		else if(conState==NetWorkBroadCastReceiver.MOBILE)
		{
			toast=	Toast.makeText(this,getString(R.string.network_mobile_tip) ,  Toast.LENGTH_SHORT);
		}
		else toast=	Toast.makeText(this,getString(R.string.network_unknow_tip) ,  Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		}
	}

	
}
