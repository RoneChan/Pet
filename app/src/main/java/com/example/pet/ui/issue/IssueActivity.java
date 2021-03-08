package com.example.pet.ui.issue;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.pet.R;
import com.example.pet.control.IssueAdapter;
import com.example.pet.entity.Pet;

import java.util.ArrayList;

public class IssueActivity extends AppCompatActivity {
    ListView lv_issues;
    ArrayList<Pet> petArrayList;
    IssueAdapter issueAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue);

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

        lv_issues=findViewById(R.id.lv_issue);

        issueAdapter=new IssueAdapter(this,petArrayList);
        lv_issues.setAdapter(issueAdapter);

    }
}