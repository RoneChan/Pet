package com.example.pet.ui.issue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pet.R;
import com.example.pet.control.EndlessRecyclerOnScrollListener;
import com.example.pet.control.TAdapter;
import com.example.pet.entity.Pet;

import java.util.ArrayList;

public class IssueActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TAdapter tAdapter;
    ImageView imageView;
    ArrayList<Pet> petArrayList;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue);

        title=findViewById(R.id.tx_issue_title);
        int titleFlag=getIntent().getIntExtra("titleFlag",-1);
        if(titleFlag==1){
            title.setText("我发布的");
        }else if(titleFlag==0){
            title.setText("我收藏的");
        }
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorThem)); //设置状态颜色

        imageView=findViewById(R.id.iv_issue_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("msg","我的发布activity结束");
                finish(); //结束activiy
            }
        });


        //...获取数据
        Pet pet = new Pet();
        pet.setAge(2);
        pet.setSex(0);
        pet.setExpelling(1);
        pet.setImage(null);
        pet.setName("旺财");
        pet.setSterillization(1);
        pet.setVaccine(1);
        petArrayList=new ArrayList<Pet>();
        petArrayList.add(pet);
        petArrayList.add(pet);
        //...

        recyclerView=findViewById(R.id.rv_issue);
        tAdapter=new TAdapter(this,petArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener=new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                //在这里写获取数据的逻辑
                //...
                Pet pet=new Pet();
                pet.setName("333");
                petArrayList.add(pet);
                petArrayList.add(pet);
                petArrayList.add(pet);
                petArrayList.add(pet);
                petArrayList.add(pet);
                petArrayList.add(pet);
                //获取数据后 传入adapter中上面写的更新数据的方法
                tAdapter.updateData(petArrayList);
            }
        };

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(tAdapter);
        recyclerView.addOnScrollListener(endlessRecyclerOnScrollListener);

        int lcp= layoutManager.findLastCompletelyVisibleItemPosition();
        if (lcp == layoutManager.getItemCount() - 2) {
            // 倒数第2项
            int fcp= layoutManager.findFirstCompletelyVisibleItemPosition();
            View child = layoutManager.findViewByPosition(lcp);
            int deltaY = recyclerView.getBottom() - recyclerView.getPaddingBottom() -
                    child.getBottom();
            // fcp为0时说明列表滚动到了顶部, 不再滚动
            if (deltaY > 0 && fcp!= 0) {
                recyclerView.smoothScrollBy(0, -deltaY);
            }
        } else if (lcp== layoutManager.getItemCount() - 1) {
            // 最后一项完全显示, 触发操作, 执行加载更多操作
            if (endlessRecyclerOnScrollListener!= null) {
                endlessRecyclerOnScrollListener.onLoadMore();
            }
        }

    }
}