/**
 * @FileName NetWorkBroardCastReceiver.java
 * @Package com.itg.broardcastreceiver
 * @Description TODO
 * @Author Alpha
 * @Date 2015-8-10 下午6:06:10 
 * @Version V1.0

 */
package com.itg.broadcastreceiver;

import java.util.ArrayList;
import java.util.List;

import com.itg.ui.view.FloatWindowService;
import com.itg.util.MyApplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
public class NetWorkBroadCastReceiver extends BroadcastReceiver {

	public final static int NONET = -1;
	public final static int WIFI = 1;
	public final static int MOBILE = 2;
	public final static int UNKNOW = 0;

	public NetWorkBroadCastReceiver() {
	}

	public static List<NetWorkBroadCastReceiverCallBack> netCallBacks = new ArrayList<NetWorkBroadCastReceiver.NetWorkBroadCastReceiverCallBack>();

	@Override
	public void onReceive(Context context, Intent intent) {
		String action= intent.getAction();
		if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
			getNetWorkInfo(context);
		}
//		else if(action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
//			String reason=intent.getStringExtra("reason");
//			if(reason.equals("homekey"))
//			{
//				if(FloatWindowService.view!=null) 
//				{
//					if(FloatWindowService.view.isShown())
//					{
//						FloatWindowService.view.setVisibility(View.GONE);
//					}
//				}
//			}
//		}
	}
	

	public static void getNetWorkInfo(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (netCallBacks!=null && netCallBacks.size() > 0) {
				if(networkInfo==null)
				{
					netCallBacks.get(0).onDisconnected();
				}
				else if (networkInfo.isConnected()) {
					int netState = networkInfo.getType();
					if (netState == ConnectivityManager.TYPE_WIFI) {
						netCallBacks.get(0).onConnected(WIFI);
					} else if (netState == ConnectivityManager.TYPE_MOBILE) {
						netCallBacks.get(0).onConnected(MOBILE);
					}
				
			}
		}
	}

	public interface NetWorkBroadCastReceiverCallBack {
		void onConnected(int conState);
		void onDisconnected();
	}

}
