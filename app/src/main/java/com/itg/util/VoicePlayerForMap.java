/**
 * @FileName VoicePlayerForMap.java
 * @Package com.itg.util
 * @Description TODO
 * @Author Alpha
 * @Date 2015-10-8 上午9:38:27 
 * @Version V1.0

 */
package com.itg.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;


import com.itg.iguide.R;
import com.itg.ui.view.FloatWindowService;
import com.itg.ui.view.mapwidget.Popup;

public class VoicePlayerForMap implements MediaPlayer.OnPreparedListener,
		MediaPlayer.OnCompletionListener {

	
	private Timer mTimer;
	private Popup context;

	// public VoicePlayerForMap(SeekBar seekBar,MapWidgetActivity context) {
	// this.seekBar=seekBar;
	// this.context=context;
	// MyApplication.mediaPlayer=new MediaPlayer();
	// MyApplication.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
	// MyApplication.mediaPlayer.setOnPreparedListener(this);
	// MyApplication.mediaPlayer.setOnCompletionListener(this);
	// mTimer=new Timer();
	//
	// }
	public VoicePlayerForMap(Popup context) {
		this.context = context;

		MyApplication.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		MyApplication.mediaPlayer.setOnPreparedListener(this);
		MyApplication.mediaPlayer.setOnCompletionListener(this);
		 mTimer=new Timer();
	}

	public String formatTime(long time) {
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
		 timerTask= getTaskInstance();
		 }
		 try {
				if(mTimer==null) mTimer=new Timer();
			 mTimer.schedule(timerTask, 0, 10);
		} catch (Exception e) {
			// TODO: handle exception
		}
		

		/* 在线音频获取时间需要在onPrepared后获取 */
		// context.voiceTotalTime.setText((formatTime(MyApplication.mediaPlayer.getDuration()))
		// +"");
		// context.voiceLength=MyApplication.mediaPlayer.getDuration();

	}

	public String getCurrentDuration() {
		return formatTime(MyApplication.mediaPlayer.getCurrentPosition());
	}

//	public void seekTo(int millionSeconds) {
//
//		if (millionSeconds == 0) {
//			int duration = MyApplication.mediaPlayer.getDuration();
//			int currentPos = seekBar.getProgress() * duration
//					/ seekBar.getMax();
//			MyApplication.mediaPlayer.seekTo(currentPos);
//		} else {
//			int currentPos = MyApplication.mediaPlayer.getCurrentPosition();
//			MyApplication.mediaPlayer.seekTo(currentPos + millionSeconds);
//		}
//
//	}

	public static void pause() {
		if (MyApplication.mediaPlayer.isPlaying()) {
			MyApplication.mediaPlayer.pause();
		}
	}

	public static void start() {
		MyApplication.mediaPlayer.start();

	}

	public static void release() {

		// mTimer.cancel();
		// mTimer.purge();
		MyApplication.mediaPlayer.stop();
		// MyApplication.mediaPlayer.release();
	}

	public void playByUrl(String url) {

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

	private boolean isCancel = false;
	private TimerTask timerTask;

	private TimerTask getTaskInstance() {
		TimerTask timerTask = new TimerTask() {

			@Override
			public void run() {
				if (MyApplication.mediaPlayer.isPlaying())
					handler.sendEmptyMessage(0);
			}
		};
		return timerTask;
	}

	static int pluger = 0;
	private static Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int duration = MyApplication.mediaPlayer.getDuration();
			int currentPos = MyApplication.mediaPlayer.getCurrentPosition();
			pluger = currentPos;
		
			 long pos=MyApplication.seekBar.getMaxProgress()*currentPos/(duration==0?1:duration);
			// seekBar.setProgress((int)pos);
			MyApplication.seekBar.setProgress((int)pos);
		}
	};

	@Override
	public void onCompletion(MediaPlayer mp) {
	
		 if(timerTask!=null)
		 {
		 timerTask.cancel();
			mTimer.cancel();
			mTimer.purge();
		 isCancel=true;
		// handler.removeCallbacks(timerTask);
		 }
		if (pluger > 20) //一个语音自然结束
		{
			MyApplication.isPlayFinished=true;
			context.voiceTipTextView.setText("讲解");
			//设置悬浮窗样式
			FloatWindowService.playControl.setImageResource(R.drawable.float_btn_play);
			MyApplication.seekBar.setProgress(0);
			//设置悬浮窗样式
			context.playList.clear();
		}
//		else {
//			Iterator<Entry<Integer, Boolean>> iterator= context.playList.entrySet().iterator();
//			iterator.remove();
//		}
		
	}
}
