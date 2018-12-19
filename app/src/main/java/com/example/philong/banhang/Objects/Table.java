package com.example.philong.banhang.Objects;

import java.io.Serializable;

public class Table {
    private String table_name;
    private String table_describe;

    public Table(String table_name, String table_describle) {
        this.table_name = table_name;
        this.table_describe = table_describle;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getTable_describe() {
        return table_describe;
    }

    public void setTable_describe(String table_describe) {
        this.table_describe = table_describe;
    }
}
