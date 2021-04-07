package com.example.pet.ui.message;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;

import com.example.pet.MainActivity;
import com.example.pet.R;
import com.example.pet.entity.Pet;
import com.example.pet.tool.RealPathFromUriUtils;
import com.example.pet.ui.choosePic.ChoosePicActivity;
import com.example.pet.ui.home.CityChooseActivity;
import com.google.android.material.textfield.TextInputEditText;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static com.example.pet.jdbc.NetworkSettings.POST_PET;
import static com.example.pet.jdbc.NetworkSettings.UPLOAD_IMAGE;
import static com.example.pet.ui.home.HomeFragment.CICTY_COOSE;

public class AddFragment extends Fragment {
    public static final int GET_PICTURE=108;
    ConstraintLayout constraintLayout;
    View view;
    EditText tiet_name,tiet_age;
    TextInputEditText tiet_story,tiet_condition;
    Spinner sp_sex,sp_vaccine,sp_expelling,sp_sterillization;
    TextView tv_city;
    Button btn_confirm;
    GridLayout gl_add_imgs;
    Pet pet= new Pet();
    String url1,url2,url3,url4,video;
    int unUploadPicNum,neededUpPicNum;


    static int REQUEST_CODE_IMAGE = 101;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add, container, false);
        constraintLayout = view.findViewById(R.id.cl_add_choose_img);
        tiet_name=view.findViewById(R.id.ev_add_name);
        tiet_age=view.findViewById(R.id.ev_add_age);
        tiet_story=view.findViewById(R.id.tiet_add_story);
        tiet_condition=view.findViewById(R.id.tiet_add_condition);
        sp_sex=view.findViewById(R.id.sp_add_sex);
        sp_vaccine=view.findViewById(R.id.sp_add_vaccine);
        sp_expelling=view.findViewById(R.id.sp_add_expelling);
        sp_sterillization=view.findViewById(R.id.sp_add_sterillization);
        tv_city=view.findViewById(R.id.tv_add_city);
        btn_confirm=view.findViewById(R.id.btn_add_confirm);
        gl_add_imgs=view.findViewById(R.id.gl_add_imgs);

        tv_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), CityChooseActivity.class);
                startActivityForResult(intent,CICTY_COOSE);
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = tiet_name.getText().toString();
                if(name ==null ){
                    Toast.makeText(getContext(),"请输入名字",Toast.LENGTH_SHORT).show();
                }else{
                    pet.setName(name);
                }
                String age = tiet_age.getText().toString();
                if(age ==null){
                    Toast.makeText(getContext(),"请输入年龄",Toast.LENGTH_SHORT).show();
                }else{
                    pet.setAge(Integer.parseInt(age));
                }
                String story = tiet_story.getText().toString();
                if(story ==null){
                    Toast.makeText(getContext(),"请输入年龄",Toast.LENGTH_SHORT).show();
                }else{
                    pet.setStory(story);
                }
                String condition = tiet_condition.getText().toString();
                if(condition ==null){
                    Toast.makeText(getContext(),"请输入领养条件",Toast.LENGTH_SHORT).show();
                }else{
                    pet.setCondition(condition);
                }
                String sex = (String)sp_sex.getSelectedItem();
                if(sex.equals("女")){
                    pet.setSex(1);
                }else{
                    pet.setSex(0);
                }
                String vaccine = (String)sp_vaccine.getSelectedItem();
                if(vaccine.equals("是")){
                    pet.setVaccine(1);
                }else{
                    pet.setVaccine(0);
                }
                String expelling = (String)sp_expelling.getSelectedItem();
                if(expelling.equals("是")){
                    pet.setExpelling(1);
                }else{
                    pet.setExpelling(0);
                }
                String sterillization = (String)sp_sterillization.getSelectedItem();
                if(sterillization.equals("是")){
                    pet.setSterillization(1);
                }else{
                    pet.setSterillization(0);
                }
                pet.setCity(tv_city.getText().toString());
                pet.setPhone(MainActivity.userId);
                if(url1==null){
                    Toast.makeText(getContext(),"请选择图片",Toast.LENGTH_SHORT).show();
                }else{
                    {
                        File file =new File(url1);
                        uploadImage(file);
                    }
                    if(url2!=null){
                        File file =new File(url2);
                        uploadImage(file);
                    }
                    if(url3!=null){
                        File file =new File(url3);
                        uploadImage(file);
                    }
                    if(url4!=null){
                        File file =new File(url4);
                        uploadImage(file);
                    }
                }
                pet.setVideo(video);
            }
        });


        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(view.getContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(view.getContext(),
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {   //权限还没有授予，需要在这里写申请权限的代码
                    // 第二个参数是一个字符串数组，里面是你需要申请的权限 可以设置申请多个权限
                    // 最后一个参数是标志你这次申请的权限，该常量在onRequestPermissionsResult中使用到
                    ActivityCompat.requestPermissions((Activity) view.getContext(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {

                    Intent intent = new Intent(getActivity(), ChoosePicActivity.class);
                    startActivityForResult(intent,1);
/*
                    //权限已经被授予，在这里直接写要执行的相应方法即可
                    Intent imgIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    imgIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    imgIntent.setType("image/*");
                    imgIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    imgIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(imgIntent, "Select Picture"), REQUEST_CODE_IMAGE);
               */
                }

            }
        });
        return view;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_IMAGE) {
                Uri uri = data.getData();
                String path = RealPathFromUriUtils.getRealPathFromUri(getContext(), uri);
                File file = new File(path);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                // 设置边距
                params.setMargins(0,0,4,4);
                params.setGravity(Gravity.FILL);
                int p=dip2px(getContext(),120);
                params.width =p;
                params.height =p;
                ImageView imageView=new ImageView(getContext());
                imageView.setImageURI(Uri.parse(path));
                gl_add_imgs.addView(imageView,0,params);
                //uploadImage(file);

            }
            if(requestCode==CICTY_COOSE){
                String city= data.getExtras().getString("city");
                tv_city.setText(city);
                tv_city.setTextColor(getResources().getColor(R.color.colorCharacter));
            }
        }if(resultCode==GET_PICTURE){
            ArrayList<String> uris=new ArrayList<>();
            url1 = data.getStringExtra("url1");
            uris.add(url1);
            url2 = data.getStringExtra("url2");
            if(url2!=null){
                uris.add(url2);
            }
            url3 = data.getStringExtra("url3");
            if(url3!=null){
                uris.add(url3);
            }
            url4 = data.getStringExtra("url4");
            if(url4!=null){
                uris.add(url4);
            }
            unUploadPicNum = uris.size();
            neededUpPicNum=uris.size();
            for(int i=0;i<uris.size();i++){
                Uri uri = Uri.parse(uris.get(i));
                //String path = RealPathFromUriUtils.getRealPathFromUri(getContext(), uri);
                //File file = new File(path);
                File file =new File(uris.get(i));
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                // 设置边距
                params.setMargins(0,0,4,4);
                params.setGravity(Gravity.FILL);
                int p=dip2px(getContext(),120);
                params.width =p;
                params.height =p;
                ImageView imageView=new ImageView(getContext());
                //imageView.setImageURI(Uri.parse(path));
                imageView.setImageURI(uri);
                gl_add_imgs.addView(imageView,0,params);
            }
            //uploadImage(file);
        }
    }

    public void uploadPet(Pet pet){
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("id",pet.getId());
        builder.add("name",pet.getName());
        builder.add("story",pet.getStory());
        builder.add("age",pet.getAge()+"");
        builder.add("sex",pet.getSex()+"");
        builder.add("vaccine",pet.getVaccine()+"");
        builder.add("sterillization",pet.getSterillization()+"");
        builder.add("expelling",pet.getExpelling()+"");
        builder.add("condition",pet.getCondition());
        builder.add("city",pet.getCity());
        builder.add("url1",pet.getUrl1());
        builder.add("url2",pet.getUrl2());
        builder.add("uel3",pet.getUrl3());
        builder.add("url4",pet.getUrl4());
        builder.add("video",pet.getVideo());
        builder.add("phone",pet.getPhone());
        OkHttpClient httpClient = new OkHttpClient();
        FormBody formBody=builder.build();
        Request request=new Request.Builder().url(POST_PET).post(formBody).build();
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //在这里根据返回内容执行具体的操作
                final String resdata = response.body().string();
                System.out.println(resdata);

            }
        });
    }

    public void uploadImage(File file) {
        OkHttpClient httpClient = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/octet-stream");//设置类型，类型为八位字节流
        RequestBody requestBody = RequestBody.create(mediaType, file);//把文件与类型放入请求体

        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), requestBody)//文件名
                .build();
        Request request = new Request.Builder()
                .url(UPLOAD_IMAGE)
                .post(multipartBody)
                .build();
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //在这里根据返回内容执行具体的操作
                final String resdata = response.body().string();
                switch (neededUpPicNum - unUploadPicNum){
                    case 0:
                        pet.setUrl1(resdata);
                        unUploadPicNum--;
                        break;
                    case 1:
                        pet.setUrl2(resdata);
                        unUploadPicNum--;
                        break;
                    case 2:
                        pet.setUrl3(resdata);
                        unUploadPicNum--;
                        break;
                    case 3:
                        pet.setUrl4(resdata);
                        unUploadPicNum--;
                        break;
                }
                System.out.println(resdata);
            }
        });
    }

    //dp转换为px
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}