package com.codecool.poster_xd_api.servlets;

import com.codecool.poster_xd_api.models.Comment;
import com.google.gson.JsonObject;

import java.util.Optional;

public class CommentServlet extends PosterAbstractServlet<Comment, String>{

    @Override
    Optional<Comment> createPojoFromJsonObject(JsonObject jsonObject) {
        return Optional.empty();
    }

    @Override
    protected void updateObject(JsonObject requestAsJson, Comment comment) {

    }
}