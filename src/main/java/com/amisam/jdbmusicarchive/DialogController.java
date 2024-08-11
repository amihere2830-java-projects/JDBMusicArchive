package com.amisam.jdbmusicarchive;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import com.amisam.jdbmusicarchive.datamodel.Songs;
import com.amisam.jdbmusicarchive.engine.FileStorage;
import com.amisam.jdbmusicarchive.datamodel.SongItem;

public class DialogController {
    // Properties
    @FXML
    private TextField trackField;
    @FXML
    private TextArea titleField;
    @FXML
    private TextArea albumField;



    //Methods
    @FXML
    public SongItem processResults(){
        // Retrieves data from the dialog
        int track = FileStorage.stringToInteger(trackField.getText()); //itemPieces[0];
        String title = titleField.getText();
        int album = FileStorage.stringToInteger(albumField.getText());

        SongItem newItem = new SongItem(track, title, album);
        Songs.getInstance().setSongItems(newItem);
        return newItem;
    }
}