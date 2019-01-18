package com.itg.ui.activity;

import java.util.List;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.itg.adapter.CollectAdapter;
import com.itg.bean.DownloadInfo;
import com.itg.db.DownloadDBUtil;
import com.itg.iguide.R;
import com.itg.ui.view.CeHuaListView;
/**
 * 我的收藏    我的下载
 * @author tong.han
 *
 */
public class CollectActivity extends BaseActivity {
	private TextView text, user_name;
	private ImageView image_back, head_image, gap_image;
	private String text_name, user;
	private SharedPreferences shared;
	public CeHuaListView collectListView;
	private DownloadDBUtil util;
	private List<DownloadInfo> infoList;
	private CollectAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.iguide_collect_layout);
		initView();
	}
	
	public void initView(){
		shared = getSharedPreferences("UserMsg", MODE_PRIVATE);
		user = shared.getString("phone", "");
		text_name = getIntent().getStringExtra("key");
		text = (TextView) findViewById(R.id.iguide_titletwo_center_text);
		image_back = (ImageView) findViewById(R.id.iguide_titletwo_left_image);
		head_image = (ImageView) findViewById(R.id.iguide_titletwo_touxiang_image);
		user_name = (TextView) findViewById(R.id.iguide_titletwo_username_text);
		collectListView = (CeHuaListView) findViewById(R.id.iguide_collect_listview);
		gap_image = (ImageView) findViewById(R.id.iguide_collect_imageView);
		image_back.setOnClickListener(listener);
		text.setText(text_name);
		user_name.setText(user);
		requestImage(head_image);
		//从数据库查询数据
		util = new DownloadDBUtil(CollectActivity.this);
		infoList = util.query();
		//判断集合的大小,等于0时,显示没有下载的图片 否则,显示集合
		if(infoList.size() == 0){
			gap_image.setVisibility(View.VISIBLE);
		} else {
			gap_image.setVisibility(View.GONE);
			adapter = new CollectAdapter(infoList, CollectActivity.this);
			collectListView.setAdapter(adapter);
		}
	}
	
	View.OnClickListener listener = new View.OnClickListener() {
		
		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			switch (view.getId()) {
			case R.id.iguide_titletwo_left_image:
				finish();
				break;
			}
		}
	};
	
	
}
