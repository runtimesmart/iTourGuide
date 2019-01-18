/**
 * @FileName InfinitePagerAdapter.java
 * @Package com.itg.adapter
 * @Description TODO
 * @Author Alpha
 * @Date 2015-9-22 下午2:26:43 
 * @Version V1.0

 */
package com.itg.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

public class InfinitePagerAdapter extends PagerAdapter {

	private List<ImageView> imageViewList;

	public InfinitePagerAdapter(List<ImageView> imageViewList) {
		this.imageViewList = imageViewList;
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		position = position % imageViewList.size();
		if(position>imageViewList.size()){
			position=0;
		}
		ImageView imageView = imageViewList.get(position);
		ViewParent viewParent = imageView.getParent();
		if (viewParent != null) {
			((ViewGroup) viewParent).removeView(imageView);
		}

		container.addView(imageViewList.get(position));
		return imageViewList.get(position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		
//		 ((ViewPager)container).removeView(imageViewList.get(position%imageViewList.size()));
//		 super.destroyItem(container, position, object);
	}

}
