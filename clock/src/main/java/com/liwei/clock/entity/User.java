package com.liwei.clock.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by LIWEI on 2017/10/18.
 */
@Entity
public class User {
    @Id
    private Long userId;
    private String userName;
    private String nicename;

    @Transient
    private int tempUsageCount; // not persisted

    @Generated(hash = 1201156628)
    public User(Long userId, String userName, String nicename) {
        this.userId = userId;
        this.userName = userName;
        this.nicename = nicename;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNicename() {
        return this.nicename;
    }

    public void setNicename(String nicename) {
        this.nicename = nicename;
    }

}
