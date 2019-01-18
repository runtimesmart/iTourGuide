/**
* @FileName HotPotDistrict.java
* @Package com.itg.bean
* @Description TODO
* @Author Alpha
* @Date 2015-8-21 下午2:44:22 
* @Version V1.0

*/
package com.itg.bean;


public class HotPotDistrict {

	private int id;
	private String DistrictName;
	private String DistrictImage;
	private String ProvinceName;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDistrictName() {
		return DistrictName;
	}
	public void setDistrictName(String districtName) {
		DistrictName = districtName;
	}
	public String getDistrictImage() {
		return DistrictImage;
	}
	public void setDistrictImage(String districtImage) {
		DistrictImage = districtImage;
	}
	public String getProvinceName() {
		return ProvinceName;
	}
	public void setProvinceName(String provinceName) {
		ProvinceName = provinceName;
	}
	
}
