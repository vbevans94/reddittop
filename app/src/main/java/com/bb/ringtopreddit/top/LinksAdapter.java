package com.bb.ringtopreddit.top;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bb.ringtopreddit.R;
import com.bb.ringtopreddit.data.model.Picture;
import com.bb.ringtopreddit.data.model.Preview;
import com.bb.ringtopreddit.data.model.RedditLink;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LinksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_LOADING = 0;
    private static final int TYPE_LINK = 1;
    private static final RedditLink LOADING_PLACEHOLDER = new RedditLink();

    private final List<RedditLink> data = new ArrayList<>();
    private final Context context;
    private final LayoutInflater inflater;
    private final OnPictureSelectedListener listener;
    private boolean isLoading;

    LinksAdapter(Context context, OnPictureSelectedListener listener) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    public void addData(List<RedditLink> links) {
        data.addAll(links);
        notifyItemRangeInserted(getItemCount(), links.size());
    }

    @Override
    public int getItemViewType(int position) {
        return isLoading && position == data.size() - 1 ? TYPE_LOADING : TYPE_LINK;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_LOADING:
                return new LoadingHolder(inflater.inflate(R.layout.item_loading, parent, false));

            default:
            case TYPE_LINK:
                return new LinkHolder(inflater.inflate(R.layout.item_link, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LinkHolder) {
            ((LinkHolder) holder).bind(data.get(position));
        }
    }

    public void setIsLoading(boolean loading) {
        isLoading = loading;

        if (isLoading) {
            data.add(LOADING_PLACEHOLDER);
            notifyItemInserted(data.size() - 1);
        } else {
            int position = data.indexOf(LOADING_PLACEHOLDER);
            if (position >= 0) {
                data.remove(position);
                notifyItemRemoved(position);
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class LoadingHolder extends RecyclerView.ViewHolder {

        LoadingHolder(View itemView) {
            super(itemView);
        }
    }

    class LinkHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_title)
        TextView textTitle;

        @BindView(R.id.text_author)
        TextView textAuthor;

        @BindView(R.id.text_comments)
        TextView textComments;

        @BindView(R.id.text_when)
        TextView textWhen;

        @BindView(R.id.image_thumbnail)
        ImageView imageThumbnail;

        @BindDimen(R.dimen.image_size)
        int imageSize;

        private RedditLink link;

        LinkHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        void bind(RedditLink link) {
            this.link = link;

            textTitle.setText(link.getTitle());
            textAuthor.setText(context.getString(R.string.by_author, link.getAuthor()));
            textComments.setText(context.getString(R.string.n_comments, link.getCommentsAmount()));
            textWhen.setText(link.getHoursAgo());

            Glide.with(itemView.getContext())
                    .load(link.getThumbnail())
                    .apply(new RequestOptions()
                            .placeholder(R.mipmap.ic_launcher)
                            .centerCrop())
                    .into(imageThumbnail);
        }

        @OnClick(R.id.image_thumbnail)
        void onThumbnailClicked() {
            Preview preview = link.getPreview();
            if (preview != null && preview.getImages() != null) {
                listener.onPictureSelected(preview.getImages().get(0).getSource());
            } else {
                listener.onNoPicture();
            }
        }
    }

    public interface OnPictureSelectedListener {

        void onPictureSelected(Picture picture);

        void onNoPicture();
    }
}
