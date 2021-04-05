package com.example.pet.ui.home;

import android.content.Intent;
import android.net.Network;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.pet.MainActivity;
import com.example.pet.R;
import com.example.pet.control.EndlessRecyclerOnScrollListener;
import com.example.pet.control.MyLocationListener;
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

import static android.app.Activity.RESULT_OK;


public class HomeFragment extends Fragment {
    static int CICTY_COOSE=100;
    static final int UPDATE=001;

    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    public ArrayList<Pet> pets=new ArrayList<>();
    String city="珠海";
    String catalog="狗狗";
    RecyclerView recyclerView;
    TAdapter adapter;
    ConstraintLayout cl_city_choose;
    TextView tv_city;
    TextView tv_no_data;

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private static final MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
    Handler handler;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.colorThem)); //设置状态颜色

        tv_no_data=view.findViewById(R.id.tv_home_no_data);
        tv_city=view.findViewById(R.id.tv_home_city);
        cl_city_choose=view.findViewById(R.id.cl_home_city);
        cl_city_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),CityChooseActivity.class);
                startActivityForResult(intent,CICTY_COOSE);
            }
        });

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case UPDATE:
                        tv_no_data.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                        break;
                    case MainActivity.NO_DATA:
                        tv_no_data.setVisibility(View.VISIBLE);
                        break;
                }
            }
        };

        getPets(city,"猫猫");


        //初始化LocationClient类
        mLocationClient = new LocationClient(getContext());
        //声明LocationClient类
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
        //配置定位SDK参数
        LocationClientOption option = new LocationClientOption();

        //可选，是否需要地址信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的地址信息，此处必须为true
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setPriority(LocationClientOption.NetWorkFirst);  //设置定位优先级
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，设置是否需要最新版本的地址信息。默认需要，即参数为true
        option.setNeedNewVersionRgc(true);

        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
        mLocationClient.setLocOption(option);
        mLocationClient.start();


        recyclerView=view.findViewById(R.id.recyclerView2);
        adapter=new TAdapter(view.getContext(),pets);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());

        int i=0;
        EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener=new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                //在这里写获取数据的逻辑
                //...
                /*
                    Pet pet = new Pet();
                    pet.setName("333");
                    ArrayList<Pet> newpet = new ArrayList<>();
                    newpet.add(pet);
                    newpet.add(pet);
                    newpet.add(pet);


                    //获取数据后 传入adapter中上面写的更新数据的方法
                    adapter.updateData(newpet);  */

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
        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==CICTY_COOSE){
                city= data.getExtras().getString("city");
                tv_city.setText(city);
                pets.clear();
                getPets(city,catalog);
            }
        }
    }

    public static String resloverUrl(String url){
        if(url.contains("127.0.0.1")){
            return url.replace("127.0.0.1", NetworkSettings.HOST);
        }
        return url;
    }

    public void getPets(String city,String catalog) {
        Request request = null;
        try {
            request = new Request.Builder().url(NetworkSettings.GEI_BY_CITY + "/?city=" + city+"&catalog="+catalog).put(
                    RequestBody.create(
                            mapper.writeValueAsString(city), mediaType
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
                    if(jsonArray.length()==0){
                        Message msg = new Message();
                        msg.what = MainActivity.NO_DATA;
                        handler.sendMessage(msg);
                    }
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
                        pet.setVedio(resloverUrl(temp.getString("video")));
                        pets.add(pet);
                    }
                   // adapter.setArrayList(pets);
                    Message msg = new Message();
                    msg.what = UPDATE;
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}