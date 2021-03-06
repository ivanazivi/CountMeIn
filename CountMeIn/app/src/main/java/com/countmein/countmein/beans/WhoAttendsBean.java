package com.countmein.countmein.beans;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by sweet_000 on 6/6/2017.
 */

public class WhoAttendsBean implements Serializable {

    public String activityId;
    public String userId;
    public String ownerId;

    public WhoAttendsBean(){

    }

    public WhoAttendsBean(String activity, String userId, String ownerId){
        this.activityId = activity;
        this.userId = userId;
        this.ownerId = ownerId;

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

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
