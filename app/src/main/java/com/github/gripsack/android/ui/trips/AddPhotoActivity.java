package com.github.gripsack.android.ui.trips;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.gripsack.android.R;
import com.github.gripsack.android.data.model.Trip;
import com.github.gripsack.android.ui.MainActivity;
import com.github.gripsack.android.ui.companions.CompanionsActivity;
import com.github.gripsack.android.utils.FirebaseUtil;

import org.parceler.Parcels;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddPhotoActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 123;
    private static final String APP_TAG = "my_camera_app";
    private String mPhotoFileName = "photo.jpg";

    @BindView(R.id.rvResults)
    RecyclerView rvPhotos;
    @BindView(R.id.btnAddPhoto)
    ImageButton btnAddPhoto;
    private ArrayList<Bitmap> images;
    PhotosAdapter adapter;
    @BindView(R.id.preview)
    ImageView ivPreview;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvDone)
    TextView tvDone;

    private Trip trip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        trip=new Trip();
        trip=(Trip) Parcels.unwrap(getIntent()
                .getParcelableExtra("Trip"));

        setSupportActionBar(toolbar);
        images=new ArrayList<Bitmap>();

        //TODO:Get previous images from firebase
        getDummyTripPhotos();
        adapter=new PhotosAdapter(this,images);

        rvPhotos.setAdapter(adapter);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvPhotos.setLayoutManager(layoutManager);

        tvDone.setOnClickListener(this);

    }

    //TODO:remove and get from Firebase
    private void getDummyTripPhotos(){
        Bitmap image=BitmapFactory.decodeResource(getResources(), R.drawable.travel1);
        images.add(image);
        image=BitmapFactory.decodeResource(getResources(), R.drawable.travel2);
        images.add(image);
        image=BitmapFactory.decodeResource(getResources(), R.drawable.travel3);
        images.add(image);
        image=BitmapFactory.decodeResource(getResources(), R.drawable.travel4);
        images.add(image);
        image=BitmapFactory.decodeResource(getResources(), R.drawable.travel5);
        images.add(image);
        image=BitmapFactory.decodeResource(getResources(), R.drawable.travel6);
        images.add(image);
        image=BitmapFactory.decodeResource(getResources(), R.drawable.travel7);
        images.add(image);
        image=BitmapFactory.decodeResource(getResources(), R.drawable.travel8);
        images.add(image);
        image=BitmapFactory.decodeResource(getResources(), R.drawable.travel9);
        images.add(image);
        image=BitmapFactory.decodeResource(getResources(), R.drawable.travel10);
        images.add(image);

    }

    public void capturePhoto(View view){
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(mPhotoFileName)); // set the image file name

        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    // Returns the Uri for a photo stored on disk given the fileName
    public Uri getPhotoFileUri(String fileName) {
        // Only continue if the SD Card is mounted
        if (isExternalStorageAvailable()) {
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            File mediaStorageDir = new File(
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                Log.d(APP_TAG, "failed to create directory");
            }

            // Return the file target for the photo based on filename
            return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + fileName));
        }
        return null;
    }

    // Returns true if external storage for photos is available
    public boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri takenPhotoUri = getPhotoFileUri(mPhotoFileName);

                Glide.with(this).load(takenPhotoUri).centerCrop().into(ivPreview);

                //Get image to save Firebase
                BitmapDrawable bitmapDrawable = ((BitmapDrawable) ivPreview.getDrawable());
                Bitmap bitmap = bitmapDrawable .getBitmap();
                encodeBitmapAndSaveToFirebase(bitmap);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

   //Encode the image to save in Firebase
    public void encodeBitmapAndSaveToFirebase(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        FirebaseUtil.saveImage(imageEncoded,trip.getTripId());
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id) {
            case R.id.tvDone:
                Intent intent = new Intent(this, EditTripActivity.class).putExtra("Trip", Parcels.wrap(trip));
                startActivity(intent);
                finish();
                break;
        }
    }
}
