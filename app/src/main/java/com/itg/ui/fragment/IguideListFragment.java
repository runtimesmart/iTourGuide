package com.itg.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import com.itg.adapter.DistrictListAdapter;
import com.itg.bean.DistrictMap;
import com.itg.httpRequest.asynctask.HotPotDistrictListTask;
import com.itg.iguide.R;
import com.itg.ui.activity.DistrictInfoActivity;
import com.itg.ui.view.refreshlistview.RefreshCallback;
import com.itg.ui.view.refreshlistview.RefreshListView;
import com.itg.util.ActivityConfig;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
/**
 *  主页列表数据
 * @author tong.han
 *
 */
public class IguideListFragment extends BaseFragment implements OnClickListener,OnItemClickListener,
RefreshCallback,TextWatcher{
	private ImageView search_image, left_image, center_image;
	private TextView center_text;
	private RefreshListView iguideListView;
	private List<DistrictMap> districtList;
	private DistrictListAdapter adapter;
	private String currentLocation;
	private int bouceHeight;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.iguide_list_fragment, null);
		String temLatlan = null;
		if(savedInstanceState==null)
		{
			temLatlan="";
		}
		else if(savedInstanceState!=null)
	   {
		   temLatlan=  savedInstanceState.getString("latlan");
	   }
		initComponent(view,temLatlan);
		return view;
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(!currentLocation.equals(""))
		outState.putCharSequence("latlan", currentLocation);
	}
	private void initComponent(View view,String latlan)
	{
		center_text=(EditText) getActivity().findViewById(R.id.iguide_title_center_text);
		iguideListView=(RefreshListView) view.findViewById(R.id.iguide_list_view);
		iguideListView.callback=this;
		districtList=new ArrayList<DistrictMap>();
	
		center_text.addTextChangedListener(this);
		initData(latlan,null);
		 
	}
	private void initData(String latlan,ViewGroup viewGroup)
	{
		if(latlan.equals(""))
			currentLocation =((TextView)getActivity().findViewById(R.id.iguide_latlng)).getText().toString();
			else currentLocation=latlan;
		adapter=new DistrictListAdapter(districtList,getActivity(),currentLocation);
		new HotPotDistrictListTask(districtList, adapter,viewGroup).execute(IguideListFragment.class.toString(),center_text.getText().toString(),String.valueOf(bouceHeight));
//		new HotPotDistrictListTask(districtList, adapter, getActivity().getBaseContext()).execute(IguideListFragment.class.toString(),center_text.getText().toString());
		iguideListView.setAdapter(adapter);
		iguideListView.setOnItemClickListener(this);
	}

	
		/**
		 * EditText 文字改变事件
		 * **/
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			new HotPotDistrictListTask(districtList, adapter,null).execute(IguideListFragment.class.toString(),s.toString(),"");
//			new HotPotDistrictListTask(districtList, adapter, getActivity().getBaseContext()).execute(IguideListFragment.class.toString(),s.toString());
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}
		
		@Override
		public void afterTextChanged(Editable s) {
		}
		
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case 1:
			
			break;

		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent =new Intent(getActivity(),DistrictInfoActivity.class);
		intent.putExtra("districtId", districtList.get(position-1).getId());
		startActivity(intent);
//		ActivityConfig.enterAnimation(IguideListFragment.this.getActivity());
		getActivity().overridePendingTransition(R.anim.iguide_push_right_left_in, R.anim.iguide_push_left_left_out);
	}
	@Override
	public void setOnRefreshListener(int bouceHeight) {
		ViewGroup headView=(ViewGroup) getActivity().findViewById(R.id.listviewheader);
		if(BaseFragment.isConnected)
		{
			this.bouceHeight=bouceHeight;
			initData("",headView);
		}
		else {
			headView.measure(0, 0);
			headView.setPadding(0, -bouceHeight, 0, 0);
		}
		
	
	}

}
