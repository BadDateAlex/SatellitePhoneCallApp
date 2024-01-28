package com.hoho.android.usbserial.examples;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportFragmentManager().addOnBackStackChangedListener(this);//回退栈监视器
        //是否是应用第一次启动
        if (savedInstanceState == null)
            //将一个新的DeviceFragment实例添加到具有ID为R.id.fragment的布局容器中
            //getSupportFragmentManager()是在获取Fragment管理器
            //beginTransaction()是在开启一个新的事务
            //将DeviceFragment添加到碎片容器中，并命名为device
            getSupportFragmentManager().beginTransaction().add(R.id.fragment, new DevicesFragment(), "devices").commit();
        else//不是第一次启动
            onBackStackChanged();
    }

    @Override
    public void onBackStackChanged() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(getSupportFragmentManager().getBackStackEntryCount()>0);//用于设置是否显示返回按钮
        //回退栈中有Fragment的记录，即getBackStackEntryCount()的返回值大于0，那么就将返回按钮显示出来；否则，将返回按钮隐藏
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //当Activity已经处于启动状态（已经经历过onCreate()，onStart()，onResume()生命周期方法）时，
    // 如果有新的Intent传入，就会触发onNewIntent()方法。
    @Override
    protected void onNewIntent(Intent intent) {
        if("android.hardware.usb.action.USB_DEVICE_ATTACHED".equals(intent.getAction())) {
            TerminalFragment terminal = (TerminalFragment)getSupportFragmentManager().findFragmentByTag("terminal");
            if (terminal != null)
                terminal.status("USB device detected");
        }
        super.onNewIntent(intent);
    }

}
