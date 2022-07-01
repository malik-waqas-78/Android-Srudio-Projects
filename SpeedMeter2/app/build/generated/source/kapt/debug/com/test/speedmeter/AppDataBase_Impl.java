package com.test.speedmeter;

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
  private volatile DbLocationRectDao _dbLocationRectDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `DbLocationRect` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `point1_lat` REAL NOT NULL, `point2_lat` REAL NOT NULL, `point3_lat` REAL NOT NULL, `point4_lat` REAL NOT NULL, `point1_longi` REAL NOT NULL, `point2_longi` REAL NOT NULL, `point3_longi` REAL NOT NULL, `point4_longi` REAL NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '99723a1613a7cfc67ec9ea1ec26ea8f7')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `DbLocationRect`");
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
        final HashMap<String, TableInfo.Column> _columnsDbLocationRect = new HashMap<String, TableInfo.Column>(9);
        _columnsDbLocationRect.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDbLocationRect.put("point1_lat", new TableInfo.Column("point1_lat", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDbLocationRect.put("point2_lat", new TableInfo.Column("point2_lat", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDbLocationRect.put("point3_lat", new TableInfo.Column("point3_lat", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDbLocationRect.put("point4_lat", new TableInfo.Column("point4_lat", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDbLocationRect.put("point1_longi", new TableInfo.Column("point1_longi", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDbLocationRect.put("point2_longi", new TableInfo.Column("point2_longi", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDbLocationRect.put("point3_longi", new TableInfo.Column("point3_longi", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDbLocationRect.put("point4_longi", new TableInfo.Column("point4_longi", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDbLocationRect = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDbLocationRect = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDbLocationRect = new TableInfo("DbLocationRect", _columnsDbLocationRect, _foreignKeysDbLocationRect, _indicesDbLocationRect);
        final TableInfo _existingDbLocationRect = TableInfo.read(_db, "DbLocationRect");
        if (! _infoDbLocationRect.equals(_existingDbLocationRect)) {
          return new RoomOpenHelper.ValidationResult(false, "DbLocationRect(com.test.speedmeter.DbLocationRect).\n"
                  + " Expected:\n" + _infoDbLocationRect + "\n"
                  + " Found:\n" + _existingDbLocationRect);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "99723a1613a7cfc67ec9ea1ec26ea8f7", "6038332b7d151e787c8920bc08cfa3a1");
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
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "DbLocationRect");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `DbLocationRect`");
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
    _typeConvertersMap.put(DbLocationRectDao.class, DbLocationRectDao_Impl.getRequiredConverters());
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
  DbLocationRectDao dbLocationRectDao() {
    if (_dbLocationRectDao != null) {
      return _dbLocationRectDao;
    } else {
      synchronized(this) {
        if(_dbLocationRectDao == null) {
          _dbLocationRectDao = new DbLocationRectDao_Impl(this);
        }
        return _dbLocationRectDao;
      }
    }
  }
}
