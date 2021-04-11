package com.example.pet.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pet.MainActivity;
import com.example.pet.R;
import com.example.pet.entity.Pet;
import com.example.pet.jdbc.NetworkSettings;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.example.pet.jdbc.NetworkSettings.LOGIN;
import static com.example.pet.jdbc.NetworkSettings.POST_PET;

public class LoginActivity extends AppCompatActivity {
    EditText et_phone,et_password;
    Button btn_login;
    static final int LOGIN_SUCCES=1012;
    static final int LOGIN_FAIL=1013;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case LOGIN_SUCCES:
                        Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_LONG).show();
                        MainActivity.userId=et_phone.getText().toString();
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        intent.putExtra("loginFlag","login");
                        startActivity(intent);
                        finish();
                        break;
                    case LOGIN_FAIL:
                        Toast.makeText(LoginActivity.this,"密码错误",Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
        
        et_phone=findViewById(R.id.username);
        et_password=findViewById(R.id.password);
        btn_login=findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = et_phone.getText().toString();
                if(phone.length()!=11 && phone.matches("^[0-9]*$")){
                    Toast.makeText(LoginActivity.this,"请输入正确的手机号",Toast.LENGTH_SHORT).show();
                }else{
                    String password = et_password.getText().toString();
                    Login(phone,password);
                }

            }
        });
    }

    public void Login(String userId,String password){
        FormBody.Builder builder = new FormBody.Builder();

        builder.add("userId",userId);
        builder.add("password",password);

        OkHttpClient httpClient = new OkHttpClient();
        FormBody formBody=builder.build();
        Request request=new Request.Builder().url(LOGIN).post(formBody).build();
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //在这里根据返回内容执行具体的操作
                final String resdata = response.body().string();
                Message message=new Message();
                if(resdata.equals("true")){
                    message.what=LOGIN_SUCCES;
                }else if(resdata.equals("false")){
                    message.what=LOGIN_FAIL;
                }
                handler.sendMessage(message);

            }
        });
    }
}