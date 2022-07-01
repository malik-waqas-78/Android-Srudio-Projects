package com.test.testroomapp;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class MovieDao_Impl implements MovieDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Movie> __insertionAdapterOfMovie;

  private final EntityDeletionOrUpdateAdapter<Movie> __deletionAdapterOfMovie;

  public MovieDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMovie = new EntityInsertionAdapter<Movie>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR IGNORE INTO `Movie` (`title`,`year`,`rated`,`released`,`runtime`,`plots`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Movie value) {
        if (value.getTitle() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getTitle());
        }
        if (value.getYear() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getYear());
        }
        if (value.getRated() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getRated());
        }
        if (value.getReleased() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getReleased());
        }
        if (value.getRuntime() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getRuntime());
        }
        if (value.getPlots() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getPlots());
        }
      }
    };
    this.__deletionAdapterOfMovie = new EntityDeletionOrUpdateAdapter<Movie>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `Movie` WHERE `title` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Movie value) {
        if (value.getTitle() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getTitle());
        }
      }
    };
  }

  @Override
  public void insertAll(final Movie... movies) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfMovie.insert(movies);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Movie movie) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfMovie.handle(movie);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Movie> getAll() {
    final String _sql = "SELECT * FROM movie";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "year");
      final int _cursorIndexOfRated = CursorUtil.getColumnIndexOrThrow(_cursor, "rated");
      final int _cursorIndexOfReleased = CursorUtil.getColumnIndexOrThrow(_cursor, "released");
      final int _cursorIndexOfRuntime = CursorUtil.getColumnIndexOrThrow(_cursor, "runtime");
      final int _cursorIndexOfPlots = CursorUtil.getColumnIndexOrThrow(_cursor, "plots");
      final List<Movie> _result = new ArrayList<Movie>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Movie _item;
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        final String _tmpYear;
        if (_cursor.isNull(_cursorIndexOfYear)) {
          _tmpYear = null;
        } else {
          _tmpYear = _cursor.getString(_cursorIndexOfYear);
        }
        final String _tmpRated;
        if (_cursor.isNull(_cursorIndexOfRated)) {
          _tmpRated = null;
        } else {
          _tmpRated = _cursor.getString(_cursorIndexOfRated);
        }
        final String _tmpReleased;
        if (_cursor.isNull(_cursorIndexOfReleased)) {
          _tmpReleased = null;
        } else {
          _tmpReleased = _cursor.getString(_cursorIndexOfReleased);
        }
        final String _tmpRuntime;
        if (_cursor.isNull(_cursorIndexOfRuntime)) {
          _tmpRuntime = null;
        } else {
          _tmpRuntime = _cursor.getString(_cursorIndexOfRuntime);
        }
        final String _tmpPlots;
        if (_cursor.isNull(_cursorIndexOfPlots)) {
          _tmpPlots = null;
        } else {
          _tmpPlots = _cursor.getString(_cursorIndexOfPlots);
        }
        _item = new Movie(_tmpTitle,_tmpYear,_tmpRated,_tmpReleased,_tmpRuntime,_tmpPlots);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Movie> loadAllByTitle(final ArrayList<String> titles) {
    StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT * FROM movie WHERE title IN (");
    final int _inputSize = titles.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (String _item : titles) {
      if (_item == null) {
        _statement.bindNull(_argIndex);
      } else {
        _statement.bindString(_argIndex, _item);
      }
      _argIndex ++;
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "year");
      final int _cursorIndexOfRated = CursorUtil.getColumnIndexOrThrow(_cursor, "rated");
      final int _cursorIndexOfReleased = CursorUtil.getColumnIndexOrThrow(_cursor, "released");
      final int _cursorIndexOfRuntime = CursorUtil.getColumnIndexOrThrow(_cursor, "runtime");
      final int _cursorIndexOfPlots = CursorUtil.getColumnIndexOrThrow(_cursor, "plots");
      final List<Movie> _result = new ArrayList<Movie>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Movie _item_1;
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        final String _tmpYear;
        if (_cursor.isNull(_cursorIndexOfYear)) {
          _tmpYear = null;
        } else {
          _tmpYear = _cursor.getString(_cursorIndexOfYear);
        }
        final String _tmpRated;
        if (_cursor.isNull(_cursorIndexOfRated)) {
          _tmpRated = null;
        } else {
          _tmpRated = _cursor.getString(_cursorIndexOfRated);
        }
        final String _tmpReleased;
        if (_cursor.isNull(_cursorIndexOfReleased)) {
          _tmpReleased = null;
        } else {
          _tmpReleased = _cursor.getString(_cursorIndexOfReleased);
        }
        final String _tmpRuntime;
        if (_cursor.isNull(_cursorIndexOfRuntime)) {
          _tmpRuntime = null;
        } else {
          _tmpRuntime = _cursor.getString(_cursorIndexOfRuntime);
        }
        final String _tmpPlots;
        if (_cursor.isNull(_cursorIndexOfPlots)) {
          _tmpPlots = null;
        } else {
          _tmpPlots = _cursor.getString(_cursorIndexOfPlots);
        }
        _item_1 = new Movie(_tmpTitle,_tmpYear,_tmpRated,_tmpReleased,_tmpRuntime,_tmpPlots);
        _result.add(_item_1);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Movie> findByTitle(final String title) {
    final String _sql = "SELECT * FROM movie WHERE title LIKE ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (title == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, title);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "year");
      final int _cursorIndexOfRated = CursorUtil.getColumnIndexOrThrow(_cursor, "rated");
      final int _cursorIndexOfReleased = CursorUtil.getColumnIndexOrThrow(_cursor, "released");
      final int _cursorIndexOfRuntime = CursorUtil.getColumnIndexOrThrow(_cursor, "runtime");
      final int _cursorIndexOfPlots = CursorUtil.getColumnIndexOrThrow(_cursor, "plots");
      final List<Movie> _result = new ArrayList<Movie>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Movie _item;
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        final String _tmpYear;
        if (_cursor.isNull(_cursorIndexOfYear)) {
          _tmpYear = null;
        } else {
          _tmpYear = _cursor.getString(_cursorIndexOfYear);
        }
        final String _tmpRated;
        if (_cursor.isNull(_cursorIndexOfRated)) {
          _tmpRated = null;
        } else {
          _tmpRated = _cursor.getString(_cursorIndexOfRated);
        }
        final String _tmpReleased;
        if (_cursor.isNull(_cursorIndexOfReleased)) {
          _tmpReleased = null;
        } else {
          _tmpReleased = _cursor.getString(_cursorIndexOfReleased);
        }
        final String _tmpRuntime;
        if (_cursor.isNull(_cursorIndexOfRuntime)) {
          _tmpRuntime = null;
        } else {
          _tmpRuntime = _cursor.getString(_cursorIndexOfRuntime);
        }
        final String _tmpPlots;
        if (_cursor.isNull(_cursorIndexOfPlots)) {
          _tmpPlots = null;
        } else {
          _tmpPlots = _cursor.getString(_cursorIndexOfPlots);
        }
        _item = new Movie(_tmpTitle,_tmpYear,_tmpRated,_tmpReleased,_tmpRuntime,_tmpPlots);
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
