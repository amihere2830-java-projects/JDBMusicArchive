package com.amisam.jdbmusicarchive.engine;

import com.amisam.jdbmusicarchive.datamodel.SongItem;
import com.amisam.jdbmusicarchive.model.Song;

import javafx.collections.ObservableList;

public interface IStorage {
    String URL = "";
    void storeSongs() throws Exception;
    void loadSongs() throws Exception;
    ObservableList<SongItem> getSongItems();
    void closeResources();
}
