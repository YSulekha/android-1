package com.github.gripsack.android.ui.trips;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.gripsack.android.R;
import com.github.gripsack.android.data.model.Trip;
import com.github.gripsack.android.ui.helpers.CardScaleHelper;
import com.github.gripsack.android.utils.BlurBitmapUtils;
import com.github.gripsack.android.utils.ViewSwitchUtils;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class DisplayPhotosActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ImageView mBlurView;
    private TextView tvLocation;
    private TextView tvComment;
    private List<Integer> mPhotos = new ArrayList<>();
   // private List<String> mTexts = new ArrayList<>();
    private List<String> mComments = new ArrayList<>();
    private CardScaleHelper mCardScaleHelper = null;
    private Runnable mBlurRunnable;
    private int mLastPos = -1;
    private ImageView ivAddPhoto;
    private Trip trip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_photos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        trip=new Trip();
        trip=(Trip) Parcels.unwrap(getIntent()
                .getParcelableExtra("Trip"));
        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void init() {
        //for (int i = 0; i < 10; i++) {
        mPhotos.add(R.drawable.travel_1);
        mPhotos.add(R.drawable.travel_2);
        mPhotos.add(R.drawable.travel_3);
        mPhotos.add(R.drawable.travel_4);
        mPhotos.add(R.drawable.travel_5);
        mPhotos.add(R.drawable.travel_6);
        mPhotos.add(R.drawable.travel_7);
        mPhotos.add(R.drawable.travel_8);
        mPhotos.add(R.drawable.travel_9);
        mPhotos.add(R.drawable.travel_10);
        //}

   /*     mTexts.add("San Francisco");

        mTexts.add("New York");
        mTexts.add("Mountain View");
        mTexts.add("Canada");
        mTexts.add("San Francisco");
        mTexts.add("New York");
        mTexts.add("Mountain View");
        mTexts.add("Canada");
        mTexts.add("San Francisco");
        mTexts.add("New York");*/

        mComments.add("Yeah we'll be counting stars!!");
        mComments.add("Colours definitely fill me with happiness! I'm so in love with pink and purple bougainvilleas... ❤");
        mComments.add("Feel good!");
        mComments.add("I am on the road to point out the importance of life belt.\n Yes we call it life belt instead of safety belt because beside providing \n safety it connects us to life in traffic! ❤ !");
        mComments.add("You know they are just people like me and you...");
        mComments.add("Two travellers after wandering around the world, today we came together");
        mComments.add("Have an abundant, peaceful, happy and above all a healthy day...");
        mComments.add("Beautiful landscape and mum!");
        mComments.add("Feel tired!");
        mComments.add("Happy Sunday dear cornelian cherry juice lovers! ❤");

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(new CardAdapter(mPhotos));
        mCardScaleHelper = new CardScaleHelper();
        mCardScaleHelper.setCurrentItemPos(2);
        mCardScaleHelper.attachToRecyclerView(mRecyclerView);
        tvLocation=(TextView)findViewById(R.id.tvLocation) ;
        tvComment=(TextView)findViewById(R.id.tvComment) ;
        ivAddPhoto=(ImageView)findViewById(R.id.ivAddPhoto);
        ivAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DisplayPhotosActivity.this,AddPhotoActivity.class)
                        .putExtra("Trip", Parcels.wrap(trip));
                startActivity(intent);
            }
        });

        initBlurBackground();
    }

    private void initBlurBackground() {
        mBlurView = (ImageView) findViewById(R.id.blurView);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    notifyBackgroundChange();
                }
            }
        });

        notifyBackgroundChange();
    }

    private void notifyBackgroundChange() {
        if (mLastPos == mCardScaleHelper.getCurrentItemPos()) return;
        mLastPos = mCardScaleHelper.getCurrentItemPos();
        final int resId = mPhotos.get(mCardScaleHelper.getCurrentItemPos());
        mBlurView.removeCallbacks(mBlurRunnable);
        mBlurRunnable = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
                ViewSwitchUtils.startSwitchBackgroundAnim(mBlurView, BlurBitmapUtils.getBlurBitmap(mBlurView.getContext(), bitmap, 15));
            }
        };

       // tvLocation.setText(mTexts.get(mLastPos));

        tvComment.setText(mComments.get(mLastPos));

        mBlurView.postDelayed(mBlurRunnable, 500);
    }
}
