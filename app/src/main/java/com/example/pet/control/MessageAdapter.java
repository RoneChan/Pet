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
import com.example.pet.entity.MyMessage;
import com.example.pet.entity.Pet;

import java.util.ArrayList;

public class MessageAdapter extends BaseAdapter {
    ArrayList<MyMessage> messageArrayList;
    private Context context;

    public MessageAdapter(Context context, ArrayList<MyMessage> messageArrayList) {
        this.messageArrayList = messageArrayList;
        this.context = context;
    }
    @Override
    public int getCount() {
        return messageArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return messageArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageViewHolder viewHolder;
        if (convertView == null) {
            //若是第一次创建
            convertView = LayoutInflater.from(context).inflate(R.layout.message_item, parent, false);
            viewHolder = new MessageViewHolder();
            viewHolder.img=convertView.findViewById(R.id.iv_message_img);
            viewHolder.name=convertView.findViewById(R.id.tv_message_name);
            viewHolder.message=convertView.findViewById(R.id.tv_message_content);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (MessageViewHolder) convertView.getTag();
        }

        MyMessage message=messageArrayList.get(position);
        viewHolder.img.setImageBitmap(message.getImg());
        viewHolder.name.setText(message.getName());
        String msg = message.getMessages().get(0);
        if(msg.length()>20){
            viewHolder.message.setText(msg.substring(0,12)+"...");
        }else{
            viewHolder.message.setText(msg);
        }

        return null;
    }

    class MessageViewHolder{
        ImageView img;
        TextView name,message;
    }
}
