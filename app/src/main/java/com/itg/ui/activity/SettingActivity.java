package com.itg.ui.activity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itg.httpRequest.SceneryOnlineService;
import com.itg.iguide.R;
import com.itg.util.AppConfig;
import com.itg.util.FileUtils;
import com.itg.util.SDFileUtil;
import com.itg.util.SizeFile;

public class SettingActivity extends BaseActivity {
	private TextView text, username, size_text;
	private ImageView image_back;
	private RelativeLayout touxiang, shoucang, jilu, xiazai, pop_relative, huancun;
	private PopupWindow mPopupWindow;
	private Button pick, pictrue, back;
	private ImageView image_head, image_head2;
	
	//dialog的view
	private TextView dialog_title;
	private Button dialog_no, dialog_ok;
	
	private String date, userImgName, phone, userPhoto;
	private String file_size[] = {"/sdcard/iguide/maps"};
	private String file_text = "/sdcard/iguide/maps";
	private String srcPath = "/sdcard/itourguide/UserPhoto/image.jpg"; // 头像名称
	
	private SharedPreferences shareds;
	private FileOutputStream fileout = null;
	private SizeFile sizeFile;
	private Dialog dialog;
	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				try {
					if (userPhoto != null && !userPhoto.equals("null")) {
						showUserHead(userPhoto);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 2:
				Toast.makeText(SettingActivity.this, "头像上传成功", 0).show();
				break;
			case 3:
				Toast.makeText(SettingActivity.this, "头像上传失败,请重新上传", 0).show();
				break;
			case 4:
				Toast.makeText(SettingActivity.this, "用户电话号码有误,请重新登录", 0).show();
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.iguide_setting_layout);
		initView();
	}
	
