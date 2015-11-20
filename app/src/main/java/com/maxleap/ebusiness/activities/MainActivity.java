package com.maxleap.ebusiness.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxleap.ebusiness.R;
import com.maxleap.ebusiness.fragments.CategoryFragment;
import com.maxleap.ebusiness.fragments.MainFragment;
import com.maxleap.ebusiness.fragments.MineFragment;
import com.maxleap.ebusiness.fragments.ShopFragment;

public class MainActivity extends BaseActivity {

    private FragmentTabHost mTabHost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        initTab();
    }

    private void initTab() {
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        mTabHost.getTabWidget().setDividerDrawable(null);
        mTabHost.addTab(mTabHost.newTabSpec("mainTab").setIndicator(getTabView(R.drawable.btn_home, R.string.activity_main_tab_home)),
                MainFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("categoryTab").setIndicator(getTabView(R.drawable.btn_category, R.string.activity_main_tab_category)),
                CategoryFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("shopTab").setIndicator(getTabView(R.drawable.btn_shop, R.string.activity_main_tab_shop)),
                ShopFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("mineTab").setIndicator(getTabView(R.drawable.btn_mine, R.string.activity_main_tab_mine)),
                MineFragment.class, null);
    }

    public void selectTab(int index) {
        mTabHost.setCurrentTab(index);
    }

    private View getTabView(int imgId, int txtId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.view_home_tab, null);
        ((ImageView) view.findViewById(R.id.tab_img)).setImageResource(imgId);
        ((TextView) view.findViewById(R.id.tab_label)).setText(txtId);
        return view;
    }


}
