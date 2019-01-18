/**
* @FileName DistrictMapCallBack.java
* @Package com.itg.ui.fragment
* @Description TODO
* @Author Alpha
* @Date 2015-8-31 下午5:27:01 
* @Version V1.0

*/
package com.itg.httpRequest;

import java.util.List;

import com.itg.bean.DistrictMap;

public interface IDistrictMapCallback {
	void getDistrictMapInfo(List<DistrictMap> mapList);
}
