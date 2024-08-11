/*=================================Import Packages==========================*/
package com.amisam.jdbmusicarchive.model;

import com.amisam.jdbmusicarchive.datamodel.SongItem;
/*==================================Import Resources========================*/
import com.amisam.jdbmusicarchive.utilities.ThreadColor;

import java.net.URI;
import java.sql.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*=============================== DataSource =================================
 * DataSource Class Definition
 *
 * -------------------------------------------------------------------------
 * FIELDS:
 * -------------------------------------------------------------------------
 * public: (Database connection): -  DB_NAME, DB_DRIVER, DB_PATH, DB_URL
 * -------                            CONNECTION_NAME, STATEMENT_NAME
 *
 *         (albums) -  TABLE_ALBUMS,COLUMN_ALBUM_ID,COLUMN_ALBUM_NAME,
 *                     COLUMN_ALBUM_ARTIST,albums_schema, albums_columns
 *
 *         (artists) - TABLE_ARTISTS,COLUMN_ARTIST_ID, COLUMN_ARTIST_NAME,
 *                           artists_schema, artists_columns

 *          (songs) -   TABLE_SONGS,COLUMN_SONG_ID,COLUMN_SONG_TRACK,
 *                      COLUMN_SONG_TITLE,COLUMN_SONG_ALBUM,songs_schema,
 *                      song_columns
 *
 *          (artist_list) - TABLE_ARTIST_SONG_VIEW,CREATE_ARTIST_FOR_SONG_VIEW,
 *                          QUERY_VIEW_SONG_INFO_PREP,artist_list_schema
 *
 *          (artists - Insert) -    INSERT_ARTIST
 *
 *          (albums - Insert)  -    INSERT_ALBUMS
 *
 *          (songs - Insert) -      INSERT_SONGS
 *
 *          (query artist) -        QUERY_ARTIST
 *          (query album) -         QUERY_ALBUM
 *
 * privates: (Database connection): - conn
 * --------
 *           (Prepared Statements): - querySongInfoView
 *
 *                                    insertIntoArtists
 *                                    insertIntoAlbums
 *                                    insertIntoSongs
 *
 *                                    queryArtist
 *                                    queryAlbum
 *
 * -------------------------------------------------------------------------
 * METHODS:
 * -------------------------------------------------------------------------
 * public:  public boolean open()
 *          public void close(String resource)
 *          public List<Artist> queryArtists(String select)
 *          public List<Album> queryAlbum(String select)
 *          public List<Song> querySong(String select)
 *          public void querySongMetaData(String select)
 *          public int getCount(String select)
 *          public  List<SongArtist> querySongInfoView(String title)
 *          public boolean createViewForSongArtists()
 *          public void insertSong(String title, String artist,
 *                     String album, int track)
 *
 * privates:    private int insertArtist(String name)
 *              private int insertAlbum(String name, int artistId)
 */

public class DataSource {
    /* ---------------------------- GLOBALS VARIABLES --------------------*/
    /* --------------------------DATABASE-------------------------*/
    public static final String DB_NAME = "/src/main/resources/com/amisam/jdbmusicarchive/music.db";
    public static final String DB_DRIVER = "jdbc:sqlite";
    public static final String DB_PATH = System.getProperty("user.dir");
    public static final String DB_URL = DB_DRIVER + ":" + DB_PATH + DB_NAME;
    public static final String CONNECTION_NAME = "conn";
    public static final String STATEMENT_NAME = "statement";
    private Connection conn;

    /* --------------------TABLES & FIELDS-------------------------*/
    // 1. albums table
    public static final String TABLE_ALBUMS = "albums";
    public static final String COLUMN_ALBUM_ID = "_id";
    public static final String COLUMN_ALBUM_NAME = "name";
    public static final String COLUMN_ALBUM_ARTIST = "artist";

