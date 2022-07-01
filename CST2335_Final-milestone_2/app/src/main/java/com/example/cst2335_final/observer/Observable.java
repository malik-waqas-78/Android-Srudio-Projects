package com.example.cst2335_final.observer;

import com.example.cst2335_final.beans.SearchItem;

import java.lang.reflect.Array;
import java.util.ArrayList;

public interface Observable {
    void registerObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers(ArrayList<SearchItem> searchItems);
}
