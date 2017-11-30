package cn.edu.gdmec.android.mobileguard.m8trafficmonitor;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import cn.edu.gdmec.android.mobileguard.R;
import cn.edu.gdmec.android.mobileguard.m8trafficmonitor.db.dao.TrafficDao;
import cn.edu.gdmec.android.mobileguard.m8trafficmonitor.service.TrafficMonitoringService;
import cn.edu.gdmec.android.mobileguard.m8trafficmonitor.utils.SystemInfoUtils;
public class TrafficMonitoringActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences mSP;
    private Button mCorrectFlowBtn;
    private TextView mTotalTV;
    private TextView mUsedTV;
    private TextView mToDayTV;
    private TrafficDao dao;
    private ImageView mRemindIMGV;
    private TextView mRemindTV;
    private CorrectFlowReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_monitoring);
        mSP = getSharedPreferences("config", MODE_PRIVATE);
        boolean flag = mSP.getBoolean("is", false);
        if (!flag) {
            startActivity(new Intent(this, OperatorSetActivity.class));
            finish();
        }
        if (!SystemInfoUtils.isServiceRunning(this, "cn.android")) {
            startService(new Intent(this, TrafficMonitoringService.class));
        }
        initView();
        registReceiver();
        initData();
    }
    private void registReceiver() {
     receiver=new CorrectFlowReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("android");
        registerReceiver(receiver,filter);
    }
    private void initData() {
      long totalflow=mSP.getLong("totalflow",0);
      long usedflow=mSP.getLong("used",0);
        if (totalflow>0&usedflow>=0){
            float scale=usedflow/totalflow;
            if (scale>0.9){
                mRemindTV.setText("用完了");
                mRemindIMGV.setEnabled(false);
            }
            else{
                mRemindIMGV.setEnabled(true);
                mTotalTV.setText("充足");
            }
        }
        mTotalTV.setText("liuliang"+ Formatter.formatFileSize(this,totalflow));
        mUsedTV.setText("yijingyong"+Formatter.formatFileSize(this,usedflow));
        dao=new TrafficDao(this);
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-mm-dd");
        String dataString=sdf.format(date);
        long mobileGPRS=dao.getMobileGPRS(dataString);
        if (mobileGPRS<0){
            mobileGPRS=0;
        }
           mToDayTV.setText("已经使用"+Formatter.formatFileSize(this,mobileGPRS));
    }
    private void initView(){
       findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.rose_red));
        ImageView mLeftImgv=(ImageView)findViewById(R.id.imgv_leftbtn);
        ((TextView)findViewById(R.id.tv_title)).setText("流量监控");
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        ImageView mRightImgv=(ImageView)findViewById(R.id.imgv_rightbtn);
        mRightImgv.setImageResource(R.drawable.back);
        mRightImgv.setOnClickListener(this);
        mCorrectFlowBtn=(Button)findViewById(R.id.btn_correction_flow);
        mCorrectFlowBtn.setOnClickListener(this);
        mTotalTV=(TextView)findViewById(R.id.tv_month_totalgprs);
        mUsedTV=(TextView)findViewById(R.id.tv_month_usedgprs);
        mToDayTV=(TextView)findViewById(R.id.tv_today_gprs);
        mRemindIMGV=(ImageView)findViewById(R.id.imgv_traffic_remind);
        mRemindTV=(TextView)findViewById(R.id.tv_traffic_remind);
    }
    @Override
    public void onClick(View v) {
     switch (v.getId()){
         case R.id.imgv_leftbtn:
             finish();
             break;
         case R.id.imgv_rightbtn:
             startActivity(new Intent(this,OperatorSetActivity.class));
             break;
         case R.id.btn_correction_flow:
             int i=mSP.getInt("operators",0);
             SmsManager smsManager=SmsManager.getDefault();
             switch (i){
                 case 0:
                     Toast.makeText(this,"没设置",Toast.LENGTH_LONG).show();
                     break;
                 case 1:
                     smsManager.sendTextMessage("10086",null,"cxll",null,null);
                     break;
                 case 2:
                     break;
                 case 3:
                     break;
             }
     }
    }
    class CorrectFlowReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] objs = (Object[]) intent.getExtras().get("pd");
            for (Object obj : objs) {
                SmsMessage smsManager = SmsMessage.createFromPdu((byte[]) obj);
                String body = smsManager.getMessageBody();
                String address = smsManager.getOriginatingAddress();
                if (!address.equals("10086")) {
                    return;
                }
                String[] split = body.split(".");
                System.out.println(split[0]);
                long left = 0;
                long used = 0;
                long beyond = 0;
                for (int i = 0; i < split.length; i++) {
                    if (split[i].contains("已经用的")) {
                        String usedflow = split[i].substring(9, split[i].length());
                        used = getStringTofloat(usedflow);
                    } else if (split[i].contains("可用")) {
                        String leftflow = split[i].substring(3, split[i].length());
                        left = getStringTofloat(leftflow);
                    } else if (split[i].contains("可用")) {
                        String beyondflow = split[i].substring(3, split[i].length());
                        beyond = getStringTofloat(beyondflow);
                    }
                }
                SharedPreferences.Editor edit = mSP.edit();
                System.out.println("--" + left);
                edit.putLong("tatal", used + left);
                edit.putLong("used", used + beyond);
                edit.commit();
                mTotalTV.setText("流量" + Formatter.formatFileSize(context, (used)));
                mUsedTV.setText("流量" + Formatter.formatFileSize(context, (used)));
            }
        }
    }
        private long getStringTofloat(String str) {
            long flow=0;
            if (!TextUtils.isEmpty(str)){
                if (str.contains("R")){
                    String[] split=str.split("R");
                    float m=Float.parseFloat(split[0]);
                    flow=(long)(m);
                }
                else if (str.contains("e")){
                    String[] split=str.split("R");
                    float m=Float.parseFloat(split[0]);
                    flow=(long)(m);
                }
                else if (str.contains("r")){
                    String[] split=str.split("R");
                    float m=Float.parseFloat(split[0]);
                    flow=(long)(m*1024);
                }
            }
            return flow;
        }
        public void onDestroy(){
            if (receiver!=null){
                unregisterReceiver(receiver);
                receiver=null;
            }
            super.onDestroy();
        }
    }
