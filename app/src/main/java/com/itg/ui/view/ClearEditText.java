/**
* @FileName ClearEditText.java
* @Package com.itg.ui.view
* @Description TODO
* @Author Alpha
* @Date 2015-8-19 下午3:33:23 
* @Version V1.0

*/
package com.itg.ui.view;


import com.itg.iguide.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

public class ClearEditText extends EditText {

	public ClearEditText(Context context) {
		super(context);

	}

	public ClearEditText(Context context, AttributeSet attrs) {
		this(context, attrs,android.R.attr.editTextStyle);

	}

	public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();

	}

	
	@SuppressWarnings("deprecation")
	private void init()
	{
		Drawable drawable= getCompoundDrawables()[2];
		if(drawable==null)
		{
			drawable= getResources().getDrawable(R.drawable.iguide_search_txt_cancel_selector);
		}
		drawable.setBounds(0, 0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
     //   addTextChangedListener(watcher)
	
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction()==MotionEvent.ACTION_UP)
		{
		boolean touchCancel=event.getX()<(getWidth()-getPaddingRight()) && event.getX()>(getWidth()-getTotalPaddingRight());
	  if(touchCancel)	
		  {
		   this.setText("");
		  }
		}
	  return super.onTouchEvent(event);
	}

}