    // albums schema
    public static String albums_schema = "(" +
            COLUMN_ALBUM_ID + " INTEGER, " +
            COLUMN_ALBUM_NAME + " TEXT, " +
            COLUMN_ALBUM_ARTIST + " INTEGER)";
    public static List<String> albums_columns = new ArrayList<>();

    // 2. artists table
    public static final String TABLE_ARTISTS = "artists";
    public static final String COLUMN_ARTIST_ID = "_id";
    public static final String COLUMN_ARTIST_NAME = "name";

    //artists schema
    public static String artists_schema = "(" +
            COLUMN_ARTIST_ID + " INTEGER, " +
            COLUMN_ARTIST_NAME + " TEXT)";
    public static List<String> artists_columns = new ArrayList<>();

    // 3. songs table
    public static final String TABLE_SONGS = "songs";
    public static final String COLUMN_SONG_ID = "_id";
    public static final String COLUMN_SONG_TRACK = "track";
    public static final String COLUMN_SONG_TITLE = "title";
    public static final String COLUMN_SONG_ALBUM = "album";

    // songs schema
    public static String songs_schema = "(" +
            COLUMN_SONG_ALBUM + " INTEGER, " +
            COLUMN_SONG_TITLE + " TEXT, " +
            COLUMN_SONG_TRACK + " INTEGER)";
    public static List<String> song_columns = new ArrayList<>();

    // TABLE VIEWS
    // 4. artist_list
    public static final String TABLE_ARTIST_SONG_VIEW = "artist_list";
    public static final String CREATE_ARTIST_FOR_SONG_VIEW = "CREATE " +
            "VIEW IF NOT EXISTS " + TABLE_ARTIST_SONG_VIEW + " AS SELECT " +
            TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", " +
            TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + " AS " +
            COLUMN_SONG_ALBUM +", "+ TABLE_SONGS + "." + COLUMN_SONG_TRACK+
            ", " + TABLE_SONGS + "." + COLUMN_SONG_TITLE + " FROM " +
            TABLE_SONGS + " INNER JOIN " + TABLE_ALBUMS + " ON " +
            TABLE_SONGS + "." + COLUMN_SONG_ALBUM + " = " + TABLE_ALBUMS +
            "." + COLUMN_ALBUM_ID + " INNER JOIN " + TABLE_ARTISTS + " ON "+
            TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST + " = " +TABLE_ARTISTS +
            "." + COLUMN_ARTIST_ID + " ORDER BY " + TABLE_ARTISTS + "." +
            COLUMN_ARTIST_NAME + ", "+TABLE_ALBUMS + "."+COLUMN_ALBUM_NAME +
            ", "+TABLE_SONGS +"." + COLUMN_SONG_TRACK;

    //Query artist_list
    public static final String QUERY_VIEW_SONG_INFO_PREP = "SELECT "+
            COLUMN_ARTIST_NAME+", " + COLUMN_SONG_ALBUM + ", " +
            COLUMN_SONG_TRACK + " FROM " + TABLE_ARTIST_SONG_VIEW +
            " WHERE " + COLUMN_SONG_TITLE + " = ?";
    // artist_list schema
    public static final String artist_list_schema = "(" +
            COLUMN_ARTIST_NAME + " TEXT, " +
            COLUMN_SONG_ALBUM + " TEXT, " +
            COLUMN_SONG_TRACK + " INTEGER, " +
            COLUMN_SONG_TITLE + " TEXT)";

    // query artist
    public static final String QUERY_ARTIST = "SELECT "+COLUMN_ARTIST_ID+
            " FROM "+TABLE_ARTISTS+" WHERE "+COLUMN_ARTIST_NAME+" = ?";
    // query album
    public static final String QUERY_ALBUM = "SELECT "+COLUMN_ALBUM_ID+
            " FROM "+TABLE_ALBUMS+" WHERE "+COLUMN_ALBUM_NAME+" = ?";

