package com.countmein.countmein.beans;

import java.io.Serializable;

/**
 * Created by sweet_000 on 6/9/2017.
 */

public class User_ActivityBean implements Serializable {

    public String activityId;
    public String userId;

    public User_ActivityBean(){

    }

    public User_ActivityBean(String activity, String userId){
        this.activityId = activity;
        this.userId = userId;

    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

