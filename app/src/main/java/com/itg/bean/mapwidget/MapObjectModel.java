package com.itg.bean.mapwidget;

import android.location.Location;

public class MapObjectModel 
{
	private String x;
	private String y;
	private int id;
	private String caption;
	private Location location;
	private String imageName;
	public String getImageName() {
		return imageName;
	}


	public void setImageName(String imageName) {
		this.imageName = imageName;
	}


	public String getVoiceName() {
		return voiceName;
	}


	public void setVoiceName(String voiceName) {
		this.voiceName = voiceName;
	}


	public void setX(String x) {
		this.x = x;
	}


	public void setY(String y) {
		this.y = y;
	}


	public void setId(int id) {
		this.id = id;
	}


	public void setCaption(String caption) {
		this.caption = caption;
	}


	public void setLocation(Location location) {
		this.location = location;
	}


	private String voiceName;

	

	public int getId() 
	{
		return id;
	}

	
	public String getX() 
	{
		return x;
	}


	public String getY() 
	{
		return y;
	}
	
	
	public Location getLocation()
	{
		return location;
	}
	
	
	public String getCaption()
	{
		return caption;
	}

}
