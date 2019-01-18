package com.itg.adapter;

import java.util.ArrayList;
import java.util.List;

import com.itg.bean.SearchHistoryBean;
import com.itg.iguide.R;
import com.itg.ui.activity.SearchActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SearchAutoAdapter extends BaseAdapter {
	private Context mContext;
	private List<SearchHistoryBean> mOriginalValues;// 所有的Item
	private List<SearchHistoryBean> mObjects;// 过滤后的item
	private final Object mLock = new Object();
	private int mMaxMatch = 10;// 最多显示多少个选项,负数表示全部
	
	public SearchAutoAdapter(Context context, int maxMatch) {
		this.mContext = context;
		this.mMaxMatch = maxMatch;
		mOriginalValues = initSearchHistory();
		mObjects = mOriginalValues;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return null == mObjects ? 0 : mObjects.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null == mObjects ? 0 : mObjects.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.aty_hot_item, parent, false);
			holder = new ViewHolder();
			holder.text = (TextView) convertView
					.findViewById(R.id.aty_hot_textview);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		SearchHistoryBean data = mObjects.get(position);
		holder.text.setText(data.getName());
		return convertView;
	}
	
	/**
	 * 读取历史搜索记录
	 */
	public List initSearchHistory() {
		SharedPreferences sp = mContext.getSharedPreferences(SearchActivity.SEARCH_HISTORY, 0);
		String longhistory = sp.getString(SearchActivity.SEARCH_HISTORY, "");
		String[] hisArrays = longhistory.split(",");
		mOriginalValues = new ArrayList<SearchHistoryBean>();
		if (hisArrays.length == 0) {
			return null;
		}
		for (int i = 0; i < hisArrays.length; i++) {
			SearchHistoryBean bean = new SearchHistoryBean();
			bean.setName(hisArrays[i]);
			mOriginalValues.add(bean);
		}
		return mOriginalValues;
	}
	
	class ViewHolder{
		TextView text;
	}

}
