/**
* @FileName VoicePlayer.java
* @Package com.itg.util
* @Description TODO
* @Author Alpha
* @Date 2015-9-23 下午2:38:15 
* @Version V1.0

*/
package com.itg.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.itg.iguide.R;
import com.itg.ui.activity.HotpotInfoActivity;
import com.itg.ui.view.FloatWindowService;
import android.media.AudioManager;
import android.media.MediaPlayer; 
import android.os.Handler;
import android.os.Message;
import android.widget.SeekBar;
public class VoicePlayer implements MediaPlayer.OnPreparedListener,MediaPlayer.OnCompletionListener {


	//private static Handler handler;
	private Timer mTimer;
	private HotpotInfoActivity context;
	public int totalViceTime;
	public VoicePlayer(HotpotInfoActivity context) {
		this.context=context;
		MyApplication.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		MyApplication.mediaPlayer.setOnPreparedListener(this);
		MyApplication.mediaPlayer.setOnCompletionListener(this);
		mTimer=new Timer();	
	
	}
	public VoicePlayer() {

		MyApplication.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		MyApplication.mediaPlayer.setOnPreparedListener(this);
		MyApplication.mediaPlayer.setOnCompletionListener(this);
		mTimer=new Timer();	
	}


	public String formatTime(long time)
	{
		Date date = new Date(time);
		SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
		return formatter.format(date);
	}
	@Override
	public void onPrepared(MediaPlayer mp) {

		mp.start();
		if(context==null)return;
		if(isCancel || timerTask==null)
		{
			timerTask=	getTaskInstance();
		}
		if(mTimer==null) mTimer=new Timer();
		mTimer.schedule(timerTask, 0, 10);
		
		/*在线音频获取时间需要在onPrepared后获取*/
		MyApplication.totalVoiceTime=formatTime(MyApplication.mediaPlayer.getDuration());
		context.voiceTotalTime.setText((formatTime(MyApplication.mediaPlayer.getDuration())) +"");
		context.voiceLength=MyApplication.mediaPlayer.getDuration();

	}
	
	public String getCurrentDuration()
	{
		return formatTime(MyApplication.mediaPlayer.getCurrentPosition()) ;
	}
	
	public void  seekTo(int millionSeconds) {
		
		if(millionSeconds==0)
		{
			int duration= MyApplication.mediaPlayer.getDuration();
			int currentPos=HotpotInfoActivity.seekBar.getProgress()*duration/HotpotInfoActivity.seekBar.getMax();	
			MyApplication.mediaPlayer.seekTo(currentPos);
		}
		else {
			int currentPos=MyApplication.mediaPlayer.getCurrentPosition();
			MyApplication.mediaPlayer.seekTo(currentPos+millionSeconds);
		}
	
	}

	public static void pause()
	{
		if(MyApplication.mediaPlayer.isPlaying())
		{
			MyApplication.mediaPlayer.pause();
		}
	}
	public static void start()
	{
		MyApplication.mediaPlayer.start();
		
	}
	public static void release()
	{
		MyApplication.mediaPlayer.stop();
	}
	
	public void playByUrl(String url)
	{
		MyApplication.mediaPlayer.reset();
		try {
			MyApplication.mediaPlayer.setDataSource(url);
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
	private boolean isCancel=false;
	private TimerTask timerTask;
	private TimerTask getTaskInstance()
	{
		TimerTask timerTask=new TimerTask() {
			
			@Override
			public void run() {
				if(MyApplication.mediaPlayer.isPlaying())
				handler.sendEmptyMessage(0);
			}
		};
		return timerTask;
	}

	static int pluger=0;
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			int duration=MyApplication.mediaPlayer.getDuration();
			int currentPos=MyApplication.mediaPlayer.getCurrentPosition();
			pluger=currentPos;
			long pos=HotpotInfoActivity.seekBar.getMax()*currentPos/(duration==0?1:duration);
			HotpotInfoActivity.seekBar.setProgress((int)pos);
			MyApplication.seekBar.setProgress((int)pos);
		}
	};
	
	

	/**
	 * 本意当前语音播放结束时执行
	 * 
	 * 多个语音切换时，播放第二个语音时，会执行这个方法，意为结束前一个语音，
	 * 此处设置pluger判断是否是开始播放，则代表此操作时多个音频切换操作
	 * **/
	@Override
	public void onCompletion(MediaPlayer mp) {
	
		if(timerTask!=null)
		{		
			timerTask.cancel();
			mTimer.cancel();
			mTimer.purge();
			mTimer=null;
//			System.gc();
			isCancel=true;
		//	handler.removeCallbacks(timerTask);
		}
		if(pluger>10)
		{
			MyApplication.isPlayFinished=true;
		context.ivPlayBtn.setImageResource(R.drawable.voice_begin);
		
		//设置悬浮窗样式
		FloatWindowService.playControl.setImageResource(R.drawable.float_btn_play);
		MyApplication.seekBar.setProgress(0);
		//设置悬浮窗样式
		
		context.isPause=false;
		context.tap=1;
		}
		
//		((AudioManager) context.getSystemService(Context.AUDIO_SERVICE))
//				.requestAudioFocus(new OnAudioFocusChangeListener() {
//					@Override
//					public void onAudioFocusChange(int focusChange) {
//					}
//				}, AudioManager.STREAM_MUSIC,
//						AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

	}
}
