package com.itg.ui.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.R.string;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itg.bean.RequestGetSMSBean;
import com.itg.httpRequest.WebServiceUtil;
import com.itg.iguide.R;
import com.itg.util.AppConfig;
import com.itg.util.DES_Encrypt;
import com.itg.util.JsonUtil;

public class LoginActivity extends BaseActivity {
	private LinearLayout share_weixin, share_qq, share_sina;
	private EditText edit_phone, edit_sms;
	private Button button_sms, login_button, login_button2;
	private List<RequestGetSMSBean> GetSMSlist;
	private TextView title;
	private ImageView back;
	public String msgCode;
	private SharedPreferences sharePhone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.iguide_login_layout);
		initView();
	}

	public void initView() {
		sharePhone = getSharedPreferences("UserMsg", MODE_PRIVATE);
		title = (TextView) findViewById(R.id.iguide_title_center_text);
		back = (ImageView) findViewById(R.id.iguide_title_left_image);
		share_weixin = (LinearLayout) findViewById(R.id.iguide_login_share_weixin);
		share_qq = (LinearLayout) findViewById(R.id.iguide_login_share_qq);
		share_sina = (LinearLayout) findViewById(R.id.iguide_login_share_sina);
		edit_phone = (EditText) findViewById(R.id.iguide_login_phone_text);
		edit_sms = (EditText) findViewById(R.id.iguide_login_sms_text);
		button_sms = (Button) findViewById(R.id.iguide_login_sms_button);
		login_button = (Button) findViewById(R.id.iguide_login_button);
		login_button2 = (Button) findViewById(R.id.iguide_login_button2);
		GetSMSlist = new ArrayList<RequestGetSMSBean>();
		title.setText("登  录");
		//判断本地是否存有手机号
		String phone = sharePhone.getString("phone", "");
		if(phone.length() > 0 && phone != null){
			edit_phone.setText(phone);
		}
		back.setImageResource(R.drawable.login_jitou);
		back.setOnClickListener(listener);
		button_sms.setOnClickListener(listener);
		share_weixin.setOnClickListener(listener);
		login_button.setOnClickListener(listener);
		login_button2.setOnClickListener(listener);
	}
	
	View.OnClickListener listener = new View.OnClickListener() {

		@SuppressLint("ShowToast") @Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {
			case R.id.iguide_title_left_image:
				Intent start = new Intent(LoginActivity.this, HomeActivity.class);
				startActivity(start);
				break;
			case R.id.iguide_login_sms_button:
				if (checkPhone(edit_phone.getText().toString())) {
					//验证码倒计时
					TimeCount time = new TimeCount(60000, 1000);
					time.start();// 开始计时
					// 获取验证码
					String szImei = getIMEI();
					String param[] = {edit_phone.getText().toString(), szImei};
					new RequestGetSMSTask().execute(param);//执行网络请求
				}
				break;
				//登录
			case R.id.iguide_login_button:
				Toast.makeText(getApplicationContext(), msgCode, 0).show();
				String code = edit_sms.getText().toString();
				//判断电话号码格式是否正确
				if(checkPhone(edit_phone.getText().toString())){
					//判断验证码格式是否正确
					if(code.length() != 0 && !code.equals("")){
						//判断验证码内容是否相同
						if(code.equals(msgCode)){
							Editor editor = sharePhone.edit();
							editor.putString("phone", edit_phone.getText().toString());
							editor.commit();
							//跳转
							finish();
						} else {
							Toast.makeText(getApplicationContext(), "请输入正确的验证码", 0).show();
						}
					} else {
						Toast.makeText(getApplicationContext(), "请输入验证码", 0).show();
					}
				}
				break;
			case R.id.iguide_login_button2:

				break;
			case R.id.iguide_login_share_weixin:

				break;
			case R.id.iguide_login_share_qq:

				break;
			case R.id.iguide_login_share_sina:

				break;
			}
		}
	};
	
	// 获取手机的唯一标识
		public String getIMEI() {
			TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(LoginActivity.TELEPHONY_SERVICE);
			String szImei = TelephonyMgr.getDeviceId();
			return szImei;
		}

	public boolean checkPhone(String phone) {
		Pattern pattern = Pattern.compile("^[1][3-8]+\\d{9}");
		Matcher matcher = pattern.matcher(phone);
		if (!phone.equals("")) {
			if (matcher.matches() == true) {
				return true;
			} else {
				Toast.makeText(LoginActivity.this, "请输入正确的手机号", 0).show();
			}
		} else {
			Toast.makeText(LoginActivity.this, "请填写手机号", 0).show();
			return false;
		}
		return false;
	}
	
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			button_sms.setText("重新验证");
			if (button_sms.getText().toString().equals("重新验证")) {
				button_sms.setBackgroundResource(R.drawable.login_yazheng);
			}
			button_sms.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			button_sms.setClickable(false);
			button_sms.setText(millisUntilFinished / 1000 + "秒");
			button_sms.setBackgroundColor(Color.GRAY);
		}
	}
	
	class RequestGetSMSTask extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... param) {
			// TODO Auto-generated method stub
			String SMSurl = AppConfig.HOTPOT_SERVICE_URL + "NewAccount/UserMobileVerify.svc?wsdl";
			String SMSSoapAction = "http://tempuri.org/IUserMobileVerify/verifyMobileByCode";
			String SMSmethod = "verifyMobileByCode";
			String SMSstring[] = {"mobileNo", "deviceId"};
			String params[] = {param[0],param[1]};
			//获取数据
			String result = WebServiceUtil.GetWsMsg(SMSurl, SMSSoapAction, SMSmethod, SMSstring, params);
			return result;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(!result.equals("")){
				msgCode = result;
			}
		}
	}
}
