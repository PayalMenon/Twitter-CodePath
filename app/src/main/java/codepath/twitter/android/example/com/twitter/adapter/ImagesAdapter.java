package codepath.twitter.android.example.com.twitter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.twitter.android.example.com.twitter.R;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder>{

    private List<String> mImageList;
    private Context mContext;

    public ImagesAdapter(List<String> imageList){
        this.mImageList = imageList;
    }

    @Override
    public ImagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_row_layout, parent, false);
        return new ImagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImagesViewHolder holder, int position) {
        String imageUrl = mImageList.get(position);
        Glide.with(mContext).
                load(imageUrl).
                asBitmap().
                placeholder(R.drawable.ic_tweet).
                into(holder.tweetImage);
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if(mImageList != null) {
            size = mImageList.size();
        }
        return size;
    }

    public class ImagesViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.iv_detail_tweet_image)
        ImageView tweetImage;

        public ImagesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
