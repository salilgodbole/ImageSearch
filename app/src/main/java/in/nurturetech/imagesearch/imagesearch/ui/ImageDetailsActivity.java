package in.nurturetech.imagesearch.imagesearch.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import in.nurturetech.imagesearch.imagesearch.Constants;
import in.nurturetech.imagesearch.imagesearch.R;
import in.nurturetech.imagesearch.imagesearch.entities.ImageModel;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ImageDetailsActivity extends AppCompatActivity {
    private ImageView bigImageView = null;
    private TextView txtImageTitle = null;
    private ImageModel imageModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // inside your activity (if you did not enable transitions in your theme)
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_details);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        imageModel = getIntent().getParcelableExtra(Constants.IMAGE_MODEL_INTENT_EXTRA);
        txtImageTitle = (TextView) findViewById(R.id.txt_image_title);
        txtImageTitle.setText(imageModel.getTitle());
        // Initialize imageview
        bigImageView = (ImageView) findViewById(R.id.img_view_big);

        String imageUrl = imageModel.getUrl();

        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(bigImageView);

        // set an exit transition
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new ChangeBounds());
        }
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
}