    /*--------------- Insert Records ----------------------*/
    // artists - Insert
    public static final String INSERT_ARTIST = "INSERT INTO "+TABLE_ARTISTS +
            "(" + COLUMN_ARTIST_NAME + ") VALUES(?)";
    // albums - Insert
    public static final String INSERT_ALBUMS = "INSERT INTO "+TABLE_ALBUMS +
            "(" + COLUMN_ALBUM_NAME + ", " + COLUMN_ALBUM_ARTIST+") VALUES"+
            "(?, ?)";
    // songs - Insert
    public static final String INSERT_SONGS = "INSERT INTO " + TABLE_SONGS +
            "("+COLUMN_SONG_TRACK+", "+COLUMN_SONG_TITLE+", " +
            COLUMN_SONG_ALBUM + ") VALUES(?, ?, ?)";

    /* -----------Create Prepared Statements---------------*/
    private PreparedStatement querySongInfoView;

    private PreparedStatement insertIntoArtists;
    private PreparedStatement insertIntoAlbums;
    private PreparedStatement insertIntoSongs;

    private PreparedStatement queryArtist;
    private PreparedStatement queryAlbum;



    /* ----------------------METHODS----------------------------- */

    /*=====================DataSource===============
    * Constructor for DataSource Class
    * */
    public DataSource() {
        song_columns.add(COLUMN_SONG_ALBUM);
        song_columns.add(DataSource.COLUMN_SONG_TITLE);
        song_columns.add(DataSource.COLUMN_SONG_TRACK);

        albums_columns.add(COLUMN_ALBUM_NAME);
        albums_columns.add(COLUMN_ALBUM_ARTIST);

        artists_columns.add(COLUMN_ARTIST_NAME);
    }

    /*===================== Open ====================
    * Open Database Connection
    * Return[boolean]: true on success, false on failure
     */
    public boolean open(){
        try {
            conn = DriverManager.getConnection(DB_URL);
            System.out.println(ThreadColor.GREEN+"Connection Success!"+
                    ThreadColor.RESET);

            /*--------Initialize Prepared Statements------------*/
            querySongInfoView = conn.prepareStatement(QUERY_VIEW_SONG_INFO_PREP);

            /*get generated keys after insertion for later use*/
            insertIntoArtists = conn.prepareStatement(INSERT_ARTIST,
                    Statement.RETURN_GENERATED_KEYS);
            insertIntoAlbums = conn.prepareStatement(INSERT_ALBUMS,
                    Statement.RETURN_GENERATED_KEYS);
            insertIntoSongs = conn.prepareStatement(INSERT_SONGS);

            queryArtist = conn.prepareStatement(QUERY_ARTIST);
            queryAlbum = conn.prepareStatement(QUERY_ALBUM);

            return (true);
        } catch (SQLException e){
            System.out.println(ThreadColor.RED+"Couldn't connect to database: " +
                    e.getMessage()+ThreadColor.RESET);
            return (false);
        }
    }

    /*===================== close ===================
     * Close Database Connection & Statement
     * @resource[String]: Connection name
     * Return: void
     */
    public void close(String resource){
        try { //close resources: PreparedStatement, Connection AND Statement
            //close PreparedStatements
            if (querySongInfoView != null){
                querySongInfoView.close();
            }

            if(insertIntoArtists != null){
                insertIntoArtists.close();
            }
            if(insertIntoAlbums != null){
                insertIntoAlbums.close();
            }
            if(insertIntoSongs != null){
                insertIntoSongs.close();
            }

            if (queryArtist != null){
                queryArtist.close();
            }
            if (queryAlbum != null){
                queryAlbum.close();
            }
            //close connection
            if (resource.equalsIgnoreCase(CONNECTION_NAME)){
                if (conn != null){
                    conn.close();
                }
            } else if (resource.equalsIgnoreCase(STATEMENT_NAME)) {
                //close Statement
                if (conn != null){
                    conn.close();
                }
            }else {
                System.out.println(ThreadColor.RED+"Couldn't close " + resource+
                        ": Check your resource name ["+ThreadColor.BLUE+resource+
                        ThreadColor.RED+"]"+ThreadColor.RESET);
                return;
            }
            System.out.println(ThreadColor.GREEN+resource + " closed "+
                    ThreadColor.RESET);

        } catch (SQLException e){
            System.out.println(ThreadColor.RED+"Couldn't close " + resource+": "+
                    e.getMessage()+ThreadColor.RESET);
        }
    }

