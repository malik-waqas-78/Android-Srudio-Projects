package com.test.speedmeter;

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
public final class DbLocationRectDao_Impl extends DbLocationRectDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DbLocationRect> __insertionAdapterOfDbLocationRect;

  private final EntityDeletionOrUpdateAdapter<DbLocationRect> __deletionAdapterOfDbLocationRect;

  public DbLocationRectDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDbLocationRect = new EntityInsertionAdapter<DbLocationRect>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR IGNORE INTO `DbLocationRect` (`id`,`point1_lat`,`point2_lat`,`point3_lat`,`point4_lat`,`point1_longi`,`point2_longi`,`point3_longi`,`point4_longi`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, DbLocationRect value) {
        stmt.bindLong(1, value.id);
        stmt.bindDouble(2, value.getPoint1Lat());
        stmt.bindDouble(3, value.getPoint2Lat());
        stmt.bindDouble(4, value.getPoint3Lat());
        stmt.bindDouble(5, value.getPoint4Lat());
        stmt.bindDouble(6, value.getPoint1Longi());
        stmt.bindDouble(7, value.getPoint2Longi());
        stmt.bindDouble(8, value.getPoint3Longi());
        stmt.bindDouble(9, value.getPoint4Longi());
      }
    };
    this.__deletionAdapterOfDbLocationRect = new EntityDeletionOrUpdateAdapter<DbLocationRect>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `DbLocationRect` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, DbLocationRect value) {
        stmt.bindLong(1, value.id);
      }
    };
  }

  @Override
  public void insert(final DbLocationRect dbLocationRect) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfDbLocationRect.insert(dbLocationRect);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final DbLocationRect dbLocationRect) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfDbLocationRect.handle(dbLocationRect);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<DbLocationRect> getAll() {
    final String _sql = "SELECT * FROM dbLocationRect";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfPoint1Lat = CursorUtil.getColumnIndexOrThrow(_cursor, "point1_lat");
      final int _cursorIndexOfPoint2Lat = CursorUtil.getColumnIndexOrThrow(_cursor, "point2_lat");
      final int _cursorIndexOfPoint3Lat = CursorUtil.getColumnIndexOrThrow(_cursor, "point3_lat");
      final int _cursorIndexOfPoint4Lat = CursorUtil.getColumnIndexOrThrow(_cursor, "point4_lat");
      final int _cursorIndexOfPoint1Longi = CursorUtil.getColumnIndexOrThrow(_cursor, "point1_longi");
      final int _cursorIndexOfPoint2Longi = CursorUtil.getColumnIndexOrThrow(_cursor, "point2_longi");
      final int _cursorIndexOfPoint3Longi = CursorUtil.getColumnIndexOrThrow(_cursor, "point3_longi");
      final int _cursorIndexOfPoint4Longi = CursorUtil.getColumnIndexOrThrow(_cursor, "point4_longi");
      final List<DbLocationRect> _result = new ArrayList<DbLocationRect>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final DbLocationRect _item;
        _item = new DbLocationRect();
        _item.id = _cursor.getInt(_cursorIndexOfId);
        final double _tmpPoint1Lat;
        _tmpPoint1Lat = _cursor.getDouble(_cursorIndexOfPoint1Lat);
        _item.setPoint1Lat(_tmpPoint1Lat);
        final double _tmpPoint2Lat;
        _tmpPoint2Lat = _cursor.getDouble(_cursorIndexOfPoint2Lat);
        _item.setPoint2Lat(_tmpPoint2Lat);
        final double _tmpPoint3Lat;
        _tmpPoint3Lat = _cursor.getDouble(_cursorIndexOfPoint3Lat);
        _item.setPoint3Lat(_tmpPoint3Lat);
        final double _tmpPoint4Lat;
        _tmpPoint4Lat = _cursor.getDouble(_cursorIndexOfPoint4Lat);
        _item.setPoint4Lat(_tmpPoint4Lat);
        final double _tmpPoint1Longi;
        _tmpPoint1Longi = _cursor.getDouble(_cursorIndexOfPoint1Longi);
        _item.setPoint1Longi(_tmpPoint1Longi);
        final double _tmpPoint2Longi;
        _tmpPoint2Longi = _cursor.getDouble(_cursorIndexOfPoint2Longi);
        _item.setPoint2Longi(_tmpPoint2Longi);
        final double _tmpPoint3Longi;
        _tmpPoint3Longi = _cursor.getDouble(_cursorIndexOfPoint3Longi);
        _item.setPoint3Longi(_tmpPoint3Longi);
        final double _tmpPoint4Longi;
        _tmpPoint4Longi = _cursor.getDouble(_cursorIndexOfPoint4Longi);
        _item.setPoint4Longi(_tmpPoint4Longi);
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
