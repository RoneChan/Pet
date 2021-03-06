package com.example.pet.control;

import android.os.Message;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;

import static com.example.pet.ui.home.HomeFragment.CICTY_COOSE;
import static com.example.pet.ui.home.HomeFragment.homeHandler;

public class MyLocationListener  extends BDAbstractLocationListener {
    private String city;

    @Override
    public void onReceiveLocation(BDLocation location){
        //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
        //以下只列举部分获取地址相关的结果信息
        //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
/*
        String addr = location.getAddrStr();    //获取详细地址信息
        country = location.getCountry();    //获取国家
        province = location.getProvince();    //获取省份
        city = location.getCity();    //获取城市
        String district = location.getDistrict();    //获取区县
        String street = location.getStreet();    //获取街道信息
        String adcode = location.getAdCode();    //获取adcode
        String town = location.getTown();    //获取乡镇信息

 */
        city = location.getCity();
        city = city.substring(0,city.length()-1);
        //返回码
        int i = location.getLocType();
        Log.i("tag","city="+city);
        Message message =new Message();
        message.what=CICTY_COOSE;
        homeHandler.sendMessage(message);
    }

    public String getCity() {
        return city;
    }
}
