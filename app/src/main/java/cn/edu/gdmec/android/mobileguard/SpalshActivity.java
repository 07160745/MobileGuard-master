package cn.edu.gdmec.android.mobileguard;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import cn.edu.gdmec.android.mobileguard.m1home.utils.MyUtils;
import cn.edu.gdmec.android.mobileguard.m1home.utils.VersionUpdateUtils;
public class SpalshActivity extends AppCompatActivity {
    private TextView mVersionTV;
    private TextView mTVVersion;
    private String mVersion;
    private static final int MY_PERMISSIONS_REQUEST_PACKAGE_USAGE__STATS = 1101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);
        getSupportActionBar().hide();
        mVersion = MyUtils.getVersion(getApplicationContext());
        mVersionTV = (TextView) findViewById(R.id.tv_splash_version);
        mVersionTV.setText("版本号:" + mVersion);
        if (!hasPermission()) {
            startActivityForResult(new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS), MY_PERMISSIONS_REQUEST_PACKAGE_USAGE__STATS);
        }
        VersionUpdateUtils.DownloadCallback downloadCallback = new VersionUpdateUtils.DownloadCallback();
    };
    final VersionUpdateUtils versionUpdateUtils = new VersionUpdateUtils(mVersion, SpalshActivity.this);
    private boolean hasPermission() {
        AppOpsManager appOps=(AppOpsManager)getSystemService(Context.APP_OPS_SERVICE);
        int mode=0;
        if (Build.VERSION.SDK_INT> Build.VERSION_CODES.KITKAT){
            mode=appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(),getPackageName());
        }
        return mode== AppOpsManager.MODE_ALLOWED;
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        if (requestCode == MY_PERMISSIONS_REQUEST_PACKAGE_USAGE__STATS) {
            if (!hasPermission()){
                startActivityForResult(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),MY_PERMISSIONS_REQUEST_PACKAGE_USAGE__STATS);
            }
        }
    }
}


