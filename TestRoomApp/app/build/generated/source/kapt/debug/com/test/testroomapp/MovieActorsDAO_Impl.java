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
public final class MovieActorsDAO_Impl implements MovieActorsDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MovieActors> __insertionAdapterOfMovieActors;

  private final EntityDeletionOrUpdateAdapter<MovieActors> __deletionAdapterOfMovieActors;

  public MovieActorsDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMovieActors = new EntityInsertionAdapter<MovieActors>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR IGNORE INTO `MovieActors` (`movie_title`,`actor_name`) VALUES (?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, MovieActors value) {
        if (value.getMovieTitle() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getMovieTitle());
        }
        if (value.getActorName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getActorName());
        }
      }
    };
    this.__deletionAdapterOfMovieActors = new EntityDeletionOrUpdateAdapter<MovieActors>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `MovieActors` WHERE `movie_title` = ? AND `actor_name` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, MovieActors value) {
        if (value.getMovieTitle() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getMovieTitle());
        }
        if (value.getActorName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getActorName());
        }
      }
    };
  }

  @Override
  public void insertAll(final MovieActors... moviesActor) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfMovieActors.insert(moviesActor);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final MovieActors movieActors) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfMovieActors.handle(movieActors);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<MovieActors> getAll() {
    final String _sql = "SELECT * FROM movieActors";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfMovieTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "movie_title");
      final int _cursorIndexOfActorName = CursorUtil.getColumnIndexOrThrow(_cursor, "actor_name");
      final List<MovieActors> _result = new ArrayList<MovieActors>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final MovieActors _item;
        final String _tmpMovieTitle;
        if (_cursor.isNull(_cursorIndexOfMovieTitle)) {
          _tmpMovieTitle = null;
        } else {
          _tmpMovieTitle = _cursor.getString(_cursorIndexOfMovieTitle);
        }
        final String _tmpActorName;
        if (_cursor.isNull(_cursorIndexOfActorName)) {
          _tmpActorName = null;
        } else {
          _tmpActorName = _cursor.getString(_cursorIndexOfActorName);
        }
        _item = new MovieActors(_tmpMovieTitle,_tmpActorName);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<MovieActors> findMovieTitleByActor(final String actorName) {
    final String _sql = "SELECT * FROM movieActors WHERE actor_name Like ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (actorName == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, actorName);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfMovieTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "movie_title");
      final int _cursorIndexOfActorName = CursorUtil.getColumnIndexOrThrow(_cursor, "actor_name");
      final List<MovieActors> _result = new ArrayList<MovieActors>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final MovieActors _item;
        final String _tmpMovieTitle;
        if (_cursor.isNull(_cursorIndexOfMovieTitle)) {
          _tmpMovieTitle = null;
        } else {
          _tmpMovieTitle = _cursor.getString(_cursorIndexOfMovieTitle);
        }
        final String _tmpActorName;
        if (_cursor.isNull(_cursorIndexOfActorName)) {
          _tmpActorName = null;
        } else {
          _tmpActorName = _cursor.getString(_cursorIndexOfActorName);
        }
        _item = new MovieActors(_tmpMovieTitle,_tmpActorName);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<MovieActors> findActorByMovieTitle(final String movieTitle) {
    final String _sql = "SELECT * FROM movieActors WHERE movie_title LIKE ?";
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
      final int _cursorIndexOfMovieTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "movie_title");
      final int _cursorIndexOfActorName = CursorUtil.getColumnIndexOrThrow(_cursor, "actor_name");
      final List<MovieActors> _result = new ArrayList<MovieActors>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final MovieActors _item;
        final String _tmpMovieTitle;
        if (_cursor.isNull(_cursorIndexOfMovieTitle)) {
          _tmpMovieTitle = null;
        } else {
          _tmpMovieTitle = _cursor.getString(_cursorIndexOfMovieTitle);
        }
        final String _tmpActorName;
        if (_cursor.isNull(_cursorIndexOfActorName)) {
          _tmpActorName = null;
        } else {
          _tmpActorName = _cursor.getString(_cursorIndexOfActorName);
        }
        _item = new MovieActors(_tmpMovieTitle,_tmpActorName);
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
