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
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.bumptech.glide.request.RequestOptions;
import com.example.pet.MainActivity;
import com.example.pet.R;

import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.example.pet.control.ListVideoAdapter;
import com.example.pet.control.MyImageAdapter;
import com.example.pet.entity.ImageBean;
import com.example.pet.entity.MyImageBean;
import com.example.pet.entity.Pet;
import com.example.pet.jdbc.NetworkSettings;
import com.example.pet.ui.login.LoginActivity;
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
    TextView tv_story,tv_condition;
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
        tv_condition=findViewById(R.id.tv_detail_condition);

        Intent intent=getIntent();
        pet=(Pet) intent.getSerializableExtra("pet");
        int type=intent.getIntExtra("type",-1);

        btn_apply = findViewById(R.id.btn_test);
        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+pet.getPhone()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //Toast.makeText(DetailActivity.this, "sdfsdf",Toast.LENGTH_SHORT);
            }
        });

        if(type==1 || type==3){//???home????????????
            btn_apply.setVisibility(View.VISIBLE);
        }else if(type==2){
            btn_apply.setVisibility(View.GONE);
        }

        String name = pet.getName();
        String story = pet.getStory();
        String condition = pet.getCondition();
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
        tv_title.setText(name+"??????????????????????????????");
        if(sex==0){
            tv_sex.setText("??????");
        }else if(sex==1){
            tv_sex.setText("??????");
        }else{
            tv_sex.setText("??????");
        }

        tv_age.setText(age+"???");

        if(expelling==0){
            tv_expelling.setText("?????????");
        }else{
            tv_expelling.setText("?????????");
        }
        if(sterillization==0){
            tv_sterilization.setText("?????????");
        }else{
            tv_sterilization.setText("?????????");
        }
        if(vaccine==0){
            tv_vaccine.setText("?????????");
        }else{
            tv_vaccine.setText("?????????");
        }
        tv_story.setText(story);
        tv_condition.setText(condition);
        if(!MainActivity.userId.equals("")) {
            QueryCollect(pet.getId());
        }
        JzvdStd jzvdStd = (JzvdStd) findViewById(R.id.jz_video_player);

        String s= pet.getVideo();
        if(!s.equals("null")) {
            jzvdStd.setUp(pet.getVideo()
                    , "", JzvdStd.SCREEN_NORMAL);
            Glide.with(this).setDefaultRequestOptions(
                    new RequestOptions()
                            .frame(1000000)
                            .centerCrop()
            ).asBitmap().load(pet.getVideo()).into(jzvdStd.posterImageView);
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
        //?????????????????? CircleIndicator??????????????????????????????????????????
        banner.setIndicator(new CircleIndicator(this));
        //????????????
        banner.start();


        cl_back_home=findViewById(R.id.cl_detail_backhome);
        cl_collection=findViewById(R.id.cl_detail_collection);
        cl_back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                intent.putExtra("fragment", 0);
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
                if (MainActivity.userId.equals("")) {
                    Toast.makeText(DetailActivity.this, "?????????", Toast.LENGTH_LONG).show();
                } else {
                    if (flag_collection == 0) {
                        //?????????->??????
                        ChangeCollect(1);
                        flag_collection = 1;
                        //????????????
                        iv_collection.setImageResource(R.drawable.ic_collect_selected);
                    } else {
                        flag_collection = 0;
                        //??????->?????????
                        ChangeCollect(0);
                        //????????????
                        iv_collection.setImageResource(R.drawable.ic_collect);
                    }
                }
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
                Log.e("????????????Error???",e.getMessage());
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