package com.example.pet.control;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.pet.R;
import com.example.pet.entity.Pet;
import com.example.pet.entity.Video;
import com.example.pet.jdbc.NetworkSettings;
import com.example.pet.ui.detail.DetailActivity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JzvdStd;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.example.pet.ui.home.HomeFragment.resloverUrl;

public class ListVideoAdapter extends RecyclerView.Adapter {
    Context mContext;
    List<String> urlList = new ArrayList<String>();
    List<Video> list = new ArrayList<>();
    static final int GETET=002;
    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GETET:
                    Intent intent=new Intent(mContext, DetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("pet",pet);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    break;
            }
        }
    };
    Pet pet = new Pet();

    public ListVideoAdapter(Context mContext,List<Video> list) {
        this.mContext=mContext;
        this.list = list;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vedio_item, parent, false);
        VideoViewHolder viewHolder = new VideoViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Video video = list.get(position);
        ((VideoViewHolder) holder).mp_video.setUp(video.getVideo(), video.getName());
        Glide.with(mContext).setDefaultRequestOptions(
                new RequestOptions()
                        .frame(1000000)
                        .centerCrop()
        ).load(video.getVideo()).into(((VideoViewHolder) holder).mp_video.posterImageView);
        ((VideoViewHolder) holder).tv_name.setText(video.getName());
        ((VideoViewHolder) holder).tv_age.setText(video.getAge() + "岁");
        Glide.with(mContext).load(video.getUrl()).into(((VideoViewHolder) holder).iv_pic);
        ((VideoViewHolder) holder).cl_pet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //请求该id详细信息
                getPet(video.getId());
            }
        });
        // ((VideoViewHolder) holder).mp_video.setUp(urlList.get(position),name);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        public View rootView;
        public JzvdStd mp_video;
        public TextView tv_name, tv_age;
        public ImageView iv_pic;
        public ConstraintLayout cl_pet;

        public VideoViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            this.mp_video = rootView.findViewById(R.id.jz_video_player);
            this.tv_name = rootView.findViewById(R.id.tv_video_name);
            this.tv_age = rootView.findViewById(R.id.tv_video_age);
            this.iv_pic = rootView.findViewById(R.id.iv_video_image);
            this.cl_pet = rootView.findViewById(R.id.cl_video_pet);
        }
    }

    public void getPet(String id) {
        final OkHttpClient client = new OkHttpClient();
        final ObjectMapper mapper = new ObjectMapper();
        final MediaType mediaType = MediaType.parse("application/json;charset=utf-8");

        Request request = null;
        try {
            request = new Request.Builder().url(NetworkSettings.GET_BY_ID + "/?id=" + id).put(
                    RequestBody.create(
                            mapper.writeValueAsString(id), mediaType
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
                JSONObject object = null;
                try {
                    object = new JSONObject(requestBody.string());
                    pet.setId(object.getString("id"));
                    pet.setName(object.getString("name"));
                    pet.setStory(object.getString("story"));
                    pet.setAge(object.getInt("age"));
                    pet.setSex(object.getInt("sex"));
                    pet.setVaccine(object.getInt("vaccine"));
                    pet.setSterillization(object.getInt("sterillization"));
                    pet.setExpelling(object.getInt("expelling"));
                    pet.setUrl1(resloverUrl(object.getString("url1")));
                    pet.setUrl2(resloverUrl(object.getString("url2")));
                    pet.setUrl3(resloverUrl(object.getString("url3")));
                    pet.setUrl4(resloverUrl(object.getString("url4")));
                    pet.setVideo(resloverUrl(object.getString("video")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.what = GETET;
                handler.sendMessage(msg);
            }
        });
    }
}