	public void initView() {
		sizeFile = new SizeFile();
		shareds = SettingActivity.this.getSharedPreferences("UserMsg", Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
		phone = shareds.getString("phone", "");
		
		text = (TextView) findViewById(R.id.iguide_titletwo_center_text);
		image_back = (ImageView) findViewById(R.id.iguide_titletwo_left_image);
		touxiang = (RelativeLayout) findViewById(R.id.iguide_setting_touxiang_relative);
		shoucang = (RelativeLayout) findViewById(R.id.iguide_setting_shoucang_relative);
		jilu = (RelativeLayout) findViewById(R.id.iguide_setting_jilu_relative);
		xiazai = (RelativeLayout) findViewById(R.id.iguide_setting_xiazai_relative);
		huancun = (RelativeLayout) findViewById(R.id.iguide_setting_huancun_relative);
		image_head = (ImageView) findViewById(R.id.iguide_titletwo_touxiang_image);
		image_head2 = (ImageView) findViewById(R.id.iguide_setting_touxiang_image);
		username = (TextView) findViewById(R.id.iguide_titletwo_username_text);
		size_text = (TextView) findViewById(R.id.iguide_setting_huancun_text2);
		
		String user_name = shareds.getString("phone", "");
		image_back.setOnClickListener(listener);
		touxiang.setOnClickListener(listener);
		shoucang.setOnClickListener(listener);
		jilu.setOnClickListener(listener);
		xiazai.setOnClickListener(listener);
		huancun.setOnClickListener(listener);
		
		requestImage(image_head);   //更改头像
		requestImage(image_head2); //更改头像
		username.setText(user_name);
		text.setText("个人信息");
		String i = sizeFile.seekSize(file_size);
		if(i == null){
			size_text.setText("");
		} else {
			size_text.setText(i);
		}
	}
	
	View.OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {
			case R.id.iguide_titletwo_left_image:
				finish();
				break;
			case R.id.iguide_setting_touxiang_relative:
				// 头像 pop
				showPrompt();
				break;
			case R.id.iguide_setting_shoucang_relative:
				// 我的收藏
				Intent intent = new Intent(getApplicationContext(), CollectActivity.class);
				intent.putExtra("key", "我的收藏");
				startActivity(intent);
				break;
			case R.id.iguide_setting_jilu_relative:
				// 历史记录
				startActivity(new Intent(getApplicationContext(), RecordActivity.class));
				break;
			case R.id.iguide_setting_xiazai_relative:
				// 我的下载
				Intent intent1 = new Intent(getApplicationContext(), CollectActivity.class);
				intent1.putExtra("key", "我的下载");
				startActivity(intent1);
				break;
			case R.id.iguide_setting_huancun_relative:
				RelativeLayout view = (RelativeLayout) LayoutInflater.from(SettingActivity.this).inflate(R.layout.iguide_dialog, null);
				dialog = new Dialog(SettingActivity.this, R.style.add_dialog);
				dialog.setContentView(view);
				dialog.show();
				dialogView(view);
				break;
			}
		}
	};
	
	//缓存清理  实例化
	public void dialogView(View view){
		//实例化
		dialog_title = (TextView) view.findViewById(R.id.text2);
		dialog_ok = (Button) view.findViewById(R.id.iguide_dialog_ok_button);
		dialog_no = (Button) view.findViewById(R.id.iguide_dialog_no_button);
		//添加点击事件
		dialog_title.setText("您确定要清理缓存吗?");
		dialog_no.setText("暂不清理");
		dialog_ok.setText("清理");
		dialog_no.setOnClickListener(dialogListener);
		dialog_ok.setOnClickListener(dialogListener);
	}
	
	View.OnClickListener dialogListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iguide_dialog_no_button: //不允许
				dialog.dismiss();//关闭对话框  
				break;

			case R.id.iguide_dialog_ok_button:  //允许
				File file = new File(file_text);
				boolean bb = sizeFile.deleteFile(file);
				if(bb){
					Toast.makeText(SettingActivity.this, "缓存已清除成功", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(SettingActivity.this, "您还没有缓存哦", Toast.LENGTH_SHORT).show();
				}
				size_text.setText("");
				dialog.dismiss();
				break;
			}
		}
	};
	
	// 更改头像
			public void showPrompt() {
				SharedPreferences share = getSharedPreferences("UserMsg", MODE_PRIVATE);
				String shouji = share.getString("phone", "");
				SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
				date = sDateFormat.format(new java.util.Date()) + "|" + shouji;
				View view = LayoutInflater.from(SettingActivity.this).inflate(R.layout.iguide_setting_pop_layout, null);
				if (mPopupWindow == null) {
					mPopupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
					popInitView(view);
					ColorDrawable dw = new ColorDrawable(0x90000000);
					mPopupWindow.setBackgroundDrawable(dw);  //设置背景透明
					mPopupWindow.setAnimationStyle(R.style.PopupAnimation);
					mPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
					mPopupWindow.update();
				} else {
					mPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
				}
			}
			
			private void popInitView(View view) {
				// TODO Auto-generated method stub pick, pictrue, back;
				pick = (Button) view.findViewById(R.id.iguide_pop_pick_button);
				pictrue = (Button) view.findViewById(R.id.iguide_pop_picture_button);
				back = (Button) view.findViewById(R.id.iguide_pop_back_button);
				pop_relative = (RelativeLayout) view.findViewById(R.id.pop_relative);
				pick.setOnClickListener(popListener);
				pictrue.setOnClickListener(popListener);
				back.setOnClickListener(popListener);
				pop_relative.setOnClickListener(popListener);
			}
			
			View.OnClickListener popListener = new View.OnClickListener() {
				
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					switch (view.getId()) {
					case R.id.iguide_pop_pick_button:
						// 拍照
						camera();
						break;

					case R.id.iguide_pop_picture_button:
						// 相册调用
						gallery();
						break;

					case R.id.iguide_pop_back_button:
						// 取消pop
						mPopupWindow.dismiss();
						break;
						
					case R.id.pop_relative:
						mPopupWindow.dismiss();
						break;
					}
				}
			};
			
			 //从相机获取
			public void camera() {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File("/sdcard/itourguide/", date + ".jpg")));
				startActivityForResult(intent, 2);
			}
			
			 // 从相册获取
			public void gallery() {
				// 激活系统图库，选择一张图片
				Intent intent = new Intent(Intent.ACTION_PICK);
				 intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
				 startActivityForResult(intent, 1);
			}
			
			protected void onActivityResult(int requestCode, int resultCode, Intent data) {
				switch (requestCode) {
				// 如果是直接从相册获取
				case 1:
					if (data != null) {
						startPhotoZoom(data.getData()); // 裁剪图片
					}
					break;
				// 如果是调用相机拍照时
				case 2:
					File temp = new File("/sdcard/itourguide/" + date + ".jpg");
					startPhotoZoom(Uri.fromFile(temp));
					break;
				// 取得裁剪后的图片
				case 3:
					if (data != null) {
						setPicToView(data);
					}
					break;
				};
			}

		 // 裁剪图片方法实现
		public void startPhotoZoom(Uri uri) {
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(uri, "image/*");
			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", 200);
			intent.putExtra("outputY", 200);
			intent.putExtra("return-data", true);
			startActivityForResult(intent, 3);
		}
		
		// 保存裁剪之后的图片数据
		private void setPicToView(Intent picdata) {
			Bundle extras = picdata.getExtras();
			if (extras != null) {
				final Bitmap photo = extras.getParcelable("data");
				image_head.setImageBitmap(photo);
				image_head2.setImageBitmap(photo);
				File file = new File("/sdcard/itourguide/UserPhoto/");
				file.mkdirs();// 创建文件夹
				String fileName = "/sdcard/itourguide/UserPhoto/" + date + ".jpg";
//				Log.i("tag", "date：" + date);
				userImgName = date + ".jpg";
				Log.i("tag", "userImgName：" + userImgName);
				Log.i("tag", "图片名称：" + fileName);
				try {
					fileout = new FileOutputStream(fileName);// 将数据写入/sdcard/myImage/文件夹下
					photo.compress(Bitmap.CompressFormat.JPEG, 100, fileout);// 把数据写入文件
					fileout.flush();
					fileout.close();
					Log.i("tag", "图片写入成功");
					Editor editor = shareds.edit(); // 获取编辑器
					editor.putString("userImage", userImgName);
					Log.i("tag", "图片写入成功" + userImgName);
					editor.commit();// 提交修改
				} catch (Exception e) {
					e.printStackTrace();
					Log.i("tag", "处理图片异常：" + e.getMessage());
				}
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						uploadFile();
					}
				}).start();
				mPopupWindow.dismiss();
			}
		}
		
		/* 上传文件至Server的方法 */
		private void uploadFile() {
			String uploadUrl = "http://103.10.85.178:81/android/UserPhotoHandler.aspx";
			
			String end = "\r\n";
			String twoHyphens = "--";
			String boundary = "******";
			try{
				URL url = new URL(uploadUrl);
				HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
				httpURLConnection.setDoInput(true);
				httpURLConnection.setDoOutput(true);
				httpURLConnection.setUseCaches(false);
				httpURLConnection.setRequestMethod("POST");
				httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
				httpURLConnection.setRequestProperty("Charset", "UTF-8");
				httpURLConnection.setRequestProperty("Content-Type","multipart/form-data;boundary=" + boundary);
				DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
				dos.writeBytes(twoHyphens + boundary + end);
				dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + srcPath.substring(srcPath.lastIndexOf("/") + 1)+ "\"" + end);
				dos.writeBytes(end);
				FileInputStream fis = new FileInputStream(srcPath);
				byte[] buffer = new byte[8192]; 
				int count = 0;
				while ((count = fis.read(buffer)) != -1){
					dos.write(buffer, 0, count);
				}
				fis.close();
				dos.writeBytes(end);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
				dos.flush();
				InputStream is = httpURLConnection.getInputStream();
				InputStreamReader isr = new InputStreamReader(is, "utf-8");
				BufferedReader br = new BufferedReader(isr);
				String result = br.readLine();
				dos.close();
				is.close();
				//上传图片
				if(phone!=null){
					new Thread(searchUserPhoto).start();
				}else{
					handler.sendEmptyMessage(4);
				}
			} catch (Exception e){
				e.printStackTrace();
				Log.i("tag", "上传图片异常:"+e.getMessage());
			}
		}
		
		Runnable searchUserPhoto = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String updateUserImgUrl = AppConfig.HOTPOT_SERVICE_URL + "UserInfo/UserPhotoEditById.svc";
				String soapaction = "http://tempuri.org/IUserPhotoEditById/userEditByUid";
				String methodName = "userEditByUid";
				String pare[] = { "mobile", "userPhoto" };
				String ps[] = { phone, userImgName };
				Log.i("tag", "userImgName:" + userImgName);
				String updateUserInfoResult = SceneryOnlineService.GetWsMsg1(
						updateUserImgUrl, soapaction, methodName, pare, ps);
				Log.i("tag", "updateUserInfoResult:" + updateUserInfoResult);
				if (updateUserInfoResult.equals("1")) {
					handler.sendEmptyMessage(2);
				} else {
					handler.sendEmptyMessage(3);
				}
			}
		};

	public void showUserHead(final String date) throws Exception {
		SDFileUtil ioFileDemo = new SDFileUtil();
		if (ioFileDemo.IsFileExists("SceneryOnline/UserPhoto/" + date)) {
			String fileName = Environment.getExternalStorageDirectory()
					+ "/SceneryOnline/UserPhoto/" + date;
			Log.i("searchTag", "fileName----------------:" + fileName);
		} else {
			// TODO Auto-generated method stub
			new Thread(new Runnable() {

				@Override
				public void run() {
					String fileurl = AppConfig.HOTPOT_SERVICE_URL
							+ "UploadFiles/UserPhoto/";
					String fileuri = Environment.getExternalStorageDirectory()
							+ "/SceneryOnline/UserPhoto/";
					DownLoadFile(fileurl, fileuri, date);
				}
			});
		}
	}

	public int DownLoadFile(String fileurl, String path, String filename) {
		// 获取文件
		InputStream inputStream = null;// 输入流，从网络中把文件读取到手机内存里。
		OutputStream outputStream = null;// 输出流，江都进来的文件输出到SDCard中。
		File file = null;
		try {

			FileUtils fileUtils = new FileUtils();
			// 判断是否存在文件
			if (fileUtils.IsFileExists(path + "/" + filename)) {
				return 1;
			}

			URL url = new URL(fileurl);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			inputStream = conn.getInputStream();
			// 在SD卡中创建文件夹
			fileUtils.CreateSDDir(path);
			// 在刚创建的文件夹中创建文件
			// fileUtils.CreateFile(filename);

			file = fileUtils.CreateFile(path + "/" + filename);
			outputStream = new FileOutputStream(file);

			// byte buffer[] = new byte[4*1024];
			int totalSize = conn.getContentLength();
			// 获取文件的长度大小
			byte buffer[] = new byte[totalSize / 1000];
			// 每次写多少

			int length = 0;
			int tol = 0;
			System.out.println("-------" + totalSize);

			while ((length = inputStream.read(buffer)) != -1) {
				Thread.sleep(100);
				tol += length;
				// tol+=inputStream.read(buffer);
				// if(tol%(totalSize/100)==0){
				int p = tol / (totalSize / 100);
				Message msg = handler.obtainMessage();
				msg.arg1 = p;
				msg.sendToTarget();
				// } 
				outputStream.write(buffer, 0, length);
			}

			outputStream.flush();
			handler.sendEmptyMessage(1);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return -1;
		} finally {
			try {
				inputStream.close();
				outputStream.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}

		return 0;
	}

	//读取保存的图片
	public Bitmap readImage(String date){
		Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/itourguide/UserPhoto/" + date + ".jpg");  
		return bitmap;
	}
}
