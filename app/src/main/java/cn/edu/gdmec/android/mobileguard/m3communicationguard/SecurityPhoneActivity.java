package cn.edu.gdmec.android.mobileguard.m3communicationguard;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.Choreographer;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.edu.gdmec.android.mobileguard.R;
import cn.edu.gdmec.android.mobileguard.m3communicationguard.adapter.BlackContactAdapter;
import cn.edu.gdmec.android.mobileguard.m3communicationguard.db.dao.BlackNumberDao;
import cn.edu.gdmec.android.mobileguard.m3communicationguard.entity.BlackContactInfo;

/**
 * Created by pc on 2017/11/4.
 */
public class SecurityPhoneActivity extends AppCompatActivity implements View.OnClickListener {
    private FrameLayout mHaveBlackNunber;
    private FrameLayout mNoBlackNumber;
    private BlackNumberDao dao;
    private ListView mListView;
    private int pagenumber = 0;
    private int pagesize = 4;
    private int totalNumber;
    private List<BlackContactInfo> pageblacknumber = new ArrayList<BlackContactInfo>();
    private BlackContactAdapter adapter;

    public void fillData() {
        dao = new BlackNumberDao(SecurityPhoneActivity.this);
        totalNumber = dao.getTotalNumber();
        if (totalNumber == 0) {
            mHaveBlackNunber.setVisibility(View.GONE);
            mNoBlackNumber.setVisibility(View.VISIBLE);
        } else if (totalNumber > 0) {
            mHaveBlackNunber.setVisibility(View.VISIBLE);
            mNoBlackNumber.setVisibility(View.GONE);
            pagenumber = 0;
        }
        if (pageblacknumber.size() > 0) {
            pageblacknumber.clear();
        }
        pageblacknumber.addAll(dao.getPageBlackNumber(pagenumber,pagesize));
        if (adapter == null) {
            adapter = new BlackContactAdapter(pageblacknumber, SecurityPhoneActivity.this);
            adapter.setCallBack(new BlackContactAdapter.BlackContactCallBack() {
                @Override
                public void DataSizeChanged() {
                    fillData();
                }
            }
            );
            mListView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
    private void initView(){
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.bright_purple));
        ImageView mLeftImgv=(ImageView)findViewById(R.id.imgv_leftbtn);
        ((TextView)findViewById(R.id.tv_title)).setText("通讯卫视");
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        mHaveBlackNunber=(FrameLayout)findViewById(R.id.fl_haveblacknumber);
        mNoBlackNumber=(FrameLayout)findViewById(R.id.fl_noblacknumber);
        findViewById(R.id.btn_add_blacknumber).setOnClickListener(this);
        mListView=(ListView)findViewById(R.id.lv_blacknumbers);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener(){
            public void onScrollStateChanged(AbsListView absListView,int i){
                switch (i) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        int lastVisiblePosition = mListView.getLastVisiblePosition();
                        if (lastVisiblePosition == pageblacknumber.size() - 1) {
                            pagenumber++;
                            if (pagenumber * pagesize>= totalNumber) {
                                Toast.makeText(SecurityPhoneActivity.this, "没有更多了", Toast.LENGTH_SHORT).show();
                            } else {
                                pageblacknumber.addAll(dao.getPageBlackNumber(pagenumber,pagesize));
                                adapter.notifyDataSetChanged();
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
            }
        });
    }
    public void onCreate(Bundle savedInstanceState){
super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_phone);
        initView();
        fillData();
    }
    @Override
    public void onClick(View view) {
       switch(view.getId()){
           case R.id.imgv_leftbtn:
               finish();
               break;
           case R.id.btn_add_blacknumber:
               startActivity(new Intent(this,AddBlackNumberActivity.class));
               break;
       }
    }
    protected void onResume(){
        super.onResume();
        if (dao.getTotalNumber()>0){
            mHaveBlackNunber.setVisibility(View.VISIBLE);
            mNoBlackNumber.setVisibility(View.GONE);
        }
        else{
            mHaveBlackNunber.setVisibility(View.GONE);
            mNoBlackNumber.setVisibility(View.VISIBLE);
        }
        pagenumber=0;
        pageblacknumber.clear();
        pageblacknumber.addAll(dao.getPageBlackNumber(pagenumber,pagesize));
        if (adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }
}
