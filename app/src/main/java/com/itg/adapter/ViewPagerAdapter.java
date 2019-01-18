package com.itg.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ViewPagerAdapter extends PagerAdapter {

	private List<ImageView> imageViewList;

	public ViewPagerAdapter(List<ImageView> imageViewList) {
		this.imageViewList = imageViewList;
	}

	@Override
	public int getCount() {
		return imageViewList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(imageViewList.get(position));
		return imageViewList.get(position);
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewGroup) container).removeView((View)object);
	}
}
