/**
 * @FileName BaseFragment.java
 * @Package com.itg.ui.fragment
 * @Description TODO
 * @Author Alpha
 * @Date 2015-8-11 上午10:20:25 
 * @Version V1.0

 */
package com.itg.ui.fragment;

import com.itg.broadcastreceiver.NetWorkBroadCastReceiver;
import com.itg.broadcastreceiver.NetWorkBroadCastReceiver.NetWorkBroadCastReceiverCallBack;
import com.itg.iguide.R;
import com.itg.util.NetWorkDetected;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

public class BaseFragment extends Fragment implements
		NetWorkBroadCastReceiverCallBack {

	public static boolean isConnected=false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		//注入继承NetWorkBroadCastReceiverCallBack 的类对象，有两层
				NetWorkBroadCastReceiver.netCallBacks.add(this);
				//首次进入，先检测一次网络，必须有NetWorkBroadCastReceiverCallBack，因此放后边。
				NetWorkBroadCastReceiver.getNetWorkInfo(getActivity());
	}
	

	@Override
	public void onResume() {	
		super.onResume();
//		//注入继承NetWorkBroadCastReceiverCallBack 的类对象，有两层
//		NetWorkBroadCastReceiver.netCallBacks.add(this);
//		//首次进入，先检测一次网络，必须有NetWorkBroadCastReceiverCallBack，因此放后边。
//		NetWorkBroadCastReceiver.getNetWorkInfo(getActivity());
		
		
	}
	
	@Override
	public void onDisconnected() {
		isConnected=false;
		Toast toast=null;
		 toast = Toast.makeText(getActivity(),
				 this.getString(R.string.network_nonet_tip), Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	@Override
	public void onConnected(int conState) {
		isConnected=true;
		Toast toast=null;
		if (conState == NetWorkBroadCastReceiver.WIFI) {
			toast = Toast.makeText(getActivity(),
					getActivity().getString(R.string.network_wifi_tip), Toast.LENGTH_SHORT);
		} else if (conState == NetWorkBroadCastReceiver.MOBILE) {
			toast = Toast.makeText(getActivity(),
					getActivity().getString(R.string.network_mobile_tip), Toast.LENGTH_SHORT);
		} else
		{
			toast = Toast.makeText(getActivity(),
					getActivity().getString(R.string.network_unknow_tip), Toast.LENGTH_SHORT);
	
		}
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

}
