/**
 * @FileName GPSChangeReceiver.java
 * @Package com.itg.broadcastreceiver
 * @Description TODO
 * @Author Alpha
 * @Date 2015-8-17 下午4:55:23 
 * @Version V1.0

 */
package com.itg.broadcastreceiver;

import java.util.ArrayList;
import java.util.List;

import com.itg.util.NetWorkDetected;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class GPSChangeReceiver extends BroadcastReceiver {
	public static List<GPSChangeListener> callbackListener = new ArrayList<GPSChangeReceiver.GPSChangeListener>();

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
			if (callbackListener.size() > 0) {
				if (NetWorkDetected.isOPen(context.getApplicationContext())) {
					callbackListener.get(0).stateChangeCallback(this, true);
				} else {
					callbackListener.get(0).stateChangeCallback(this, false);
				}

			}
		}
	}

	public interface GPSChangeListener {
		public void stateChangeCallback(GPSChangeReceiver receiver,
				boolean state);
	}

}
