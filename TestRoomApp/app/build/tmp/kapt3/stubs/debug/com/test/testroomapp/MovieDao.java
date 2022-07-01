package com.test.testroomapp;

import java.lang.System;

@androidx.room.Dao()
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\bg\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\'J\u0016\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u00072\u0006\u0010\b\u001a\u00020\tH\'J\u000e\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00050\u0007H\'J!\u0010\u000b\u001a\u00020\u00032\u0012\u0010\f\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00050\r\"\u00020\u0005H\'\u00a2\u0006\u0002\u0010\u000eJ&\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00050\u00072\u0016\u0010\u0010\u001a\u0012\u0012\u0004\u0012\u00020\t0\u0011j\b\u0012\u0004\u0012\u00020\t`\u0012H\'\u00a8\u0006\u0013"}, d2 = {"Lcom/test/testroomapp/MovieDao;", "", "delete", "", "movie", "Lcom/test/testroomapp/Movie;", "findByTitle", "", "title", "", "getAll", "insertAll", "movies", "", "([Lcom/test/testroomapp/Movie;)V", "loadAllByTitle", "titles", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "app_debug"})
public abstract interface MovieDao {
    
    @org.jetbrains.annotations.NotNull()
    @androidx.room.Query(value = "SELECT * FROM movie")
    public abstract java.util.List<com.test.testroomapp.Movie> getAll();
    
    @org.jetbrains.annotations.NotNull()
    @androidx.room.Query(value = "SELECT * FROM movie WHERE title IN (:titles)")
    public abstract java.util.List<com.test.testroomapp.Movie> loadAllByTitle(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<java.lang.String> titles);
    
    @org.jetbrains.annotations.NotNull()
    @androidx.room.Query(value = "SELECT * FROM movie WHERE title LIKE :title")
    public abstract java.util.List<com.test.testroomapp.Movie> findByTitle(@org.jetbrains.annotations.NotNull()
    java.lang.String title);
    
    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.IGNORE)
    public abstract void insertAll(@org.jetbrains.annotations.NotNull()
    com.test.testroomapp.Movie... movies);
    
    @androidx.room.Delete()
    public abstract void delete(@org.jetbrains.annotations.NotNull()
    com.test.testroomapp.Movie movie);
}