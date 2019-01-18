package com.itg.ui.view.mapwidget;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.itg.bean.mapwidget.MapObjectModel;
import com.itg.httpRequest.asynctask.ImageTask;
import com.itg.iguide.R;
import com.itg.ui.activity.HotpotInfoActivity;
import com.itg.ui.activity.MapWidgetActivity;
import com.itg.util.AppConfig;
import com.itg.util.MyApplication;
import com.itg.util.VoicePlayerForMap;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Popup extends MapPopupBase implements OnClickListener {
	public final static int ZERO = 0;
	public final static int PADDING_BOTTOM = 11;
	public final static int PADDING_TOP = 5;
	public final static int PADDING_LEFT = 15;
	public final static int PADDING_RIGHT = 10;
	public final static float DEF_TEXT_SIZE = 16;
	public final static int IMAGE_SIZE = 30;
	public final static int MAX_EMS = 14;

	// private String voiceTipTxt="讲解";
	// private TextView text;
	private View view;
	// private RelativeLayout layout;
	private TextView hpTitleTextView;
	private ImageView hotpotImageView;
	private ViewGroup parentView;
	// 语音tip
	public TextView voiceTipTextView;
	private RelativeLayout voicePanelLayout;
	private VoicePlayerForMap voicePlayer;
	private MapObjectModel objectModel;
	private MapWidgetActivity context;
	// 点击pop的索引
	// private Integer popIndex;
	public HashMap<Integer, Boolean> playList = new LinkedHashMap<Integer, Boolean>();
	public TextView hidObjIdTextView;

	public Popup(MapWidgetActivity context, RelativeLayout popView,
			ViewGroup parentView) {
		super(context, parentView);
		this.context = context;
		this.view = popView;
		this.parentView = parentView;
		container.addView(view);
		// layout = (RelativeLayout) popView
		// .findViewById(R.id.iguide_hotpot_container);
		hpTitleTextView = (TextView) popView
				.findViewById(R.id.iguide_hotpot_map_title);
		hotpotImageView = (ImageView) popView
				.findViewById(R.id.iguide_hotpot_img_thumb);
		voiceTipTextView = (TextView) popView
				.findViewById(R.id.iguide_hotpot_voice_tip);
		// voiceTipTextView.setText("讲解");
		voicePanelLayout = (RelativeLayout) popView
				.findViewById(R.id.iguide_voice_panel);
		hidObjIdTextView = (TextView) popView
				.findViewById(R.id.iguide_hotpot_hid_objid);
		voicePlayer = new VoicePlayerForMap(this);
		voicePanelLayout.setOnClickListener(this);
		hotpotImageView.setOnClickListener(this);
		// TextView hpTitleTextView=(TextView)
		// view.findViewById(R.id.iguide_hotpot_map_title);
		// hpTitleTextView.setText(text)
		// text = new TextView(context);
		//
		// // text.setPadding((int)(PADDING_LEFT * dipScaleFactor),
		// // (int)(PADDING_TOP * dipScaleFactor),
		// // (int)(PADDING_RIGHT * dipScaleFactor),
		// // (int)(PADDING_BOTTOM * dipScaleFactor));
		//
		// //text.setBackgroundResource(R.drawable.ditu_jxbiao_hv);
		// text.setBackgroundColor(Color.WHITE);
		// text.setTextSize(DEF_TEXT_SIZE);
		// text.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
		// text.setMaxEms(MAX_EMS);
		// text.setTextColor(Color.BLACK);
		//
		// container.addView(text);
		//
		// text.setFocusable(true);
		// text.setClickable(true);
	}

	public void moveBy(int dx, int dy) {
		if (lastX != -1 && lastY != -1) {
			int paddingBottom = 0;
			int paddingRight = 0;
			if (container.getPaddingTop() > (screenHeight - (view.getHeight() + 3))) {
				paddingBottom = (container.getPaddingBottom() - dy);
			}

			if (container.getPaddingLeft() > (screenWidth - (view.getWidth() + 3))) {
				paddingRight = container.getPaddingRight() - dx;
			}

			container
					.setPadding(container.getPaddingLeft() + dx,
							container.getPaddingTop() + dy, paddingRight,
							paddingBottom);
		}
	}

	public void show(int theX, int theY) {
		hide();

		container.measure(view.getWidth(), view.getHeight());

		int x = theX - (getWidth() / 2);
		int y = theY - getHeight();

		container.setPadding(x, y, 0, 0);

		lastX = x;
		lastY = y;

		parentView.addView(container);
		container.setVisibility(View.VISIBLE);
	}

	// 加载Pop数据--图片 标题 语音等
	public void loadHotpotPopInfo(MapObjectModel objectModel) {
		this.objectModel = objectModel;
		hpTitleTextView.setText(objectModel.getCaption());

		if (!playList.isEmpty() && playList.get(objectModel.getId()) != null
				&& playList.get(objectModel.getId())) {
			if (!isPlay)
			{
				voiceTipTextView.setText("暂停");
			}
			else
				voiceTipTextView.setText("讲解中");
		} else {
			voiceTipTextView.setText("讲解");
		}
		int objId = objectModel.getId();
		hidObjIdTextView.setText(objId + "");
		if (!playList.containsKey(objId))
			playList.put(objId, false);

		new ImageTask(hotpotImageView, null).execute(
				objectModel.getImageName(), Popup.class.toString());

	}
	
//
//	public void showVoiceControl()
//	{
//		 floatService.view.setVisibility(View.VISIBLE);
//	}
	
	boolean isPlay = false;

	// int mediaStatus=0;
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iguide_voice_panel:
			int objId = Integer.parseInt(hidObjIdTextView.getText().toString());
		//	if(MyApplication.mediaPlayer.isPlaying()) VoicePlayer.release();
			MyApplication.isPlayActivited=true;
			context.setVoiceMax();
			if (isPlay) {
				if (voiceTipTextView.getText().equals("讲解中")) {
					isPlay = false;
					// setIsPlayTrue(objId,isPlay);
					voiceTipTextView.setText("暂停");
					VoicePlayerForMap.pause();
				} else if (voiceTipTextView.getText().equals("讲解")) {
					isPlay = true;
					setPlayFalse();
					setIsPlayTrue(objId, isPlay);
					voiceTipTextView.setText("讲解中");
					voicePlayer.playByUrl(AppConfig.HOTPOT_VOICE_URL
							+ objectModel.getVoiceName());
					MyApplication.currentMediaUrl=AppConfig.HOTPOT_VOICE_URL
							+ objectModel.getVoiceName();
				}
			} else {
				if (voiceTipTextView.getText().equals("暂停")) {
					isPlay = true;
					voiceTipTextView.setText("讲解中");
					VoicePlayerForMap.start();
				} else {

					isPlay = true;
					setPlayFalse();
				//	playList.clear();
					setIsPlayTrue(objId, isPlay);
					voiceTipTextView.setText("讲解中");
					voicePlayer.playByUrl(AppConfig.HOTPOT_VOICE_URL
							+ objectModel.getVoiceName());
					MyApplication.currentMediaUrl=AppConfig.HOTPOT_VOICE_URL
							+ objectModel.getVoiceName();
				}

			}

			break;
		case R.id.iguide_hotpot_img_thumb:
			Intent intent = new Intent(context, HotpotInfoActivity.class);
			intent.putExtra("hotpotid", objectModel.getId());
			intent.putExtra("hotpotname", objectModel.getCaption());
			intent.putExtra("hotpotimage", objectModel.getImageName());
			intent.putExtra("hotpotvoiece", objectModel.getVoiceName());
			VoicePlayerForMap.pause();
			context.startActivity(intent);
			context.enterAnimation();
			break;
		default:
			break;
		}
	}

	private void setIsPlayTrue(int id, boolean isPlay) {
		for (Integer keyId : playList.keySet()) {
			if (id == keyId) {
				playList.put(id, isPlay);
			}
		}
	}
	private void setPlayFalse()
	{
		for (Integer keyId : playList.keySet()) {
			if (playList.get(keyId)) {
				playList.put(keyId, false);
			}
		}
	}
	// public void setText(String theText)
	// {
	// text.setPadding((int)(PADDING_LEFT * dipScaleFactor),
	// (int)(PADDING_TOP * dipScaleFactor),
	// (int)(PADDING_RIGHT * dipScaleFactor),
	// (int)(PADDING_BOTTOM * dipScaleFactor));
	//
	// text.setText(theText + "   ");
	// }

	// public void setIcon(BitmapDrawable theDrawable)
	// {
	// if (theDrawable != null) {
	// theDrawable.setBounds(0,0, (int) (theDrawable.getBitmap().getWidth()),
	// (int)(theDrawable.getBitmap().getHeight()));
	// }
	//
	// view.setCompoundDrawables(null, null, theDrawable, null);
	// }
	//
	//
	// public void removeIcon()
	// {
	// text.setCompoundDrawables(null, null, null, null);
	// }
	//
	//
	// public void setOnClickListener(View.OnTouchListener listener)
	// {
	// if(text != null){
	// text.setOnTouchListener(listener);
	// }
	// }
}
