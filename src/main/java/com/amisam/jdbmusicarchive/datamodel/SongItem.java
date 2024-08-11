package com.amisam.jdbmusicarchive.datamodel;

public class SongItem {
    private int id;
    private int track;// shortDescription;
    private String title; //details;
    private int album;

    public SongItem(Integer id, Integer track, String title, Integer album) {
        this.id = id;
        this.track = track;
        this.title = title;
        this.album = album;
    }

    public SongItem(Integer track, String title, Integer album) {
        this.id = Songs.numSongs + 1;
        this.track = track;
        this.title = title;
        this.album = album;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTrack() {
        return track;
    }

    public void setTrack(Integer track) {
        this.track = track;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getAlbum() {
        return album;
    }

    public void setAlbum(Integer album) {
        this.album = album;
    }

    @Override
    public String toString() {
        return "SongItem [id=" + id + ", track=" + track + ", title=" + title + ", album=" + album + "]";
    }

//    @Override
//    public String toString() {
//        return shortDescription;
//    }
}
