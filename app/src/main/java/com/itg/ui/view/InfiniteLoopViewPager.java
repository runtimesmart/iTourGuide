/**
* @FileName InfinaiteLoopViewPager.java
* @Package com.itg.ui.view
* @Description TODO
* @Author Alpha
* @Date 2015-9-18 下午2:14:15 
* @Version V1.0

*/
package com.itg.ui.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

public class InfiniteLoopViewPager extends ViewPager {

	public InfiniteLoopViewPager(Context context) {
		super(context);

	}

	public InfiniteLoopViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);

	}
	
	@Override
	public void setAdapter(PagerAdapter arg0) {
		super.setAdapter(arg0);
		setCurrentItem(0);
	}
	
	@Override
	public void setCurrentItem(int item, boolean smoothScroll) {
		item=item%getAdapter().getCount();
		super.setCurrentItem(item, true);

	}

}
