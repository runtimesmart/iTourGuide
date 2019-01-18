/**
* @FileName HotpotDistrictInfoCallback.java
* @Package com.itg.httpRequest.asynctask.offlineline
* @Description TODO
* @Author Alpha
* @Date 2015-10-29 上午10:33:37 
* @Version V1.0

*/
package com.itg.httpRequest.asynctask.offlineline;

import java.util.List;

import com.itg.bean.District;
import com.itg.bean.HotPot;
import com.itg.bean.HotPotDistrictConfig;
import com.itg.bean.HotpotConfig;

public interface HotpotDistrictInfoCallback {

	void setHotpotDistrictInfo(List<District> district,List<HotPot> hotpotConfigs);

}
