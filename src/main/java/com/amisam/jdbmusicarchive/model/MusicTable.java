package com.amisam.jdbmusicarchive.model;

import java.util.ArrayList;
import java.util.List;

public class MusicTable {
    private String table_name;
    private String schema;
    private List<String> columns = new ArrayList<>();
    private String fields;
    private Integer count = 0;

    /* Constructor */
    public MusicTable(String name) {
        this.table_name = name;
    }

    /* Methods */

    public void create_schema(String schema) {
        this.schema = schema;
    }

    public void setFields(List<String> columns) {
        Integer i;
        StringBuilder sb = new StringBuilder("(");

        for (i = 0; i < columns.size(); i++){
            this.columns.add(columns.get(i));

            if (i + 1 == columns.size()){
                sb.append(columns.get(i) + ")");
            } else {
                sb.append(columns.get(i) + ", ");
            }
        }
        this.fields = sb.toString();
    }

    public List<String> getColumns() {
        return columns;
    }

    public String getTable_name() {
        return table_name;
    }

    public String getSchema() {
        return schema;
    }

    public String getFields() {
        return fields;
    }

}
