package com.countmein.countmein.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Home on 5/20/2017.
 */

public class User extends BaseModel implements Serializable {
    public User(String id,String username, String photoUrl) {
        super(id);
        this.username = username;
        this.photoUrl = photoUrl;
    }
    private String tokens;

    private String username;

    public User(String id,String username, String photoUrl,String tokens){
        super(id);
        this.username=username;
        this.photoUrl=photoUrl;
        this.tokens=tokens;
    }

    public String getTokens() {
        return tokens;
    }

    public void setTokens(String tokens) {
        this.tokens = tokens;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return getId().equals(user.getId());

    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    private String photoUrl;
}
