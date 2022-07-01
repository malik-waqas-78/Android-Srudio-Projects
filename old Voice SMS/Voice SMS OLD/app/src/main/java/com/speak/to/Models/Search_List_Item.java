package com.speak.to.Models;

public class Search_List_Item {
    private int row_icon;
    private int row_title;

    public Search_List_Item(int row_icon, int row_title) {
        this.row_icon = row_icon;
        this.row_title = row_title;
    }

    public int getRow_icon() {
        return row_icon;
    }

    public void setRow_icon(int row_icon) {
        this.row_icon = row_icon;
    }

    public int getRow_title() {
        return row_title;
    }

    public void setRow_title(int row_title) {
        this.row_title = row_title;
    }
}
