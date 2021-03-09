package com.example.pet.ui.issue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.pet.R;
import com.example.pet.control.IssueAdapter;
import com.example.pet.entity.Pet;
import com.example.pet.ui.detail.DetailActivity;
import com.example.pet.ui.detail.testActivity;

import java.util.ArrayList;

public class IssueActivity extends AppCompatActivity {
    ListView lv_issues;
    ImageView imageView;
    ArrayList<Pet> petArrayList;
    IssueAdapter issueAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorThem)); //设置状态颜色

        imageView=findViewById(R.id.iv_issue_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("msg","我的发布activity结束");
                finish(); //结束activiy
            }
        });

        Pet pet = new Pet();
        pet.setAge(2);
        pet.setSex(0);
        pet.setExpelling(1);
        pet.setImages(null);
        pet.setName("旺财");
        pet.setStory("流浪狗");
        pet.setSterillization(1);
        pet.setVaccine(1);

        petArrayList=new ArrayList<Pet>();
        petArrayList.add(pet);
        petArrayList.add(pet);

        lv_issues=findViewById(R.id.lv_issue);

        issueAdapter=new IssueAdapter(this,petArrayList);
        lv_issues.setAdapter(issueAdapter);

        lv_issues.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                //Intent intent = new Intent(getApplicationContext(), testActivity.class);
                intent.putExtra("id",petArrayList.get(position).getId());
                startActivity(intent);
            }
        });

    }
}