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

import com.andy.andymvp.R;
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

    LinearLayoutManager linearLayoutManager;

    private MainPresenterImpl mMainPresenter;

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


        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRefresh() {
        mMainPresenter.getDataForList(getContext(), false);
    }

    @Override
    public void onGetDataSuccess(String message, ResponseData responseData) {

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
