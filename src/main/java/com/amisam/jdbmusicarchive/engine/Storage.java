package com.amisam.jdbmusicarchive.engine;

import com.amisam.jdbmusicarchive.datamodel.SongItem;
import com.amisam.jdbmusicarchive.model.Song;
import javafx.collections.ObservableList;

public class Storage {
    
    private IStorage storage;

    public Storage(IStorage storage) {
        this.storage = storage;
    }

    public void storeSongs() throws Exception {
        try {
            storage.storeSongs();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadSongs() {
        try {
            storage.loadSongs();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
