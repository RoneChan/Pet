package com.example.pet.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.pet.R;
import com.example.pet.ui.issue.IssueActivity;

public class MineFragment extends Fragment {
    ConstraintLayout cl_issue;
    ConstraintLayout cl_collect;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.colorThem)); //设置状态颜色

        cl_issue=view.findViewById(R.id.cl_issue);
        cl_issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),IssueActivity.class);
                intent.putExtra("titleFlag",1);
                startActivity(intent);
            }
        });

        cl_collect=view.findViewById(R.id.cl_collect);
        cl_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),IssueActivity.class);
                intent.putExtra("titleFlag",0);
                startActivity(intent);
            }
        });


        return view;
    }
}

