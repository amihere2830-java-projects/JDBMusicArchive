package com.amisam.jdbmusicarchive.model;

public class Album {
    private String schema;
    private int id;
    private String name;
    private int artist_id;

    /* Setters*/
    public void setSchema(String schema) {
        this.schema = schema;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArtist_id(int artist_id) {
        this.artist_id = artist_id;
    }

    /* Getters */
    public String getSchema() {
        return schema;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getArtist_id() {
        return artist_id;
    }
}
