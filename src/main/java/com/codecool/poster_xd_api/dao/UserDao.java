package com.codecool.poster_xd_api.dao;

import com.codecool.poster_xd_api.models.User;

public class UserDao extends AbstractDao<User> {

    public UserDao() {
        super();
        this.aClass = User.class;
    }
}
