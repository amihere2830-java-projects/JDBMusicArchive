package com.amisam.jdbmusicarchive.model;

public class SQLStatement {
    private  MusicTable table;

    public SQLStatement(MusicTable table) {
        this.table = table;
    }

    public MusicTable getTable() {
        return table;
    }

    public String createStmt(){

        return "CREATE TABLE IF NOT EXISTS " +
                this.table.getTable_name() + " " +
                this.table.getSchema();
    }

    public String createStmt(String values_insert){

        return "INSERT INTO " +
                this.table.getTable_name() + " " +
                this.table.getFields() + " " +
                "VALUES" +
                values_insert;
    }

    public String createStmt(String update_value, String where){

        return "UPDATE " +
                this.table.getTable_name() + " " +
                "SET " +
                update_value + " " +
                "WHERE " +
                where;
    }

    /*Delete record using where*/
    public String createStmt(String where, boolean del){
        if (del){
            return "DELETE FROM " +
                    this.table.getTable_name() + " " +
                    "WHERE " +
                    where;
        } else return "";
    }

    /*Query table without where clause*/
    public String queryStmt(String headers){
        return "SELECT " +
                headers + " " +
                "FROM " +
                this.table.getTable_name();
    }

    /*Query table using where clause*/
    public String queryStmt(String headers, String where){
        return "SELECT " +
                headers + " " +
                "FROM " +
                this.table.getTable_name() + " " +
                "WHERE " +
                where;
    }

    public String dropTable_stmt(){
        return "DROP TABLE IF EXISTS " +
                this.table.getTable_name();
    }
}
