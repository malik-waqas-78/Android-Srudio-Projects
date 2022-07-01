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
public final class MovieDirectorsDao_Impl implements MovieDirectorsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MovieDirectors> __insertionAdapterOfMovieDirectors;

  private final EntityDeletionOrUpdateAdapter<MovieDirectors> __deletionAdapterOfMovieDirectors;

  public MovieDirectorsDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMovieDirectors = new EntityInsertionAdapter<MovieDirectors>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR IGNORE INTO `MovieDirectors` (`movie_title`,`director_name`) VALUES (?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, MovieDirectors value) {
        if (value.getMovieTitle() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getMovieTitle());
        }
        if (value.getDirectorName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getDirectorName());
        }
      }
    };
    this.__deletionAdapterOfMovieDirectors = new EntityDeletionOrUpdateAdapter<MovieDirectors>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `MovieDirectors` WHERE `movie_title` = ? AND `director_name` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, MovieDirectors value) {
        if (value.getMovieTitle() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getMovieTitle());
        }
        if (value.getDirectorName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getDirectorName());
        }
      }
    };
  }

  @Override
  public void insertAll(final MovieDirectors... directorName) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfMovieDirectors.insert(directorName);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final MovieDirectors directorName) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfMovieDirectors.handle(directorName);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<MovieDirectors> getAll() {
    final String _sql = "SELECT * FROM movieDirectors";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfMovieTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "movie_title");
      final int _cursorIndexOfDirectorName = CursorUtil.getColumnIndexOrThrow(_cursor, "director_name");
      final List<MovieDirectors> _result = new ArrayList<MovieDirectors>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final MovieDirectors _item;
        final String _tmpMovieTitle;
        if (_cursor.isNull(_cursorIndexOfMovieTitle)) {
          _tmpMovieTitle = null;
        } else {
          _tmpMovieTitle = _cursor.getString(_cursorIndexOfMovieTitle);
        }
        final String _tmpDirectorName;
        if (_cursor.isNull(_cursorIndexOfDirectorName)) {
          _tmpDirectorName = null;
        } else {
          _tmpDirectorName = _cursor.getString(_cursorIndexOfDirectorName);
        }
        _item = new MovieDirectors(_tmpMovieTitle,_tmpDirectorName);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<String> findMovieTitleByDirector(final String directorName) {
    final String _sql = "SELECT movie_title FROM movieDirectors WHERE director_name LIKE ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (directorName == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, directorName);
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
  public List<String> findDirectorByMovieTitle(final String movieTitle) {
    final String _sql = "SELECT director_name FROM movieDirectors WHERE movie_title LIKE ?";
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
