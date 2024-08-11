package com.amisam.jdbmusicarchive;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import com.amisam.jdbmusicarchive.datamodel.Songs;
import com.amisam.jdbmusicarchive.datamodel.SongItem;
import com.amisam.jdbmusicarchive.model.Song;

public class Controller {
    // private List<SongItem> songItems;

    @FXML
    private ListView<SongItem> songListView;

    @FXML
    private TextArea titleTextArea;

    @FXML
    private Label albumLabel;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private ContextMenu listContextMenu;

    @FXML
    private ToggleButton filterToggleButton;

    private FilteredList<SongItem> filteredList;

    private Predicate<SongItem> wantAllItems;

    private Predicate<SongItem> wantTodayItems;

    private boolean addFlag = true;



    //App initializer
    public void initialize(){
        // initialize ContextMenu
        listContextMenu = new ContextMenu();
        //Create delete menu item
        MenuItem deleteMenuItem = new MenuItem("Delete       Delete");
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //choose what to delete and delete
                SongItem item  = songListView.getSelectionModel().getSelectedItem();
                deleteSong(item);
            }
        });


        //add the deleteMenuItem  item itself to our context menu.
        listContextMenu.getItems().addAll(deleteMenuItem);


        //Attach Listener to the selected item
        songListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<SongItem>() {
            @Override
            public void changed(ObservableValue<? extends SongItem> observableValue, SongItem song, SongItem newValue) {
                // System.out.println("============================");
                // System.out.println("Title: "+song.getTitle());
                // System.out.println("============================");
                if (newValue != null){
                    song = songListView.getSelectionModel().getSelectedItem();
                    titleTextArea.setText(song.getTitle());
                    albumLabel.setText(song.getAlbum().toString());

                }
            }
        });

        wantAllItems = new Predicate<SongItem>() {
            @Override
            public boolean test(SongItem song) {
                return true;
            }
        };
        wantTodayItems = new Predicate<SongItem>() {
            @Override
            public boolean test(SongItem song) {
                return (song.getAlbum().equals(1));
            }
        };

        try{
            //filtering listView items the items and the Predicate (criteria to filter)
            //override the test method by adding the filter criteria
            filteredList = new FilteredList<>(Songs.getInstance().getSongItems(), wantAllItems);

            //Sorting listView items using the items and the Comparator
            //override the compare method  by adding the appropriate benchmark (deadlineValues)
            SortedList<SongItem> sortedList = new SortedList<SongItem>(filteredList,        //"TodoData.getInstance().getTodoItems()" can be used inplace of filteredList
                    new Comparator<SongItem>() {
                        @Override
                        public int compare(SongItem song, SongItem t1) {
                            return song.getTitle().compareTo(t1.getTitle());
                        }
                    });

            // populate listView on window with the to do items through the Singleton class
    //        todoListView.setItems(TodoData.getInstance().getTodoItems());
            songListView.setItems(sortedList);     //use sortedList
        } catch (Exception e){
            System.out.println(e.getMessage());
        }




        // set the listView selection mode to single selection
        songListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        //select the first item on the list when the app is lunched
        songListView.getSelectionModel().selectFirst();

        //Adding cell factories
        //Edit the callback method by creating object of ListCell.
        songListView.setCellFactory(new Callback<ListView<SongItem>, ListCell<SongItem>>() {
            @Override
            public ListCell<SongItem> call(ListView<SongItem> todoItemListView) {
                ListCell<SongItem> cell = new ListCell<>(){
                    //Override the updateItem (This method runs whenever
                    // the ListView wants to paint a single cell)
                    @Override
                    protected void updateItem(SongItem song, boolean empty) {
                        super.updateItem(song, empty);
                        //
                        if(empty){
                            setText(null);
                        } else {
                            setText(song.getTitle()); //This adds the required String format. No need to override it in the TodoItem class
                        }
                    }
                };

                //Attach ContextMenu instance (listContextMenu)
                cell.emptyProperty().addListener(
                        (obs, wasEmpty, isNowEmpty) -> {
                            if (isNowEmpty) {
                                cell.setContextMenu(null);
                            } else {
                                cell.setContextMenu(listContextMenu);
                            }
                        });

                return cell;
            }
        });

    }

    @FXML
    public void showNewItemDialog(){
        Dialog<ButtonType> dialog = new Dialog<>();  // ButtonType: a resultConverter property
                                                    // this is required whenever the R type is not void
        dialog.initOwner(mainBorderPane.getScene().getWindow());

        //Add title to the dialog pane
        dialog.setTitle("Add new Todo Item");
        //Add headerText to the dialog pane
        dialog.setHeaderText("Use this dialog to create a new todo item.");

        //Load the dialog FMX file (todoItemDialog.fxml) like how the mainwindow.fxml file was loaded in the main class
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("todoItemDialog.fxml"));
        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch(IOException e){
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }
        // Add buttons to the dialog pane (e.g. ok and cancel button)
        // custom buttons can as well be created
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        // display the dialog
        // two methods: "showAndWait" and "show". "showAndWait" will
        // bring up a blocking dialog whilst show will bring up a non-blocking
        // dialog. Thus, show() will return immediately after showing the dialogue.
        // It won't wait for the user to press ok or cancel buttons. Code is required
        // to pull the dialog for our results. This is often not required, especilly in this project.
        // The showAndWait() will let the event handler get suspended until ok
        // or cancel button is pressed for the event handler to receive results of
        // the dialog and resume executing.

        //Get the pressed button
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get()==ButtonType.OK) {
            //Retrieve data from the dialog using the custom DialogController class
            DialogController controller = fxmlLoader.getController();
            SongItem newItem = controller.processResults();       //Calling this custom method will retrieve
                                                                  // and persist the data from the dialog

            //select the current item added to our todoListView
            songListView.getSelectionModel().select(newItem);
        }

    }


    @FXML
    public void iconDeleteItem(){
        //Use toolbar icon to handle delete

        SongItem selectedItem = songListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null){
            deleteSong(selectedItem);
        }
    }

    @FXML
    public  void handleClickListView(){
        SongItem song = songListView.getSelectionModel().getSelectedItem();
        titleTextArea.setText(song.toString());
        albumLabel.setText(song.getAlbum().toString());

    }


    @FXML
    public void handleFilterButton(){
        SongItem selectedItem = songListView.getSelectionModel().getSelectedItem();

        //use filtering criteria to filter list
        if (filterToggleButton.isSelected()){
            // change css background color of the selected button
            if (addFlag){
                filterToggleButton.setStyle("-fx-text-fill: #7c7878; -fx-background-color: white; -fx-border-color: #7c7878; -fx-border-width: 1px; -fx-border-radius: 2px;");
                filterToggleButton.setText("all");
                addFlag = false;
            }
            
            filteredList.setPredicate(wantTodayItems);
            if (filteredList.isEmpty()){
                titleTextArea.clear();
                albumLabel.setText("");
            } else if (filteredList.contains(selectedItem)){
                songListView.getSelectionModel().select(selectedItem);
            } else {
                songListView.getSelectionModel().selectFirst();
            }
        } else {

            filteredList.setPredicate(wantAllItems);

            songListView.getSelectionModel().select(selectedItem);
            if (!addFlag){
                filterToggleButton.setStyle("-fx-text-fill: white; -fx-background-color: #7c7878; -fx-border-color: #7c7878; -fx-border-width: 1px; -fx-border-radius: 1px;");
                // change button's text to "today"
                filterToggleButton.setText("today");
                addFlag = true;
            } 
        }
    }

    @FXML
    public void deleteSong(SongItem song){
        //handles deletion of selected item
        //provide confirmation dialog (alert) before user goes ahead to delete an item

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        //set alert dialog properties (Title, HeaderText, ContentText)
        alert.setTitle("Delete Todo Item");
        alert.setHeaderText("Delete: "+song.getTitle());
        alert.setContentText("Are you sure? Press Ok to confirm, or cancel to Back out");

        //show dialog
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && (result.get() == ButtonType.OK)){
            //proceed to delete the item
            Songs.getInstance().deleteSong(song);
        }
    }
    @FXML
    public void handleExit () {
        Platform.exit();
    }

    @FXML
    public void handleKeyPressed (KeyEvent keyEvent) {
        //Exit app with keys (Ctrl + Q)
        if (keyEvent.getCode() == KeyCode.Q && keyEvent.isControlDown()) {
            handleExit ();
        }

        //deleting item with key (delete key) pressed
        SongItem selectedItem = songListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null){
            if (keyEvent.getCode().equals(KeyCode.DELETE)){
                deleteSong(selectedItem);
            }
        }

        //open dialog with keys (Ctrl+N) to add new item
        if (keyEvent.getCode() == KeyCode.N && keyEvent.isControlDown()) {
            showNewItemDialog();
        }
    }
}