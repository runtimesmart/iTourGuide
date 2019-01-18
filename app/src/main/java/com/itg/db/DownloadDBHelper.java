/**
* @FileName DownLoadDatabaseHelper.java
* @Package com.itg.db
* @Description TODO
* @Author Alpha
* @Date 2015-9-16 上午9:47:59 
* @Version V1.0

*/
package com.itg.db;

import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DownloadDBHelper extends SQLiteOpenHelper {

	
	public DownloadDBHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, 1);
		mDatabaseHelper=this;
		DownloadDBHelper.context=context;
	}
	
	private static Context context;
	private AtomicInteger mOpenCounter = new AtomicInteger();

    private static DownloadDBHelper instance;
    private static SQLiteOpenHelper mDatabaseHelper;
    public SQLiteDatabase mDatabase;


    public static synchronized DownloadDBHelper getInstance() {
		 if (instance == null) 
	    	instance=new DownloadDBHelper(context, "zipdownload.db", null, 1);

        return instance;
    }

    public synchronized SQLiteDatabase openDatabase() {
        if(mOpenCounter.incrementAndGet() == 1) {
            // Opening new database
            mDatabase = mDatabaseHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    public synchronized void closeDatabase() {
        if(mOpenCounter.decrementAndGet() == 0) {
            // Closing database
            mDatabase.close();

        }
    }
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		Cursor cursor;
		cursor=db.rawQuery("SELECT * FROM sqlite_master WHERE type = ? AND name = ?", new String[]{"table","info"});
		if(cursor.moveToNext())
		{
			
		}
		else {
			{
				db.execSQL("CREATE TABLE info (path VARCHAR(1024),title VARCHAR(1024),resourceid INTEGER,downloadlength INTEGER,filelength INTEGER,version INTEGER,timestamp DATETIME DEFAULT (datetime(CURRENT_TIMESTAMP,'localtime')) ,PRIMARY KEY(resourceid,downloadlength))");
				cursor.close();
			}
		}
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}



}
