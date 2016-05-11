package in.nurturetech.imagesearch.imagesearch.ui;

import android.content.Context;
import android.support.annotation.AnimRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import in.nurturetech.imagesearch.imagesearch.R;
import in.nurturetech.imagesearch.imagesearch.entities.ImageModel;

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private final List<ImageModel> mValues;
    private final OnRecyclerviewInteractionListener mListener;
    private int lastPosition = -1;

    public MyItemRecyclerViewAdapter(Context context, List<ImageModel> items, OnRecyclerviewInteractionListener listener) {
        mContext = context;
        mValues = items;
        mListener = listener;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return (int) System.currentTimeMillis();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        ImageModel imageModel = mValues.get(position);

        if (imageModel != null) {
            holder.mItem = imageModel;
            holder.mTitleView.setText(imageModel.getTitle());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onItemClick(holder.mImageView, holder.mTitleView, holder.mItem);
                    }
                }
            });

            String imageUrl = imageModel.getUrl();
            Glide.clear(holder.mImageView);
            Glide.with(mContext)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.mImageView);

            // Here you apply the animation when the view is bound
            setAnimation(holder.mView, position, R.anim.slide_up);
        }
    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position, @AnimRes int animationId) {
        if (position > lastPosition) {
            // If the bound view wasn't previously displayed on screen, it's animated
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), animationId);
            viewToAnimate.startAnimation(animation);

            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return mValues != null ? mValues.size() : null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final ImageView mImageView;
        public ImageModel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.title);
            mImageView = (ImageView) view.findViewById(R.id.photos);
        }
    }

    interface OnRecyclerviewInteractionListener {
        void onItemClick(ImageView imageView, TextView txtImageTitle, ImageModel imageModel);
    }
}