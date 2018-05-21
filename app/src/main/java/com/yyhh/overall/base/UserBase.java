package com.yyhh.overall.base;

/**
 * Created by yh on 2017/11/16.
 */

/*
*
* {followerNum=0,
* phone=13691460657,
* concernsNum=0,
* friendNum=0,
* eachFollowNum=0,
 * nickname=Overall用户,
 * avatar=[http://f1.sdk.mob.com/ums/avatar/9b1/69c/e87df3c97d9704e2b711abe787_500.png, http://f1.sdk.mob.com/ums/avatar/9b1/69c/e87df3c97d9704e2b711abe787_100.png, http://f1.sdk.mob.com/ums/avatar/9b1/69c/e87df3c97d9704e2b711abe787_50.png]}
* */
public class UserBase {
    private String phone;
    private String nickname;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
