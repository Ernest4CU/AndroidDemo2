package com.qlemon.test;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.qlemon.test.fragment.CardFragment;
import com.qlemon.test.fragment.LockFrament;
import com.qlemon.test.fragment.ScanFragment;

/**
 * 串口测试主活动类
 * @author JeanSit added by 2016-01-12
 */
public class MainActivity extends FragmentActivity implements TabHost.OnTabChangeListener {

    private FragmentTabHost tabHost;

    private LayoutInflater inflater;

    private Class fragmentArray[] = {LockFrament.class, ScanFragment.class, CardFragment.class};

    private String tagArray[] = {"lockerTag", "scanTag", "cardTag"};

    private int imageArray[] = {R.drawable.ele_lock_01, R.drawable.scanner_01, R.drawable.id_card_03};

    private String textArray[] = {"电子锁", "扫描枪", "身份证"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        inflater = LayoutInflater.from(this);
        tabHost = (FragmentTabHost)findViewById(R.id.tabHost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.tabContent);
        int count = fragmentArray.length;
        for (int i=0; i<count; i++) {
            TabHost.TabSpec tabSpec = tabHost.newTabSpec(tagArray[i]).setIndicator(getTabItemView(i));
            tabHost.addTab(tabSpec, fragmentArray[i], null);
            if (i==0) {
                tabHost.getTabWidget().getChildTabViewAt(i).setBackgroundColor(Color.GRAY);
            } else {
                tabHost.getTabWidget().getChildTabViewAt(i).setBackgroundColor(Color.WHITE);
            }
        }

        tabHost.setOnTabChangedListener(this);
    }

    private View getTabItemView(int index) {
        View view = inflater.inflate(R.layout.tab_item_view, null);
        ImageView imageView = (ImageView)view.findViewById(R.id.imageview);
        imageView.setImageResource(imageArray[index]);
        TextView textView = (TextView)view.findViewById(R.id.textview);
        textView.setText(textArray[index]);
        return view;
    }

    @Override
    public void onTabChanged(String tabId) {
        for (int i=0; i<tagArray.length; i++) {
            if (tabId.equals(tagArray[i])) {
                tabHost.getTabWidget().getChildTabViewAt(i).setBackgroundColor(Color.GRAY);
            } else {
                tabHost.getTabWidget().getChildTabViewAt(i).setBackgroundColor(Color.WHITE);
            }
        }

    }
}
