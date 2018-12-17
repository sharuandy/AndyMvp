package com.andy.andymvp.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andy.andymvp.DashBoardActivity;
import com.andy.andymvp.R;
import com.andy.andymvp.RvListAdapter;
import com.andy.andymvp.data.ResponseData;
import com.andy.andymvp.interfaces.MainView;
import com.andy.andymvp.presenter.MainPresenterImpl;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, MainView {

    @BindView(R.id.srl_list)
    SwipeRefreshLayout srlList;

    @BindView(R.id.rv_list)
    RecyclerView rvList;

    @BindView(R.id.tv_info)
    TextView tvInfo;

    private RvListAdapter rvListAdapter;
    LinearLayoutManager linearLayoutManager;

    private MainPresenterImpl mMainPresenter;

    private boolean wasLoadingState = false;
    private boolean wasRestoringState = false;
    private Parcelable savedRecyclerLayoutState;


    private final String LOADING_TAG = "DATA_LOADING";
    private final String CONTENT_TAG = "DATA_CONTENT";
    private final String STATE_TAG = "DATA_LAYOUT_MANAGER_STATE";


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycler, container, false);

        //Inintializes ButterKnife and binds the views to fragment
        ButterKnife.bind(this, rootView);

        //Configuring Swiperefresh progress colors
        srlList.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        linearLayoutManager = new LinearLayoutManager(getContext());
        rvList.setLayoutManager(linearLayoutManager);
        srlList.setOnRefreshListener(this);


        mMainPresenter = new MainPresenterImpl(this);

        if (wasLoadingState) {
            // it was loading already so restart fetching anyway
            mMainPresenter.getDataForList(getContext(), false);
        } else {
            // it was not loading now it wither restores cached data or fetch from network
            mMainPresenter.getDataForList(getContext(), wasRestoringState);
        }

        if (savedInstanceState != null) {
            wasLoadingState = savedInstanceState.getBoolean(LOADING_TAG, false);
            wasRestoringState = savedInstanceState.getBoolean(CONTENT_TAG, false);
            savedRecyclerLayoutState = savedInstanceState.getParcelable(STATE_TAG);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        boolean isRestoringVal = false;
        boolean isLoadingState = false;


        if (savedInstanceState != null) {
            isRestoringVal = savedInstanceState.getBoolean(CONTENT_TAG, false);
            isLoadingState = savedInstanceState.getBoolean(LOADING_TAG, false);

            wasLoadingState = savedInstanceState.getBoolean(LOADING_TAG, false);
            wasRestoringState = savedInstanceState.getBoolean(CONTENT_TAG, false);
            savedRecyclerLayoutState = savedInstanceState.getParcelable(STATE_TAG);
        }
        if (isLoadingState) {
            // it was loading already so restart fetching anyway
            mMainPresenter.getDataForList(getContext(), false);
        } else {
            // it was not loading then, now it whether restores cached data or fetch from network
            mMainPresenter.getDataForList(getContext(), isRestoringVal);
        }

        super.onActivityCreated(savedInstanceState);
        // Configure the Swipe refreshing colors

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (rvListAdapter != null && rvListAdapter.getItemCount() != 0) {
            // for data restoring purpose
            outState.putBoolean(CONTENT_TAG, true);
        } else {
            outState.putBoolean(CONTENT_TAG, false);
        }

        if (srlList != null && srlList.isRefreshing()) {
            // saving the loading state
            outState.putBoolean(LOADING_TAG, true);
        } else {
            outState.putBoolean(LOADING_TAG, false);
        }

        outState.putParcelable(STATE_TAG, linearLayoutManager.onSaveInstanceState());

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRefresh() {
        mMainPresenter.getDataForList(getContext(), false);
    }

    @Override
    public void onGetDataSuccess(String message, ResponseData responseData) {
        srlList.setVisibility(View.VISIBLE);
        tvInfo.setVisibility(View.GONE);

        ((DashBoardActivity) getActivity()).setActionBarTitle(responseData.getTitle());

        rvListAdapter = new RvListAdapter(getContext(), responseData.getRows());
        rvList.setAdapter(rvListAdapter);

        if (savedRecyclerLayoutState != null) {
            rvList.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);

        }
        savedRecyclerLayoutState = null;
    }

    @Override
    public void onGetDataFailure(String message) {
        srlList.setVisibility(View.GONE);
        tvInfo.setVisibility(View.VISIBLE);
        tvInfo.setText(message);
    }

    @Override
    public void showProgress() {
        hideProgress();
        srlList.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        if (srlList != null && srlList.isRefreshing()) {
            srlList.setRefreshing(false);
        }
    }

    @Override
    public void onStop() {
        mMainPresenter.onDestroy();
        super.onStop();
    }
}
