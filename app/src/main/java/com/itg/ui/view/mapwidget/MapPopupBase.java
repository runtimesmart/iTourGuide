package com.itg.ui.view.mapwidget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MapPopupBase
{
	protected ViewGroup parentView;
	
	protected RelativeLayout container;
	protected float dipScaleFactor;
	protected int lastX;
	protected int lastY;
	protected int screenHeight;
	protected int screenWidth;
	

	public MapPopupBase(Context context, ViewGroup parentView) 
	{
	    screenHeight = context.getResources().getDisplayMetrics().heightPixels;
	    screenWidth = context.getResources().getDisplayMetrics().widthPixels;
	    
		this.parentView = parentView;
    	dipScaleFactor = context.getResources().getDisplayMetrics().density;
    	
		container = new RelativeLayout(context);
		container.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		lastX = lastY = -1;
	}		
	
	
	public void show(ViewGroup view, int theX, int theY)
	{
		hide();

		container.measure(view.getWidth(), view.getHeight());
		
		int x = theX - (getWidth() / 2);
		int y = theY - getHeight();
		
		container.setPadding(x, y, 0, 0);

		lastX = x;
		lastY = y;
		
		parentView.addView(container);
		container.setVisibility(View.VISIBLE);
	}

	
	public int getHeight()
	{
		return container.getMeasuredHeight();
	}
	
	
	public int getWidth()
	{
		return container.getMeasuredWidth();
	}
	
	
	public boolean isVisible()
	{
		return container.getVisibility() == View.VISIBLE;
	}
	
	
	public void hide()
	{
		container.setPadding(0, 0, 0, 0);
		container.setVisibility(View.INVISIBLE);
		parentView.removeView(container);
	}
	
	
	public void setOnClickListener(View.OnTouchListener listener)
	{
	    if(container != null){
	        container.setOnTouchListener(listener);
	    }
	}
}
