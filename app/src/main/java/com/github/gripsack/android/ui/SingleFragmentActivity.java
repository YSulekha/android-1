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

        FragmentManager fm = getSupportFragmentManager();
        mActivityFragment = fm.findFragmentById(R.id.fragment_container);

        if (mActivityFragment == null) {
            mActivityFragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, mActivityFragment)
                    .commit();
        }
    }


    protected void bind() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
    }
}