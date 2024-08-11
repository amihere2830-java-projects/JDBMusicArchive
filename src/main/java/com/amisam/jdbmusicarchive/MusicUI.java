package com.amisam.jdbmusicarchive;

import java.io.IOException;

import com.amisam.jdbmusicarchive.datamodel.Songs;
import com.amisam.jdbmusicarchive.engine.DBStorage;
import com.amisam.jdbmusicarchive.engine.IStorage;
import com.amisam.jdbmusicarchive.engine.Storage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class MusicUI extends Application{
    public static IStorage storage = (IStorage) App.getStorage();

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("mainwindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 877, 533);
        
        Image icon = new Image(getClass().getResourceAsStream("logo1.png"));
        stage.getIcons().add(icon);
        
        stage.setTitle("Music Archive");
        stage.setScene(scene);
        stage.show();
    }

    public static void launchMusic() {
        
        launch();
    }

    @Override
    public void stop() throws Exception {
        try{
            Songs.getInstance().storeSongs();

            MusicUI.storage.storeSongs();

            if (!App.storageType.equals("file")){
                MusicUI.storage.closeResources();
            }

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    //override the init method to load the TodoItems from file
    @Override
    public void init() throws Exception {
        try{
            // MusicUI.storage.loadSongs();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
