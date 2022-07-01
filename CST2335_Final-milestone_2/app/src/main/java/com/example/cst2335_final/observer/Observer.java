package com.example.cst2335_final.observer;

import com.example.cst2335_final.beans.SearchItem;

import java.util.ArrayList;

public interface Observer {
    void update(ArrayList<SearchItem> searchItems);
    void update(Integer progress);
}
