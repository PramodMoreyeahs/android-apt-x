package com.apt_x.app.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by shivanivani on 22/4/21.
 */

@Entity
public class JoinBean {

    @Id()
    String id;

    String description;

    @Generated(hash = 1219347047)
    public JoinBean(String id, String description) {
        this.id = id;
        this.description = description;
    }

    @Generated(hash = 1802098061)
    public JoinBean() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
