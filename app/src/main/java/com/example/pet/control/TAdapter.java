package com.example.pet.control;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pet.MainActivity;
import com.example.pet.R;
import com.example.pet.entity.Pet;
import com.example.pet.jdbc.NetworkSettings;
import com.example.pet.ui.detail.DetailActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class TAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    static  final int ADD_DELETE=1010;
    static  final int COLLECT_DELETE=1011;
    Context mContext;
    ArrayList<Pet> pets;
    int loadMoreFlag=1;
    int flag;

    public void setloadMoreFlag(int flag){
        loadMoreFlag=flag;
    }

    public TAdapter(Context context, ArrayList<Pet> list) {
        mContext = context;
        pets = list;
    }
    public void setArrayList( ArrayList<Pet> list){
        pets = list;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            //item
            View view;
            if (flag == 1) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.issue_home_item, parent, false);
            } else {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.issue_add_item, parent, false);
            }
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        } else if(viewType == 1){
            //????????????????????????item
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.loading_item, parent, false);
            FooterHolder footerHolder = new FooterHolder(view);
            return footerHolder;
        }else{
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.padd_item, parent, false);
            PaddingHolder paddingHolder = new PaddingHolder(view);
            return paddingHolder;
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            //??????item
            //????????????
            Pet pet=pets.get(position);
            String name = pet.getName();
            int age = pet.getAge();
            String story = pet.getStory();
            int expelling=pet.getExpelling();
            int sterillization = pet.getSterillization();
            int vaccine=pet.getVaccine();
            String url1=pet.getUrl1();

            holder.itemView.setTag(position);
            ((ViewHolder) holder).name.setText(name);
            ((ViewHolder) holder).age.setText(age+"???");
            if(story.length()>15){
                ((ViewHolder) holder).story.setText(story.substring(0,15)+"...");
            }else{
                ((ViewHolder) holder).story.setText(story);
            }
            if(expelling==0){
                ((ViewHolder) holder).expelling.setText("?????????");
            }else{
                ((ViewHolder) holder).expelling.setText("?????????");
            }

            if(sterillization==0){
                ((ViewHolder) holder).sterillization.setText("?????????");
            }else{
                ((ViewHolder) holder).sterillization.setText("?????????");
            }

            if(vaccine==0){
                ((ViewHolder) holder).vaccine.setText("?????????");
            }else{
                ((ViewHolder) holder).vaccine.setText("?????????");
            }
            if(flag!=1){
                Handler handler = new Handler(){
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        switch (msg.what){
                            case ADD_DELETE:
                                ((ViewHolder) holder).btn.setText("?????????");
                                ((ViewHolder) holder).btn.setBackground(mContext.getResources().getDrawable(R.drawable.been_delete_button_shape));
                                break;
                            case COLLECT_DELETE:
                                ((ViewHolder) holder).btn.setText("?????????");
                                ((ViewHolder) holder).btn.setBackground(mContext.getResources().getDrawable(R.drawable.been_delete_button_shape));
                                break;
                        }
                    }
                };
                String str = ((ViewHolder) holder).btn.getText().toString();
                ((ViewHolder) holder).btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(str.equals("??????")||str.equals("??????")){
                            Request request = null;
                            try {
                                if(flag == 2) {//??????
                                    request = new Request.Builder().url(NetworkSettings.CHANGE_ISSUE + "/?type=1&userId=u"+MainActivity.userId+"&issueId="+ pet.getId() ).get().build();
                                }else if(flag==3){//??????
                                    request = new Request.Builder().url(NetworkSettings.CHANGE_ISSUE + "/?type=0&userId=u"+MainActivity.userId+"&issueId="+ pet.getId() ).get().build();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            OkHttpClient client = new OkHttpClient();
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                    String str = e.getMessage();
                                }
                                @Override
                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                    ResponseBody requestBody = response.body();
                                    if(flag==2){//??????
                                        Message msg=new Message();
                                        msg.what=ADD_DELETE;
                                        handler.sendMessage(msg);
                                    }else{
                                        Message msg=new Message();
                                        msg.what=COLLECT_DELETE;
                                        handler.sendMessage(msg);
                                    }
                                }
                            });
                        }
                    }
                });
            }
            Glide.with(mContext).load(url1).into(((ViewHolder) holder).img);
           // ((ViewHolder) holder).img.setImageURI(Uri.parse(url1));
        }else if(holder instanceof FooterHolder){
            //????????????????????????item ????????????????????????gif????????????

        }
    }

    @Override
    public int getItemCount() {
        return pets.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == pets.size()) {
            //???????????? ?????????item
            if(loadMoreFlag==1){
                return 1;
            }else {
                return 3;
            }
        } else{
            return 0;
        }
    }

    //?????????????????????????????? ????????????
    public void updateData(ArrayList<Pet> list){
        //???????????????????????????  list?????????????????????
        //... list?????????????????? ?????????mList???
        for(Pet r: list){
            pets.add(r);
        }
        if(list.size()<10){
            loadMoreFlag=0;
        }
        //??????????????????item
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView name,age,vaccine,sterillization,expelling,story;
        Button btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.iv_home_image);
            name = itemView.findViewById(R.id.tv_home_name);
            age = itemView.findViewById(R.id.tv_home_age);
            story = itemView.findViewById(R.id.tv_home_story);
            expelling = itemView.findViewById(R.id.tv_home_expelling);
            sterillization = itemView.findViewById(R.id.tv_home_sterillization);
            vaccine= itemView.findViewById(R.id.tv_home_vaccine);
            if(flag!=1){
                btn=itemView.findViewById(R.id.btn_issue_delete);
                if (flag == 3) {
                    btn.setText("??????");
                }
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = (int) v.getTag();
                    Pet pet = pets.get(position);
                    Intent intent=new Intent(mContext, DetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("pet",pet);
                    intent.putExtras(bundle);
                    intent.putExtra("type",flag);
                    mContext.startActivity(intent);

                }
            });
        }
    }
    //??????"????????????"item viewholder
    public class FooterHolder extends RecyclerView.ViewHolder {
        TextView ivLoad;
        public FooterHolder(View itemView) {
            super(itemView);
            ivLoad= itemView.findViewById(R.id.tv_loading);
        }
    }

    //??????pading item viewholder
    public class PaddingHolder extends RecyclerView.ViewHolder {
        public PaddingHolder(View itemView) {
            super(itemView);
        }
    }


}
