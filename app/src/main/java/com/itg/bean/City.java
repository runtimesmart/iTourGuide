/**
* @FileName City.java
* @Package com.itg.bean
* @Description TODO
* @Author Alpha
* @Date 2015-8-25 下午6:38:12 
* @Version V1.0

*/
package com.itg.bean;



public class City {

	public City() {
	}

	 public int cityId;
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public int getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}
	public String getCIndex() {
		return cIndex;
	}
	public void setCIndex(String cIndex) {
		this.cIndex = cIndex;
	}

	public String cityName;
     public int provinceId;
     public String cIndex ;
	

}
