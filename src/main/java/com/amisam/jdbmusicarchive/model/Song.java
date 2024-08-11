package com.amisam.jdbmusicarchive.model;

import java.time.temporal.TemporalAccessor;

public class Song {
    private int id;
    private int tract_id;
    private String title;
    private int album_id;
    private String schema;

    public Song() {
        this.schema = DataSource.songs_schema;
    }

    /*SETTERS*/

    public void setId(int id) {
        this.id = id;
    }

    public void setTract_id(int tract_id) {
        this.tract_id = tract_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    /* GETTERS*/

    public int getId() {
        return id;
    }

    public int getTract_id() {
        return tract_id;
    }

    public String getTitle() {
        return title;
    }

    public int getAlbum_id() {
        return album_id;
    }

    public String getDetails() {
        return "ID: " + getId() + " Tract ID: " + getTract_id() + " Title: " + getTitle() + " Album ID: " + getAlbum_id();
    }

    public TemporalAccessor getDeadline() {
        return null;
    }
}
