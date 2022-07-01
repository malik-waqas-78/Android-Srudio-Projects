package com.test.testroomapp;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0005\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0004"}, d2 = {"Lcom/test/testroomapp/DataBaseManager;", "", "()V", "Companion", "app_debug"})
public final class DataBaseManager {
    @org.jetbrains.annotations.NotNull()
    public static final com.test.testroomapp.DataBaseManager.Companion Companion = null;
    @org.jetbrains.annotations.Nullable()
    private static com.test.testroomapp.AppDataBase db;
    @org.jetbrains.annotations.Nullable()
    private static com.test.testroomapp.MovieDao movieDao;
    @org.jetbrains.annotations.Nullable()
    private static com.test.testroomapp.MovieWriterDao movieWriterDao;
    @org.jetbrains.annotations.Nullable()
    private static com.test.testroomapp.MovieDirectorsDao movieDirectorsDao;
    @org.jetbrains.annotations.Nullable()
    private static com.test.testroomapp.MovieActorsDAO movieActorsDAO;
    @org.jetbrains.annotations.Nullable()
    private static com.test.testroomapp.MovieGenreDao movieGenreDao;
    
    public DataBaseManager() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010\'\u001a\u0004\u0018\u00010\nJ\b\u0010(\u001a\u0004\u0018\u00010\u0010J\b\u0010)\u001a\u0004\u0018\u00010\u0016J\b\u0010*\u001a\u0004\u0018\u00010\u001cJ\b\u0010+\u001a\u0004\u0018\u00010\"J\u0010\u0010,\u001a\u0004\u0018\u00010\u00042\u0006\u0010-\u001a\u00020.J\u000e\u0010/\u001a\u0002002\u0006\u00101\u001a\u00020.R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001c\u0010\t\u001a\u0004\u0018\u00010\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001c\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u001c\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR\u001c\u0010\u001b\u001a\u0004\u0018\u00010\u001cX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u001e\"\u0004\b\u001f\u0010 R\u001c\u0010!\u001a\u0004\u0018\u00010\"X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b#\u0010$\"\u0004\b%\u0010&\u00a8\u00062"}, d2 = {"Lcom/test/testroomapp/DataBaseManager$Companion;", "", "()V", "db", "Lcom/test/testroomapp/AppDataBase;", "getDb", "()Lcom/test/testroomapp/AppDataBase;", "setDb", "(Lcom/test/testroomapp/AppDataBase;)V", "movieActorsDAO", "Lcom/test/testroomapp/MovieActorsDAO;", "getMovieActorsDAO", "()Lcom/test/testroomapp/MovieActorsDAO;", "setMovieActorsDAO", "(Lcom/test/testroomapp/MovieActorsDAO;)V", "movieDao", "Lcom/test/testroomapp/MovieDao;", "getMovieDao", "()Lcom/test/testroomapp/MovieDao;", "setMovieDao", "(Lcom/test/testroomapp/MovieDao;)V", "movieDirectorsDao", "Lcom/test/testroomapp/MovieDirectorsDao;", "getMovieDirectorsDao", "()Lcom/test/testroomapp/MovieDirectorsDao;", "setMovieDirectorsDao", "(Lcom/test/testroomapp/MovieDirectorsDao;)V", "movieGenreDao", "Lcom/test/testroomapp/MovieGenreDao;", "getMovieGenreDao", "()Lcom/test/testroomapp/MovieGenreDao;", "setMovieGenreDao", "(Lcom/test/testroomapp/MovieGenreDao;)V", "movieWriterDao", "Lcom/test/testroomapp/MovieWriterDao;", "getMovieWriterDao", "()Lcom/test/testroomapp/MovieWriterDao;", "setMovieWriterDao", "(Lcom/test/testroomapp/MovieWriterDao;)V", "getMAD", "getMD", "getMDD", "getMGD", "getMWD", "getMovieDB", "context", "Landroid/content/Context;", "initMovieDB", "", "applicationContext", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.Nullable()
        public final com.test.testroomapp.AppDataBase getDb() {
            return null;
        }
        
        public final void setDb(@org.jetbrains.annotations.Nullable()
        com.test.testroomapp.AppDataBase p0) {
        }
        
        @org.jetbrains.annotations.Nullable()
        public final com.test.testroomapp.MovieDao getMovieDao() {
            return null;
        }
        
        public final void setMovieDao(@org.jetbrains.annotations.Nullable()
        com.test.testroomapp.MovieDao p0) {
        }
        
        @org.jetbrains.annotations.Nullable()
        public final com.test.testroomapp.MovieWriterDao getMovieWriterDao() {
            return null;
        }
        
        public final void setMovieWriterDao(@org.jetbrains.annotations.Nullable()
        com.test.testroomapp.MovieWriterDao p0) {
        }
        
        @org.jetbrains.annotations.Nullable()
        public final com.test.testroomapp.MovieDirectorsDao getMovieDirectorsDao() {
            return null;
        }
        
        public final void setMovieDirectorsDao(@org.jetbrains.annotations.Nullable()
        com.test.testroomapp.MovieDirectorsDao p0) {
        }
        
        @org.jetbrains.annotations.Nullable()
        public final com.test.testroomapp.MovieActorsDAO getMovieActorsDAO() {
            return null;
        }
        
        public final void setMovieActorsDAO(@org.jetbrains.annotations.Nullable()
        com.test.testroomapp.MovieActorsDAO p0) {
        }
        
        @org.jetbrains.annotations.Nullable()
        public final com.test.testroomapp.MovieGenreDao getMovieGenreDao() {
            return null;
        }
        
        public final void setMovieGenreDao(@org.jetbrains.annotations.Nullable()
        com.test.testroomapp.MovieGenreDao p0) {
        }
        
        @org.jetbrains.annotations.Nullable()
        public final com.test.testroomapp.AppDataBase getMovieDB(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final com.test.testroomapp.MovieWriterDao getMWD() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final com.test.testroomapp.MovieDirectorsDao getMDD() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final com.test.testroomapp.MovieDao getMD() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final com.test.testroomapp.MovieActorsDAO getMAD() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final com.test.testroomapp.MovieGenreDao getMGD() {
            return null;
        }
        
        public final void initMovieDB(@org.jetbrains.annotations.NotNull()
        android.content.Context applicationContext) {
        }
    }
}