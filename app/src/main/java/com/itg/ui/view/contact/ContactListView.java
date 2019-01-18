package com.itg.ui.view.contact;
import android.widget.ListView;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ListAdapter;

public class ContactListView extends ListView {

	protected boolean mIsFastScrollEnabled = false;
	protected IndexScroller mScroller = null;
	protected GestureDetector mGestureDetector = null;

	// additional customization
	protected boolean inSearchMode = false; // whether is in search mode
	protected boolean autoHide = false; // alway show the scroller

	public ContactListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ContactListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public IndexScroller getScroller() {
		return mScroller;
	}

	@Override
	public boolean isFastScrollEnabled() {
		return mIsFastScrollEnabled;
	}

	// override this if necessary for custom scroller
	public void createScroller() {
		mScroller = new IndexScroller(getContext(), this);
		mScroller.setAutoHide(autoHide);
		mScroller.setShowIndexContainer(true);

		if (autoHide)
			mScroller.hide();
		else
			mScroller.show();
	}

	@Override
	public void setFastScrollEnabled(boolean enabled) {
		mIsFastScrollEnabled = enabled;
		if (mIsFastScrollEnabled) {
			if (mScroller == null) {
				createScroller();
			}
		} else {
			if (mScroller != null) {
				mScroller.hide();
				mScroller = null;
			}
		}
	}

//	 @Override
//	 protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//	 int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
//	 MeasureSpec.AT_MOST);
//	 super.onMeasure(widthMeasureSpec, expandSpec);
//	
//	 }

//	public void setListViewHeightBasedOnChildren(ListView listView) {
//		// 获取ListView对应的Adapter
//		ListAdapter listAdapter = listView.getAdapter();
//		if (listAdapter == null) {
//			return;
//		}
//		int totalHeight = 0;
//		for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
//			View listItem = listAdapter.getView(i, null,
//					listView);
//			listItem.measure(0, 0); // 计算子项View 的宽高
//			totalHeight += listItem.getMeasuredHeight()+20; // 统计所有子项的总高度
//		}
//		ViewGroup.LayoutParams params = listView.getLayoutParams();
//		params.height = totalHeight
//				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//		// listView.getDividerHeight()获取子项间分隔符占用的高度
//		// params.height最后得到整个ListView完整显示需要的高度
//		listView.setLayoutParams(params);
//	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);

		if (mScroller != null)
			mScroller.draw(canvas);
	}

	
	
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// Intercept ListView's touch event
		if (mScroller != null && mScroller.onTouchEvent(ev))
		{	
			invalidate();//重绘消除字母提示
			return true;
		}

		if (mGestureDetector == null)
		{
			mGestureDetector = new GestureDetector(getContext(),
					new GestureDetector.SimpleOnGestureListener()
					{

						@Override
						public boolean onFling(MotionEvent e1, MotionEvent e2,
								float velocityX, float velocityY)
						{
							// If fling happens, index bar shows
							mScroller.show();
							return super.onFling(e1, e2, velocityX, velocityY);
						}

					});
		}
		mGestureDetector.onTouchEvent(ev);

		return super.onTouchEvent(ev);
	}
	

	

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		if (mScroller != null)
			mScroller.setAdapter(adapter);

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (mScroller != null)
			mScroller.onSizeChanged(w, h, oldw, oldh);
		if (this.getAdapter().getCount() > 0) {
			mScroller.setAdapter(this.getAdapter());
			invalidate();
		}

	}



}
