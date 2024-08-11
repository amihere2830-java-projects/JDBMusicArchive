package com.amisam.jdbmusicarchive.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.util.List;

import com.amisam.jdbmusicarchive.App;
import com.amisam.jdbmusicarchive.MusicUI;
import com.amisam.jdbmusicarchive.engine.Storage;

import com.amisam.jdbmusicarchive.model.Song;
import com.amisam.jdbmusicarchive.model.SongArtist;

public class Songs {
    private static Songs instance = new Songs();
    public static int numSongs;
    
    // private static String filename = "TodoListItem.txt";

    private ObservableList<SongItem> songItems;

    public static Songs getInstance() {
        return instance;
    }

    //Create a private constructor
    //this prevents the TodoData class from being instantiated
    //in order to access todoItems list
    public Songs() {
        this.songItems = MusicUI.storage.getSongItems();

    }

    public ObservableList<SongItem> getSongItems() {
        return songItems;
    }
    // Add items to the TodoItems list
    public void setSongItems(SongItem item){
        songItems.add(item);
    }

    public void storeSongs() throws IOException{

        try {
            MusicUI.storage.storeSongs();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteSong(SongItem song){
        this.songItems.remove(song);
    }

}
