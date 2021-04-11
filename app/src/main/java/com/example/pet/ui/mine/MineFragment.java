package com.example.pet.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.pet.MainActivity;
import com.example.pet.R;
import com.example.pet.ui.issue.IssueActivity;
import com.example.pet.ui.login.LoginActivity;

import java.awt.font.TextAttribute;

public class MineFragment extends Fragment {
    ConstraintLayout cl_issue;
    ConstraintLayout cl_collect;
    TextView tv_userId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.colorThem)); //设置状态颜色

        cl_issue=view.findViewById(R.id.cl_issue);
        tv_userId=view.findViewById(R.id.tv_mine_userid);
        tv_userId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.userId.equals("")) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, 100);
                }
            }
        });

        if(!MainActivity.userId.equals("")){
            tv_userId.setText(MainActivity.userId);
        }

        cl_issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.userId.equals("")){
                    Toast.makeText(getContext(),"请登录",Toast.LENGTH_LONG).show();
                }else {
                    Intent intent = new Intent(getContext(), IssueActivity.class);
                    intent.putExtra("titleFlag", 1);
                    startActivity(intent);
                }
            }
        });

        cl_collect=view.findViewById(R.id.cl_collect);
        cl_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.userId.equals("")){
                    Toast.makeText(getContext(),"请登录",Toast.LENGTH_LONG).show();
                }else {
                    Intent intent = new Intent(getContext(), IssueActivity.class);
                    intent.putExtra("titleFlag", 0);
                    startActivity(intent);
                }
            }
        });


        return view;
    }
}

