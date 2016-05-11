package in.nurturetech.imagesearch.imagesearch.ui;

import android.app.ActivityOptions;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.nurturetech.imagesearch.imagesearch.Callback;
import in.nurturetech.imagesearch.imagesearch.Constants;
import in.nurturetech.imagesearch.imagesearch.NTError;
import in.nurturetech.imagesearch.imagesearch.R;
import in.nurturetech.imagesearch.imagesearch.Utils;
import in.nurturetech.imagesearch.imagesearch.classes.NTRecyclerView;
import in.nurturetech.imagesearch.imagesearch.daemons.ImageClient;
import in.nurturetech.imagesearch.imagesearch.entities.ImageModel;

public class ImageListActivity extends AppCompatActivity implements MyItemRecyclerViewAdapter.OnRecyclerviewInteractionListener, NTRecyclerView.NTRecyclerViewListener {

    private List<ImageModel> imageModelList = new ArrayList<>();
    private ProgressBar progressBar = null;
    private NTRecyclerView recyclerView = null;
    private RecyclerView.Adapter adapter = null;
    private String searchTopic = null;
    private ImageClient imageClient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // inside your activity (if you did not enable transitions in your theme)
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setAllowEnterTransitionOverlap(true);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);

        adapter = new MyItemRecyclerViewAdapter(this, imageModelList, this);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        recyclerView = (NTRecyclerView) findViewById(R.id.list);
        recyclerView.setListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(null);

        imageClient = ImageClient.getClient(this);

        startTimer();
        hideProgressBar();

        handleIntent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setIconified(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchTopic = newText;
                fetchImages();
                return false;
            }
        });

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchTopic = query;
            fetchImages();
        }
    }

    private void fetchImages() {
        imageClient.cancelPendingRequests();

        if (!Utils.isNetworkConnected(this)) {
            Toast.makeText(ImageListActivity.this, "Internet is not connected", Toast.LENGTH_SHORT).show();
            return;
        }

        showProgressBar();

        imageClient.getImages(searchTopic, new Callback<List<ImageModel>>() {
            @Override
            public void onSuccess(List<ImageModel> imageModelsList) {
                // Before loading items clear the items list.
                imageModelList.clear();
                imageModelList.addAll(imageModelsList);

                // Notify the adapter that new data has been added.
                adapter.notifyDataSetChanged();
                // Tell recyclerview that load more is completed.
                recyclerView.loadMoreComplete();
                // Hide Progress Bar
                hideProgressBar();
            }

            @Override
            public void onError(NTError error) {
                Toast.makeText(ImageListActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                hideProgressBar();
            }
        });
    }

    private void reset() {
        hideProgressBar();
        searchTopic = "";
        imageModelList.clear();
        adapter.notifyDataSetChanged();
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        startTimer();
    }

    private void startTimer() {
        new CountDownTimer(10000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Change the color of the progressBar
                // #FF0000 #F7FE2E #088A08 #0A2229
                String colorCode = "#FF0080";
                switch ((int) (millisUntilFinished / 1000)) {
                    case 1:
                    case 6:
                        colorCode = "#0040FF";
                        break;
                    case 2:
                    case 7:
                        colorCode = "#FF0000";
                        break;
                    case 3:
                    case 8:
                        colorCode = "#AEB404";
                        break;
                    case 4:
                    case 9:
                        colorCode = "#088A08";
                        break;
                    case 5:
                    case 0:
                        colorCode = "#4000FF";
                        break;
                }
                progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor(colorCode), PorterDuff.Mode.MULTIPLY);
            }

            @Override
            public void onFinish() {
                // Change the color of the progressBar
                progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#A9BCF5"), PorterDuff.Mode.MULTIPLY);

                startTimer();
            }
        }.start();
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(ImageView imageView, TextView txtImageTitle, ImageModel imageModel) {
        Intent i = new Intent(ImageListActivity.this, ImageDetailsActivity.class);
        // Pass the imageUrl so that the big image can be shown in the details activity.
        i.putExtra(Constants.IMAGE_MODEL_INTENT_EXTRA, imageModel);

        View sharedImageView = imageView;
        View sharedImageTitleView = txtImageTitle;
        String imageViewTransitionName = getString(R.string.image_view_transition_name);
        String imageTitleTransitionName = getString(R.string.image_title_transition_name);

        ActivityOptions transitionActivityOptions;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Pair<View, String> p1 = Pair.create(sharedImageView, imageViewTransitionName);
            Pair<View, String> p2 = Pair.create(sharedImageTitleView, imageTitleTransitionName);

            transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(ImageListActivity.this, p1, p2);

            startActivity(i, transitionActivityOptions.toBundle());
            // set an exit transition
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setExitTransition(new Explode());
            }
        }
    }

    @Override
    public void onLoadMore() {
        showProgressBar();
        fetchImages();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (imageClient != null) {
            imageClient.destroy();
        }
    }
}