package cn.edu.gdmec.android.mobileguard;
import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import java.util.List;
import java.util.Random;
import cn.edu.gdmec.android.mobileguard.m3communicationguard.db.dao.BlackNumberDao;
import cn.edu.gdmec.android.mobileguard.m3communicationguard.entity.BlackContactInfo;
@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BlackNumberDaoTest {
    private Context context;
    private BlackNumberDao dao;
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        dao = new BlackNumberDao(context);
    }
    public void t1Add() throws Exception {
        Random random = new Random(8979);
        for (long i = 1; i < 30; i++) {
            BlackContactInfo info = new BlackContactInfo();
            info.phoneNumber = 135000000l + i + "";
            info.contactName = "zhamgsan" + i;
            info.mode = random.nextInt(3) + 1;
            dao.add(info);
        }
    }

    public void t2Delete() throws Exception {
        BlackNumberDao dao = new BlackNumberDao(context);
        BlackContactInfo info = new BlackContactInfo();
        for (long i = 1; i < 5; i++) {
            info.phoneNumber = 135000000l + i + "";
            dao.delete(info);
        }
    }
    public void t3GetPageBlackNumber() throws Exception {
        BlackNumberDao dao = new BlackNumberDao(context);
        List<BlackContactInfo> list = dao.getPageBlackNumber(2, 5);
        for (int i = 0; i < list.size(); i++) {
            Log.i("TestBlackNumberDao", list.get(i).phoneNumber);
        }
    }
    public void t4GetBlackContactMode() throws Exception {
        BlackNumberDao dao = new BlackNumberDao(context);
        int mode = dao.getBlackContactMode(135000000l + "");
        Log.i("TestBlackNumberDao", mode + "");
    }
    public void t5GetTotalNumber() throws Exception {
        BlackNumberDao dao = new BlackNumberDao(context);
        int tatal = dao.getTotalNumber();
        Log.i("TestBlackNumberDao", "数据总天目" + tatal);
    }

    public void t6IsNumberExist() throws Exception {
        BlackNumberDao dao = new BlackNumberDao(context);
        boolean isExist = dao.IsNumberExist(135000000l + "");
        if (isExist) {
            Log.i("TestBlackNumberDao", "改号码在数据库中");
        } else {
            Log.i("TestBlackNumberDao", "改号码不在数据库中");
        }
    }
}