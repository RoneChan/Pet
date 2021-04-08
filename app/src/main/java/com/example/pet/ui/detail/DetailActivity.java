package com.example.pet.ui.detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pet.MainActivity;
import com.example.pet.R;

import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.example.pet.control.MyImageAdapter;
import com.example.pet.entity.ImageBean;
import com.example.pet.entity.MyImageBean;
import com.example.pet.entity.Pet;
import com.example.pet.jdbc.NetworkSettings;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import cn.jzvd.JzvdStd;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.example.pet.MainActivity.NO_DATA;


//https://blog.csdn.net/u013144863/article/details/52958669?utm_medium=distribute.pc_relevant.none-task-blog-baidujs_utm_term-0&spm=1001.2101.3001.4242
public class DetailActivity extends AppCompatActivity {

    //private static final String Tag = testActivity.class.getSimpleName();
    private ConstraintLayout cl_back_home,cl_collection;
    private Button btn_apply;
    private ImageView iv_collection;
    private int flag_collection=0;
    Banner banner;
    Pet pet;
    TextView tv_title,tv_name,tv_sex,tv_age,tv_expelling,tv_sterilization,tv_vaccine;
    TextView tv_story;
    Handler handler;
    final private int COLLECTED=006;


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
        btn_apply=findViewById(R.id.btn_detail_apply);

        Intent intent=getIntent();
        pet=(Pet) intent.getSerializableExtra("pet");
        int type=intent.getIntExtra("type",-1);

        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+pet.getPhone()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        if(type==1 || type==3){//从home跳转过来
            btn_apply.setVisibility(View.VISIBLE);
        }else if(type==2){
            btn_apply.setVisibility(View.GONE);
        }

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
        QueryCollect(pet.getId());
        JzvdStd jzvdStd = (JzvdStd) findViewById(R.id.jz_video_player);

        String s= pet.getVideo();
        if(!s.equals("null")) {
            jzvdStd.setUp(pet.getVideo()
                    , "", JzvdStd.SCREEN_NORMAL);
            Glide.with(this).load(pet.getUrl1()).into(jzvdStd.posterImageView);
        }else{
            jzvdStd.setVisibility(View.GONE);
        }



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

        handler =new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case COLLECTED:
                        flag_collection=1;
                        iv_collection.setImageResource(R.drawable.ic_collect_selected);
                        break;
                }
            }
        };

        iv_collection= findViewById(R.id.iv_detail_collection);
        cl_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag_collection==0){
                    //收藏逻辑，更新数据库
                    //未收藏->收藏
                    ChangeCollect(1);
                    flag_collection=1;
                    //改变图标
                    iv_collection.setImageResource(R.drawable.ic_collect_selected);
                }else {
                    //收藏逻辑，更新数据库
                    //未实现
                    flag_collection=0;
                    //收藏->未收藏
                    ChangeCollect(0);
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

    private void ChangeCollect(int type){
        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();
        MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
        Request request = null;
        try {
            request = new Request.Builder().url(NetworkSettings.CHANGE_COLLECT + "/?collectFlag=" + type+"&userId=u"+MainActivity.userId+"&issueId="+pet.getId()).put(
                    RequestBody.create(
                            mapper.writeValueAsString(pet), mediaType
                    )
            ).build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                String str = e.getMessage();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                ResponseBody requestBody = response.body();
                try {
                    String str = requestBody.string();
                    int flag = Integer.parseInt(str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void QueryCollect(String issueId){
        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();
        MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
        Request request = null;
        try {
            request = new Request.Builder().url(NetworkSettings.QUERY_COLLECT + "/?userId=u"+MainActivity.userId+"&issueId="+issueId).put(
                    RequestBody.create(
                            mapper.writeValueAsString(pet), mediaType
                    )
            ).build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                String str = e.getMessage();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                ResponseBody requestBody = response.body();
                try {
                    String str = requestBody.string();
                    int flag = Integer.parseInt(str);
                    if(flag!=0){
                        Message msg= new Message();
                        msg.what=COLLECTED;
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });
    }
}