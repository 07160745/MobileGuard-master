package cn.edu.gdmec.android.mobileguard.m3communicationguard;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.gdmec.android.mobileguard.R;
import cn.edu.gdmec.android.mobileguard.m2theftguard.ContactSelectActivity;
import cn.edu.gdmec.android.mobileguard.m3communicationguard.db.dao.BlackNumberDao;
import cn.edu.gdmec.android.mobileguard.m3communicationguard.entity.BlackContactInfo;

/**
 * Created by pc on 2017/11/4.
 */
public class AddBlackNumberActivity extends AppCompatActivity implements View.OnClickListener {
    private CheckBox mSmsCB;
    private CheckBox mTelCB;
    private EditText mNumET;
    private EditText mNameET;
    private BlackNumberDao dao;
    public void initView(){
         findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.bright_purple));
        ImageView mLeftImgv=(ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        mSmsCB=(CheckBox)findViewById(R.id.cb_blacknumber_sms);
        mTelCB=(CheckBox)findViewById(R.id.cb_blacknumber_tel);
        mNumET=(EditText)findViewById(R.id.et_balcknumber);
        mNameET=(EditText)findViewById(R.id.et_blacknumber);
        findViewById(R.id.add_blacknum_btn).setOnClickListener(this);
        findViewById(R.id.add_fromcontact_btn).setOnClickListener(this);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (data!=null){
            String phone=data.getStringExtra("phone");
            String name=data.getStringExtra("name");
            mNameET.setText(name);
            mNumET.setText(phone);
        }
    }
    protected void onCreate(Bundle savedInstanceState){
super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_black_number);
        dao = new BlackNumberDao(AddBlackNumberActivity.this);
        initView();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.add_blacknum_btn:
                String number = mNumET.getText().toString().trim();
                String name = mNameET.getText().toString().trim();
                if (TextUtils.isEmpty(number) || TextUtils.isEmpty(name)) {
                    Toast.makeText(this, "电话啊号码手机不猛为空", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    BlackContactInfo blackContactInfo = new BlackContactInfo();
                    blackContactInfo.phoneNumber = number;
                    blackContactInfo.contactName = name;
                    if (mSmsCB.isChecked() & mTelCB.isChecked()) {
                        blackContactInfo.mode = 3;
                    } else if (mSmsCB.isChecked() & !mTelCB.isChecked()) {
                        blackContactInfo.mode = 2;
                    } else if (!mSmsCB.isChecked() & mTelCB.isChecked()) {
                        blackContactInfo.mode = 1;
                    } else {
                        Toast.makeText(this, "前选择拦截", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!dao.IsNumberExist(blackContactInfo.phoneNumber)) {
                        dao.add(blackContactInfo);
                    } else {
                        Toast.makeText(this, "改号码当前已经被加入黑名单", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }
                break;
            case R.id.add_fromcontact_btn:
                startActivityForResult(new Intent(this, ContactSelectActivity.class), 0);
        }
    }
}
