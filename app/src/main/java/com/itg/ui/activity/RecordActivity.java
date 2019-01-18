package com.itg.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.itg.iguide.R;
/**
 * 历史记录
 * @author tong.han
 *
 */
public class RecordActivity extends BaseActivity {
	private ImageView head_image, back_image;
	private TextView user_name, title;
	private SharedPreferences shared;
	private String user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.iguide_record_layout);
		initView();
	}
	
	//实例化
	public void initView(){
		shared = getSharedPreferences("UserMsg", MODE_PRIVATE);
		user = shared.getString("phone", "");
		title = (TextView) findViewById(R.id.iguide_titletwo_center_text);
		head_image = (ImageView) findViewById(R.id.iguide_titletwo_touxiang_image);
		user_name = (TextView) findViewById(R.id.iguide_titletwo_username_text);
		back_image = (ImageView) findViewById(R.id.iguide_titletwo_left_image);
		title.setText("历史记录");
		user_name.setText(user);
		requestImage(head_image);
		back_image.setOnClickListener(listener);
	}
	
	View.OnClickListener listener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.iguide_titletwo_left_image:
				finish();
				break;
			}
		}
	};
}
