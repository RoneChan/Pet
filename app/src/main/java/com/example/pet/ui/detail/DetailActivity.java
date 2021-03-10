package com.example.pet.ui.detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pet.R;
import com.example.pet.control.MyPagerAdapter;

import java.util.ArrayList;
//https://blog.csdn.net/u013144863/article/details/52958669?utm_medium=distribute.pc_relevant.none-task-blog-baidujs_utm_term-0&spm=1001.2101.3001.4242
public class DetailActivity extends AppCompatActivity {

    private static final String Tag = testActivity.class.getSimpleName();
    private ViewPager BannerViegPager;//轮播图Viewpager
    private TextView BannerTittle;//轮播图标题
    private LinearLayout BannerPointLayout;
    private ArrayList<ImageView> mImageViews;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int item = (BannerViegPager.getCurrentItem() + 1);
            BannerViegPager.setCurrentItem(item);
            mHandler.sendEmptyMessageDelayed(0, 4000);
        }
    };

    private final int[] imageIds = {
            R.drawable.test1,
            R.drawable.test2,
            R.drawable.test3,
    };
    private final String[] textStrings = {
            "今夏最美",
            "欢度元宵",
            "情人节",
    };
    /*上次高亮显示的位置*/
    private int prePosition = 0;
    private boolean isDragging = false;//是否已经滚动

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //ViewPager  使用
        //1在布局中定义ViewPager
        //2在代码中实例化ViewPager
        //3准备数据
        //4设置适配器（pageradapter）-item布局绑定数据
        //图片资源id
        BannerViegPager = findViewById(R.id.vp_pictures);
        BannerTittle = findViewById(R.id.myTittle);
        BannerPointLayout = (LinearLayout) findViewById(R.id.ll_live_indicator);
        mImageViews = new ArrayList<>();
        for (int i = 0; i < imageIds.length; i++) {
            ImageView mImageView = new ImageView(this);
            mImageView.setImageResource(imageIds[i]);
            mImageViews.add(mImageView);
            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.indicator_select);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);

            if (i == 0) {
                point.setEnabled(true);//显示红色
            } else {
                point.setEnabled(false);//显示灰色
                layoutParams.leftMargin = 20;
            }
            point.setLayoutParams(layoutParams);
            BannerPointLayout.addView(point);
        }
        //BannerViegPager.setAdapter(new MePagerAdapter());
        BannerViegPager.setAdapter(new MyPagerAdapter(mImageViews,this));
        BannerTittle.setText(textStrings[prePosition]);
        //设置viewpager页面的改变
        BannerViegPager.addOnPageChangeListener(new MyOnPageChangeLister());
        //设置中间位置
        BannerViegPager.setCurrentItem(Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2) % mImageViews.size());
        mHandler.sendEmptyMessageDelayed(0, 3000);
    }

    class MyOnPageChangeLister implements ViewPager.OnPageChangeListener {
        /**
         * 功能：当页面滚动时回掉这个方法
         *
         * @param position             当前页面位置
         * @param positionOffset       滑动页面的百分比
         * @param positionOffsetPixels 在屏幕上滑动的像数
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * 功能：当页面选中停留时回掉这个方法
         *
         * @param position 当前页面位置
         *                 返回值：空
         */
        @Override
        public void onPageSelected(int position) {
            Log.i("textStrings", "position==" + position);
            Log.i("textStrings", "position%" + "%" + imageIds.length + "==" + position % imageIds.length);
            //设置对应页面的文本信息
            BannerTittle.setText(textStrings[position % imageIds.length]);
            //把上一个高亮设置默认灰色
            BannerPointLayout.getChildAt(prePosition).setEnabled(false);
            //把当前设置为高亮红色
            BannerPointLayout.getChildAt(position % imageIds.length).setEnabled(true);
            prePosition = position % imageIds.length;

        }

        /**
         * 功能：当页面滚动状态变化的时候回掉这个方法
         * 静止->滑动
         * 滑动->静止
         * 静止->拖拽
         *
         * @param state 当前页面位置
         *              返回值：空
         */
        @Override
        public void onPageScrollStateChanged(int state) {
       /* SCROLL_STATE_IDLE：空闲状态
        SCROLL_STATE_DRAGGING：滑动状态
        SCROLL_STATE_SETTLING：滑动后自然沉降的状态*/
            if (ViewPager.SCROLL_STATE_DRAGGING == state) {
                isDragging = true;
                mHandler.removeCallbacksAndMessages(null);
                Log.i(Tag, "SCROLL_STATE_DRAGGING-----------");
            } else if (ViewPager.SCROLL_STATE_IDLE == state) {
                Log.i(Tag, "SCROLL_STATE_IDLE-----------");
            } else if (ViewPager.SCROLL_STATE_SETTLING == state && isDragging) {
                isDragging = false;
                mHandler.removeCallbacksAndMessages(null);
                mHandler.sendEmptyMessageDelayed(0, 4000);
                Log.i(Tag, "SCROLL_STATE_SETTLING------------");
            }
        }
    }

}