package com.example.pet.ui.video;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pet.MainActivity;
import com.example.pet.R;
import com.example.pet.control.ListVideoAdapter;
import com.example.pet.entity.Video;
import com.example.pet.jdbc.NetworkSettings;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import cn.jzvd.JzvdStd;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.pet.MainActivity.NO_DATA;
import static com.example.pet.ui.home.HomeFragment.resloverUrl;

public class VideoFragment extends Fragment {
    static final int GET_VIDEO = 003;
    RecyclerView recyclerView;
    TextView tv_no_data;
    int firstVisibleItem, lastVisibleItem;
    private List<String> urlList;
    List<Video> videoList = new ArrayList<>();
    ListVideoAdapter adapter;
    int getVideo = 0; //目前已经加载的视频
    boolean falg_all = false;  //是否已经浏览完全部的视频
    Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {  //切换Fragment，将视频暂停
        super.onPause();
        for (int i = 0; i <= lastVisibleItem; i++) {
            if (recyclerView == null || recyclerView.getChildAt(i) == null) {
                return;
            }
            JzvdStd videoView = recyclerView.getChildAt(i).findViewById(R.id.jz_video_player);
            //释放资源
            videoView.releaseAllVideos();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        recyclerView=view.findViewById(R.id.rv_vedio);
        tv_no_data=view.findViewById(R.id.tv_vedio_no_data);
        handler =new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case GET_VIDEO:
                        tv_no_data.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                        break;
                    case NO_DATA:
                        tv_no_data.setVisibility(View.VISIBLE);
                        break;
                }
            }
        };
        initView(view.getContext());
        addListener();
        return view;
    }



    private void LoadVideo(){
        if(falg_all==true){
            return;
        }
        Request request = null;
        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();
        final MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
        try {
            request = new Request.Builder().url(NetworkSettings.GET_VEDIO + "/?getNumber=" + getVideo).put(
                    RequestBody.create(
                            mapper.writeValueAsString(getVideo), mediaType
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
                        Video video = new Video();
                        video.setId(temp.getString("id"));
                        video.setName(temp.getString("name"));
                        video.setAge(temp.getInt("age"));
                        video.setUrl(resloverUrl(temp.getString("url1")));
                        video.setVideo(resloverUrl(temp.getString("video")));
                        videoList.add(video);
                    }
                    if(videoList.size()==0){
                        Message msg = new Message();
                        msg.what = NO_DATA;
                        handler.sendMessage(msg);
                    }
                    if (jsonArray.length()<10){
                        falg_all=true;
                    }
                    getVideo+=jsonArray.length();
                    Message msg = new Message();
                    msg.what = GET_VIDEO;
                    handler.sendMessage(msg);
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initView(Context context){

        LoadVideo();
       // adapter=new ListVideoAdapter(urlList);
        adapter=new ListVideoAdapter(getContext(),videoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }

    private void addListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private boolean isSlidingUpward = false;
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE://停止滚动
                        //这里执行，视频的自动播放与停止
                        autoPlayVideo(recyclerView);
                        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                        int lastItemPosition = manager.findLastCompletelyVisibleItemPosition();
                        int itemCount = manager.getItemCount();
                        // 判断是否滑动到了最后一个item，并且是向上滑动
                        if (lastItemPosition == (itemCount - 1) && isSlidingUpward) {
                            //加载更多
                            LoadVideo();
                        }
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING://拖动
                        autoPlayVideo(recyclerView);
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING://惯性滑动
                        JzvdStd.releaseAllVideos();
                        break;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                isSlidingUpward = dy > 0;
            }
        });
    }


    private void autoPlayVideo(RecyclerView recyclerView) {
        if (firstVisibleItem == 0 && lastVisibleItem == 0 && recyclerView.getChildAt(0) != null) {
            JzvdStd videoView = null;
            if (recyclerView != null && recyclerView.getChildAt(0) != null) {
                videoView = recyclerView.getChildAt(0).findViewById(R.id.jz_video_player);
            }
            if (videoView != null) {
                if (videoView.state == JzvdStd.STATE_PAUSE || videoView.state ==  JzvdStd.STATE_NORMAL) {
                    //播放第一个显示完整的视频
                    videoView.startVideo();
                }
            }
        }

        for (int i = 0; i <= lastVisibleItem; i++) {
            if (recyclerView == null || recyclerView.getChildAt(i) == null) {
                return;
            }
            JzvdStd videoView = recyclerView.getChildAt(i).findViewById(R.id.jz_video_player);
            if (videoView != null) {
                Rect rect = new Rect();
                //获取视图本身的可见坐标，把值传入到rect对象中
                videoView.getLocalVisibleRect(rect);
                //获取视频的高度
                int videoHeight = videoView.getHeight();
                if (rect.top <= 100 && rect.bottom >= videoHeight) {
                    //若是显示完全的第一个视频
                    if (videoView.state == JzvdStd.STATE_PAUSE || videoView.state ==  JzvdStd.STATE_NORMAL) {
                        videoView.startVideo();
                    }
                    return;
                }
                //释放资源
                videoView.releaseAllVideos();
            } else {
                videoView.releaseAllVideos();
            }

        }

    }
}