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

    @BindView(R.id.progress_bar)
    View progressBar;

    @BindView(R.id.list_links)
    RecyclerView listLinks;

    private Unbinder unbinder;
    private LinksAdapter adapter;
    private LinearLayoutManager layoutManager;

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
        listLinks.addOnScrollListener(new RecyclerView.OnScrollListener() {

            static final int THRESHOLD = 5;

            int firstVisibleItem;
            int visibleItemCount;
            int totalItemCount;
            int previousTotal;
            boolean loading = true;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = listLinks.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if (totalItemCount > previousTotal) {
                            loading = false;
                            previousTotal = totalItemCount;
                        }
                    }
                    if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + THRESHOLD)) {
                        presenter.loadMore();

                        loading = true;
                    }
                }
            }
        });

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
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError(int errorResId) {
        Toast.makeText(getContext(), errorResId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showData(List<RedditLink> links) {
        adapter.addData(links);
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
