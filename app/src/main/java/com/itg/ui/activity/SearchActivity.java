/**
 * @FileName SearchActivity.java
 * @Package com.itg.ui.activity
 * @Description TODO
 * @Author Alpha
 * @Date 2015-8-19 下午1:45:17 
 * @Version V1.0

 */
package com.itg.ui.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.itg.adapter.DistrictSearchAdapter;
import com.itg.adapter.SearchAutoAdapter;
import com.itg.bean.HotPotDistrict;
import com.itg.bean.SearchHistoryBean;
import com.itg.httpRequest.asynctask.HotPotDistrictSearchTask;
import com.itg.iguide.R;
import com.itg.util.AppConfig;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class SearchActivity extends BaseActivity implements OnClickListener,
OnCompletionListener,OnEditorActionListener {

	private ImageView voiceButton;
	// 语音听写对象
	private SpeechRecognizer mIat;
	// 语音听写UI
	private RecognizerDialog mIatDialog;
	// 用HashMap存储听写结果
	private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

	private EditText mSearchText;
	private Toast mToast;
	private SharedPreferences mSharedPreferences;
	// 引擎类型
	private String mEngineType = SpeechConstant.TYPE_CLOUD;
	
	public static final String SEARCH_HISTORY = "search_history";

    private TextView cancelText;
    
    private String keyword;
    private ListView searchResultListView;
    private GridView hotSearchGridView;
    private LinearLayout linear;
    private List<HotPotDistrict> districtList;
    private DistrictSearchAdapter adapter;
    private SearchAutoAdapter mSearchAutoAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.iguide_search_layout);
		initComponent();
	}

	private void initComponent() {
		mSearchAutoAdapter = new SearchAutoAdapter(this, -1);
		mSearchText = (EditText) findViewById(R.id.iguide_search);
		voiceButton = (ImageView) findViewById(R.id.iguide_audio);
		cancelText = (TextView) findViewById(R.id.iguide_search_cancel_txt);
		searchResultListView = (ListView) findViewById(R.id.iguide_search_result);
		hotSearchGridView = (GridView) findViewById(R.id.iguide_search_gridview);
		linear = (LinearLayout) findViewById(R.id.iguide_search_linear_layout);
		districtList = mSearchAutoAdapter.initSearchHistory();  //读取搜索历史
		if(districtList.size() <= 0){   //判断搜索历史的长度
			linear.setVisibility(View.GONE);
		} else {
			UpdateRecord();  //显示保存的搜索数据
			linear.setVisibility(View.VISIBLE);
		}
		//设置取消GridView的点击效果
		hotSearchGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		districtList = new ArrayList<HotPotDistrict>();   //搜索结果List的初始化
		adapter = new DistrictSearchAdapter(districtList, SearchActivity.this);  //搜索适配的adapter的初始化
		cancelText.setOnClickListener(this);    //语音搜索的监听
		voiceButton.setOnClickListener(this);   //删除对话框数据监听
		mSearchText.setOnEditorActionListener(this);   //键盘搜索按钮的监听
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.iguide_search_cancel_txt:
			//ActivityConfig.exitAnimation(SearchActivity.this);   //动画,选出城市
			this.exitAnimation();
			break;
		case R.id.iguide_audio:
			initIflyTek();  //初始化语音组件
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(event.getKeyCode()==KeyEvent.KEYCODE_BACK)
//			ActivityConfig.exitAnimation(SearchActivity.this);
			this.exitAnimation();
			return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		
		String inputName = mSearchText.getText().toString().replace("。", "");
		Log.i("tag", inputName);//TextUtils.isEmpty(inputName)
		if(!inputName.equals("") && inputName != null && inputName.length() > 0){
			new HotPotDistrictSearchTask(adapter,districtList,SearchActivity.this).execute(inputName,SearchActivity.class.toString());
			searchResultListView.setAdapter(adapter);
			searchResultListView.setOnItemClickListener(onItemClick);
			linear.setVisibility(View.GONE);
			saveSearchHistory(inputName);  //存入搜索的数据
			searchResultListView.setVisibility(View.VISIBLE);
			mSearchAutoAdapter.initSearchHistory();
		} else {
			Toast.makeText(getApplicationContext(), "请填入搜索的数据", Toast.LENGTH_SHORT).show();
		}
		return true;
	}
	
	//搜索出数据的item点击事件
		OnItemClickListener onItemClick = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SearchActivity.this, DistrictInfoActivity.class);
				intent.putExtra("districtId", districtList.get(arg2).getId());
				Log.i("searchTag", "点击了");
				startActivity(intent);
			}
		};
	
	/*
	 * 保存搜索记录
	 */
	private void saveSearchHistory(String text) {
		if (text.length() < 1) {
			return;
		}
		SharedPreferences sp = getSharedPreferences(SEARCH_HISTORY, 0);
		String longhistory = sp.getString(SEARCH_HISTORY, "");
		String[] tmpHistory = longhistory.split(",");
		ArrayList<String> history = new ArrayList<String>(Arrays.asList(tmpHistory));
		if (history.size() > 0) { 
			int i;
			for (i = 0; i < history.size(); i++) {
				if (text.equals(history.get(i))) {
					history.remove(i);
					break;
				}
			}
			history.add(0, text);
		}
		if (history.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < history.size(); i++) {
				sb.append(history.get(i) + ",");
			}
			sp.edit().putString(SEARCH_HISTORY, sb.toString()).commit();
		} else {
			sp.edit().putString(SEARCH_HISTORY, text + ",").commit();
		}
	}
	
	//显示保存的搜索数据
		public void UpdateRecord(){
			mSearchAutoAdapter = new SearchAutoAdapter(this, -1);
			hotSearchGridView.setAdapter(mSearchAutoAdapter);
			hotSearchGridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
					SearchHistoryBean data = (SearchHistoryBean) mSearchAutoAdapter.getItem(position);
					mSearchText.setText(data.getName());
					keyword = data.getName();
					saveSearchHistory(keyword); //保存搜索记录
					new HotPotDistrictSearchTask(adapter,districtList,SearchActivity.this).execute(data.getName(),SearchActivity.class.toString());
					searchResultListView.setAdapter(adapter);
					searchResultListView.setOnItemClickListener(onItemClick);
					linear.setVisibility(View.GONE);
					saveSearchHistory(data.getName());  //存入搜索的数据
				}
			});
			mSearchAutoAdapter.initSearchHistory(); //读取搜索记录
			mSearchAutoAdapter.notifyDataSetChanged();
		}
		
	
	/**
	 * 初始化讯飞语音组件
	 * **/
	private void initIflyTek()
	{
		recordOn();
		SpeechUtility.createUtility(SearchActivity.this, SpeechConstant.APPID
				+AppConfig.IFLY_TECK);
		mIat = SpeechRecognizer.createRecognizer(SearchActivity.this,
				mInitListener);

		// 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
		// 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
		mIatDialog = new RecognizerDialog(SearchActivity.this, mInitListener);

		mSharedPreferences = getSharedPreferences("com.iflytek.setting",
				Activity.MODE_PRIVATE);
		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

		// 设置参数
		setParam();

		// 显示听写对话框
		mIatDialog.setListener(mRecognizerDialogListener);
		mIatDialog.show();
	}

	/**
	 * 语音提示
	 * **/
	private void recordOn() {
		MediaPlayer mPlayer = MediaPlayer.create(SearchActivity.this, R.raw.on);
       mPlayer.setOnCompletionListener(this);
		final AudioManager audioManager = (AudioManager) this
				.getSystemService(this.AUDIO_SERVICE);

		audioManager.setStreamVolume(AudioManager.STREAM_ALARM,
				audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM),
				AudioManager.FLAG_PLAY_SOUND);
		
		try {
			mPlayer.prepare();
			mPlayer.setLooping(false);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mPlayer.start();
	
	}
	@Override
	public void onCompletion(MediaPlayer mp) {
		mp.release();
	}

	private void showTip(final String str) {
		mToast.setText(str);
		mToast.show();
	}

	/**
	 * 初始化监听器。
	 */
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {

			if (code != ErrorCode.SUCCESS) {
				showTip("初始化失败，错误码：" + code);
			}
		}
	};

	/**
	 * 听写UI监听器
	 */
	private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
		public void onResult(RecognizerResult results, boolean isLast) {
			printResult(results);
		
		}

		/**
		 * 识别回调错误.
		 */
		public void onError(SpeechError error) {
			showTip(error.getPlainDescription(true));
		}

	};

	private void printResult(RecognizerResult results) {
		String text = com.itg.util.JsonParser.parseIatResult(results
				.getResultString());

		String sn = null;
		// 读取json结果中的sn字段
		try {
			JSONObject resultJson = new JSONObject(results.getResultString());
			sn = resultJson.optString("sn");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		mIatResults.put(sn, text);

		StringBuffer resultBuffer = new StringBuffer();
		for (String key : mIatResults.keySet()) {
			resultBuffer.append(mIatResults.get(key));
		}

		mSearchText.setText(resultBuffer.toString());
		mSearchText.setSelection(mSearchText.length());
		mSearchText.setFocusable(true);
		mSearchText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		InputMethodManager imm = (InputMethodManager) mSearchText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);  
		imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED); 
			
	}

	/**
	 * 参数设置
	 * 
	 * @param param
	 * @return
	 */
	public void setParam() {
		// 清空参数
		mIat.setParameter(SpeechConstant.PARAMS, null);

		// 设置听写引擎
		mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
		// 设置返回结果格式
		mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

		String lag = mSharedPreferences.getString("iat_language_preference",
				"mandarin");
		if (lag.equals("en_us")) {
			// 设置语言
			mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
		} else {
			// 设置语言
			mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
			// 设置语言区域
			mIat.setParameter(SpeechConstant.ACCENT, lag);
		}

		// 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
		mIat.setParameter(SpeechConstant.VAD_BOS,
				mSharedPreferences.getString("iat_vadbos_preference", "4000"));

		// 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
		mIat.setParameter(SpeechConstant.VAD_EOS,
				mSharedPreferences.getString("iat_vadeos_preference", "1000"));

		// 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
		mIat.setParameter(SpeechConstant.ASR_PTT,
				mSharedPreferences.getString("iat_punc_preference", "1"));

		// 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
		// 注：AUDIO_FORMAT参数语记需要更新版本才能生效
		mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
		mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH,
				Environment.getExternalStorageDirectory() + "/msc/iat.wav");

		// 设置听写结果是否结果动态修正，为“1”则在听写过程中动态递增地返回结果，否则只在听写结束之后返回最终结果
		// 注：该参数暂时只对在线听写有效
		mIat.setParameter(SpeechConstant.ASR_DWA,
				mSharedPreferences.getString("iat_dwa_preference", "0"));
	}


}
