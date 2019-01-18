/**
* @FileName InfiniteLooperPagerAdapter.java
* @Package com.itg.adapter
* @Description TODO
* @Author Alpha
* @Date 2015-9-18 下午3:01:27 
* @Version V1.0

*/
package com.itg.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.widget.ImageView;

public class InfiniteLooperPagerAdapter extends PagerAdapter {

	private List<ImageView> images;
	public InfiniteLooperPagerAdapter( List<ImageView> images) {
		this.images=images;
	}

	@Override
	public int getCount() {
		return images.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return false;
	}

}
