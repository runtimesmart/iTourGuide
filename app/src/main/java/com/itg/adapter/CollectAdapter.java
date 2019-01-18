package com.itg.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itg.bean.DownloadInfo;
import com.itg.db.DownloadDBUtil;
import com.itg.iguide.R;
import com.itg.ui.activity.CollectActivity;
import com.itg.ui.activity.DistrictInfoActivity;
import com.itg.util.FileUtils;

public class CollectAdapter extends BaseAdapter {
	private List<DownloadInfo> infoList;
	private CollectActivity mContext;
	private DownloadDBUtil util;
	
	public CollectAdapter(List<DownloadInfo> infoList, CollectActivity mContext) {
		super();
		this.infoList = infoList;
		this.mContext = mContext;
		util = new DownloadDBUtil(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return infoList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return infoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	public List<Bitmap> resultBitmap(int id){
		List<Bitmap> bitmap = new ArrayList<Bitmap>();
		List<Bitmap> bitmaps = new ArrayList<Bitmap>();
		String imagePath = "/sdcard/iguide/offline/" + id + "/image/thumb/";
		File imageFile = new File(imagePath);
		//获取文件的名称数组
		File[] files = imageFile.listFiles();
		//获取第一个得到的文件的名称
		String name = files[0].getName();
		Bitmap bitmapImage = null;
		FileInputStream bitmapInput = null;
		//判断文件夹是否存在
		if(imageFile.exists() && imageFile.isDirectory()){
			try {
				if(bitmapInput == null){
					bitmapInput = new FileInputStream(imagePath + name);
				}
				bitmapImage = BitmapFactory.decodeStream(bitmapInput);
				bitmap.add(bitmapImage);
				//释放资源
				bitmapImage = null;
				bitmapInput.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		bitmaps.addAll(bitmap);
		return bitmaps;
	}
	
	
	public static void RecursionDeleteFile(File file){
        if(file.isFile()){
            file.delete();
            return;
        }
        if(file.isDirectory()){
            File[] childFile = file.listFiles();
            if(childFile == null || childFile.length == 0){
                file.delete();
                return;
            }
            for(File f : childFile){
                RecursionDeleteFile(f);
            }
            file.delete();
        }
    }
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final TextHolder holder;
		if(convertView == null){
			//初始化
			convertView = LayoutInflater.from(mContext).inflate(R.layout.iguide_collect_item_layout, null);
			holder = new TextHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.iguide_collect_image);
			holder.date = (TextView) convertView.findViewById(R.id.iguide_collect_date);
			holder.title = (TextView) convertView.findViewById(R.id.iguide_collect_name);
			holder.textButton = (Button) convertView.findViewById(R.id.iguide_collect_textbutton);
			holder.delete = (TextView) convertView.findViewById(R.id.delete);
			convertView.setTag(holder);
		} else {
			holder = (TextHolder) convertView.getTag();
		}
		//请求图片
		List<Bitmap> bitmapList = resultBitmap(infoList.get(position).getResourceid());
		//赋值
		holder.date.setText(infoList.get(position).getTimestamp());
		holder.title.setText(infoList.get(position).getTitle());
		holder.image.setImageBitmap(bitmapList.get(0));
		//对listView的Itemized设置点击事件
		holder.textButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, DistrictInfoActivity.class);
				intent.putExtra("districtId", infoList.get(position).getResourceid());
				mContext.startActivity(intent);
			}
		});
		
		//设置删除的点击事件
		holder.delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				util.delete(infoList.get(position).getResourceid());
				String path = "/sdcard/iguide/offline/" + infoList.get(position).getResourceid();
				FileUtils fileutil = new FileUtils();
				//判断文件是否存在
				boolean boo = fileutil.IsFile(path);
				if(boo){
					File file = new File(path);
					RecursionDeleteFile(file);
				} else {
					Log.i("tag", "文件找不到");
				}
				infoList.remove(position);
				notifyDataSetChanged();
				mContext.collectListView.turnToNormal();
				Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
			}
		});
		return convertView;
	}
	
	class TextHolder{
		ImageView image;
		TextView date, title,delete;
		Button textButton;
	}
}
