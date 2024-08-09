package com.amisam.jdbmusicarchive.model;

public class Artist {
    private int id;
    private String name;
    private String schema;

    public Artist() {
        this.schema = DataSource.artists_schema;
    }

    /*----Setters---*/
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*----Getters---*/
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSchema() {
        return schema;
    }
}
