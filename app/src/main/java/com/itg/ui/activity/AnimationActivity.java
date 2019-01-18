package com.itg.ui.activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import com.itg.adapter.ViewPagerAdapter;
import com.itg.iguide.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 项目开机动画
 * 
 * @author tong.han
 *
 * @date 2015年8月3日下午3:10:16
 */
public class AnimationActivity extends Activity{
	private TextView textButton;
	private List<ImageView> imageList;
	private ViewPager viewpager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SharedPreferences animationShare = getSharedPreferences("animation", Context.MODE_WORLD_READABLE+Context.MODE_WORLD_WRITEABLE);
		Editor editor = animationShare.edit();
		boolean isFirst = animationShare.getBoolean("animation", false);
		if(isFirst){
			Intent intent = new Intent(AnimationActivity.this, HomeActivity.class);
			startActivity(intent);
		} else {
			setContentView(R.layout.iguide_viewflipper_layout);
			editor.putBoolean("animation", true);
			editor.commit();
			initView();
		}
	}
	
	public void initView(){
		viewpager = (ViewPager) findViewById(R.id.iguide_viewpager); 
		textButton = (TextView) findViewById(R.id.iguide_textview);
		imageList = new ArrayList<ImageView>();
		WeakReference<List<ImageView>> weakReference=new WeakReference<List<ImageView>>(imageList);
	    ImageView imageView1 = new ImageView(this);
		WeakReference<ImageView> weakReference1=new WeakReference<ImageView>(imageView1);
		weakReference1.get().setImageResource(R.drawable.one);
	    //将图片填充整个屏幕
	    imageView1.setScaleType(android.widget.ImageView.ScaleType.FIT_XY);
	    weakReference.get().add(imageView1);
	    ImageView imageView2 = new ImageView(this);
	    WeakReference<ImageView> weakReference2=new WeakReference<ImageView>(imageView2);
	    weakReference2.get().setImageResource(R.drawable.two);
	    imageView2.setScaleType(android.widget.ImageView.ScaleType.FIT_XY);
	    weakReference.get().add(imageView2);
	    ImageView imageView3 = new ImageView(this);
	    WeakReference<ImageView> weakReference3=new WeakReference<ImageView>(imageView3);
	    weakReference3.get().setImageResource(R.drawable.stree);
	    weakReference3.get().setScaleType(android.widget.ImageView.ScaleType.FIT_XY);
	    weakReference.get().add(imageView3);
		viewpager.setAdapter(new ViewPagerAdapter(weakReference.get()));
		viewpager.setOnPageChangeListener(pageListener);
	}
	
	OnPageChangeListener pageListener = new OnPageChangeListener() {
		
		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			if(position == 2){
				textButton.setVisibility(View.VISIBLE);
				textButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						startActivity(new Intent(getApplicationContext(), HomeActivity.class));
					}
				});
			} else {
				textButton.setVisibility(View.GONE);
			}
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}
	};
}
