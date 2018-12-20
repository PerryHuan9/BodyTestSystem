package com.example.perry.yoursidesystem.fragment.heathfragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.perry.yoursidesystem.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by perry on 2017/12/15.
 */

public class HeathFragment extends Fragment {
    private List<HeathTitle> titleList;
    private RecyclerView recyclerView;
    private HeathAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_heath, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        initList();
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        adapter = new HeathAdapter(titleList, getActivity());
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void initList() {
        titleList = new ArrayList<>();
        titleList.add(new HeathTitle(R.string.hTitle1, R.drawable.h1_1, R
                .drawable.h1_2, R.drawable.h1_3));
        titleList.add(new HeathTitle(R.string.hTitle2, R.drawable.h2_1, R
                .drawable.h2_2, R.drawable.h2_3));
        titleList.add(new HeathTitle(R.string.hTitle3, R.drawable.h3_1, R
                .drawable.h3_2, R.drawable.h3_3));
        titleList.add(new HeathTitle(R.string.hTitle4, R.drawable.h4_1, R
                .drawable.h4_2, R.drawable.h4_3));
        titleList.add(new HeathTitle(R.string.hTitle5, R.drawable.h5_1, R
                .drawable.h5_2, R.drawable.h5_3));
        titleList.add(new HeathTitle(R.string.hTitle6, R.drawable.h1_1, R
                .drawable.h6_2, R.drawable.h6_3));
        titleList.add(new HeathTitle(R.string.hTitle7, R.drawable.h7_1, R
                .drawable.h7_2, R.drawable.h7_3));
        titleList.add(new HeathTitle(R.string.hTitle8, R.drawable.h8_1, R
                .drawable.h8_2, R.drawable.h8_3));
        titleList.add(new HeathTitle(R.string.hTitle9, R.drawable.h9_1, R
                .drawable.h9_2, R.drawable.h9_3));
        titleList.add(new HeathTitle(R.string.hTitle10, R.drawable.h10_1, R
                .drawable.h10_2, R.drawable.h10_3));
        titleList.add(new HeathTitle(R.string.hTitle11, R.drawable.h11_1, R
                .drawable.h11_2, R.drawable.h11_3));
        titleList.add(new HeathTitle(R.string.hTitle12, R.drawable.h12_1, R
                .drawable.h12_2, R.drawable.h12_3));
        titleList.add(new HeathTitle(R.string.hTitle13, R.drawable.h13_1, R
                .drawable.h13_2, R.drawable.h13_3));
        titleList.add(new HeathTitle(R.string.hTitle14, R.drawable.h14_1, R
                .drawable.h14_2, R.drawable.h14_3));
        titleList.add(new HeathTitle(R.string.hTitle15, R.drawable.h15_1, R
                .drawable.h15_2, R.drawable.h15_3));
        titleList.add(new HeathTitle(R.string.hTitle16, R.drawable.h16_1, R
                .drawable.h16_2, R.drawable.h16_3));
        titleList.add(new HeathTitle(R.string.hTitle17, R.drawable.h17_1, R
                .drawable.h17_2, R.drawable.h17_3));
        titleList.add(new HeathTitle(R.string.hTitle18, R.drawable.h18_1, R
                .drawable.h18_2, R.drawable.h18_3));


    }


}
