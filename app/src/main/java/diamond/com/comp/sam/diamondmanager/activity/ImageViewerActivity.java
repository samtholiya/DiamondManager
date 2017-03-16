package diamond.com.comp.sam.diamondmanager.activity;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

import diamond.com.comp.sam.diamondmanager.R;
import uk.co.senab.photoview.PhotoViewAttacher;

import static diamond.com.comp.sam.diamondmanager.App.HOST_ADDRESS;
import static diamond.com.comp.sam.diamondmanager.App.HOST_PATH;

public class ImageViewerActivity extends AppCompatActivity {

    public static final String IMAGE_URI = "image_uri";

    ImageView mImageView;
    private PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
/*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
*/

        Uri imageUri = getIntent().getParcelableExtra(IMAGE_URI);

        mImageView = (ImageView) findViewById(R.id.image_view_layer);
        mAttacher = new PhotoViewAttacher(mImageView);
        Log.d(getClass().getName(), "{Path} :" + imageUri.getPath());

        mAttacher.setMinimumScale(0);

        if (imageUri.getPath().contains(HOST_PATH)) {
            Log.d(getClass().getName(), "internet url");
            Glide
                    .with(ImageViewerActivity.this)
                    .load(HOST_ADDRESS + imageUri.getPath())
                    .crossFade()
                    .into(mImageView);
        } else {
            Glide
                    .with(this)
                    .load(Uri.fromFile(new File(imageUri.getPath())))
                    .crossFade()
                    .into(mImageView);
        }
/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    private Drawable getD() {
        //      mAttacher.update();
        return null;
    }

}
