/**
 * @FileName District.java
 * @Package com.itg.bean
 * @Description TODO
 * @Author Alpha
 * @Date 2015-9-9 上午9:29:21 
 * @Version V1.0

 */
package com.itg.bean;

import java.util.List;

public class District {

	private int districtId;
	private int timeStamp;
	private String hotpotPath;
	private String districtName;
	private String districtImage;
	private String districtMapName;
	private String districtDescription;
	private String districtTicket;
	private String distirctTraffic;
	private String districtInnerline;
	private int districtOfflineId;
	private String latitude;
	private String longtitude;
	private String voice;

	public String getHotpotPath() {
		return hotpotPath;
	}

	public void setHotpotPath(String hotpotPath) {
		this.hotpotPath = hotpotPath;
	}

	public int getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(int timeStamp) {
		this.timeStamp = timeStamp;
	}

	public int getDistrictId() {
		return districtId;
	}

	public void setDistrictId(int districtId) {
		this.districtId = districtId;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getDistrictImage() {
		return districtImage;
	}

	public void setDistrictImage(String districtImage) {
		this.districtImage = districtImage;
	}

	public String getDistrictDescription() {
		return districtDescription;
	}

	public void setDistrictDescription(String districtDescription) {
		this.districtDescription = districtDescription;
	}

	public String getDistrictTicket() {
		return districtTicket;
	}

	public void setDistrictTicket(String districtTicket) {
		this.districtTicket = districtTicket;
	}

	public String getDistirctTraffic() {
		return distirctTraffic;
	}

	public void setDistirctTraffic(String distirctTraffic) {
		this.distirctTraffic = distirctTraffic;
	}

	public String getDistrictInnerline() {
		return districtInnerline;
	}

	public void setDistrictInnerline(String districtInnerline) {
		this.districtInnerline = districtInnerline;
	}

	public int getDistrictOfflineId() {
		return districtOfflineId;
	}

	public void setDistrictOfflineId(int districtOfflineId) {
		this.districtOfflineId = districtOfflineId;
	}

	public String getDistrictMapName() {
		return districtMapName;
	}

	public void setDistrictMapName(String districtMapName) {
		this.districtMapName = districtMapName;
	}

	public String getVoice() {
		return voice;
	}

	public void setVoice(String voice) {
		this.voice = voice;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(String longtitude) {
		this.longtitude = longtitude;
	}

	private List<HotPot> hotPot;

	public List<HotPot> getHotPot() {
		return hotPot;
	}

	public void setHotPot(List<HotPot> hotPot) {
		this.hotPot = hotPot;
	}

}
