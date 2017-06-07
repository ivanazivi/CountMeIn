package com.countmein.countmein.beans;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by sweet_000 on 6/6/2017.
 */

public class JustId  implements Serializable {

    public String activityId;
    public String userId;

    public  JustId(){

    }

    public  JustId(String activity, String userId){
        this.activityId = activity;
        this.userId = userId;

    }
}