    /* --------------------QUERIES-------------------*/
    /*=================queryArtists==================
    * Queries Artists table
    * @select[String]: SQL select statement
    * Return[List]: List of artists (id, name)
    */
    public List<Artist> queryArtists(String select) {
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(select)) {
            int i = 0;

            List<Artist> artists = new ArrayList<>();
            while (results.next()) {
                i++;
                /*create artist*/
                Artist artist = new Artist();

                artist.setId(results.getInt(COLUMN_ARTIST_ID));
                artist.setName(results.getString(COLUMN_ARTIST_NAME));

                /*Fill artists*/
                artists.add(artist);
            }
            System.out.println("------------------------------------------");
            System.out.println(i + " artists retrieved from Artists database.");
            System.out.println("==========================================");

            return (artists);
        } catch (SQLException e) {
            System.out.println(ThreadColor.RED+"Couldn't create statement"+
                    ThreadColor.RESET);
            e.printStackTrace();
            return (null);
        }
    }

    /*=================queryAlbum==================
     * Queries Album table
     * @select[String]: SQL select statement
     * Return[List]: List of albums (id, name, artist_id)
     */
    public List<Album> queryAlbum(String select){
        try (Statement statement = conn.createStatement();
            ResultSet results = statement.executeQuery(select)){
            int i = 0;

            /*create album*/
            List<Album> albums = new ArrayList<>();
            while (results.next()){
                i++;

                Album album = new Album();

                album.setId(results.getInt(COLUMN_ALBUM_ID));
                album.setName(results.getString(COLUMN_ALBUM_NAME));
                album.setArtist_id(results.getInt(COLUMN_ALBUM_ARTIST));
                album.setSchema(albums_schema);

                albums.add(album);
            }

            System.out.println("----------------------------------------");
            System.out.println(i + " albums retrieved from Album database.");
            System.out.println("=========================================");
            return (albums);

        } catch (SQLException e){
            System.out.println(ThreadColor.RED+"Couldn't create statement"+
                    ThreadColor.RESET);
            e.printStackTrace();
            return (null);
        }
    }

    /*=================querySong==================
     * Queries Song table
     * @select[String]: SQL select statement
     * Return[List]: List of songs (id, track, title, album_id)
     */
    public List<Song> querySong(String select){
        try (Statement statement = conn.createStatement();
            ResultSet results = statement.executeQuery(select)){
            int i = 0;

            List<Song> songs = new ArrayList<>();
            while (results.next()){
                i++;

                Song song = new Song();

                song.setId(results.getInt(COLUMN_SONG_ID));
                song.setTract_id(results.getInt(COLUMN_SONG_TRACK));
                song.setTitle(results.getString(COLUMN_SONG_TITLE));
                song.setAlbum_id(results.getInt(COLUMN_ALBUM_ID));

                songs.add(song);
            }
            return (songs);

        } catch (SQLException e){
            System.out.println(ThreadColor.RED+"Couldn't create statement"+
                    ThreadColor.RESET);
            e.printStackTrace();
            return (null);
        }
    }

    /*=================querySongMetaData=======
     * Queries Song table for its MetaData info
     * @select[String]: SQL select statement
     * Return: void
     */
    public void querySongMetaData(String select){
        int i;

        try (Statement statement = conn.createStatement();
        ResultSet results = statement.executeQuery(select)) {

            ResultSetMetaData meta = results.getMetaData();
            int numColumns = meta.getColumnCount();
            if (numColumns == 0){
                System.out.println(ThreadColor.RED+"No columns found in the "+
                        "songs table"+ThreadColor.RESET);
                return;
            }
            // Map<String, Object> songs = new HashMap<>();

            // for (i = 1; i <= numColumns; i++){
                
            //     // songs.put(meta.getColumnName(i), meta.getColumnType(i));
            //     System.out.format("Column "+ThreadColor.YELLOW+"%d"+ThreadColor.RESET+" in the songs table is names"+ThreadColor.YELLOW+" %s\n"+ThreadColor.RESET,
            //             i, meta.getColumnName(i));
            // }
        } catch (SQLException e){
            System.out.println(ThreadColor.RED+"Couldn't create statement"+
                    ThreadColor.RESET);
            e.printStackTrace();
        }
    }

    /*=================getCount============================
     * Gets the total counts of records from a queries
     * @select[String]: SQL select statement
     * Return[int]: total counts based on a query statement
     */
    public int getCount(String select){

        try (Statement statement = conn.createStatement();
        ResultSet results = statement.executeQuery(select)){

            return (results.getInt(1));

        } catch (SQLException e){
            System.out.println(ThreadColor.RED+"Couldn't create statement"+
                    ThreadColor.RESET);
            e.printStackTrace();
        }
        return (-1);
    }

    /*------------------------QUERY VIEWS------------------*/
    /*=================querySongInfoView============================
     * Gets the total counts of records from a queries
     * @title[String]: Song's title to be queried
     * Return[SongArtist]: a list of SongArtists
     */
    public  List<SongArtist> querySongInfoView(String title){
        System.out.println("Querying \""+ title +"\"...");

        try{
            int i = 0;

            //pre-compile SQL statement
            querySongInfoView.setString(1, title);
            //execute the pre-compiled SQL statement
            ResultSet results = querySongInfoView.executeQuery();

            List<SongArtist> songArtists = new ArrayList<>();
            while(results.next()){
                i++;
                SongArtist songArtist = new SongArtist();
                songArtist.setArtistName(results.getString(1));
                songArtist.setAlbumName(results.getString(2));
                songArtist.setTrack(results.getInt(3));

                songArtists.add(songArtist);
            }

            System.out.println("----------------------------------------");
            System.out.println(i + " artists retrieved from artist_list view table.");
            System.out.println("=========================================");
            return (songArtists);

        } catch (SQLException e){
            System.out.println(ThreadColor.RED+"Couldn't create statement"+
                    ThreadColor.RESET);
            e.printStackTrace();

            return (null);
        }
    }



    /* ------------------------CREATE----------------------------------- */
    /* =================createViewForSongArtists=======================
    * Creates View table for SongArtists
    * Return[boolean]: true on success, false on failure
    */
    public boolean createViewForSongArtists() {
        try (Statement statement = conn.createStatement()) {

           statement.execute(CREATE_ARTIST_FOR_SONG_VIEW);

            return (true);

        } catch (SQLException e) {
            System.out.println(ThreadColor.RED +"Couldn't create statement" +
                    ThreadColor.RESET);
            e.printStackTrace();
            return (false);
        }
    }

    /* ------------------------INSERTS----------------------------------- */
    /* ======================insertArtist===============================
    * Inserts record into Artists table
    * @name: Artist's name
    * Throws: SQLException
    * Return[int]: _id of generated record, or _id of existing artist
    */
    private int insertArtist(String name) throws SQLException {
        int affectedRows;
        ResultSet results, generatedKeys;

        /*------------ 1. query the artist if exists--------------*/
        //build sql statement - query
        queryArtist.setString(1, name);
        //execute sql statement - query
        results = queryArtist.executeQuery();
        if (results.next()){
            return (results.getInt(1));
        } else {
            /*-------- 2. insert into artist if not exists-------*/
            //build sql statement - insert
            insertIntoArtists.setString(1, name);
            //execute sql statement - insert
            affectedRows = insertIntoArtists.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException(ThreadColor.RED +"Couldn't insert "+
                        "artist: "+name+"!"+ThreadColor.RESET);
            }
            //retrieve _id from the newly created record
            generatedKeys = insertIntoArtists.getGeneratedKeys();
            if (generatedKeys.next()) {
                return (generatedKeys.getInt(1));
            } else {
                throw  new SQLException(ThreadColor.RED +"Couldn't get _id "+
                        "for artist: "+name+"!"+ThreadColor.RESET);
            }
        }
    }

    /* ======================insertAlbum===============================
     * Inserts record into Artists table
     * @name[String]: Artist's name
     * @artistId[int]: Artist's _id
     * Throws: SQLException
     * Return[int]: _id of generated record, or _id of existing album
     */
    private int insertAlbum(String name, int artistId) throws SQLException {
        int affectedRows;
        ResultSet results, generatedKeys;

        /*------------ 1. query the album if exists--------------*/
        //build sql statement - query
        queryAlbum.setString(1, name);
        //execute sql statement - query
        results = queryAlbum.executeQuery();
        if (results.next()){
            return (results.getInt(1));
        } else {
            /*-------- 2. insert into album if not exists-------*/
            //build sql statement - insert
            insertIntoAlbums.setString(1, name);
            //execute sql statement - insert
            affectedRows = insertIntoAlbums.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException(ThreadColor.RED +"Couldn't insert "+
                        "album: "+name+"!"+ThreadColor.RESET);
            }
            //retrieve _id from the newly created record
            generatedKeys = insertIntoAlbums.getGeneratedKeys();
            if (generatedKeys.next()) {
                return (generatedKeys.getInt(1));
            } else {
                throw  new SQLException(ThreadColor.RED +"Couldn't get _id "+
                        "for album: "+name+"!"+ThreadColor.RESET);
            }
        }
    }

    /* ======================insertSong===============================
     * Inserts record into Artists table
     * @title[String]: Song's title
     * @artist: Artist's name
     * @album[String]: Album name
     * @track[int]: Track's _id
     * Return: void
     */
    public void insertSong(String title, String artist, String album,
                           int track){
        try{
            int artistId, albumId, affectedRows;

            //1. mute Autocommit before insertion of record
            System.out.println("Setting Default (true) auto-commit off");
            conn.setAutoCommit(false);

            //get _ids of artist and album from Artist and Album tables
            //using their insertion methods: insertArtist and insertAlbum

            //NB: Song is not first Queried. This will cause duplicates in db
            // when insertion is done

            //1.1.build sql statement - insert-query song
            artistId = insertArtist(artist);
            albumId = insertAlbum(album, artistId);

            //1.2. build sql statement - insert song
            insertIntoSongs.setInt(1, track);
            insertIntoSongs.setString(2, title);
            insertIntoSongs.setInt(3, albumId);

            //2. execute sql statement - insert song
            affectedRows = insertIntoSongs.executeUpdate();
            if (affectedRows == 1) {
                //TRANSACTION SUCCESSFUL
                //commit transaction and all changes made
                conn.commit();
            } else {
                throw new SQLException(ThreadColor.RED +"Couldn't insert "+
                        "song: "+title+"!"+ThreadColor.RESET);
            }
        } catch (Exception e){
            System.out.println("Insert Song exception: "+e.getMessage());
            //row back earlier transactions: insertArtist(artist); and
            //insertAlbum(album, artistId);
            try {
                System.out.println("Performing rollback...");
                conn.rollback();
            } catch (SQLException e2) {
                System.out.println("Rollback problem! "+ e2.getMessage());
            }
        } finally {
            //2. set Autocommit back on
            try {
                System.out.println("Setting Autocommit back on to default");
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Couldn't reset auto-commit back to default" +
                        "(true)! "+e.getMessage());
            }
        }
    }
}