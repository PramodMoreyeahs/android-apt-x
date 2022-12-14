package com.apt_x.app.model.bean;

import java.io.Serializable;

/**
 * Created by shivanivani on 22/4/21.
 */

@SuppressWarnings("ALL")
public class CommonResponse implements Serializable {
    private String code;
    private  String message;
    private String user_campaign_unique_id;
    private String  bonus;


    public String getUser_campaign_unique_id() {
        return user_campaign_unique_id;
    }

    public void setUser_campaign_unique_id(String user_campaign_unique_id) {
        this.user_campaign_unique_id = user_campaign_unique_id;
    }

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
