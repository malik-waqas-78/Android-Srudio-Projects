package com.test.testroomapp;

import java.lang.System;

@androidx.room.Database(entities = {com.test.testroomapp.Movie.class, com.test.testroomapp.MovieActors.class, com.test.testroomapp.MovieDirectors.class, com.test.testroomapp.MovieWriter.class, com.test.testroomapp.MovieGenre.class}, version = 1)
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\'\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&J\b\u0010\u0007\u001a\u00020\bH&J\b\u0010\t\u001a\u00020\nH&J\b\u0010\u000b\u001a\u00020\fH&\u00a8\u0006\r"}, d2 = {"Lcom/test/testroomapp/AppDataBase;", "Landroidx/room/RoomDatabase;", "()V", "movieActorsDao", "Lcom/test/testroomapp/MovieActorsDAO;", "movieDao", "Lcom/test/testroomapp/MovieDao;", "movieDirectorDao", "Lcom/test/testroomapp/MovieDirectorsDao;", "movieGenreDao", "Lcom/test/testroomapp/MovieGenreDao;", "movieWriterDao", "Lcom/test/testroomapp/MovieWriterDao;", "app_debug"})
public abstract class AppDataBase extends androidx.room.RoomDatabase {
    
    public AppDataBase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.test.testroomapp.MovieDao movieDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.test.testroomapp.MovieActorsDAO movieActorsDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.test.testroomapp.MovieWriterDao movieWriterDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.test.testroomapp.MovieDirectorsDao movieDirectorDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.test.testroomapp.MovieGenreDao movieGenreDao();
}