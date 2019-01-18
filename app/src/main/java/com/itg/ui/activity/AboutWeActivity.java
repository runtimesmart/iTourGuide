package com.itg.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.itg.iguide.R;

public class AboutWeActivity extends BaseActivity {
	private ImageView back, image1, image2;
	private TextView title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.iguide_about_layout);
		initView();
	}
	
	public void initView(){
		back = (ImageView) findViewById(R.id.iguide_title_left_image);
		title = (TextView) findViewById(R.id.iguide_title_center_text);
		image1 = (ImageView) findViewById(R.id.iguide_about_image1);
		image2 = (ImageView) findViewById(R.id.iguide_about_image2);
		back.setImageResource(R.drawable.login_jitou);
		title.setText("关于我们");
		back.setOnClickListener(listener);
		image1.setOnClickListener(listener);
		image2.setOnClickListener(listener);
	}
	
	View.OnClickListener listener = new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {
			//返回
			case R.id.iguide_title_left_image:
				finish();
				break;
				//公司概况
			case R.id.iguide_about_image1:
				Intent intent = new Intent(getApplicationContext(), SurveyActivity.class);
				intent.putExtra("key", "公司概况");
				intent.putExtra("index", "0");
				startActivity(intent);
				break;
				//用户协议
			case R.id.iguide_about_image2:
				Intent intent2 = new Intent(getApplicationContext(), SurveyActivity.class);
				intent2.putExtra("key", "用户协议");
				intent2.putExtra("index", "1");
				startActivity(intent2);
				break;
			}
		}
	};
}
