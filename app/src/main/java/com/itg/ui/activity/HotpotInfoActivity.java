/**
 * @FileName HotPotInfoActivity.java
 * @Package com.itg.ui.activity
 * @Description TODO
 * @Author Alpha
 * @Date 2015-9-14 下午6:52:02 
 * @Version V1.0

 */
package com.itg.ui.activity;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import com.itg.adapter.InfinitePagerAdapter;
import com.itg.httpRequest.IGaussBgCallback;
import com.itg.httpRequest.IHotPotImageCallback;
import com.itg.httpRequest.asynctask.FetchGaussImageTask;
import com.itg.httpRequest.asynctask.HotPotImageTask;
import com.itg.iguide.R;
import com.itg.jni.FastJniBlur;
import com.itg.ui.view.FloatWindowService;
import com.itg.ui.view.FloatWindowService.Floatbinder;
import com.itg.util.AppConfig;
import com.itg.util.MyApplication;
import com.itg.util.SDFileUtil;
import com.itg.util.VoicePlayer;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class HotpotInfoActivity extends BaseActivity implements
		IGaussBgCallback, OnClickListener, IHotPotImageCallback,
		OnPageChangeListener,OnSeekBarChangeListener{

	private TextView hotPotTitle;
	private Intent intent;
	private RelativeLayout gaussBg;
	private ImageView backBtn;
	// private InfiniteLoopViewPager infiniteViewPager;
	private ViewPager infiniteViewPager;
	private LinearLayout group;
	private ImageView[] tipImages;
	private List<Bitmap> bitmapList;
	private List<ImageView> imagesViews = new ArrayList<ImageView>();

	/*语音播放*/
	public ImageView ivPlayBtn;
	private VoicePlayer voicePlayer;
	public static SeekBar seekBar;
	private String  hotpotVoice;
	public TextView voiceTotalTime;
	private TextView voiceCurrentTime;
	private ImageView voiceLeftBtn,voiceRightBtn;
	public boolean isPause=false;
	public int voiceLength=0;
	private FloatWindowService floatService;
	public int hotpotId;
	private SeekBar fakeSeekBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.iguide_hotpot_info_layout);

		initComponent();
		initMediaComponent();
		Intent intent= new Intent(HotpotInfoActivity.this, FloatWindowService.class);
		
		getApplicationContext().bindService(intent, connection, Context.BIND_AUTO_CREATE);
	}
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
	


	/**
	 * 获取传递值，并初始化标题和图片
	 * **/
	private void initComponent() {
		intent = getIntent();
		 hotpotId = intent.getIntExtra("hotpotid", 0);

		 hotpotVoice = intent.getStringExtra("hotpotvoiece");

		hotPotTitle = (TextView) findViewById(R.id.iguide_hotpot_header_title);
		backBtn = (ImageView) findViewById(R.id.iguide_title_left_image);
		infiniteViewPager = (ViewPager) findViewById(R.id.iguide_hotpot_image_gallary);
		gaussBg = (RelativeLayout) findViewById(R.id.iguide_hotpot_bg);
		String hotpotName = intent.getStringExtra("hotpotname");
		String hotpotImage = intent.getStringExtra("hotpotimage");
		backBtn.setOnClickListener(this);
		hotPotTitle.setText(hotpotName);
		String imageString = hotpotImage;
		String[] images = { imageString };
		boolean isOffline=false;
		String imagePath = "";
		if(hotpotImage.contains("/"))
		{
			isOffline=true;
			String tempath=hotpotImage.replace("thumb", "large");
			 imagePath= tempath.substring(0, tempath.lastIndexOf("/")+1);
			File file=new File(new SDFileUtil().getSDCard()+AppConfig.WIDGET_OFFLINE_PATH+ imagePath);
			images=file.list();
			imageString =imagePath+images[0];
		}
	
		else if (hotpotImage.contains(",")) {
			images = hotpotImage.split(",");
			imageString = images[0];
		}
	
		new FetchGaussImageTask(this,isOffline).execute(imageString,
				HotpotInfoActivity.class.toString());
		new HotPotImageTask(imagesViews, this,isOffline,imagePath).execute(images);
	}
	/**
	 * 初始化语音播放控件
	 * **/
	private void initMediaComponent()
	{
		
		seekBar=(SeekBar) findViewById(R.id.iguide_voice_progress);
		 fakeSeekBar=(SeekBar) findViewById(R.id.iguide_voice_progress_fake);
		if(hotpotVoice.equals(""))
		{
			LinearLayout voiceLinearLayout=(LinearLayout) findViewById(R.id.iguide_voice_container);
			voiceLinearLayout.setVisibility(View.GONE);
		}
		else {
			ivPlayBtn=(ImageView) findViewById(R.id.iguide_voice_speed_play);
			voiceTotalTime=(TextView) findViewById(R.id.iguide_voice_endtime);
			if(MyApplication.currentPlayId==hotpotId && HotpotInfoActivity.seekBar.getProgress()<HotpotInfoActivity.seekBar.getMax())
			{			
				isPause=false;
				tap=0;
				ivPlayBtn.setImageResource(R.drawable.voice_stop);
				voiceTotalTime.setText(MyApplication.totalVoiceTime);
				setRealProgressBarDisplay();
			}
			else {
				ivPlayBtn.setImageResource(R.drawable.voice_begin);
			}

			voiceCurrentTime=(TextView) findViewById(R.id.iguide_voice_currenttime);
			voiceLeftBtn=(ImageView) findViewById(R.id.iguide_voice_speed_left);
			voiceRightBtn=(ImageView) findViewById(R.id.iguide_voice_speed_right);
			voiceLeftBtn.setOnClickListener(this);
			voiceRightBtn.setOnClickListener(this);
			
			seekBar.setOnSeekBarChangeListener(this);
			ivPlayBtn.setOnClickListener(this);
			voicePlayer=new VoicePlayer(this);
		}
	}
	
	/**
	 * 设置真实progressBar显示
	 * **/
	private void setRealProgressBarDisplay(){
		fakeSeekBar.setVisibility(View.GONE);
		HotpotInfoActivity.seekBar.setVisibility(View.VISIBLE);
	}

	@SuppressWarnings("deprecation")
	private void blur(Bitmap bitmapCache) {
		Display display = getWindowManager().getDefaultDisplay();
		Point point = new Point();
		display.getSize(point);
		int x = point.x;
		int y = point.y;
		// 生成高斯模糊图片
		Bitmap bitmap = FastJniBlur.doBlurJniBitMap(bitmapCache, 20, true);
//		Bitmap bitmap = FastJniBlur.doBlur(bitmapCache, 20, true);


		// 将模糊图放大至屏幕大小
		Canvas canvas = new Canvas();
		canvas.drawBitmap(bitmap, null, new Rect(x / 10, y / 10, x, y), null);
		// 在屏幕显示图片区域
		bitmap = Bitmap.createBitmap(bitmap, bitmap.getWidth() / 2,
				bitmap.getHeight() / 2, bitmap.getWidth() / 5,
				bitmap.getHeight() / 2);
		// bitmap = Bitmap.createScaledBitmap(bitmap, x, y,false);
		gaussBg.setBackgroundDrawable(new BitmapDrawable(bitmap));
	}

	/* 背景模糊图片处理回调 */
	@Override
	public void setGaussBg(Bitmap bitmap) {
		blur(bitmap);
	}

	/* 景点图片回调 */
	@SuppressLint("NewApi")
	@Override
	public void setHotPotImage(List<Bitmap> bitmapList) {
		if (bitmapList.size() == 1) {
			infiniteViewPager.setVisibility(View.GONE);
			ImageView iView = (ImageView) findViewById(R.id.iguide_hotpot_image_container);
			iView.setImageBitmap(bitmapList.get(0));
			iView.setVisibility(View.VISIBLE);

		} else {
			 group=(LinearLayout) findViewById(R.id.iguide_image_tip_indicator);
			 tipImages=new ImageView[bitmapList.size()];
			for (int i = 0; i < bitmapList.size(); i++) {
				ImageView originalImageView = new ImageView(this);
				WeakReference<ImageView> originalReference=new WeakReference<ImageView>(originalImageView);
				originalReference.get().setImageBitmap(bitmapList.get(i));
				ImageView tipImageView = new ImageView(this);
				tipImages[i]=tipImageView;
				if(i==0)
				{
					tipImageView.setBackgroundResource(R.drawable.voice_biao01);
				}
				else {
					tipImageView.setBackgroundResource(R.drawable.voice_biao02);
				}
				DisplayMetrics displayMetrics=new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
				
				LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(new LayoutParams((int)(12*displayMetrics.density),(int)(12*displayMetrics.density)));
				params.setMargins(0, 0, (int) (5*displayMetrics.density), 0);
				tipImageView.setLayoutParams(params);
				group.addView(tipImageView);
				imagesViews.add(originalImageView);
			}

			InfinitePagerAdapter pageAdapterContainer = new InfinitePagerAdapter(imagesViews);
			infiniteViewPager.setAdapter(pageAdapterContainer);
			infiniteViewPager.setOnPageChangeListener(this);
			infiniteViewPager.setCurrentItem(imagesViews.size() * 1000);
		//	this.bitmapList=bitmapList;
		}

		
	}
	/*判断滑动状态 0什么也没做，1正在滑动，2滑动结束*/
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}
/*	当页面在滑动的时候会调用此方法，在滑动被停止之前，此方法回一直得到调用。其中三个参数的含义分别为：
	arg0 :当前页面，及你点击滑动的页面
	arg1:当前页面偏移的百分比
	arg2:当前页面偏移的像素位置  */
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}
/*页面滑动结束后，页面静止调用*/
	@Override
	public void onPageSelected(int arg0) {
		int position=arg0%imagesViews.size();
		setTipImageBg(position);
	}
	private void setTipImageBg(int  position)
	{
		for (int i = 0; i < group.getChildCount(); i++) {
			if(i==position)
				tipImages[i].setBackgroundResource(R.drawable.voice_biao01);
			else
				tipImages[i].setBackgroundResource(R.drawable.voice_biao02);
		}
	}
	public int tap=1;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iguide_title_left_image: //返回

			if(MyApplication.isPlayActivited)
				FloatWindowService.view.setVisibility(View.VISIBLE);
			this.exitAnimation();
			break;
		case R.id.iguide_voice_speed_play://播放
			MyApplication.isPlayActivited=true;
			MyApplication.currentPlayId=hotpotId;
			HotpotInfoActivity.this.setVoiceMax();
			//if(MyApplication.mediaPlayer.isPlaying()) VoicePlayerForMap.release(); //关闭地图页面播放
			if (tap % 2 == 1) {
				if (isPause)
					VoicePlayer.start();
				else
				{
					setRealProgressBarDisplay();
					String mediaSource=hotpotVoice.contains("/")?(new SDFileUtil().getSDCard()+AppConfig.WIDGET_OFFLINE_PATH+hotpotVoice): (AppConfig.HOTPOT_VOICE_URL+ hotpotVoice);
					voicePlayer.playByUrl(mediaSource);
					MyApplication.currentMediaUrl=mediaSource;
				}
				ivPlayBtn.setImageResource(R.drawable.voice_stop);
				tap++;
			} else if (tap % 2 == 0) {
				VoicePlayer.pause();
				isPause = true;
				ivPlayBtn.setImageResource(R.drawable.voice_begin);
				tap++;
			}
			break;
			case R.id.iguide_voice_speed_left:

				voicePlayer.seekTo(-voiceLength/50);
				break;
			case R.id.iguide_voice_speed_right:
				voicePlayer.seekTo(voiceLength/50);
				break;
		}

	
	}
	/*SeekBar拖动事件*/
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if(MyApplication.currentPlayId==hotpotId)
		voiceCurrentTime.setText(voicePlayer.getCurrentDuration());
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		voicePlayer.seekTo(0);
	}
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
		{
			if(MyApplication.isPlayActivited)
				FloatWindowService.view.setVisibility(View.VISIBLE);
			this.exitAnimation();
		
			//ActivityConfig.exitAnimation(HotpotInfoActivity.this);
//			if(!hotpotVoice.equals(""))
//			voicePlayer.release();
		}
		return super.onKeyDown(keyCode, event);
	}

	



	/* OnGestureListener */
	/*
	 * @Override public boolean onFling(MotionEvent e1, MotionEvent e2, float
	 * velocityX, float velocityY) { if(e1.getX()-e2.getX()>50 &&
	 * Math.abs(velocityX)>0)//向左滑动 {
	 * viewFlipper.setInAnimation(AnimationUtils.loadAnimation
	 * (this,R.anim.iguide_push_from_right_in));
	 * viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
	 * R.anim.iguide_push_left_left_out)); viewFlipper.showNext(); return true;
	 * } else if(e2.getX()-e1.getX()>50 && Math.abs(velocityX)>0) {
	 * viewFlipper.setInAnimation
	 * (AnimationUtils.loadAnimation(this,R.anim.iguide_push_left_righ_in));
	 * viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
	 * R.anim.iguide_push_from_left_out)); viewFlipper.showPrevious(); return
	 * true; } return false; }
	 * 
	 * @Override public boolean onDown(MotionEvent e) { return false; }
	 * 
	 * @Override public void onShowPress(MotionEvent e) { }
	 * 
	 * @Override public boolean onSingleTapUp(MotionEvent e) { return false; }
	 * 
	 * @Override public boolean onScroll(MotionEvent e1, MotionEvent e2, float
	 * distanceX, float distanceY) { return false; }
	 * 
	 * @Override public void onLongPress(MotionEvent e) { }
	 */

}
