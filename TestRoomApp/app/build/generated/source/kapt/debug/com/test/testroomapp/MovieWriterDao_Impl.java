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
public final class MovieWriterDao_Impl implements MovieWriterDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MovieWriter> __insertionAdapterOfMovieWriter;

  private final EntityDeletionOrUpdateAdapter<MovieWriter> __deletionAdapterOfMovieWriter;

  public MovieWriterDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMovieWriter = new EntityInsertionAdapter<MovieWriter>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR IGNORE INTO `MovieWriter` (`movie_title`,`writer_name`) VALUES (?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, MovieWriter value) {
        if (value.getMovieTitle() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getMovieTitle());
        }
        if (value.getWriterName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getWriterName());
        }
      }
    };
    this.__deletionAdapterOfMovieWriter = new EntityDeletionOrUpdateAdapter<MovieWriter>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `MovieWriter` WHERE `movie_title` = ? AND `writer_name` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, MovieWriter value) {
        if (value.getMovieTitle() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getMovieTitle());
        }
        if (value.getWriterName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getWriterName());
        }
      }
    };
  }

  @Override
  public void insertAll(final MovieWriter... directorName) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfMovieWriter.insert(directorName);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final MovieWriter directorName) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfMovieWriter.handle(directorName);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<MovieWriter> getAll() {
    final String _sql = "SELECT * FROM movieWriter";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfMovieTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "movie_title");
      final int _cursorIndexOfWriterName = CursorUtil.getColumnIndexOrThrow(_cursor, "writer_name");
      final List<MovieWriter> _result = new ArrayList<MovieWriter>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final MovieWriter _item;
        final String _tmpMovieTitle;
        if (_cursor.isNull(_cursorIndexOfMovieTitle)) {
          _tmpMovieTitle = null;
        } else {
          _tmpMovieTitle = _cursor.getString(_cursorIndexOfMovieTitle);
        }
        final String _tmpWriterName;
        if (_cursor.isNull(_cursorIndexOfWriterName)) {
          _tmpWriterName = null;
        } else {
          _tmpWriterName = _cursor.getString(_cursorIndexOfWriterName);
        }
        _item = new MovieWriter(_tmpMovieTitle,_tmpWriterName);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<String> findMovieTitleByWriter(final String writerName) {
    final String _sql = "SELECT movie_title FROM movieWriter WHERE writer_name LIKE ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (writerName == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, writerName);
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
  public List<String> findWriterByMovieTitle(final String movieTitle) {
    final String _sql = "SELECT writer_name FROM movieWriter WHERE movie_title LIKE ?";
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
