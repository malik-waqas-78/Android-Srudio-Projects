package com.test.testroomapp;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u0000 \u001f2\u00020\u0001:\u0001\u001fB\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0010\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016H\u0002J\u0012\u0010\u0017\u001a\u00020\u00142\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0014J\u0010\u0010\u001a\u001a\u00020\u00142\u0006\u0010\u001b\u001a\u00020\u0019H\u0014J\u0019\u0010\u001c\u001a\u00020\u00142\u0006\u0010\u001d\u001a\u00020\u0004H\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001eR\u0014\u0010\u0003\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u001a\u0010\u0007\u001a\u00020\bX\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR \u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006 "}, d2 = {"Lcom/test/testroomapp/SearchMovieByString;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "apiKey", "", "getApiKey", "()Ljava/lang/String;", "binding", "Lcom/test/testroomapp/databinding/ActivitySearchMovieByStringBinding;", "getBinding", "()Lcom/test/testroomapp/databinding/ActivitySearchMovieByStringBinding;", "setBinding", "(Lcom/test/testroomapp/databinding/ActivitySearchMovieByStringBinding;)V", "movieDetails", "Ljava/util/ArrayList;", "Lcom/test/testroomapp/MovieRowModel;", "getMovieDetails", "()Ljava/util/ArrayList;", "setMovieDetails", "(Ljava/util/ArrayList;)V", "", "array", "Lorg/json/JSONObject;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onSaveInstanceState", "outState", "searchMovie", "title", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "app_debug"})
public final class SearchMovieByString extends androidx.appcompat.app.AppCompatActivity {
    public com.test.testroomapp.databinding.ActivitySearchMovieByStringBinding binding;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String apiKey = "577cd78d";
    @org.jetbrains.annotations.NotNull()
    private java.util.ArrayList<com.test.testroomapp.MovieRowModel> movieDetails;
    @org.jetbrains.annotations.NotNull()
    public static final com.test.testroomapp.SearchMovieByString.Companion Companion = null;
    
    public SearchMovieByString() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.test.testroomapp.databinding.ActivitySearchMovieByStringBinding getBinding() {
        return null;
    }
    
    public final void setBinding(@org.jetbrains.annotations.NotNull()
    com.test.testroomapp.databinding.ActivitySearchMovieByStringBinding p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getApiKey() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.ArrayList<com.test.testroomapp.MovieRowModel> getMovieDetails() {
        return null;
    }
    
    public final void setMovieDetails(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<com.test.testroomapp.MovieRowModel> p0) {
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final java.lang.Object searchMovie(java.lang.String title, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    private final void getMovieDetails(org.json.JSONObject array) {
    }
    
    @java.lang.Override()
    protected void onSaveInstanceState(@org.jetbrains.annotations.NotNull()
    android.os.Bundle outState) {
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001:\u0001\u0003B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0004"}, d2 = {"Lcom/test/testroomapp/SearchMovieByString$Companion;", "", "()V", "Keys", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\t\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000b\u00a8\u0006\f"}, d2 = {"Lcom/test/testroomapp/SearchMovieByString$Companion$Keys;", "", "value", "", "(Ljava/lang/String;ILjava/lang/String;)V", "getValue", "()Ljava/lang/String;", "TITLE", "YEAR", "IMDBID", "TYPE", "POSTER", "app_debug"})
        public static enum Keys {
            /*public static final*/ TITLE /* = new TITLE(null) */,
            /*public static final*/ YEAR /* = new YEAR(null) */,
            /*public static final*/ IMDBID /* = new IMDBID(null) */,
            /*public static final*/ TYPE /* = new TYPE(null) */,
            /*public static final*/ POSTER /* = new POSTER(null) */;
            @org.jetbrains.annotations.NotNull()
            private final java.lang.String value = null;
            
            Keys(java.lang.String value) {
            }
            
            @org.jetbrains.annotations.NotNull()
            public final java.lang.String getValue() {
                return null;
            }
        }
    }
}