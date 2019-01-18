package com.itg.ui.view.rotate;

import com.itg.ui.activity.DistrictInfoActivity;
import com.itg.ui.activity.MapWidgetActivity;

import android.app.Activity;
import android.view.animation.Animation;

public class DisplayNextView implements Animation.AnimationListener {

	Object obj;

	// 动画监听器的构造函数
	Activity ac;
	int order;

	public DisplayNextView(Activity ac, int order) {
		this.ac = ac;
		this.order = order;
	}

	public void onAnimationStart(Animation animation) {
	}

	public void onAnimationEnd(Animation animation) {
		doSomethingOnEnd(order);
	}

	public void onAnimationRepeat(Animation animation) {
	}

	private final class SwapViews implements Runnable {
		public void run() {
			switch (order) {
			case Constants.KEY_FIRST_INVERSE:
				((DistrictInfoActivity) ac).jumpToSecond();
				break;
			case Constants.KEY_SECOND_CLOCKWISE:
				((MapWidgetActivity)ac).jumpToFirst();
				break;
			}
		}
	}

	public void doSomethingOnEnd(int _order) {
		switch (_order) {
		case Constants.KEY_FIRST_INVERSE:
			((DistrictInfoActivity) ac).districtHotpotLayout.post(new SwapViews());
			break;

		case Constants.KEY_SECOND_CLOCKWISE:
			((MapWidgetActivity)ac).mapLayout.post(new SwapViews());
			break;
		}
	}
}
