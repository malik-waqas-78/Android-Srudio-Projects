package com.example.cst2335_final.beans;

import java.net.URL;

/**
 * Stores title, url and section from a search on Guardian News
 */

public class SearchItem {

    private long id;
    private String title;
    private URL url;
    private String section;

    public SearchItem(){
        this.title = null;
        this.url = null;
        this.section = null;
    }

    public SearchItem(String title, URL url, String section, long id){
        this.title = title;
        this.url = url;
        this.section = section;
        this.id = id;
    }

    /**
     * setter for id
     * @param id searchItem Id
     */
    public void setId(long id) {this.id = id;}

    /**
     * setter for title
     * @param title searchItem's Title
     */
    public void setTitle(String title){ this.title = title; }

    /**
     * setter for url
     * @param url searchItem's URL
     */
    public void setUrl(URL url){ this.url = url; }

    /**
     * setter for section
     * @param section searchItems's Section
     */
    public void setSection(String section){ this.section = section; }

    /**
     * getter for ID
     * @return searchItem's Id
     */
    public long getId(){return id;}

    /**
     * getter for title
     * @return searchItem's title
     */
    public String getTitle(){ return title;}

    /**
     * getter for url
     * @return searchItem's url
     */
    public URL getUrl(){ return url; }

    /**
     * getter for section
     * @return searchItem's section
     */
    public String getSection(){ return section;}
}
