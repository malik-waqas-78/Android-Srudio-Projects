package com.test.testroomapp;

import java.lang.System;

@androidx.room.Dao()
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u0011\n\u0002\b\u0002\bg\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\'J\u0016\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u00072\u0006\u0010\b\u001a\u00020\tH\'J\u0016\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00050\u00072\u0006\u0010\u000b\u001a\u00020\tH\'J\u000e\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00050\u0007H\'J!\u0010\r\u001a\u00020\u00032\u0012\u0010\u000e\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00050\u000f\"\u00020\u0005H\'\u00a2\u0006\u0002\u0010\u0010\u00a8\u0006\u0011"}, d2 = {"Lcom/test/testroomapp/MovieActorsDAO;", "", "delete", "", "movieActors", "Lcom/test/testroomapp/MovieActors;", "findActorByMovieTitle", "", "movieTitle", "", "findMovieTitleByActor", "actorName", "getAll", "insertAll", "moviesActor", "", "([Lcom/test/testroomapp/MovieActors;)V", "app_debug"})
public abstract interface MovieActorsDAO {
    
    @org.jetbrains.annotations.NotNull()
    @androidx.room.Query(value = "SELECT * FROM movieActors")
    public abstract java.util.List<com.test.testroomapp.MovieActors> getAll();
    
    @org.jetbrains.annotations.NotNull()
    @androidx.room.Query(value = "SELECT * FROM movieActors WHERE actor_name Like :actorName")
    public abstract java.util.List<com.test.testroomapp.MovieActors> findMovieTitleByActor(@org.jetbrains.annotations.NotNull()
    java.lang.String actorName);
    
    @org.jetbrains.annotations.NotNull()
    @androidx.room.Query(value = "SELECT * FROM movieActors WHERE movie_title LIKE :movieTitle")
    public abstract java.util.List<com.test.testroomapp.MovieActors> findActorByMovieTitle(@org.jetbrains.annotations.NotNull()
    java.lang.String movieTitle);
    
    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.IGNORE)
    public abstract void insertAll(@org.jetbrains.annotations.NotNull()
    com.test.testroomapp.MovieActors... moviesActor);
    
    @androidx.room.Delete()
    public abstract void delete(@org.jetbrains.annotations.NotNull()
    com.test.testroomapp.MovieActors movieActors);
}