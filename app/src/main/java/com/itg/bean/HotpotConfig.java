/**
* @FileName HotpotConfig.java
* @Package com.itg.bean
* @Description TODO
* @Author Alpha
* @Date 2015-10-28 下午1:52:41 
* @Version V1.0

*/
package com.itg.bean;

public class HotpotConfig {

	public HotpotConfig() {
	}
	private String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getVoice() {
		return voice;
	}
	public void setVoice(String voice) {
		this.voice = voice;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLattitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	private String title;
	private String image;
	private String voice;
	private String description;
	private String latitude;
	private String longitude;
	
}
