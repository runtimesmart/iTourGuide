/**
* @FileName FloatWindowService.java
* @Package com.itg.ui.view
* @Description TODO
* @Author Alpha
* @Date 2015-11-3 上午9:47:50 
* @Version V1.0

*/
package com.itg.ui.view;

import java.io.IOException;

import com.itg.iguide.R;
import com.itg.util.MyApplication;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

public class FloatWindowService extends Service implements OnClickListener,OnLongClickListener {

	private static WindowManager windowManager;
	public static  View view;
	public static ImageView playControl;
	private LayoutParams layoutParams;
	private Floatbinder binder=null;
	
	public class Floatbinder extends Binder
	{
		public FloatWindowService getService()
		{
			return FloatWindowService.this;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		initWindow();

	}
//	@Override
//	public int onStartCommand(Intent intent, int flags, int startId) {
//		return super.onStartCommand(intent, flags, startId);
//	}
	@Override
	public IBinder onBind(Intent arg0) {
		windowManager.addView(view, layoutParams);
		if(binder==null)
		return  new Floatbinder();
		else return binder;
	}
	private void initWindow()
	{
		 windowManager=  (WindowManager) getApplication().getSystemService("window");
		 layoutParams=new WindowManager.LayoutParams();
		 DisplayMetrics displayMetrics=new DisplayMetrics();
		  windowManager.getDefaultDisplay().getMetrics(displayMetrics);
		 
		 layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;// 系统提示window
		 layoutParams.format = PixelFormat.TRANSLUCENT;// 支持透明
	        //mParams.format = PixelFormat.RGBA_8888;
		 layoutParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;// 焦点
		layoutParams.gravity=Gravity.BOTTOM | Gravity.LEFT;
		layoutParams.x=(int) (20*displayMetrics.density);
		layoutParams.y=(int) (80*displayMetrics.density);
		layoutParams.width=LayoutParams.WRAP_CONTENT;
		layoutParams.height=LayoutParams.WRAP_CONTENT;
		view=LayoutInflater.from(getApplicationContext()).inflate(R.layout.iguide_float_window, null);
		playControl=(ImageView) view.findViewById(R.id.iguide_float_control);
		playControl.setOnClickListener(this);
		playControl.setOnLongClickListener(this);
		//view.setId(1);
		MyApplication.seekBar=(CircularSeekBar) view.findViewById(R.id.iguide_seekbar);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iguide_float_control:
			if (MyApplication.mediaPlayer.isPlaying()) {
				playControl.setImageResource(R.drawable.float_btn_play);
				MyApplication.mediaPlayer.pause();
			} else {
				if(MyApplication.isPlayFinished)
				{
					MyApplication.isPlayFinished=false;
					MyApplication.mediaPlayer.reset();
					try {
						MyApplication.mediaPlayer.setDataSource(MyApplication.currentMediaUrl);
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					MyApplication.mediaPlayer.prepareAsync();
				}
				else
				MyApplication.mediaPlayer.start();
				playControl.setImageResource(R.drawable.float_btn_pause);

			}
			break;
		}
	}
	@Override
	public boolean onLongClick(View v) {
	
			view.setVisibility(View.GONE);
			if (MyApplication.mediaPlayer.isPlaying()) {
			MyApplication.mediaPlayer.pause();
		}
		return false;
	}

}
