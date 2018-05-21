package com.yyhh.overall.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.yyhh.overall.base.UserBase;

/**
 * Created by yh on 2017/11/16.
 */

public class DB {

    public static void saveLOGIN(Context context, boolean isLogin){
        SharedPreferences sp=context.getSharedPreferences("login",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putBoolean("islogin",isLogin);
        editor.commit();
    }

    public static boolean getLOGIN(Context context){
        SharedPreferences sp=context.getSharedPreferences("login",Context.MODE_PRIVATE);
        return sp.getBoolean("islogin",false);
    }
    public static void saveUser(Context context,String phone,String nickname){
        SharedPreferences preferences=context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("phone",phone);
        editor.putString("nickname",nickname);
        editor.commit();
    }
    public static UserBase getUser(Context context){
        SharedPreferences preferences=context.getSharedPreferences("user",Context.MODE_PRIVATE);
        UserBase userBase=new UserBase();

        return userBase;
    }
}
