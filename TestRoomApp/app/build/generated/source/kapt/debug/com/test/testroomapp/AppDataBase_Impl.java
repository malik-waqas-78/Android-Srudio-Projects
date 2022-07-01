package com.test.testroomapp;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDataBase_Impl extends AppDataBase {
  private volatile MovieDao _movieDao;

  private volatile MovieActorsDAO _movieActorsDAO;

  private volatile MovieWriterDao _movieWriterDao;

  private volatile MovieDirectorsDao _movieDirectorsDao;

  private volatile MovieGenreDao _movieGenreDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Movie` (`title` TEXT NOT NULL, `year` TEXT, `rated` TEXT, `released` TEXT, `runtime` TEXT, `plots` TEXT, PRIMARY KEY(`title`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `MovieActors` (`movie_title` TEXT NOT NULL, `actor_name` TEXT NOT NULL, PRIMARY KEY(`movie_title`, `actor_name`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `MovieDirectors` (`movie_title` TEXT NOT NULL, `director_name` TEXT NOT NULL, PRIMARY KEY(`movie_title`, `director_name`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `MovieWriter` (`movie_title` TEXT NOT NULL, `writer_name` TEXT NOT NULL, PRIMARY KEY(`movie_title`, `writer_name`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `MovieGenre` (`movie_title` TEXT NOT NULL, `genre_name` TEXT NOT NULL, PRIMARY KEY(`movie_title`, `genre_name`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '2c4ecc49586bb0c7e25898cd4e7863d0')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `Movie`");
        _db.execSQL("DROP TABLE IF EXISTS `MovieActors`");
        _db.execSQL("DROP TABLE IF EXISTS `MovieDirectors`");
        _db.execSQL("DROP TABLE IF EXISTS `MovieWriter`");
        _db.execSQL("DROP TABLE IF EXISTS `MovieGenre`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsMovie = new HashMap<String, TableInfo.Column>(6);
        _columnsMovie.put("title", new TableInfo.Column("title", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovie.put("year", new TableInfo.Column("year", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovie.put("rated", new TableInfo.Column("rated", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovie.put("released", new TableInfo.Column("released", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovie.put("runtime", new TableInfo.Column("runtime", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovie.put("plots", new TableInfo.Column("plots", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMovie = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMovie = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMovie = new TableInfo("Movie", _columnsMovie, _foreignKeysMovie, _indicesMovie);
        final TableInfo _existingMovie = TableInfo.read(_db, "Movie");
        if (! _infoMovie.equals(_existingMovie)) {
          return new RoomOpenHelper.ValidationResult(false, "Movie(com.test.testroomapp.Movie).\n"
                  + " Expected:\n" + _infoMovie + "\n"
                  + " Found:\n" + _existingMovie);
        }
        final HashMap<String, TableInfo.Column> _columnsMovieActors = new HashMap<String, TableInfo.Column>(2);
        _columnsMovieActors.put("movie_title", new TableInfo.Column("movie_title", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovieActors.put("actor_name", new TableInfo.Column("actor_name", "TEXT", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMovieActors = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMovieActors = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMovieActors = new TableInfo("MovieActors", _columnsMovieActors, _foreignKeysMovieActors, _indicesMovieActors);
        final TableInfo _existingMovieActors = TableInfo.read(_db, "MovieActors");
        if (! _infoMovieActors.equals(_existingMovieActors)) {
          return new RoomOpenHelper.ValidationResult(false, "MovieActors(com.test.testroomapp.MovieActors).\n"
                  + " Expected:\n" + _infoMovieActors + "\n"
                  + " Found:\n" + _existingMovieActors);
        }
        final HashMap<String, TableInfo.Column> _columnsMovieDirectors = new HashMap<String, TableInfo.Column>(2);
        _columnsMovieDirectors.put("movie_title", new TableInfo.Column("movie_title", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovieDirectors.put("director_name", new TableInfo.Column("director_name", "TEXT", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMovieDirectors = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMovieDirectors = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMovieDirectors = new TableInfo("MovieDirectors", _columnsMovieDirectors, _foreignKeysMovieDirectors, _indicesMovieDirectors);
        final TableInfo _existingMovieDirectors = TableInfo.read(_db, "MovieDirectors");
        if (! _infoMovieDirectors.equals(_existingMovieDirectors)) {
          return new RoomOpenHelper.ValidationResult(false, "MovieDirectors(com.test.testroomapp.MovieDirectors).\n"
                  + " Expected:\n" + _infoMovieDirectors + "\n"
                  + " Found:\n" + _existingMovieDirectors);
        }
        final HashMap<String, TableInfo.Column> _columnsMovieWriter = new HashMap<String, TableInfo.Column>(2);
        _columnsMovieWriter.put("movie_title", new TableInfo.Column("movie_title", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovieWriter.put("writer_name", new TableInfo.Column("writer_name", "TEXT", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMovieWriter = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMovieWriter = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMovieWriter = new TableInfo("MovieWriter", _columnsMovieWriter, _foreignKeysMovieWriter, _indicesMovieWriter);
        final TableInfo _existingMovieWriter = TableInfo.read(_db, "MovieWriter");
        if (! _infoMovieWriter.equals(_existingMovieWriter)) {
          return new RoomOpenHelper.ValidationResult(false, "MovieWriter(com.test.testroomapp.MovieWriter).\n"
                  + " Expected:\n" + _infoMovieWriter + "\n"
                  + " Found:\n" + _existingMovieWriter);
        }
        final HashMap<String, TableInfo.Column> _columnsMovieGenre = new HashMap<String, TableInfo.Column>(2);
        _columnsMovieGenre.put("movie_title", new TableInfo.Column("movie_title", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovieGenre.put("genre_name", new TableInfo.Column("genre_name", "TEXT", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMovieGenre = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMovieGenre = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMovieGenre = new TableInfo("MovieGenre", _columnsMovieGenre, _foreignKeysMovieGenre, _indicesMovieGenre);
        final TableInfo _existingMovieGenre = TableInfo.read(_db, "MovieGenre");
        if (! _infoMovieGenre.equals(_existingMovieGenre)) {
          return new RoomOpenHelper.ValidationResult(false, "MovieGenre(com.test.testroomapp.MovieGenre).\n"
                  + " Expected:\n" + _infoMovieGenre + "\n"
                  + " Found:\n" + _existingMovieGenre);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "2c4ecc49586bb0c7e25898cd4e7863d0", "28b7e24a79696ca6fb126feefb6f72a2");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "Movie","MovieActors","MovieDirectors","MovieWriter","MovieGenre");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `Movie`");
      _db.execSQL("DELETE FROM `MovieActors`");
      _db.execSQL("DELETE FROM `MovieDirectors`");
      _db.execSQL("DELETE FROM `MovieWriter`");
      _db.execSQL("DELETE FROM `MovieGenre`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(MovieDao.class, MovieDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MovieActorsDAO.class, MovieActorsDAO_Impl.getRequiredConverters());
    _typeConvertersMap.put(MovieWriterDao.class, MovieWriterDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MovieDirectorsDao.class, MovieDirectorsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MovieGenreDao.class, MovieGenreDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  public List<Migration> getAutoMigrations(
      @NonNull Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecsMap) {
    return Arrays.asList();
  }

  @Override
  public MovieDao movieDao() {
    if (_movieDao != null) {
      return _movieDao;
    } else {
      synchronized(this) {
        if(_movieDao == null) {
          _movieDao = new MovieDao_Impl(this);
        }
        return _movieDao;
      }
    }
  }

  @Override
  public MovieActorsDAO movieActorsDao() {
    if (_movieActorsDAO != null) {
      return _movieActorsDAO;
    } else {
      synchronized(this) {
        if(_movieActorsDAO == null) {
          _movieActorsDAO = new MovieActorsDAO_Impl(this);
        }
        return _movieActorsDAO;
      }
    }
  }

  @Override
  public MovieWriterDao movieWriterDao() {
    if (_movieWriterDao != null) {
      return _movieWriterDao;
    } else {
      synchronized(this) {
        if(_movieWriterDao == null) {
          _movieWriterDao = new MovieWriterDao_Impl(this);
        }
        return _movieWriterDao;
      }
    }
  }

  @Override
  public MovieDirectorsDao movieDirectorDao() {
    if (_movieDirectorsDao != null) {
      return _movieDirectorsDao;
    } else {
      synchronized(this) {
        if(_movieDirectorsDao == null) {
          _movieDirectorsDao = new MovieDirectorsDao_Impl(this);
        }
        return _movieDirectorsDao;
      }
    }
  }

  @Override
  public MovieGenreDao movieGenreDao() {
    if (_movieGenreDao != null) {
      return _movieGenreDao;
    } else {
      synchronized(this) {
        if(_movieGenreDao == null) {
          _movieGenreDao = new MovieGenreDao_Impl(this);
        }
        return _movieGenreDao;
      }
    }
  }
}
