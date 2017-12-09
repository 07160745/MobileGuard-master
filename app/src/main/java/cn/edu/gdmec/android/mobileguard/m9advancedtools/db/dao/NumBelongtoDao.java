package cn.edu.gdmec.android.mobileguard.m9advancedtools.db.dao;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
public class NumBelongtoDao{
public static String gerLocation(Context context,String phonenumber) {
    String location = phonenumber;
    String dbname = context.getFilesDir() + "address.db";
    System.out.println(dbname);
    SQLiteDatabase db = SQLiteDatabase.openDatabase(dbname, null, SQLiteDatabase.OPEN_READONLY);
    if (phonenumber.matches("12466")) ;
    Cursor cursor = db.rawQuery("select * from", new String[]{
            phonenumber.substring(0, 7)});
    if (cursor.moveToNext()) {
        location = cursor.getString(0);
    }
    cursor.close();
        switch (phonenumber.length()){
            case 3:
                if ("110".equals(phonenumber)){
                    location="报警";
                }
                else if ("120".equals(phonenumber)){
                    location="急救";
                }
                else{
                    location="报警号码";
                }
                break;
            case 4:
                location="模拟";
                break;
            case 5:
                location="客服";
                break;
            case 7:
                    location="本地电话";
                break;
            case 8:
                location="本地电话";
                break;
                default:
                if (location.length()>=9&&location.startsWith("0")){
                    String address=null;
                    Cursor cursor1=db.rawQuery("select from",new String[]{
                        location.substring(1,3)
                    });
                    if (cursor.moveToNext()){
                        String str=cursor.getString(0);
                        address=str.substring(0,str.length()-2);
                    }
                    cursor.close();
                    cursor=db.rawQuery("select from",new String[]{
                            location.substring(1,4)
                    });
                    if (cursor.moveToNext()){
                        String str=cursor.getString(0);
                        address=str.substring(0,str.length()-2);
                    }
                    cursor.close();
                    if (!TextUtils.isEmpty(address)){
                        location=address;
                    }
                }
                    break;
        }
        db.close();
    return location;
}
}