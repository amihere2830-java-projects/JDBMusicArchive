package com.amisam.jdbmusicarchive.engine;

import java.util.List;

import com.amisam.jdbmusicarchive.App;
import com.amisam.jdbmusicarchive.MusicUI;
import com.amisam.jdbmusicarchive.datamodel.SongItem;
import com.amisam.jdbmusicarchive.datamodel.Songs;
import com.amisam.jdbmusicarchive.model.DataSource;
import com.amisam.jdbmusicarchive.model.MusicTable;
import com.amisam.jdbmusicarchive.model.SQLStatement;
import com.amisam.jdbmusicarchive.model.Song;
import com.amisam.jdbmusicarchive.model.SongArtist;
import com.amisam.jdbmusicarchive.utilities.ThreadColor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DBStorage implements IStorage {

    private MusicTable songs_table;
    public static int numSongs;
    private DataSource data_src;

    private ObservableList<SongItem> songItems = FXCollections.observableArrayList();
    private List<Song> songs_view_data;
    
    public DBStorage() {
        this.data_src = new DataSource();

        this.songs_table = new MusicTable(DataSource.TABLE_SONGS);
        this.songs_table.create_schema(DataSource.songs_schema);
        
        //Open Connection for data_source
        if (!this.data_src.open()){
            System.out.println(ThreadColor.RED+"Can't open data_src"+
                    ThreadColor.RESET);
            return;
        }

        try {
            loadSongs();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void storeSongs() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void loadSongs() throws Exception {

        String query_songs;
        
        query_songs = new SQLStatement(this.songs_table).queryStmt("*");

        if (this.data_src != null){
            System.out.println("Querying songs..."+this.data_src);
            this.data_src.querySongMetaData(query_songs);

            this.songs_view_data = this.data_src.querySong(query_songs);

            setSongItems();

        } else {
            this.songs_view_data = null;
        }
    }

    public void storeSongs(Songs songs) throws Exception {


    }

    public void closeResources() {
        if (this.data_src != null){
            
            /*-------------- CLOSE RESOURCES IN MAIN -------------*/
            this.data_src.close(DataSource.CONNECTION_NAME);
        }
    }

    private void setSongItems(){
        for (Song song: this.songs_view_data){
            this.songItems.add(new SongItem(song.getId(), song.getTract_id(), song.getTitle(), song.getAlbum_id()));
        }
    }


    @Override
    public ObservableList<SongItem> getSongItems() {
        return this.songItems;
    }
}
