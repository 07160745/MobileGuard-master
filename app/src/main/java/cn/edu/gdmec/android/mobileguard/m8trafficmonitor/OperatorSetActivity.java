package cn.edu.gdmec.android.mobileguard.m8trafficmonitor;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import cn.edu.gdmec.android.mobileguard.R;
public class OperatorSetActivity extends AppCompatActivity {
        private Spinner mSelectSP;
    private String[] operators={"踢动","踢动","踢动"};
    private ArrayAdapter mSelectadaper;
    private SharedPreferences msp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_set);
        msp=getSharedPreferences("content",MODE_PRIVATE);
        initview();
    }
    private void initview() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.rose_red));
        ImageView mLeftImgv=(ImageView)findViewById(R.id.imgv_leftbtn);
        ((TextView)findViewById(R.id.tv_title)).setText("运营商");
        mLeftImgv.setImageResource(R.drawable.back);
         mSelectSP=(Spinner)findViewById(R.id.spinner_operator_select);
        mSelectadaper=new ArrayAdapter(this,R.layout.item_spinner_operatorset,R.id.tv_provide,operators);
        mSelectSP.setAdapter(mSelectadaper);
    }
    public void onClick(View v){
        SharedPreferences.Editor edit= msp.edit();
        switch (v.getId()){
            case R.id.imgv_leftbtn:
                edit.putBoolean("is",false);
                finish();
                break;
            case R.id.btn_operator_finish:
                edit.putInt("oprt",mSelectSP.getSelectedItemPosition()+1);
                edit.putBoolean("is",true);
                edit.commit();
                startActivity(new Intent(this,TrafficMonitoringActivity.class));
                finish();
                break;
        }
    }
}
