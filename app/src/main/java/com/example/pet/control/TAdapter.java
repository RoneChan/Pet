package com.example.pet.control;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pet.R;
import com.example.pet.entity.Pet;
import com.example.pet.ui.detail.DetailActivity;

import java.util.ArrayList;


public class TAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context mContext;
    ArrayList<Pet> pets;
    int loadMoreFlag=1;

    public TAdapter(Context context, ArrayList<Pet> list) {
        mContext = context;
        pets = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         if(viewType==0){
                //item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.issue_home_item,parent,false);
                ViewHolder viewHolder = new ViewHolder(view);
                return viewHolder;
            }else {
                //底部“加载更多”item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.loading_item, parent, false);
                FooterHolder footerHolder = new FooterHolder(view);
                return footerHolder;
            }
        }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            //你的item
            //展示数据
            Pet pet=pets.get(position);
            String name = pet.getName();

            int age = 1;
            String story = "sdfsfdfssfd";
            int expelling=1;
            int sterillization = 1;
            int vaccine=1;

            holder.itemView.setTag(position);
            ((ViewHolder) holder).name.setText(name);
            ((ViewHolder) holder).age.setText(age+"");
            if(story.length()>12){
                ((ViewHolder) holder).story.setText(story.substring(0,12)+"...");
            }else{
                ((ViewHolder) holder).story.setText(story);
            }
            ((ViewHolder) holder).story.setText(story);
            if(expelling==0){
                ((ViewHolder) holder).expelling.setText("未驱虫");
            }else{
                ((ViewHolder) holder).expelling.setText("已驱虫");
            }

            if(sterillization==0){
                ((ViewHolder) holder).sterillization.setText("未绝育");
            }else{
                ((ViewHolder) holder).sterillization.setText("已绝育");
            }

            if(vaccine==0){
                ((ViewHolder) holder).vaccine.setText("未免疫");
            }else{
                ((ViewHolder) holder).vaccine.setText("已免疫");
            }
        }else if(holder instanceof FooterHolder){
            //底部“加载更多”item （等待动画用一个gif去实现）

        }
    }

    @Override
    public int getItemCount() {
        if(pets.size()<10){
            loadMoreFlag=0;
        }
        if(loadMoreFlag==1){
            return pets.size()+1;
        }else{
            return pets.size();
        }

    }

    @Override
    public int getItemViewType(int position) {
        if ( position == pets.size()) {
            //最后一个 是底部item
            return 1;
        } else{
            return 0;
        }
    }

    //提供给外部调用的方法 刷新数据
    public void updateData(ArrayList<Pet> list){
        //再此处理获得的数据  list为传进来的数据
        //... list传进来的数据 添加到mList中
        for(Pet r: list){
            pets.add(r);
        }
        if(list.size()<10){
            loadMoreFlag=0;
        }
        //最后记得刷新item
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView name,age,vaccine,sterillization,expelling,story;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.iv_home_image);
            name = itemView.findViewById(R.id.tv_home_name);
            age = itemView.findViewById(R.id.tv_home_age);
            story = itemView.findViewById(R.id.tv_home_story);
            expelling = itemView.findViewById(R.id.tv_home_expelling);
            sterillization = itemView.findViewById(R.id.tv_home_sterillization);
            vaccine= itemView.findViewById(R.id.tv_home_vaccine);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = (int) v.getTag();
                    String id = pets.get(position).getId();
                    Intent intent=new Intent(mContext, DetailActivity.class);
                    intent.putExtra("id",id);
                    mContext.startActivity(intent);

                }
            });
        }
    }
    //底部"加载更多"item viewholder
    public class FooterHolder extends RecyclerView.ViewHolder {
        TextView ivLoad;

        public FooterHolder(View itemView) {
            super(itemView);
            ivLoad= itemView.findViewById(R.id.tv_loading);
        }
    }


}
