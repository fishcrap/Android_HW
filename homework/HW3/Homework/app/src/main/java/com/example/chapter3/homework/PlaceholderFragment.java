package com.example.chapter3.homework;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.chapter3.homework.recycler.MyAdapter;

public class PlaceholderFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO ex3-3: 修改 fragment_placeholder，添加 loading 控件和列表视图控件

        View view = inflater.inflate(R.layout.fragment_placeholder, container, false);
        final String[] data = new String[]{"666", "稳啊老哥", "...", "什么鬼"};
        ListView listview = view.findViewById(R.id.listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, data);
        listview.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 这里会在 5s 后执行
                // TODO ex3-4：实现动画，将 lottie 控件淡出，列表数据淡入
                ObjectAnimator animator_lottie = ObjectAnimator
                        .ofFloat(getView().findViewById(R.id.animation_view_3), "alpha", 1f, 0f);
                animator_lottie.setDuration(1000L);

                ObjectAnimator animator_list = ObjectAnimator
                        .ofFloat(getView().findViewById(R.id.listview), "alpha",0f, 1f);
                animator_list.setDuration(1000L);

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(animator_lottie, animator_list);
                animatorSet.start();

            }
        }, 5000);
    }
}
