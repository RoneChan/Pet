package com.example.pet.control;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.List;

public class MyPagerAdapter extends androidx.viewpager.widget.PagerAdapter {
    public static final int MAX_SCROLL_VALUE = 9;

    private List<ImageView> mList;
    private Context mContext;
    private LayoutInflater mInflater;
    private boolean isBack;

    public MyPagerAdapter(List<ImageView> mList, Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mList = mList;
    }

    //当要显示的图片进行缓存时，会调用这个方法进行显示图片的初始化
    //我们将要显示的ImageView加入到ViewGroup中
    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, int position) {

      //因为当用户向左滑时position可能出现负值，所以必须进行处理
        //Log.d("Adapter", "instantiateItem: position: " + position);
        View imageView = mList.get(position % mList.size());

        //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
       /* ViewParent viewParent = ret.getParent();
        if (viewParent != null) {
            ViewGroup parent = (ViewGroup) viewParent;
            parent.removeView(ret);
        }
        container.addView(ret);
        ret.setTag(position);
        */

        if(imageView.getParent() == null){
            Log.i("IT_Real", "instantiateItem: 加载");
            container.addView(imageView);
        }else{
            isBack = true;
            Log.i("IT_Real", "instantiateItem: 手动删除了");
            ((ViewGroup) imageView.getParent()).removeView(imageView);
            Log.i("IT_Real", "instantiateItem: 加载了");
            container.addView(imageView);
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("msg", "点击事件");
                int position = (int) v.getTag() % mList.size();
                Toast.makeText(container.getContext(), "text==", Toast.LENGTH_SHORT).show();
            }
        });

        return imageView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
        container.removeView((View) object);
        isBack = false;
    }

    @Override
    public int getCount() {
        int ret=0;
        if(mList.size()>0){
            ret = MAX_SCROLL_VALUE;
        }

        //return ret;
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(View)object;
    }

    public void setData(List<ImageView> list){
        mList=list;
        notifyDataSetChanged();
    }
}


/*
public class MyPagerAdapter extends PagerAdapter {
    public static final int MAX_SCROLL_VALUE = 9;

    private List<ImageView> mImageViews;
    private Context mContext;
    private LayoutInflater mInflater;

    public MyPagerAdapter(List<ImageView> mImageViews, Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mImageViews = mImageViews;
    }
    /**
     * 功能：
     *
     * @param container Viewpager自身
     * @param position  当前实例化页面的位置
     */
    /*
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        Log.e("mes", "instantiateItem" + position);
        ImageView mImageView = mImageViews.get(position % 3);

        ViewParent viewParent = mImageView.getParent();

        if (viewParent != null) {
            ViewGroup parent = (ViewGroup) viewParent;
            parent.removeView(mImageView);
        }
        container.addView(mImageView);
        mImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.e("motionEvent", "" + motionEvent);
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN://手指按下
                        Log.e("setOnTouchListener", "手指按下");
                       // mHandler.removeCallbacksAndMessages(null);
                        break;
                    case MotionEvent.ACTION_MOVE://手指在这个控件上移动
                        Log.e("setOnTouchListener", "手指移动");
                        break;
             /*       case MotionEvent.ACTION_CANCEL://手指在这个控件上移动
                        Log.e("setOnTouchListener", "ACTION_CANCEL手指移开");
                        mHandler.sendEmptyMessageDelayed(0, 4000);
                        break;*/
/*
                    case MotionEvent.ACTION_UP://手指移开
                        Log.e("setOnTouchListener", "手指移开");
                       // mHandler.sendEmptyMessageDelayed(0, 4000);
                        break;
                }
                return false;
            }
        });
        mImageView.setTag(position);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("msg", "点击事件");
                int position = (int) v.getTag() % 3;
            }
        });
        return mImageView;
    }

    /**
     * 释放资源
     *
     * @param container Viewpager自身
     * @param position  释放的位置
     * @param object    释放的界面
     */
    /*
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
        Log.e("msg", "destroyItem" + position);
        container.removeView((View) object);

    }

    /**
     * 功能：得到viewPager总数
     * 无参
     * 返回值：空
     */
    /*
    @Override
    public int getCount() {
        //return imageViews.size();
        return Integer.MAX_VALUE;//为了设置无限轮滑
    }

    /**
     * 比较View和 object是否同一实例
     *
     * @param view
     * @param object
     * @return
     */
    /*
    @Override
    public boolean isViewFromObject(View view, Object object) {
       /* if (view == object) return true;
        else return false;*/
/*
        return view == object;

    }


}*/