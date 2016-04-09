package mlxy.tumplar.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import mlxy.tumplar.R;
import uk.co.senab.photoview.PhotoView;

public class ImageViewerActivity extends BaseActivity {
    private static final String EXTRA_IMAGE_URI = "extra_image_uri";

    public static void startWithImageUri(Context context, Uri uri) {
        Intent intent = new Intent(context, ImageViewerActivity.class);
        intent.putExtra(EXTRA_IMAGE_URI, uri);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        PhotoView photoViewPhoto = (PhotoView) findViewById(R.id.photoViewPhoto);
        photoViewPhoto.setOnViewTapListener((view, x, y) -> finish());

        final View progressBar = findViewById(R.id.progressBar);
        progressBar.setOnClickListener(view -> finish());

        Uri imageUri = getIntent().getParcelableExtra(EXTRA_IMAGE_URI);
        if (imageUri != null) {
            Picasso.with(this).load(imageUri).into(photoViewPhoto, new Callback.EmptyCallback() {
                @Override
                public void onSuccess() {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }
}
