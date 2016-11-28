package com.github.gripsack.android.ui;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;

import com.github.gripsack.android.R;

public abstract class SingleFragmentActivity extends BaseActivity {
    protected abstract Fragment createFragment();

    public Fragment mActivityFragment;
    public FragmentManager mFragmentManager;
    public Toolbar mToolbar;

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        bind();
        setSupportActionBar(mToolbar);

        mFragmentManager = getSupportFragmentManager();
        mActivityFragment = mFragmentManager.findFragmentById(R.id.fragment_container);

        if (mActivityFragment == null) {
            mActivityFragment = createFragment();
            if (mActivityFragment != null) {
                switchActivityFragment(mActivityFragment);
            }
        }
    }

    protected void switchActivityFragment(Fragment fragment) {
        mFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    protected void bind() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
    }
}