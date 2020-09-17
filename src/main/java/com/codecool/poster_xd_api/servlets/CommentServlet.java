package com.codecool.poster_xd_api.servlets;

import com.codecool.poster_xd_api.DateParser;
import com.codecool.poster_xd_api.dao.CommentDao;
import com.codecool.poster_xd_api.dao.PostDao;
import com.codecool.poster_xd_api.dao.UserDao;
import com.codecool.poster_xd_api.models.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet(name = "comments", urlPatterns = {"/comments/*"}, loadOnStartup = 1)
public class CommentServlet extends PosterAbstractServlet<Comment, String> {

    UserDao userDao = new UserDao();
    PostDao postDao = new PostDao();

    {
        this.dao = new CommentDao();
        this.objectName = "comment";
        this.rootPath = "/comments/";
    }

    @Override
    Optional<Comment> createPojoFromJsonObject(JsonObject jsonObject) {
        JsonElement idJson = jsonObject.get("id");
        if (idJson == null) return Optional.empty();

        JsonElement userIdJson = jsonObject.get("user");
        if (userIdJson == null) return Optional.empty();

        JsonElement dateString = jsonObject.get("date");
        if (dateString == null) return Optional.empty();

        JsonElement postIdJson = jsonObject.get("post");
        if (postIdJson == null) return Optional.empty();

        JsonElement content = jsonObject.get("content");
        if (content == null) return Optional.empty();

        Optional<User> optionalUser = userDao.getById(userIdJson.getAsLong());
        Optional<Post> optionalPost = postDao.getById(postIdJson.getAsLong());
        if (!(optionalUser.isPresent() && optionalPost.isPresent())) {
            return Optional.empty();
        }
        Date date1 = new DateParser().parseDate(dateString.getAsString());
        Comment comment = new Comment(optionalPost.get(), date1, optionalUser.get(), content.getAsString());
        return Optional.of(comment);
    }

    @Override
    protected void updateObject(JsonObject jsonObject, Comment comment) {
        JsonElement contentJson = jsonObject.get("content");
        if (contentJson != null) comment.setContent(contentJson.getAsString());
        dao.update(comment);
    }

    @Override
    protected void getSubPathObjects(HttpServletResponse resp, Comment object) throws IOException {
        resp.setStatus(404);
        resp.getWriter().println("Wrong path provided");
    }

    @Override
    protected void getObjectsForRoot(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Comment> commentList;
        List<List<Comment>> lists = new ArrayList<>();

        addObjectsMatchingParameter(req, lists, "date");
        addObjectsMatchingParameter(req, lists, "user");
        addObjectsMatchingParameter(req, lists, "content");

        commentList = populateObjectList(lists);

        String objectsAsJsonString = commentList.stream().map(e->(Jsonable)e).map(Jsonable::toJson).collect(Collectors.joining(",\n"));
        writeObjectsToResponseFromCollection(resp, objectsAsJsonString);
    }
}

