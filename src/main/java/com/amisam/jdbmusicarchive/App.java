package com.amisam.jdbmusicarchive;
import com.amisam.jdbmusicarchive.engine.DBStorage;
import com.amisam.jdbmusicarchive.engine.FileStorage;


public class App {
	
	/* ---------------------------- GLOBALS VARIABLES --------------------*/
    /* FLAGS */
    public static boolean artists_flag = false;
    public static boolean albums_flag = false;
    public static boolean songs_flag = false;
    public static boolean artist_list_info = false;
    public static boolean insert_records_flag = false;

    // File Storage
    public static String filename = "TodoListItem.txt";
    public static String storageType = "db";
    
    /* ---------------------------------METHODS---------------------------*/

    /*==================> main <=================
    * Entry Point
    * @args: Terminal Arguments
    * Return: void
    */
	


    public static void main(String[] args) {

        /**-----------------------------
         * Launch the Music App application
         * -----------------------------
         */

        MusicUI.launchMusic();

        }

    public static Object getStorage() {
    return storageType.equals("file") ? 
            new FileStorage() :
                new DBStorage();
    }
}