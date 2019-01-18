/**
* @FileName DistrictIntroduce.java
* @Package com.itg.ui.activity
* @Description TODO
* @Author Alpha
* @Date 2015-9-14 下午2:18:21 
* @Version V1.0

*/
package com.itg.ui.activity;

import com.itg.iguide.R;
import com.itg.util.ActivityConfig;
import com.itg.util.AppConfig;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class DistrictIntroduce extends BaseActivity implements OnClickListener {
private WebView wView;
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.iguide_district_info_introduce);
  TextView tv=	(TextView) findViewById(R.id.iguide_district_introduce);
   wView=(WebView) findViewById(R.id.iguide_district_stuff_detail);
  Intent intent=getIntent();
  CharSequence detailStuff=intent.getCharSequenceExtra("introduce");
  if(detailStuff.toString().contains(".html"))
  {
	  wView.loadUrl("file:/sdcard/"+AppConfig.WIDGET_OFFLINE_PATH+detailStuff);
	  wView.setVisibility(View.VISIBLE);
	  //禁止复制
	  wView.setOnLongClickListener(new OnLongClickListener() {
		
		@Override
		public boolean onLongClick(View v) {
			return true;
		}
	});
  }
  else
  {
  tv.setText(Html.fromHtml(detailStuff.toString()) );
  tv.setVisibility(View.VISIBLE);
  }
  TextView title= (TextView) findViewById(R.id.iguide_local_district);
  title.setText(intent.getCharSequenceExtra("title"));
  ImageView backImageBtn=(ImageView) findViewById(R.id.iguide_title_left_image);
  ImageView rightImageView=(ImageView) findViewById(R.id.iguide_title_right_image_map);
  rightImageView.setVisibility(View.GONE);
  backImageBtn.setOnClickListener(this);
}

@Override
public void onClick(View v) {
	switch (v.getId()) {
	case R.id.iguide_title_left_image:
		wView.destroy();
	//	ActivityConfig.exitAnimation(DistrictIntroduce.this);
		DistrictIntroduce.this.exitAnimation();
		break;

	default:
		break;
	}
}
//@Override
//	public void onBackPressed() {
//	ActivityConfig.exitAnimation(DistrictIntroduce.this);
//	this.finish();
//		super.onBackPressed();
//	}
@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	if(event.getKeyCode()==KeyEvent.KEYCODE_BACK )
	{
		wView.destroy();
		this.exitAnimation();
//		ActivityConfig.exitAnimation(DistrictIntroduce.this);
		return true;
	}
		return super.onKeyDown(keyCode, event);
	}

}
