/**
 * @FileName DownloadDBUtil.java
 * @Package com.itg.db
 * @Description TODO
 * @Author Alpha
 * @Date 2015-9-16 上午10:11:06 
 * @Version V1.0

 */
package com.itg.db;

import java.util.ArrayList;
import java.util.List;

import com.itg.bean.DownloadInfo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DownloadDBUtil {

	private DownloadDBHelper helper;
	// private static SQLiteDatabase db;
	public static final String locker = "lock";

	public DownloadDBUtil(Context context) {
		helper = new DownloadDBHelper(context, "zipdownload.db", null, 1);

	}

	public  void insert(String path, String title, int resourceid,
			int downloadlength, int filelength, int version) {
		//SQLiteDatabase db = helper.getWritableDatabase();
		SQLiteDatabase db = DownloadDBHelper.getInstance().openDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT path,resourceid from info where resourceid=?",
				new String[] { Integer.toString(resourceid) });
		if (cursor.getCount() == 0) {

			db.execSQL(
					"INSERT INTO info(path,title,resourceid,downloadlength,filelength,version,timestamp) VALUES(?,?,?,?,?,?,(datetime(CURRENT_TIMESTAMP,'localtime')))",
					new Object[] { path, title, resourceid, downloadlength,
							filelength, version });

		}
		cursor.close();
	//	db.close();
		DownloadDBHelper.getInstance().closeDatabase();

	}

	public void update(String path, int resourceid, int downloadlength,
			int filelength, int version) {
		//SQLiteDatabase db = helper.getWritableDatabase();
		SQLiteDatabase db = DownloadDBHelper.getInstance().openDatabase();
		db.execSQL(
				"UPDATE info SET downloadlength=? ,filelength=? ,version=?,timestamp=(datetime(CURRENT_TIMESTAMP,'localtime')) WHERE path=? AND resourceid=?",
				new Object[] { downloadlength, filelength, version, path,
						resourceid });
		//db.close();
		DownloadDBHelper.getInstance().closeDatabase();

	}

	public DownloadInfo query(int resourceid) {
		//SQLiteDatabase db = helper.getWritableDatabase();
		SQLiteDatabase db = DownloadDBHelper.getInstance().openDatabase();
		Cursor cursor = null;
		DownloadInfo info = null;
		try {
			cursor = db
					.rawQuery(
							"SELECT path,resourceid,downloadlength,filelength,version,timestamp from info where resourceid=?",
							new String[] { Integer.toString(resourceid) });
			while (cursor.moveToNext()) {
				info = new DownloadInfo(cursor.getString(0), cursor.getInt(1),
						cursor.getInt(2), cursor.getInt(3), cursor.getInt(4));
			}
		} catch (Exception e) {
			// TODO: handle exception
			cursor.close();
		//	db.close();
			DownloadDBHelper.getInstance().closeDatabase();
		}
		cursor.close();
		//db.close();
		DownloadDBHelper.getInstance().closeDatabase();
		return info;

	}

	public List<DownloadInfo> query() {
		//SQLiteDatabase db = helper.getWritableDatabase();
		SQLiteDatabase db = DownloadDBHelper.getInstance().openDatabase();
		List<DownloadInfo> downloadInfoList = new ArrayList<DownloadInfo>();
		List<DownloadInfo> downloadInfoLists = new ArrayList<DownloadInfo>();
		Cursor cursor = db.rawQuery("SELECT * FROM info", null);
		DownloadInfo downloadInfo = null;
		while (cursor.moveToNext()) {
			int id = cursor.getInt(cursor.getColumnIndex("resourceid"));
			String date = cursor.getString(cursor.getColumnIndex("timestamp"));
			String title = cursor.getString(cursor.getColumnIndex("title"));
			downloadInfo = new DownloadInfo(id, date, title);
			downloadInfoList.add(downloadInfo);
		}
		//db.close();
		DownloadDBHelper.getInstance().closeDatabase();
		cursor.close();
		downloadInfoLists.addAll(downloadInfoList);
		return downloadInfoLists;
	}
	
	public void delete(int id){
		if(id != 0){
			SQLiteDatabase db = helper.getWritableDatabase();
			db.delete("info", "resourceid = ?", new String[]{String.valueOf(id)});
		}
	}

	public void delete() {
		// if(db==null)
		// {
		//
		// }
	}

}
