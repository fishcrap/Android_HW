package com.example.chapter3.homework;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chapter3.homework.recycler.LinearItemDecoration;
import com.example.chapter3.homework.recycler.MyAdapter;
import com.example.chapter3.homework.recycler.TestData;
import com.example.chapter3.homework.recycler.TestDataSet;

public class RecyclerViewFragment extends Fragment {

    private static final String TAG = "TAG";

    private RecyclerView recyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private GridLayoutManager gridLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO ex3-3: 修改 fragment_placeholder，添加 loading 控件和列表视图控件

        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);

        recyclerView = view.findViewById(R.id.recycler);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(view.getContext());
        gridLayoutManager = new GridLayoutManager(view.getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new MyAdapter(TestDataSet.getData());
        mAdapter.setOnItemClickListener((MyAdapter.IOnItemClickListener)getActivity());
        recyclerView.setAdapter(mAdapter);
        LinearItemDecoration itemDecoration = new LinearItemDecoration(Color.WHITE);
        recyclerView.addItemDecoration(itemDecoration);
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
//        DefaultItemAnimator animator = new DefaultItemAnimator();
//        animator.setAddDuration(3000);
//        recyclerView.setItemAnimator(animator);
        return view;
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        getView().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // 这里会在 5s 后执行
//                // TODO ex3-4：实现动画，将 lottie 控件淡出，列表数据淡入
//                ObjectAnimator animator_lottie = ObjectAnimator
//                        .ofFloat(getView().findViewById(R.id.animation_view_3), "alpha", 1f, 0f);
//                animator_lottie.setDuration(1000L);
//
//                ObjectAnimator animator_list = ObjectAnimator
//                        .ofFloat(getView().findViewById(R.id.listview), "alpha",0f, 1f);
//                animator_list.setDuration(1000L);
//
//                AnimatorSet animatorSet = new AnimatorSet();
//                animatorSet.playTogether(animator_lottie, animator_list);
//                animatorSet.start();
//            }
//        }, 5000);
//    }


}
