package com.itg.ui.fragment;

import com.itg.iguide.R;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeFragment extends Fragment implements OnClickListener {
	private LinearLayout list_linear, rim_linear;
	private ImageView map_image, list_image, rim_image;
	private TextView list_text, rim_text;
	private IguideListFragment iguideListFragment;
	private MapFragment mapFragment;
	private Fragment rimFragment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.iguide_home_fragment, null);
		initView(view);
		return view;
	}

	private void initView(View view) {
		list_linear = (LinearLayout) view
				.findViewById(R.id.iguide_home_list_linear);
		map_image = (ImageView) view
				.findViewById(R.id.iguide_home_map_imageButton);
		rim_linear = (LinearLayout) view
				.findViewById(R.id.iguide_home_rim_linear);
		list_linear.setOnClickListener(this);
		rim_linear.setOnClickListener(this);
		map_image.setOnClickListener(this);
		list_image = (ImageView) view.findViewById(R.id.iguide_home_list_image);
		rim_image = (ImageView) view.findViewById(R.id.iguide_home_rim_image);
		list_text = (TextView) view.findViewById(R.id.iguide_home_list_text);
		rim_text = (TextView) view.findViewById(R.id.iguide_home_rim_text);

		fragmentSwitcher(R.id.iguide_home_map_imageButton);
	}

	@Override
	public void onClick(View v) {
		fragmentSwitcher(v.getId());
	}

	@SuppressLint("NewApi")
	private void fragmentSwitcher(int index) {
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		hideFragments(transaction);

		switch (index) {
		case R.id.iguide_home_list_linear:
			if (iguideListFragment != null && iguideListFragment.isAdded()) {
				transaction.show(iguideListFragment);
			} else {
				iguideListFragment = new IguideListFragment();
				transaction.add(R.id.container, iguideListFragment,"list");
			}
			list_image.setImageResource(R.drawable.nav_liebiao_hv);
			rim_image.setImageResource(R.drawable.nav_zhoubian);
			list_text.setTextColor(Color.parseColor("#2facf5"));
			rim_text.setTextColor(Color.parseColor("#313131"));
			break;
		case R.id.iguide_home_map_imageButton:
			if (mapFragment != null && mapFragment.isAdded()) {
				transaction.show(mapFragment);
				//切换时实时获取当前标题下的景区
				CharSequence c=((EditText)(getActivity().findViewById(
						R.id.iguide_title_center_text))).getText();
				mapFragment.setupMapData(c);
			} else {
				mapFragment = new MapFragment();
				transaction.add(R.id.container, mapFragment,"map");
			
			}
			list_image.setImageResource(R.drawable.nav_liebiao);
			rim_image.setImageResource(R.drawable.nav_zhoubian);
			list_text.setTextColor(Color.parseColor("#313131"));
			rim_text.setTextColor(Color.parseColor("#313131"));
			break;
		case R.id.iguide_home_rim_linear:
			if (rimFragment != null && rimFragment.isAdded()) {
				transaction.show(rimFragment);
			} else {
				rimFragment = new RimFragment();
				transaction.add(R.id.container, rimFragment,"rim");
			}
			list_image.setImageResource(R.drawable.nav_liebiao);
			rim_image.setImageResource(R.drawable.nav_zhoubian_hv);
			list_text.setTextColor(Color.parseColor("#313131"));
			rim_text.setTextColor(Color.parseColor("#2facf5"));
			break;
		}
		transaction.commit();
	}
//隐藏所有fragment
	private void hideFragments(FragmentTransaction transaction) {
		if (iguideListFragment != null)
			transaction.hide(iguideListFragment);
		if (mapFragment != null)
			transaction.hide(mapFragment);
		if (rimFragment != null)
			transaction.hide(rimFragment);
	}
}