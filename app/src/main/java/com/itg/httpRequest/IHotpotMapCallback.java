/**
* @FileName IHotpotMapCallBack.java
* @Package com.itg.httpRequest.asynctask
* @Description TODO
* @Author Alpha
* @Date 2015-9-29 上午10:11:53 
* @Version V1.0

*/
package com.itg.httpRequest;

import java.util.List;

import com.itg.bean.HotpotForMap;

public interface IHotpotMapCallback {
	void setHotpotMap(List<HotpotForMap> mapList);
}
