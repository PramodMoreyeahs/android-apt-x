package com.apt_x.app.model.bean;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by shivanivani on 22/4/21.
 */

public class OrmBeanList implements Serializable {

    @DatabaseField
    private String name;
    @DatabaseField
    private String contactNo;
    @DatabaseField
    private String city;
    @DatabaseField
    private String state;
    @DatabaseField
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public OrmBeanList(String id,String name, String contactNo, String city, String state) {
        this.name = name;
        this.contactNo = contactNo;
        this.city = city;
        this.state = state;
        this.id=id;
    }
    public OrmBeanList() {

    }

}
