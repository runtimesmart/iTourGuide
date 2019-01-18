/**
* @FileName DownLoadInfo.java
* @Package com.itg.bean
* @Description TODO
* @Author Alpha
* @Date 2015-9-15 下午7:18:50 
* @Version V1.0

*/
package com.itg.bean;

public class DownloadInfo {
	int filelength;
	int version;
	String path;
	int resourceid;
	int downloadLength;
	String timestamp;
	String title;

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getFilelength() {
		return filelength;
	}
	public void setFilelength(int filelength) {
		this.filelength = filelength;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getResourceid() {
		return resourceid;
	}
	public void setResourceid(int resourceid) {
		this.resourceid = resourceid;
	}
	public int getDownloadLength() {
		return downloadLength;
	}
	public void setDownloadLength(int downloadLength) {
		this.downloadLength = downloadLength;
	}
	
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public DownloadInfo(){
		
	}
	public DownloadInfo(String path,int resourceid,int downloadLength,int fileLength,int version ) {
		this.path=path;
		this.resourceid=resourceid;
		this.downloadLength=downloadLength;
		this.filelength=fileLength;
		this.version=version;
	}
	public DownloadInfo(int resourceid, String timestamp, String title) {
		super();
		this.resourceid = resourceid;
		this.timestamp = timestamp;
		this.title = title;
	}
	
}
