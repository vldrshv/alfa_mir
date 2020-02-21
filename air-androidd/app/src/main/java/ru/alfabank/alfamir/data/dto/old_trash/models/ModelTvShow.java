package ru.alfabank.alfamir.data.dto.old_trash.models;

/**
 * Created by U_M0WY5 on 20.11.2017.
 */

public class ModelTvShow {
    private int id;
    private String start;
    private String end;
    private String title;
    private String description;
    private ModelAuthor author;
    private String room;
    private int passwordexist;
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ModelAuthor getAuthor() {
        return author;
    }

    public void ModelAuthor(ModelAuthor author) {
        this.author = author;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getPasswordexist() {
        return passwordexist;
    }

    public void setPasswordexist(int passwordexist) {
        this.passwordexist = passwordexist;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
