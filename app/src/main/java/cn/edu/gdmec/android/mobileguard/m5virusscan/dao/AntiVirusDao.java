package cn.edu.gdmec.android.mobileguard.m5virusscan.dao;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.edu.gdmec.android.mobileguard.R;
public class AntiVirusDao {
    private static Context context;
    private static String dbname;
    public AntiVirusDao(Context context){
     this.context=context;
        dbname="/data/data"+context.getPackageName()+"/files/antivirus.db";
    }
    public String checkVirus(String md5){
        String desc=null;
        SQLiteDatabase db=SQLiteDatabase.openDatabase(dbname,null,SQLiteDatabase.OPEN_READONLY);
        Cursor cursor=db.rawQuery("select desc from datable where md5=?",new String[] {
                md5
        });
        if (cursor.moveToNext()){
            desc=cursor.getString(0);
        }
        cursor.close();
        db.close();
        return desc;

    }
}
