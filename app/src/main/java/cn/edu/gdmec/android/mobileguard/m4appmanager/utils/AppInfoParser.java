package cn.edu.gdmec.android.mobileguard.m4appmanager.utils;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.edu.gdmec.android.mobileguard.m4appmanager.entity.AppInfo;
public class AppInfoParser{
    public static List<AppInfo> getAppInfos(Context context) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packInfos = pm.getInstalledPackages(0);
        List<AppInfo> appinfos = new ArrayList<AppInfo>();
for (PackageInfo packInfo:packInfos){
    AppInfo appinfo=new AppInfo();
    String packname=packInfo.packageName;
     appinfo.packageName=packname;
    Drawable icon=packInfo.applicationInfo.loadIcon(pm);
    appinfo.icon=icon;
    String appname= packInfo.applicationInfo.loadLabel(pm).toString();
    appinfo.appName=appname;
    String apkpath=packInfo.applicationInfo.sourceDir;
    appinfo.apkPath=apkpath;
    File file=new File(apkpath);
    appinfo.appSize= file.length();
    int flags=packInfo.applicationInfo.flags;
    if ((ApplicationInfo.FLAG_EXTERNAL_STORAGE&flags)!=0){
        appinfo.isInRoom=false;
    }
    else {
        appinfo.isInRoom = true;
    }
    if ((ApplicationInfo.FLAG_SYSTEM&flags)!=0){
        appinfo.isUserApp=true;
    }
    else{
        appinfo.isUserApp=false;
    }
    appinfos.add(appinfo);
    appinfo=null;
}
return appinfos;
    }
}