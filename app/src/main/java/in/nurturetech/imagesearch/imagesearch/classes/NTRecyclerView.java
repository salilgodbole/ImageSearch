package in.nurturetech.imagesearch.imagesearch.classes;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by salil on 24/12/15.
 */
public class NTRecyclerView extends RecyclerView {

    private boolean loading;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private NTRecyclerViewListener listener = null;

    public NTRecyclerView(Context context) {
        super(context);

        init();
    }

    public NTRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NTRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setListener(NTRecyclerViewListener listener) {
        this.listener = listener;
    }

    public void loadMoreComplete() {
        loading = false;
    }

    private void init() {
        addOnScrollListener(recyclerViewScrollListener);
    }

    private RecyclerView.OnScrollListener recyclerViewScrollListener = new RecyclerView.OnScrollListener() {

        private int currentState;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            currentState = newState;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            LayoutManager layoutManager = getLayoutManager();

            visibleItemCount = recyclerView.getChildCount();
            totalItemCount = layoutManager.getItemCount();

            if (layoutManager instanceof LinearLayoutManager) {
                firstVisibleItem = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            } else if (layoutManager instanceof GridLayoutManager) {
                firstVisibleItem = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
            }

            if (!loading
                    && currentState != RecyclerView.SCROLL_STATE_IDLE
                    && listener != null
                    && (firstVisibleItem != 0 && (firstVisibleItem
                    + visibleItemCount == totalItemCount))) {

                // Flag to denote loading status
                loading = true;

                // Notify that scrollview is in the loading state
                listener.onLoadMore();
            }
        }
    };

    public interface NTRecyclerViewListener {
        void onLoadMore();
    }
}
