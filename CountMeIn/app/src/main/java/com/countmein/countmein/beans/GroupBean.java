package com.countmein.countmein.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Home on 6/5/2017.
 */

public class GroupBean extends  BaseModel implements Serializable{

    public String name;
    public String description;
    public String topictoken;

    public GroupBean() {
    }

    public GroupBean(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public GroupBean(String id, String name, String description, String topictoken) {
        super(id);
        this.name = name;
        this.description = description;
        this.topictoken=topictoken;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTopictoken() {
        return topictoken;
    }

    public void setTopictoken(String topictoken) {
        this.topictoken = topictoken;
    }

}
