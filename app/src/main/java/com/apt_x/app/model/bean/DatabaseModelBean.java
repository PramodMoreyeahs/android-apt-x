package com.apt_x.app.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;

/**
 * Created by shivanivani on 22/4/21.
 */

@Entity
public class DatabaseModelBean  {


    @Id()
    String id;

    @NotNull
    private String text;
    private String comment;
    private String newtest;
    @ToOne(joinProperty = "id")
    private JoinBean joinBean;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 935940563)
    private transient DatabaseModelBeanDao myDao;

    @Generated(hash = 1669836219)
    private transient String joinBean__resolvedKey;



    @Keep
    @Generated(hash = 879406100)
    public DatabaseModelBean(String id, @NotNull String text, String comment) {
        this.id = id;
        this.text = text;
        this.comment = comment;
    }

    @Generated(hash = 572073985)
    public DatabaseModelBean() {
    }

    @Generated(hash = 1145952774)
    public DatabaseModelBean(String id, @NotNull String text, String comment,
            String newtest) {
        this.id = id;
        this.text = text;
        this.comment = comment;
        this.newtest = newtest;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getNewtest() {
        return this.newtest;
    }

    public void setNewtest(String newtest) {
        this.newtest = newtest;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1075726153)
    public JoinBean getJoinBean() {
        String __key = this.id;
        if (joinBean__resolvedKey == null || joinBean__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            JoinBeanDao targetDao = daoSession.getJoinBeanDao();
            JoinBean joinBeanNew = targetDao.load(__key);
            synchronized (this) {
                joinBean = joinBeanNew;
                joinBean__resolvedKey = __key;
            }
        }
        return joinBean;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 769774639)
    public void setJoinBean(JoinBean joinBean) {
        synchronized (this) {
            this.joinBean = joinBean;
            id = joinBean == null ? null : joinBean.getId();
            joinBean__resolvedKey = id;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 399485790)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDatabaseModelBeanDao() : null;
    }

   /* @ToOne(joinProperty = "id")
    private JoinBean joinBean;


    public JoinBean getJoinBean() {
        return joinBean;
    }

    public void setJoinBean(JoinBean joinBean) {
        this.joinBean = joinBean;
    }*/

}
