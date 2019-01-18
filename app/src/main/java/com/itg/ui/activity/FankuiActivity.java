package com.itg.ui.activity;


import com.itg.iguide.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 意见反馈
 * @author tong.han
 *
 */
public class FankuiActivity extends Activity {
	private ImageView image_back;
	private Button login_button;
	private TextView text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.iguide_fankui_layout);
		initView();
	}
	
	public void initView(){
		text = (TextView) findViewById(R.id.iguide_title_center_text);
		image_back = (ImageView) findViewById(R.id.iguide_title_left_image);
		login_button = (Button) findViewById(R.id.iguide_fankui_button);
		image_back.setImageResource(R.drawable.login_jitou);
		text.setText("意见反馈");
		image_back.setOnClickListener(listener);
		login_button.setOnClickListener(listener);
	}
	
	View.OnClickListener listener = new View.OnClickListener() {
		
		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			switch(view.getId()){
			case R.id.iguide_title_left_image:
				startActivity(new Intent(getApplicationContext(), HomeActivity.class));
				break;
			case R.id.iguide_fankui_button:
				
				break;
			}
		}
	};
}
