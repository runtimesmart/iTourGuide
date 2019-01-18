/**
* @FileName RefreshListView.java
* @Package com.itg.ui.view
* @Description TODO
* @Author Alpha
* @Date 2015-10-16 上午11:42:08 
* @Version V1.0

*/
package com.itg.ui.view.refreshlistview;

import java.text.SimpleDateFormat;

import com.itg.iguide.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View.OnTouchListener;
import android.widget.AbsListView.OnScrollListener;

public class RefreshListView extends ListView implements OnScrollListener  {

	private ViewGroup headerView;
	private ViewGroup footerView;
	private TextView pullDateTextView,refreshTextView,loadMoreTextView;
	private int topViewHeight;
	private int footerViewHeight;
	public RefreshCallback callback;
	
	private int DOWN_PULL=0,REFRESHING=1, UP_PULL=2,INIT=-1;
	private int currentState=INIT;
	private int firstVisibileViewItem;
	private boolean isScrollToBottom;
	public RefreshListView(Context context) {
		super(context);

	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeaderView();
		initFooterView();
		this.setOnScrollListener(this);
	}

	public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

	}

	public RefreshListView(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);

	}
	private void initHeaderView()
	{
		headerView=(ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.iguide_listview_header, null);
		pullDateTextView=(TextView) headerView.findViewById(R.id.listViewRefreshDate);
		refreshTextView=(TextView) headerView.findViewById(R.id.listViewRefreshTxtPre);
		headerView.measure(0, 0);
		topViewHeight=headerView.getMeasuredHeight();
		headerView.setPadding(0, -topViewHeight, 0, 0);
		addHeaderView(headerView, null, false);
	}
	private void initFooterView()
	{
		footerView=(ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.iguide_listview_footer, null);
		loadMoreTextView=(TextView) footerView.findViewById(R.id.listViewLoadMore);
		footerView.measure(0, 0);
		footerViewHeight=footerView.getMeasuredHeight();
		addFooterView(footerView, null, false);
	}

private int downY, diffY;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			 downY=(int) event.getY();
			refreshTextView.setText(R.string.listview_release_refresh);
			break;
		case MotionEvent.ACTION_MOVE:
			 diffY=(int) (event.getY()-downY);
			int headerPaddingTop=(int)(-topViewHeight+diffY/2.5);
			if( firstVisibileViewItem == 0 && headerPaddingTop>-topViewHeight)
			{		
				headerView.setPadding(0, headerPaddingTop, 0, 0);
				pullDateTextView.setText(getCurrentTime());
				currentState=DOWN_PULL;
			}		
			else if(footerViewHeight>0 && isScrollToBottom)
			{
				loadMoreTextView.setText(R.string.listview_load_more);
				footerView.setPadding(0, 0, 0,(footerViewHeight));
				currentState=UP_PULL;
			}
					
			break;
		case MotionEvent.ACTION_UP:
			if(currentState==DOWN_PULL && firstVisibileViewItem == 0 && diffY>topViewHeight*2)
			{
				pullDateTextView.setText(getCurrentTime());
				refreshTextView.setText(R.string.listview_refreshing);
				callback.setOnRefreshListener(topViewHeight);
				currentState=INIT;		
			}
			else if(getLastVisiblePosition()==getCount()-1 && currentState==UP_PULL)
			{
				loadMoreTextView.setText(R.string.listview_load_over);
				footerView.setPadding(0, 0, 0,(-footerViewHeight));
				this.setSelection(getCount());		
			}
			break;

		}
		return super.onTouchEvent(event);
	}
	

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) {
			if (isScrollToBottom && getLastVisiblePosition()==getCount()-1) {			
				this.setSelection(this.getCount());
				footerView.setPadding(0, 0, 0, 35);//最后一个item被遮挡
			}
			else if(firstVisibileViewItem==0 && !isScrollToBottom) {
				this.setSelection(firstVisibileViewItem+1);			
			}
		}
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		firstVisibileViewItem=firstVisibleItem;
		if (getLastVisiblePosition() == (totalItemCount - 1)) {
			isScrollToBottom = true;
		} else {
			isScrollToBottom = false;
		}
	}


	private String getCurrentTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				com.itg.util.AppConfig.SIMPLE_DATA_FORMATE);
		return dateFormat.format(System.currentTimeMillis());
	}
}
