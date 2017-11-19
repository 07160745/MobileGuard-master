package cn.edu.gdmec.android.mobileguard.m5virusscan;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import cn.edu.gdmec.android.mobileguard.R;

public class VirusScanActivity extends AppCompatActivity implements View.OnClickListener{
private TextView mLastTimeTV;
    private SharedPreferences mSP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virus_scan);
        mSP=getSharedPreferences("config",MODE_PRIVATE);
        copyDB("antiVirus.db");
        initView();
    }

    protected void onResume() {
        String string = mSP.getString("lastVirusScan", "您还没杀毒");
        mLastTimeTV.setText(string);
        super.onResume();
    }
    private void copyDB(final String dbname) {
            new Thread(){
                public void run() {
                    try {
                        File file = new File(getFilesDir(), dbname);
                        if (file.exists() && file.length() > 0) {
                            Log.i("VirusScanActivity", "数据库已经存在");
                            return;
                        }
                        InputStream is = getAssets().open(dbname);
                    FileOutputStream fos = openFileOutput(dbname, MODE_PRIVATE);
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                    is.close();
                    fos.close();
                }
                catch(Exception e) {
                    e.printStackTrace();
                    }
            };
        }.start();
    }
    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.light_blue));
        ImageView mLeftImgv=(ImageView)findViewById(R.id.imgv_leftbtn);
        ((TextView)findViewById(R.id.tv_title)).setText("病毒查杀");
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        mLastTimeTV=(TextView)findViewById(R.id.tv_lastscantime);
        findViewById(R.id.rl_allscanvirus).setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
     switch (view.getId()){
         case R.id.imgv_leftbtn:
             finish();
             break;
         case R.id.rl_allscanvirus:
             startActivity(new Intent(this,VirusScanActivity.class));
             break;
     }
    }
}
