package com.github.gripsack.android.ui;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.github.gripsack.android.R;
import com.github.gripsack.android.ui.navigation.ViewPagerAdapter;

public abstract class TabFragmentsActivity extends BaseActivity {

    public Toolbar mToolbar;
    public ViewPager mViewPager;
    public TabLayout mTabLayout;

    protected abstract ViewPagerAdapter createViewPagerAdapter();

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_tabs;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        bind();
        setSupportActionBar(mToolbar);
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewPager.setAdapter(createViewPagerAdapter());
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onDestroy() {
        mViewPager.setAdapter(null);
        super.onDestroy();
    }

    protected void bind() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
    }
}