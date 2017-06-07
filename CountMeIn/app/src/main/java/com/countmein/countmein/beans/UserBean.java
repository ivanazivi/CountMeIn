package com.countmein.countmein.beans;

import java.io.Serializable;

/**
 * Created by Home on 5/20/2017.
 */

public class UserBean extends BaseModel implements Serializable {

    private String token;
    private String username;
    private String photoUrl;
    private String email;

    public UserBean() {
    }


    public UserBean(String id, String username, String photoUrl, String email) {
        super(id);
        this.username = username;
        this.photoUrl = photoUrl;
        this.email = email;
    }


    public UserBean(String id, String username, String photoUrl, String token, String email){
        super(id);
        this.username=username;
        this.photoUrl=photoUrl;
        this.token=token;
        this.email = email;
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

        UserBean userBean = (UserBean) o;

        return getId().equals(userBean.getId());

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
