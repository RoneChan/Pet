package com.example.pet.ui.detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pet.MainActivity;
import com.example.pet.R;

import android.view.ViewGroup.LayoutParams;
import com.example.pet.control.MyImageAdapter;
import com.example.pet.entity.ImageBean;
import com.example.pet.entity.MyImageBean;
import com.example.pet.entity.Pet;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;

import java.util.ArrayList;
import java.util.HashMap;

import cn.jzvd.JzvdStd;


//https://blog.csdn.net/u013144863/article/details/52958669?utm_medium=distribute.pc_relevant.none-task-blog-baidujs_utm_term-0&spm=1001.2101.3001.4242
public class DetailActivity extends AppCompatActivity {

    //private static final String Tag = testActivity.class.getSimpleName();
    private ConstraintLayout cl_back_home,cl_collection;
    private Button btn_apply;
    private ImageView iv_collection;
    private int flag_collection;
    int flag;
    Banner banner;

    TextView tv_title,tv_name,tv_sex,tv_age,tv_expelling,tv_sterilization,tv_vaccine;
    TextView tv_story;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tv_title=findViewById(R.id.tv_detail_title);
        tv_name=findViewById(R.id.tv_detail_name);
        tv_sex=findViewById(R.id.tv_detail_sex);
        tv_age=findViewById(R.id.tv_detail_age);
        tv_expelling=findViewById(R.id.tv_detail_expelling);
        tv_sterilization=findViewById(R.id.tv_detail_sterilization);
        tv_vaccine=findViewById(R.id.tv_detail_vaccine);
        tv_story=findViewById(R.id.tv_detail_story);

        Intent intent=getIntent();
        Pet pet=(Pet) intent.getSerializableExtra("pet");

        String name = pet.getName();
        String story = pet.getStory();
        int age=pet.getAge();
        int sex=pet.getSex();
        int vaccine = pet.getVaccine();
        int sterillization=pet.getSterillization();
        int expelling = pet.getExpelling();
        ArrayList<MyImageBean> urls=new ArrayList<>();
        if(!pet.getUrl1().equals("null")){
            urls.add(new MyImageBean(pet.getUrl1()));
        }
        if(!pet.getUrl2().equals("null")){
            urls.add(new MyImageBean(pet.getUrl2()));
        }
        if(!pet.getUrl3().equals("null")){
            urls.add(new MyImageBean(pet.getUrl3()));
        }
        if(!pet.getUrl4().equals("null")){
            urls.add(new MyImageBean(pet.getUrl4()));
        }

        tv_name.setText(name);
        tv_title.setText(name+"正在寻找一个温暖的家");
        if(sex==0){
            tv_sex.setText("男生");
        }else if(sex==1){
            tv_sex.setText("女生");
        }else{
            tv_sex.setText("未知");
        }

        tv_age.setText(age+"岁");

        if(expelling==0){
            tv_expelling.setText("未驱虫");
        }else{
            tv_expelling.setText("已驱虫");
        }
        if(sterillization==0){
            tv_sterilization.setText("未绝育");
        }else{
            tv_sterilization.setText("已绝育");
        }
        if(vaccine==0){
            tv_vaccine.setText("未免疫");
        }else{
            tv_vaccine.setText("已免疫");
        }
        tv_story.setText(story);
        JzvdStd jzvdStd = (JzvdStd) findViewById(R.id.jz_video_player);

        String s= pet.getVedio();
        if(!s.equals("null")) {
            jzvdStd.setUp(pet.getVedio()
                    , "", JzvdStd.SCREEN_NORMAL);
            Glide.with(this).load(pet.getUrl1()).into(jzvdStd.posterImageView);
        }else{
            jzvdStd.setVisibility(View.GONE);
        }

        flag = intent.getIntExtra("flag",-1);
        String id=intent.getStringExtra("id");
        btn_apply=findViewById(R.id.btn_detail_apply);
        /*
        if(flag==0){//从home跳转

        }else{//从发布跳转
            btn_apply.setVisibility(View.INVISIBLE);

        }

         */

        ArrayList<ImageBean> imageBeans=new ArrayList<>();
        imageBeans.add(new ImageBean(R.drawable.test1));
        imageBeans.add(new ImageBean(R.drawable.test2));
        imageBeans.add(new ImageBean(R.drawable.test3));
        banner=findViewById(R.id.banner);
        //banner.setAdapter(new ImageAdapter(imageBeans));
        banner.setAdapter(new MyImageAdapter(this,urls));
        banner.isAutoLoop(true);
        //设置指示器， CircleIndicator为已经定义好的类，直接用就好
        banner.setIndicator(new CircleIndicator(this));
        //开始轮播
        banner.start();


        cl_back_home=findViewById(R.id.cl_detail_backhome);
        cl_collection=findViewById(R.id.cl_detail_collection);
        btn_apply=findViewById(R.id.btn_detail_apply);

        cl_back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                intent.putExtra("fragment",0);
                startActivity(intent);
            }
        });

        iv_collection= findViewById(R.id.iv_detail_collection);
        flag_collection = 1;
        cl_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag_collection==0){
                    //收藏逻辑，更新数据库
                    //未实现
                    flag_collection=1;
                    //改变图标
                    iv_collection.setImageResource(R.drawable.ic_collect_selected);
                }else {
                    //收藏逻辑，更新数据库
                    //未实现
                    flag_collection=0;
                    //改变图标
                    iv_collection.setImageResource(R.drawable.ic_collect);
                }
            }
        });

        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //未实现
            }
        });

    }
}