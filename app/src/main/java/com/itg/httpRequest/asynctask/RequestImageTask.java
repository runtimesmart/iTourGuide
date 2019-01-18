package com.itg.httpRequest.asynctask;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.itg.bean.Province;
import com.itg.bean.ReauestImageBean;
import com.itg.httpRequest.WebServiceUtil;
import com.itg.iguide.R;
import com.itg.util.AppConfig;
import com.itg.util.DES_Encrypt;
import com.itg.util.MyApplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class RequestImageTask extends AsyncTask<String, Integer, Bitmap> {
	private ImageView image;
	private ReauestImageBean imagebean = new ReauestImageBean();;
	
	public RequestImageTask(ImageView image){
		this.image = image;
	}
	
	@Override
	protected Bitmap doInBackground(String... param) {
		// TODO Auto-generated method stub
		Log.i("tag", "图片开始加载");
		String ImageUrl = AppConfig.HOTPOT_SERVICE_URL + "UserInfo/UserInfoByNameProvider.svc?wsdl";
		String imageAction = "http://tempuri.org/IUserInfoByNameProvider/getUserInfoByName";
		String imageMethd = "getUserInfoByName";
		String imageString[] = {"userMobile"};
		String imageParames[] = {param[0]};
		Log.i("tag", "url = " + ImageUrl);
		Bitmap tmpBitmap = null;
		String image = AppConfig.IMAGE_URL + "UploadFiles/UserPhoto/";
		//获取json数据
		String imageJson = WebServiceUtil.GetWsMsg(ImageUrl, imageAction, imageMethd, imageString, imageParames);
		Log.i("tag", "imageJson = " + imageJson);
		//进行解密
		String jsonResult = "";
		try {
			jsonResult = DES_Encrypt.decryptDES(imageJson, AppConfig.DES_KEY);
			Log.i("tag", "jsonResult = " + jsonResult);
			//手动解析
			JSONObject jsonObject = new JSONObject(jsonResult);
			//转化成对象
			imagebean = (ReauestImageBean) MyApplication.parseObject(jsonObject, ReauestImageBean.class);
			String url = image + imagebean.getUserPhoto();
			InputStream inputStream = MyApplication.getHttpInputStream(url);
			tmpBitmap = BitmapFactory.decodeStream(inputStream);
			inputStream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tmpBitmap;
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}
	
	@Override
	protected void onPostExecute(Bitmap result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(result != null){
			image.setImageBitmap(result);
//			Log.i("tag", "异步加载图片完成!!!");
		} else {
			image.setImageResource(R.drawable.menu_touxiang);
		}
	}
	
}
