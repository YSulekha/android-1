package com.github.gripsack.android.ui.places;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.github.gripsack.android.R;
import com.github.gripsack.android.ui.SingleFragmentActivity;



public class MyPlacesActivity  extends SingleFragmentActivity {

    public final static String EXTRA_ITEMTYPE = "itemtype";

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, MyPlacesActivity.class);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        if(getIntent().getStringExtra(EXTRA_ITEMTYPE).equals("bucketlist")){
            getSupportActionBar().setTitle(getString(R.string.menu_item_place_bucketlist));
            return BucketlistFragment.newInstance();
        }
        if(getIntent().getStringExtra(EXTRA_ITEMTYPE).equals("visited")){
            getSupportActionBar().setTitle(getString(R.string.menu_item_place_visited));
            return VisitedFragment.newInstance();
        }
        if(getIntent().getStringExtra(EXTRA_ITEMTYPE).equals("recommended")){
            getSupportActionBar().setTitle(getString(R.string.menu_item_place_recommended));
            return RecommendedFragment.newInstance();
        }
        return null;
    }

    @Override
    protected void onAuthStateSignIn() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
