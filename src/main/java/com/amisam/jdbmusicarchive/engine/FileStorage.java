package com.amisam.jdbmusicarchive.engine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.function.Function;

import com.amisam.jdbmusicarchive.App;
import com.amisam.jdbmusicarchive.datamodel.Songs;
import com.amisam.jdbmusicarchive.datamodel.SongItem;
import com.amisam.jdbmusicarchive.model.Song;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FileStorage implements IStorage {
    private String URL = App.filename;
    private Songs songs;
    private ObservableList<SongItem> songItems;

    public FileStorage() {
    }

    public Songs getSongs() {
        return this.songs;
    }

    public void addTodItem(SongItem item){
        songItems.add(item);
    }

    @Override
    public void storeSongs() throws Exception {
        Path path = Paths.get(App.filename);
        BufferedWriter bw = Files.newBufferedWriter(path);
        try{
            Iterator<SongItem> iter = songItems.iterator();
            while (iter.hasNext()){
                SongItem item = iter.next();
                bw.write(String.format("%s\t%s\t%s",
                        item.getTrack(),
                        item.getTitle(),
                        item.getAlbum()));
                bw.newLine();
            }

        } finally {
            if (bw != null){
                bw.close();
            }
        }
    }

    @Override
    public void loadSongs() throws Exception {
        this.songItems = FXCollections.observableArrayList(); //observable list is used
        Path path = Paths.get(this.URL); 
        BufferedReader br = Files.newBufferedReader(path);

        String input;


        try {
            while ((input = br.readLine()) != null) {
                String[] itemPieces = input.split("\t");

                if (itemPieces.length < 1) break;

                try {
                    int id = stringToInteger(itemPieces[0]);
                    int track = stringToInteger(itemPieces[1]); //itemPieces[0];
                    String title = itemPieces[2];
                    int album = stringToInteger(itemPieces[3]);

                    if (id == -1 || track == -1 || album == -1) {
                        throw new Exception("Invalid data in file");
                    }

    
                    SongItem songItem = new SongItem(id, track, title, album);
                    songItems.add(songItem);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } finally {
            if (br != null) {
                br.close();
            }
        }
    }

    public static int stringToInteger(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public ObservableList<SongItem> getSongItems() {
        // TODO Auto-generated method stub
        return songItems;
    }

    @Override
    public void closeResources() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'closeResources'");
    }
}
