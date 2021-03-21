package com.example.pet.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.pet.R;
import com.example.pet.control.EndlessRecyclerOnScrollListener;
import com.example.pet.control.TAdapter;
import com.example.pet.entity.Pet;

import java.util.ArrayList;
//https://blog.csdn.net/qq_38356174/article/details/90716344?utm_medium=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromMachineLearnPai2-1.control&dist_request_id=&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromMachineLearnPai2-1.control
public class ttActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Pet> list;
    TAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tt);

        list=new ArrayList<>();
        Pet pet1 = new Pet();
        Pet pet2=new Pet();
        pet1.setName("111");
        pet2.setName("222");
        list.add(pet1);
        list.add(pet2);
        list.add(pet1);
        list.add(pet2);
        list.add(pet1);
        list.add(pet2);
        list.add(pet1);
        list.add(pet2);
        list.add(pet1);
        list.add(pet2);


        recyclerView=findViewById(R.id.recyclerView);
        adapter=new TAdapter(this,list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener=new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                //在这里写获取数据的逻辑
                //...
                Pet pet=new Pet();
                pet.setName("333");
                list.add(pet);
                list.add(pet);
                list.add(pet);
                list.add(pet);
                list.add(pet);
                list.add(pet);
                list.add(pet);
                list.add(pet);
                //获取数据后 传入adapter中上面写的更新数据的方法
                adapter.updateData(list);
            }
        };

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
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