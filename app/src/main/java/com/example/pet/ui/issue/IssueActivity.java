package com.example.pet.ui.issue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pet.MainActivity;
import com.example.pet.R;
import com.example.pet.control.EndlessRecyclerOnScrollListener;
import com.example.pet.control.TAdapter;
import com.example.pet.entity.Pet;
import com.example.pet.jdbc.NetworkSettings;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class IssueActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TAdapter tAdapter;
    ImageView imageView;
    ArrayList<Pet> petArrayList=new ArrayList<>();
    TextView title;
    TextView tv_no_date;

    Handler handler;
    Request request = null;
    OkHttpClient client = new OkHttpClient();
    ObjectMapper mapper = new ObjectMapper();
    MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
    final static int ISSUE_UPDATE=004;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue);
        handler=new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case ISSUE_UPDATE:
                        tv_no_date.setVisibility(View.GONE);
                        tAdapter.notifyDataSetChanged();
                        break;
                    case MainActivity.NO_DATA:
                        tv_no_date.setVisibility(View.VISIBLE);
                        break;
                }
            }
        };

        tv_no_date=findViewById(R.id.tv_issue_no_data);
        title = findViewById(R.id.tx_issue_title);
        int titleFlag = getIntent().getIntExtra("titleFlag", -1);
        tAdapter = new TAdapter(this, petArrayList);
        if (titleFlag == 1) {
            title.setText("我发布的");
            tAdapter.setFlag(2);
            LoadPet(titleFlag);
        } else if (titleFlag == 0) {
            title.setText("我收藏的");
            tAdapter.setFlag(3);
            LoadPet(titleFlag);
        }
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorThem)); //设置状态颜色

        imageView = findViewById(R.id.iv_issue_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("msg", "我的发布activity结束");
                finish(); //结束activiy
            }
        });

        //...获取数据
        /*
        Pet pet = new Pet();
        pet.setAge(2);
        pet.setSex(0);
        pet.setExpelling(1);
        //pet.setImage(null);
        pet.setName("旺财");
        pet.setSterillization(1);
        pet.setVaccine(1);
        petArrayList = new ArrayList<Pet>();
        petArrayList.add(pet);
        petArrayList.add(pet);

         */

        recyclerView = findViewById(R.id.rv_issue);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(tAdapter);

        /*
        EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                //在这里写获取数据的逻辑
                //...
                Pet pet = new Pet();
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

        recyclerView.addOnScrollListener(endlessRecyclerOnScrollListener);

        int lcp = layoutManager.findLastCompletelyVisibleItemPosition();
        if (lcp == layoutManager.getItemCount() - 2) {
            // 倒数第2项
            int fcp = layoutManager.findFirstCompletelyVisibleItemPosition();
            View child = layoutManager.findViewByPosition(lcp);
            int deltaY = recyclerView.getBottom() - recyclerView.getPaddingBottom() -
                    child.getBottom();
            // fcp为0时说明列表滚动到了顶部, 不再滚动
            if (deltaY > 0 && fcp != 0) {
                recyclerView.smoothScrollBy(0, -deltaY);
            }
        } else if (lcp == layoutManager.getItemCount() - 1) {
            // 最后一项完全显示, 触发操作, 执行加载更多操作
            if (endlessRecyclerOnScrollListener != null) {
                endlessRecyclerOnScrollListener.onLoadMore();
            }
        }
         */
    }

    public static String resloverUrl(String url) {
        if (url.contains("127.0.0.1")) {
            return url.replace("127.0.0.1", NetworkSettings.HOST);
        }
        return url;
    }

    //一次加载全部
    void LoadPet(int flag) {
        try {
            request = new Request.Builder().url(NetworkSettings.GET_ISSUE + "/?type=" + flag+"&userId=u"+MainActivity.userId).put(
                    RequestBody.create(
                            mapper.writeValueAsString(flag), mediaType
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
                try {
                    JSONArray jsonArray = new JSONArray(requestBody.string());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject temp = (JSONObject) jsonArray.get(i);
                        Pet pet = new Pet();
                        pet.setId(temp.getString("id"));
                        pet.setName(temp.getString("name"));
                        pet.setStory(temp.getString("story"));
                        pet.setAge(temp.getInt("age"));
                        pet.setSex(temp.getInt("sex"));
                        pet.setVaccine(temp.getInt("vaccine"));
                        pet.setSterillization(temp.getInt("sterillization"));
                        pet.setExpelling(temp.getInt("expelling"));
                        pet.setUrl1(resloverUrl(temp.getString("url1")));
                        pet.setUrl2(resloverUrl(temp.getString("url2")));
                        pet.setUrl3(resloverUrl(temp.getString("url3")));
                        pet.setUrl4(resloverUrl(temp.getString("url4")));
                        pet.setVideo(resloverUrl(temp.getString("video")));
                        petArrayList.add(pet);
                    }
                    if (petArrayList.size() == 0) {
                        Message msg = new Message();
                        msg.what = MainActivity.NO_DATA;
                        handler.sendMessage(msg);
                        return;
                    }
                    Message msg = new Message();
                    msg.what = ISSUE_UPDATE;
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}