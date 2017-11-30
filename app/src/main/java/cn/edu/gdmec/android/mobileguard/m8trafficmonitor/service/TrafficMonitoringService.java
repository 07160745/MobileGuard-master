package cn.edu.gdmec.android.mobileguard.m8trafficmonitor.service;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.TrafficStats;
import android.os.IBinder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.edu.gdmec.android.mobileguard.m8trafficmonitor.db.dao.TrafficDao;
public class TrafficMonitoringService extends Service {
    private long mOldRxBytes;
    private long mOldTxBytes;
    private TrafficDao dao;
    private SharedPreferences mSp;
    private long usedFlow;
    boolean flag = true;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    public void onCreate() {
        super.onCreate();
        mOldRxBytes = TrafficStats.getMobileRxBytes();
        mOldTxBytes = TrafficStats.getMobileTxBytes();
        dao = new TrafficDao(this);
        mSp = getSharedPreferences("config", MODE_PRIVATE);
        mThread.start();
    }
    private Thread mThread = new Thread() {
        public void run() {
            while (flag) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                updateTodayGPRS();
            }
        }
            public void updateTodayGPRS(){
                usedFlow=mSp.getLong("usedFlow",0);
                Date date=new Date();
                Calendar calendar=Calendar.getInstance();
                calendar.setTime(date);
                if (calendar.DAY_OF_MONTH==1&calendar.NOVEMBER==0&calendar.MINUTE<1&calendar.SECOND<30){
                       usedFlow=0;
                }
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-mm-dd");
                String dataString=sdf.format(date);
                long mobileGPRS=dao.getMobileGPRS(dataString);
                long mobileRxBytes=TrafficStats.getMobileRxBytes();
                long mobileTxBytes=TrafficStats.getMobileTxBytes();
                long newGprs=(mobileRxBytes+mobileTxBytes)-mOldRxBytes-mOldTxBytes;
                mOldRxBytes=mobileRxBytes;
                mOldTxBytes=mobileTxBytes;
                if (newGprs<0){
                    newGprs=mobileRxBytes+mOldTxBytes;
                }
                if(mobileGPRS==-1){
                    dao.UpdateTodayGPRS(newGprs);
                }
                else{
                    if (mobileGPRS<0){
                         mobileGPRS=0;
                    }
                    dao.UpdateTodayGPRS(mobileGPRS+newGprs);
                }
                usedFlow=usedFlow+newGprs;
                SharedPreferences.Editor edit=mSp.edit();
                edit.putLong("usedFlow",usedFlow);
                edit.commit();
            }
    };
                    public void onDestroy(){
                     if (mThread!=null&!mThread.interrupted()){
                         flag=false;
                         mThread.interrupt();
                         mThread=null;
                     }
                     super.onDestroy();
                    }
        ;}
