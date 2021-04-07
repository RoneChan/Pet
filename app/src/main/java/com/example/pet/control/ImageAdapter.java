package com.example.pet.control;

import android.content.Context;
import android.net.Uri;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pet.R;
import com.example.pet.entity.ImageBean;
import com.youth.banner.adapter.BannerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageAdapter extends BaseAdapter {
    ArrayList<String> imageList;
    LayoutInflater mInflater;
    Context mContext;
    SparseBooleanArray mSparseBooleanArray;
    int count=0;
    Map<Integer,String> url=new HashMap<>();
    public ArrayList<String> getChooseUri(){
        ArrayList<String> chooseUri=new ArrayList<>();
        for (Map.Entry<Integer, String> entry : url.entrySet()) {
            chooseUri.add(entry.getValue());
        }
       return chooseUri;
    }

    public ImageAdapter(Context context, ArrayList<String> imageList) {
        // TODO Auto-generated constructor stub
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mSparseBooleanArray = new SparseBooleanArray();
        this.imageList = imageList;
    }

    public ArrayList<String> getCheckedItems() {
        ArrayList<String> mTempArry = new ArrayList<String>();

        for (int i = 0; i < imageList.size(); i++) {
            if (mSparseBooleanArray.get(i)) {
                mTempArry.add(imageList.get(i));
            }
        }
        return mTempArry;
    }


        @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.choose_pic_item, null);
        }
        CheckBox mCheckBox = (CheckBox) convertView.findViewById(R.id.checkBox1);
        final ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView1);

        imageView.setImageURI(Uri.parse(imageList.get(position)));

        mCheckBox.setTag(position);
        mCheckBox.setChecked(mSparseBooleanArray.get(position));
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(count < 4 || (count==5 && isChecked==false)){
                   if(isChecked){
                       count++;
                       url.put(position,imageList.get(position));
                   }else{
                       count--;
                       url.remove(position);
                   }
                   mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
               }else{
                   Toast.makeText(mContext, "最多选择4张图片", Toast.LENGTH_SHORT).show();
                   mCheckBox.setChecked(false);
               }
           }
       });
        return convertView;
    }
}
