package com.itg.ui.view;

import com.itg.iguide.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

public class Loading extends Dialog {
	private static Loading loading;
	private static Loading mloading;

	public static void startLoading(Context context) {
		if (mloading == null) {
			mloading = Loading.createDialog(context);
			mloading.setMessage("正在加载中...", Color.argb(255, 255, 255, 255));
		}
		mloading.setCancelable(false);
		mloading.show();
	}

	public static void stopLoading() {
		if (mloading != null) {
			mloading.dismiss();
			mloading = null;
		}
	}

	public Loading(Context context) {
		super(context);
	}

	public Loading(Context context, int theme) {
		super(context, theme);
	}

	private static Loading createDialog(Context context) {
		loading = new Loading(context, R.style.loadingStyle);
		loading.setContentView(R.layout.loading);
		loading.getWindow().getAttributes().gravity = Gravity.CENTER;
		return loading;
	}

	public void onWindowFocusChanged(boolean hasFocus) {

		if (loading == null) {
			return;
		}

		ImageView imageView = (ImageView) loading.findViewById(R.id.loadingImageView);
		AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
		animationDrawable.start();
	}

	public Loading setTitile(String strTitle) {
		return loading;
	}

	public Loading setMessage(String strMessage, int color) {
		TextView tvMsg = (TextView) loading.findViewById(R.id.id_tv_loadingmsg);
		tvMsg.setTextColor(color);

		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}

		return loading;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Loading.stopLoading();
              dismiss();
		}
		return false;

	}

}
