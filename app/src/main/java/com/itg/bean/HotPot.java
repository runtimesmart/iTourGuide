/**
* @FileName HotPot.java
* @Package com.itg.bean
* @Description TODO
* @Author Alpha
* @Date 2015-9-9 上午9:28:19 
* @Version V1.0

*/
package com.itg.bean;

public class HotPot {

	private int hotpotId;
    public int getHotpotId() {
		return hotpotId;
	}
	public void setHotpotId(int hotpotId) {
		this.hotpotId = hotpotId;
	}
	public String getHotpotImageName() {
		return hotpotImageName;
	}
	public void setHotpotImageName(String hotpotImageName) {
		this.hotpotImageName = hotpotImageName;
	}
	public String getHotpotName() {
		return hotpotName;
	}
	public void setHotpotName(String hotpotName) {
		this.hotpotName = hotpotName;
	}
	public String getHotPotVoiceName() {
		return hotPotVoiceName;
	}
	public void setHotPotVoiceName(String hotPotVoiceName) {
		this.hotPotVoiceName = hotPotVoiceName;
	}
	private String hotpotImageName;
	private String hotpotName;
    private String hotPotVoiceName;
    private String lantitude;
    public String getLantitude() {
		return lantitude;
	}
	public void setLantitude(String lantitude) {
		this.lantitude = lantitude;
	}
	public String getLongtitude() {
		return longtitude;
	}
	public void setLongtitude(String longtitude) {
		this.longtitude = longtitude;
	}
	private String longtitude;

}
