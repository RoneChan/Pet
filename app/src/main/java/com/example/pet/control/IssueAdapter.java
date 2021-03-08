package com.example.pet.control;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pet.R;
import com.example.pet.entity.Pet;

import java.util.ArrayList;

public class IssueAdapter extends BaseAdapter {
    ArrayList<Pet> pets;
    private Context context;

    public IssueAdapter(Context context, ArrayList<Pet> pets) {
        this.pets = pets;
        this.context = context;
    }

    @Override
    public int getCount() {
        return pets.size();
    }

    @Override
    public Object getItem(int position) {
        return pets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PetViewHolder viewHolder;
        if (convertView == null) {
            //若是第一次创建
            convertView = LayoutInflater.from(context).inflate(R.layout.issue_item, parent, false);
            viewHolder = new PetViewHolder();
            viewHolder.name = convertView.findViewById(R.id.tv_issue_name);
            viewHolder.age = convertView.findViewById(R.id.tv_issue_age);
            viewHolder.sex = convertView.findViewById(R.id.tv_issue_sex);
            viewHolder.img = convertView.findViewById(R.id.iv_issue);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PetViewHolder) convertView.getTag();
        }

        String name = pets.get(position).getName();
        int age = pets.get(position).getAge();
        int sex = pets.get(position).getSex();
//        Image image = pets.get(position).getImage(0);

        //viewHolder.img.setImageMatrix();
        return convertView;
    }

    //存放复用的组件
    class PetViewHolder{
        ImageView img;
        TextView name,age,sex;

    }
}
