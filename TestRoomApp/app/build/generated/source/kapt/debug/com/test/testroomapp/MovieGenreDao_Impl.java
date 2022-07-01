package com.test.testroomapp;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class MovieGenreDao_Impl implements MovieGenreDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MovieGenre> __insertionAdapterOfMovieGenre;

  private final EntityDeletionOrUpdateAdapter<MovieGenre> __deletionAdapterOfMovieGenre;

  public MovieGenreDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMovieGenre = new EntityInsertionAdapter<MovieGenre>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR IGNORE INTO `MovieGenre` (`movie_title`,`genre_name`) VALUES (?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, MovieGenre value) {
        if (value.getMovieTitle() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getMovieTitle());
        }
        if (value.getGenreName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getGenreName());
        }
      }
    };
    this.__deletionAdapterOfMovieGenre = new EntityDeletionOrUpdateAdapter<MovieGenre>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `MovieGenre` WHERE `movie_title` = ? AND `genre_name` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, MovieGenre value) {
        if (value.getMovieTitle() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getMovieTitle());
        }
        if (value.getGenreName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getGenreName());
        }
      }
    };
  }

  @Override
  public void insertAll(final MovieGenre... genre) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfMovieGenre.insert(genre);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final MovieGenre genre) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfMovieGenre.handle(genre);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<MovieGenre> getAll() {
    final String _sql = "SELECT * FROM movieGenre";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfMovieTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "movie_title");
      final int _cursorIndexOfGenreName = CursorUtil.getColumnIndexOrThrow(_cursor, "genre_name");
      final List<MovieGenre> _result = new ArrayList<MovieGenre>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final MovieGenre _item;
        final String _tmpMovieTitle;
        if (_cursor.isNull(_cursorIndexOfMovieTitle)) {
          _tmpMovieTitle = null;
        } else {
          _tmpMovieTitle = _cursor.getString(_cursorIndexOfMovieTitle);
        }
        final String _tmpGenreName;
        if (_cursor.isNull(_cursorIndexOfGenreName)) {
          _tmpGenreName = null;
        } else {
          _tmpGenreName = _cursor.getString(_cursorIndexOfGenreName);
        }
        _item = new MovieGenre(_tmpMovieTitle,_tmpGenreName);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<String> findMovieTitleByGenre(final String genreName) {
    final String _sql = "SELECT movie_title FROM movieGenre WHERE genre_name LIKE ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (genreName == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, genreName);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final List<String> _result = new ArrayList<String>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final String _item;
        if (_cursor.isNull(0)) {
          _item = null;
        } else {
          _item = _cursor.getString(0);
        }
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<String> findGenreByMovieTitle(final String movieTitle) {
    final String _sql = "SELECT movie_title FROM movieGenre WHERE movie_title LIKE ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (movieTitle == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, movieTitle);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final List<String> _result = new ArrayList<String>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final String _item;
        if (_cursor.isNull(0)) {
          _item = null;
        } else {
          _item = _cursor.getString(0);
        }
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
