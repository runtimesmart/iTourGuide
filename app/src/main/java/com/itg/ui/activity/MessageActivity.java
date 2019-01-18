package com.itg.ui.activity;

import com.itg.iguide.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageActivity extends Activity {
	private ImageView back;
	private TextView title, text1, text2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.iguide_message_layout);
		initView();
	}
	
	public void initView(){
		back = (ImageView) findViewById(R.id.iguide_title_left_image);
		title = (TextView) findViewById(R.id.iguide_title_center_text);
		text1 = (TextView) findViewById(R.id.iguide_message_text1);
		text2 = (TextView) findViewById(R.id.iguide_message_text2);
		title.setText("消息中心");
		back.setImageResource(R.drawable.login_jitou);
		back.setOnClickListener(listener);
		setTextView();
	}
	
	@SuppressLint("ResourceAsColor")
	public void setTextView(){
		StringBuffer stringBufferText1 = new StringBuffer();
		stringBufferText1.append("\u3000\u3000为进一步扩充软件景点数量,增加景点介绍的趣味性与准确性,小编们特来借力,当然这是有偿的,有偿的,有偿的！重要的事情说三遍！\n");
		stringBufferText1.append("\u3000\u3000如果你对某个景区有着独特的解说视角,对某一景区的人文历史能娓娓道来，又或者您就是专业的带团导游，正想赚点外快，欢迎来稿，吼吼吼，我们会择优选用哦！\n");
		stringBufferText1.append("\n我们的需求:\n");
		stringBufferText1.append("① 景区文字介绍,包括景区概况介绍及其包含的各个小景点介绍,内容轻松有趣为佳;\n");
		stringBufferText1.append("② 景区语音介绍,同样包含景区概况及各小景点介绍,音频清晰,声音抑扬顿挫为佳;\n");
		stringBufferText1.append("③ 景区相关图片,包括景区整体大图及其包含各小景点对应的图片,像素不低于700*450为佳;\n");
		stringBufferText1.append("④ 景区详细导游图,可参考软件原有地图;\n");
		stringBufferText1.append("⑤ 景区详细攻略,包括票务信息、到达的交通方式、当地美食、住宿、购物及各种小贴士;\n");
		stringBufferText1.append("⑥ 软件已有的景区请勿重复发送，若资料有雷同的以最先发送者为准，之后的不再采用;\n");
		stringBufferText1.append("⑦ 所有提供的资料都需没有版权纠纷，如有版权纠纷由资料提供者负责。\n");
		text1.setText(stringBufferText1);
		
		StringBuffer stringBufferText2 = new StringBuffer();
		stringBufferText2.append("① 文字介绍50元/千字，语音介绍普通录音1元/分钟,优质录音2元/分钟，特别优秀的另算（语速过慢的不采用），景点图片5元/十张，照片形式呈现的地图5元/张，内部测绘地图10元/张，攻略50元/千字;\n");
		stringBufferText2.append("② 内容或音频全部统一发至邮箱313386421@qq.com，并留下您的联系方式和收款账号，有疑问可电话咨询010-88551739;\n");
		stringBufferText2.append("③ 每一位为我们提供资料的亲，都可以添加我们的微信公众号“i导游”，关注后报您提供资料的邮箱，可得到抢红包机会一个，金额随您的运气变化哦;\n");
		stringBufferText2.append("④ 提供的稿件一经采用，我们会以邮件或电话的形式通知，并核对您的收款账号，十天内为您转款;\n");
		stringBufferText2.append("⑤ 当然您也可以登陆软件，搜索查看您所提供的景区是否在列，内容是否与您提供的一致，如有疑问可电话咨询010-88551739，或与微信对话;\n");
		stringBufferText2.append("⑥ 最后的最后，赶快行动吧，红包都要被领走啦！");
		text2.setText(stringBufferText2);
	}
	
	View.OnClickListener listener = new View.OnClickListener() {
		
		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			switch(view.getId()){
			case R.id.iguide_title_left_image:
				finish();
				break;
			}
		}
	};
}
