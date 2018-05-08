package com.bb.ringtopreddit.top;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bb.ringtopreddit.R;
import com.bb.ringtopreddit.data.model.Picture;
import com.bb.ringtopreddit.data.model.RedditLink;
import com.bb.ringtopreddit.picture.PictureActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;

public class TopFragment extends Fragment implements TopContract.View, LinksAdapter.OnPictureSelectedListener {

    @Inject
    TopContract.Presenter presenter;

    @BindView(R.id.list_links)
    RecyclerView listLinks;

    private Unbinder unbinder;
    private LinksAdapter adapter;
    private LinearLayoutManager layoutManager;
    private boolean isLoading = true;
    private RecyclerView.OnScrollListener pagingListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if (!isLoading && !presenter.isLastPage()) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0) {
                    presenter.loadMore();
                }
            }
        }
    };

    public static Fragment create() {
        return new TopFragment();
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        unbinder = ButterKnife.bind(this, view);

        Context context = getContext();
        adapter = new LinksAdapter(context, this);
        listLinks.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(context);
        listLinks.setLayoutManager(layoutManager);
        listLinks.addOnScrollListener(pagingListener);
    }

    @Override
    public void onStart() {
        super.onStart();

        presenter.takeView(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        presenter.dropView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        unbinder.unbind();
    }

    @Override
    public void showProgress() {
        adapter.setIsLoading(true);

        isLoading = true;
    }

    @Override
    public void hideProgress() {
        adapter.setIsLoading(false);

        isLoading = false;
    }

    @Override
    public int getItemCount() {
        return adapter.getItemCount();
    }

    @Override
    public void showError(int errorResId) {
        Toast.makeText(getContext(), errorResId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showData(List<RedditLink> links) {
        adapter.replaceData(links);
    }

    @Override
    public void appendData(List<RedditLink> data) {
        adapter.addData(data);
    }

    @Override
    public void onPictureSelected(Picture picture) {
        presenter.onSelectPicture(picture);
    }

    @Override
    public void onNoPicture() {
        presenter.onNoPicture();
    }

    @Override
    public void openPictureActivity(Picture picture) {
        PictureActivity.start(getActivity(), picture);
    }
}
