package com.example.pet.ui.detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.pet.MainActivity;
import com.example.pet.R;
import com.example.pet.control.ImageAdapter;
import com.example.pet.entity.ImageBean;
import com.example.pet.entity.PetDetail;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;

import java.util.ArrayList;


//https://blog.csdn.net/u013144863/article/details/52958669?utm_medium=distribute.pc_relevant.none-task-blog-baidujs_utm_term-0&spm=1001.2101.3001.4242
public class DetailActivity extends AppCompatActivity {

    //private static final String Tag = testActivity.class.getSimpleName();
    private ConstraintLayout cl_back_home,cl_collection;
    private Button btn_apply;
    private ImageView iv_collection;
    private int flag_collection;
    PetDetail petDetail;
    int flag;
    Banner banner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent=getIntent();
        flag = intent.getIntExtra("flag",-1);
        String id=intent.getStringExtra("id");
        btn_apply=findViewById(R.id.btn_detail_apply);
        /*
        if(flag==0){//从home跳转

        }else{//从发布跳转
            btn_apply.setVisibility(View.INVISIBLE);

        }

         */

        petDetail=new PetDetail();
        petDetail.setFlagCollection(0);

        ArrayList<ImageBean> imageBeans=new ArrayList<>();
        imageBeans.add(new ImageBean(R.drawable.test1));
        imageBeans.add(new ImageBean(R.drawable.test2));
        imageBeans.add(new ImageBean(R.drawable.test3));
        banner=findViewById(R.id.banner);
        banner.setAdapter(new ImageAdapter(imageBeans));
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
        flag_collection = petDetail.getFlagCollection();
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