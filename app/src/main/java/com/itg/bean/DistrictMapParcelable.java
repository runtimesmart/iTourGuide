/**
* @FileName DistrictMap.java
* @Package com.itg.bean
* @Description TODO
* @Author Alpha
* @Date 2015-8-31 上午10:48:30 
* @Version V1.0

*/
package com.itg.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class DistrictMapParcelable implements Parcelable {

	 public int id ;
     public DistrictMapParcelable(Parcel in) {
    	 id=in.readInt();
    	 hd_Title=in.readString();
    	 hd_ImageName=in.readString();
    	 XCoordinate=in.readString();
    	 YCoordinate=in.readString();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getHd_Title() {
		return hd_Title;
	}
	public void setHd_Title(String hd_Title) {
		this.hd_Title = hd_Title;
	}
	public String getHd_ImageName() {
		return hd_ImageName;
	}
	public void setHd_ImageName(String hd_ImageName) {
		this.hd_ImageName = hd_ImageName;
	}
	public String getHd_Level() {
		return hd_Level;
	}
	public void setHd_Level(String hd_Level) {
		this.hd_Level = hd_Level;
	}
	public String getSize() {
		return Size;
	}
	public void setSize(String size) {
		Size = size;
	}
	public int getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(int timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getYCoordinate() {
		return YCoordinate;
	}
	public void setYCoordinate(String yCoordinate) {
		YCoordinate = yCoordinate;
	}
	public String getXCoordinate() {
		return XCoordinate;
	}
	public void setXCoordinate(String xCoordinate) {
		XCoordinate = xCoordinate;
	}
	public String getMapName() {
		return mapName;
	}
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
	public String hd_Title ;
     public String hd_ImageName ;
     public String hd_Level ;
     public String Size ;
     public int timeStamp ;
     public String YCoordinate ;
     public String XCoordinate ;
     public String mapName ;
	@Override
	public int describeContents() {
		
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(hd_Title);
		dest.writeString(hd_ImageName);
		dest.writeString(XCoordinate);
		dest.writeString(YCoordinate);
	}
	
	public static final Parcelable.Creator<DistrictMapParcelable> CREATOR = new Creator<DistrictMapParcelable>()
		    {
		        @Override
		        public DistrictMapParcelable[] newArray(int size)
		        {
		            return new DistrictMapParcelable[size];
		        }
		        
		        @Override
		        public DistrictMapParcelable createFromParcel(Parcel in)
		        {
		            return new DistrictMapParcelable(in);
		        }
		    };

}
