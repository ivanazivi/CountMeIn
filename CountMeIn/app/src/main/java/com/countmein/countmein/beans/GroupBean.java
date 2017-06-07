package com.countmein.countmein.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Home on 6/5/2017.
 */

public class GroupBean extends  BaseModel implements Serializable{

    public String name;
    public String description;

    public String getTopictoken() {
        return topictoken;
    }

    public void setTopictoken(String topictoken) {
        this.topictoken = topictoken;
    }

    public String topictoken;

    public GroupBean(String name, String description, List<User> friends) {
        this.name = name;
        this.description = description;
        this.friends = friends;

    }

    public List<User> friends;

    public GroupBean(String id,String name, String description, List<User> friends,String topictoken) {
        super(id);
        this.name = name;
        this.description = description;
        this.friends = friends;
        this.topictoken=topictoken;
    }

    public GroupBean() {
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

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }
}
