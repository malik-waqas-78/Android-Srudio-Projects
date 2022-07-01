package io.realm;


import android.annotation.TargetApi;
import android.os.Build;
import android.util.JsonReader;
import android.util.JsonToken;
import io.realm.ImportFlag;
import io.realm.ProxyUtils;
import io.realm.exceptions.RealmMigrationNeededException;
import io.realm.internal.ColumnInfo;
import io.realm.internal.OsList;
import io.realm.internal.OsObject;
import io.realm.internal.OsObjectSchemaInfo;
import io.realm.internal.OsSchemaInfo;
import io.realm.internal.Property;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.Row;
import io.realm.internal.Table;
import io.realm.internal.android.JsonUtils;
import io.realm.internal.objectstore.OsObjectBuilder;
import io.realm.log.RealmLog;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("all")
public class com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxy extends com.example.hissabbookapp.realmmodals.RecordDataRealm
    implements RealmObjectProxy, com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface {

    static final class RecordDataRealmColumnInfo extends ColumnInfo {
        long remarkColKey;
        long totalBalanceColKey;
        long timeColKey;
        long balanceColKey;
        long dateColKey;
        long typeColKey;
        long dayColKey;
        long monthColKey;
        long yearColKey;
        long idColKey;

        RecordDataRealmColumnInfo(OsSchemaInfo schemaInfo) {
            super(10);
            OsObjectSchemaInfo objectSchemaInfo = schemaInfo.getObjectSchemaInfo("RecordDataRealm");
            this.remarkColKey = addColumnDetails("remark", "remark", objectSchemaInfo);
            this.totalBalanceColKey = addColumnDetails("totalBalance", "totalBalance", objectSchemaInfo);
            this.timeColKey = addColumnDetails("time", "time", objectSchemaInfo);
            this.balanceColKey = addColumnDetails("balance", "balance", objectSchemaInfo);
            this.dateColKey = addColumnDetails("date", "date", objectSchemaInfo);
            this.typeColKey = addColumnDetails("type", "type", objectSchemaInfo);
            this.dayColKey = addColumnDetails("day", "day", objectSchemaInfo);
            this.monthColKey = addColumnDetails("month", "month", objectSchemaInfo);
            this.yearColKey = addColumnDetails("year", "year", objectSchemaInfo);
            this.idColKey = addColumnDetails("id", "id", objectSchemaInfo);
        }

        RecordDataRealmColumnInfo(ColumnInfo src, boolean mutable) {
            super(src, mutable);
            copy(src, this);
        }

        @Override
        protected final ColumnInfo copy(boolean mutable) {
            return new RecordDataRealmColumnInfo(this, mutable);
        }

        @Override
        protected final void copy(ColumnInfo rawSrc, ColumnInfo rawDst) {
            final RecordDataRealmColumnInfo src = (RecordDataRealmColumnInfo) rawSrc;
            final RecordDataRealmColumnInfo dst = (RecordDataRealmColumnInfo) rawDst;
            dst.remarkColKey = src.remarkColKey;
            dst.totalBalanceColKey = src.totalBalanceColKey;
            dst.timeColKey = src.timeColKey;
            dst.balanceColKey = src.balanceColKey;
            dst.dateColKey = src.dateColKey;
            dst.typeColKey = src.typeColKey;
            dst.dayColKey = src.dayColKey;
            dst.monthColKey = src.monthColKey;
            dst.yearColKey = src.yearColKey;
            dst.idColKey = src.idColKey;
        }
    }

    private static final String NO_ALIAS = "";
    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();

    private RecordDataRealmColumnInfo columnInfo;
    private ProxyState<com.example.hissabbookapp.realmmodals.RecordDataRealm> proxyState;

    com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (RecordDataRealmColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<com.example.hissabbookapp.realmmodals.RecordDataRealm>(this);
        proxyState.setRealm$realm(context.getRealm());
        proxyState.setRow$realm(context.getRow());
        proxyState.setAcceptDefaultValue$realm(context.getAcceptDefaultValue());
        proxyState.setExcludeFields$realm(context.getExcludeFields());
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$remark() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.remarkColKey);
    }

    @Override
    public void realmSet$remark(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.remarkColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.remarkColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.remarkColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.remarkColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$totalBalance() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.totalBalanceColKey);
    }

    @Override
    public void realmSet$totalBalance(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.totalBalanceColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.totalBalanceColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.totalBalanceColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.totalBalanceColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$time() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.timeColKey);
    }

    @Override
    public void realmSet$time(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.timeColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.timeColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.timeColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.timeColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$balance() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.balanceColKey);
    }

    @Override
    public void realmSet$balance(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.balanceColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.balanceColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.balanceColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.balanceColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$date() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.dateColKey);
    }

    @Override
    public void realmSet$date(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.dateColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.dateColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.dateColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.dateColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$type() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.typeColKey);
    }

    @Override
    public void realmSet$type(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.typeColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.typeColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.typeColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.typeColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$day() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.dayColKey);
    }

    @Override
    public void realmSet$day(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.dayColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.dayColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.dayColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.dayColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$month() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.monthColKey);
    }

    @Override
    public void realmSet$month(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.monthColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.monthColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.monthColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.monthColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$year() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.yearColKey);
    }

    @Override
    public void realmSet$year(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.yearColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.yearColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.yearColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.yearColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$id() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.idColKey);
    }

    @Override
    public void realmSet$id(String value) {
        if (proxyState.isUnderConstruction()) {
            // default value of the primary key is always ignored.
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        throw new io.realm.exceptions.RealmException("Primary key field 'id' cannot be changed after object was created.");
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        OsObjectSchemaInfo.Builder builder = new OsObjectSchemaInfo.Builder(NO_ALIAS, "RecordDataRealm", false, 10, 0);
        builder.addPersistedProperty(NO_ALIAS, "remark", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "totalBalance", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "time", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "balance", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "date", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "type", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "day", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "month", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "year", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "id", RealmFieldType.STRING, Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static RecordDataRealmColumnInfo createColumnInfo(OsSchemaInfo schemaInfo) {
        return new RecordDataRealmColumnInfo(schemaInfo);
    }

    public static String getSimpleClassName() {
        return "RecordDataRealm";
    }

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "RecordDataRealm";
    }

    @SuppressWarnings("cast")
    public static com.example.hissabbookapp.realmmodals.RecordDataRealm createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = Collections.<String> emptyList();
        com.example.hissabbookapp.realmmodals.RecordDataRealm obj = null;
        if (update) {
            Table table = realm.getTable(com.example.hissabbookapp.realmmodals.RecordDataRealm.class);
            RecordDataRealmColumnInfo columnInfo = (RecordDataRealmColumnInfo) realm.getSchema().getColumnInfo(com.example.hissabbookapp.realmmodals.RecordDataRealm.class);
            long pkColumnKey = columnInfo.idColKey;
            long objKey = Table.NO_MATCH;
            if (json.isNull("id")) {
                objKey = table.findFirstNull(pkColumnKey);
            } else {
                objKey = table.findFirstString(pkColumnKey, json.getString("id"));
            }
            if (objKey != Table.NO_MATCH) {
                final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
                try {
                    objectContext.set(realm, table.getUncheckedRow(objKey), realm.getSchema().getColumnInfo(com.example.hissabbookapp.realmmodals.RecordDataRealm.class), false, Collections.<String> emptyList());
                    obj = new io.realm.com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxy();
                } finally {
                    objectContext.clear();
                }
            }
        }
        if (obj == null) {
            if (json.has("id")) {
                if (json.isNull("id")) {
                    obj = (io.realm.com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxy) realm.createObjectInternal(com.example.hissabbookapp.realmmodals.RecordDataRealm.class, null, true, excludeFields);
                } else {
                    obj = (io.realm.com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxy) realm.createObjectInternal(com.example.hissabbookapp.realmmodals.RecordDataRealm.class, json.getString("id"), true, excludeFields);
                }
            } else {
                throw new IllegalArgumentException("JSON object doesn't have the primary key field 'id'.");
            }
        }

        final com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface objProxy = (com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) obj;
        if (json.has("remark")) {
            if (json.isNull("remark")) {
                objProxy.realmSet$remark(null);
            } else {
                objProxy.realmSet$remark((String) json.getString("remark"));
            }
        }
        if (json.has("totalBalance")) {
            if (json.isNull("totalBalance")) {
                objProxy.realmSet$totalBalance(null);
            } else {
                objProxy.realmSet$totalBalance((String) json.getString("totalBalance"));
            }
        }
        if (json.has("time")) {
            if (json.isNull("time")) {
                objProxy.realmSet$time(null);
            } else {
                objProxy.realmSet$time((String) json.getString("time"));
            }
        }
        if (json.has("balance")) {
            if (json.isNull("balance")) {
                objProxy.realmSet$balance(null);
            } else {
                objProxy.realmSet$balance((String) json.getString("balance"));
            }
        }
        if (json.has("date")) {
            if (json.isNull("date")) {
                objProxy.realmSet$date(null);
            } else {
                objProxy.realmSet$date((String) json.getString("date"));
            }
        }
        if (json.has("type")) {
            if (json.isNull("type")) {
                objProxy.realmSet$type(null);
            } else {
                objProxy.realmSet$type((String) json.getString("type"));
            }
        }
        if (json.has("day")) {
            if (json.isNull("day")) {
                objProxy.realmSet$day(null);
            } else {
                objProxy.realmSet$day((String) json.getString("day"));
            }
        }
        if (json.has("month")) {
            if (json.isNull("month")) {
                objProxy.realmSet$month(null);
            } else {
                objProxy.realmSet$month((String) json.getString("month"));
            }
        }
        if (json.has("year")) {
            if (json.isNull("year")) {
                objProxy.realmSet$year(null);
            } else {
                objProxy.realmSet$year((String) json.getString("year"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static com.example.hissabbookapp.realmmodals.RecordDataRealm createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        boolean jsonHasPrimaryKey = false;
        final com.example.hissabbookapp.realmmodals.RecordDataRealm obj = new com.example.hissabbookapp.realmmodals.RecordDataRealm();
        final com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface objProxy = (com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) obj;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (false) {
            } else if (name.equals("remark")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$remark((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$remark(null);
                }
            } else if (name.equals("totalBalance")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$totalBalance((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$totalBalance(null);
                }
            } else if (name.equals("time")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$time((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$time(null);
                }
            } else if (name.equals("balance")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$balance((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$balance(null);
                }
            } else if (name.equals("date")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$date((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$date(null);
                }
            } else if (name.equals("type")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$type((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$type(null);
                }
            } else if (name.equals("day")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$day((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$day(null);
                }
            } else if (name.equals("month")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$month((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$month(null);
                }
            } else if (name.equals("year")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$year((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$year(null);
                }
            } else if (name.equals("id")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$id((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$id(null);
                }
                jsonHasPrimaryKey = true;
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        if (!jsonHasPrimaryKey) {
            throw new IllegalArgumentException("JSON object doesn't have the primary key field 'id'.");
        }
        return realm.copyToRealmOrUpdate(obj);
    }

    static com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxy newProxyInstance(BaseRealm realm, Row row) {
        // Ignore default values to avoid creating unexpected objects from RealmModel/RealmList fields
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        objectContext.set(realm, row, realm.getSchema().getColumnInfo(com.example.hissabbookapp.realmmodals.RecordDataRealm.class), false, Collections.<String>emptyList());
        io.realm.com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxy obj = new io.realm.com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxy();
        objectContext.clear();
        return obj;
    }

    public static com.example.hissabbookapp.realmmodals.RecordDataRealm copyOrUpdate(Realm realm, RecordDataRealmColumnInfo columnInfo, com.example.hissabbookapp.realmmodals.RecordDataRealm object, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        if (object instanceof RealmObjectProxy && !RealmObject.isFrozen(object) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null) {
            final BaseRealm otherRealm = ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm();
            if (otherRealm.threadId != realm.threadId) {
                throw new IllegalArgumentException("Objects which belong to Realm instances in other threads cannot be copied into this Realm instance.");
            }
            if (otherRealm.getPath().equals(realm.getPath())) {
                return object;
            }
        }
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        RealmObjectProxy cachedRealmObject = cache.get(object);
        if (cachedRealmObject != null) {
            return (com.example.hissabbookapp.realmmodals.RecordDataRealm) cachedRealmObject;
        }

        com.example.hissabbookapp.realmmodals.RecordDataRealm realmObject = null;
        boolean canUpdate = update;
        if (canUpdate) {
            Table table = realm.getTable(com.example.hissabbookapp.realmmodals.RecordDataRealm.class);
            long pkColumnKey = columnInfo.idColKey;
            String value = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$id();
            long objKey = Table.NO_MATCH;
            if (value == null) {
                objKey = table.findFirstNull(pkColumnKey);
            } else {
                objKey = table.findFirstString(pkColumnKey, value);
            }
            if (objKey == Table.NO_MATCH) {
                canUpdate = false;
            } else {
                try {
                    objectContext.set(realm, table.getUncheckedRow(objKey), columnInfo, false, Collections.<String> emptyList());
                    realmObject = new io.realm.com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxy();
                    cache.put(object, (RealmObjectProxy) realmObject);
                } finally {
                    objectContext.clear();
                }
            }
        }

        return (canUpdate) ? update(realm, columnInfo, realmObject, object, cache, flags) : copy(realm, columnInfo, object, update, cache, flags);
    }

    public static com.example.hissabbookapp.realmmodals.RecordDataRealm copy(Realm realm, RecordDataRealmColumnInfo columnInfo, com.example.hissabbookapp.realmmodals.RecordDataRealm newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (com.example.hissabbookapp.realmmodals.RecordDataRealm) cachedRealmObject;
        }

        com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface unmanagedSource = (com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) newObject;

        Table table = realm.getTable(com.example.hissabbookapp.realmmodals.RecordDataRealm.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, flags);

        // Add all non-"object reference" fields
        builder.addString(columnInfo.remarkColKey, unmanagedSource.realmGet$remark());
        builder.addString(columnInfo.totalBalanceColKey, unmanagedSource.realmGet$totalBalance());
        builder.addString(columnInfo.timeColKey, unmanagedSource.realmGet$time());
        builder.addString(columnInfo.balanceColKey, unmanagedSource.realmGet$balance());
        builder.addString(columnInfo.dateColKey, unmanagedSource.realmGet$date());
        builder.addString(columnInfo.typeColKey, unmanagedSource.realmGet$type());
        builder.addString(columnInfo.dayColKey, unmanagedSource.realmGet$day());
        builder.addString(columnInfo.monthColKey, unmanagedSource.realmGet$month());
        builder.addString(columnInfo.yearColKey, unmanagedSource.realmGet$year());
        builder.addString(columnInfo.idColKey, unmanagedSource.realmGet$id());

        // Create the underlying object and cache it before setting any object/objectlist references
        // This will allow us to break any circular dependencies by using the object cache.
        Row row = builder.createNewObject();
        io.realm.com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxy managedCopy = newProxyInstance(realm, row);
        cache.put(newObject, managedCopy);

        return managedCopy;
    }

    public static long insert(Realm realm, com.example.hissabbookapp.realmmodals.RecordDataRealm object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && !RealmObject.isFrozen(object) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getObjectKey();
        }
        Table table = realm.getTable(com.example.hissabbookapp.realmmodals.RecordDataRealm.class);
        long tableNativePtr = table.getNativePtr();
        RecordDataRealmColumnInfo columnInfo = (RecordDataRealmColumnInfo) realm.getSchema().getColumnInfo(com.example.hissabbookapp.realmmodals.RecordDataRealm.class);
        long pkColumnKey = columnInfo.idColKey;
        String primaryKeyValue = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$id();
        long objKey = Table.NO_MATCH;
        if (primaryKeyValue == null) {
            objKey = Table.nativeFindFirstNull(tableNativePtr, pkColumnKey);
        } else {
            objKey = Table.nativeFindFirstString(tableNativePtr, pkColumnKey, primaryKeyValue);
        }
        if (objKey == Table.NO_MATCH) {
            objKey = OsObject.createRowWithPrimaryKey(table, pkColumnKey, primaryKeyValue);
        } else {
            Table.throwDuplicatePrimaryKeyException(primaryKeyValue);
        }
        cache.put(object, objKey);
        String realmGet$remark = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$remark();
        if (realmGet$remark != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.remarkColKey, objKey, realmGet$remark, false);
        }
        String realmGet$totalBalance = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$totalBalance();
        if (realmGet$totalBalance != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.totalBalanceColKey, objKey, realmGet$totalBalance, false);
        }
        String realmGet$time = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$time();
        if (realmGet$time != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.timeColKey, objKey, realmGet$time, false);
        }
        String realmGet$balance = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$balance();
        if (realmGet$balance != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.balanceColKey, objKey, realmGet$balance, false);
        }
        String realmGet$date = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$date();
        if (realmGet$date != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.dateColKey, objKey, realmGet$date, false);
        }
        String realmGet$type = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$type();
        if (realmGet$type != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.typeColKey, objKey, realmGet$type, false);
        }
        String realmGet$day = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$day();
        if (realmGet$day != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.dayColKey, objKey, realmGet$day, false);
        }
        String realmGet$month = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$month();
        if (realmGet$month != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.monthColKey, objKey, realmGet$month, false);
        }
        String realmGet$year = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$year();
        if (realmGet$year != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.yearColKey, objKey, realmGet$year, false);
        }
        return objKey;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.example.hissabbookapp.realmmodals.RecordDataRealm.class);
        long tableNativePtr = table.getNativePtr();
        RecordDataRealmColumnInfo columnInfo = (RecordDataRealmColumnInfo) realm.getSchema().getColumnInfo(com.example.hissabbookapp.realmmodals.RecordDataRealm.class);
        long pkColumnKey = columnInfo.idColKey;
        com.example.hissabbookapp.realmmodals.RecordDataRealm object = null;
        while (objects.hasNext()) {
            object = (com.example.hissabbookapp.realmmodals.RecordDataRealm) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && !RealmObject.isFrozen(object) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getObjectKey());
                continue;
            }
            String primaryKeyValue = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$id();
            long objKey = Table.NO_MATCH;
            if (primaryKeyValue == null) {
                objKey = Table.nativeFindFirstNull(tableNativePtr, pkColumnKey);
            } else {
                objKey = Table.nativeFindFirstString(tableNativePtr, pkColumnKey, primaryKeyValue);
            }
            if (objKey == Table.NO_MATCH) {
                objKey = OsObject.createRowWithPrimaryKey(table, pkColumnKey, primaryKeyValue);
            } else {
                Table.throwDuplicatePrimaryKeyException(primaryKeyValue);
            }
            cache.put(object, objKey);
            String realmGet$remark = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$remark();
            if (realmGet$remark != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.remarkColKey, objKey, realmGet$remark, false);
            }
            String realmGet$totalBalance = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$totalBalance();
            if (realmGet$totalBalance != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.totalBalanceColKey, objKey, realmGet$totalBalance, false);
            }
            String realmGet$time = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$time();
            if (realmGet$time != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.timeColKey, objKey, realmGet$time, false);
            }
            String realmGet$balance = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$balance();
            if (realmGet$balance != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.balanceColKey, objKey, realmGet$balance, false);
            }
            String realmGet$date = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$date();
            if (realmGet$date != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.dateColKey, objKey, realmGet$date, false);
            }
            String realmGet$type = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$type();
            if (realmGet$type != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.typeColKey, objKey, realmGet$type, false);
            }
            String realmGet$day = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$day();
            if (realmGet$day != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.dayColKey, objKey, realmGet$day, false);
            }
            String realmGet$month = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$month();
            if (realmGet$month != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.monthColKey, objKey, realmGet$month, false);
            }
            String realmGet$year = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$year();
            if (realmGet$year != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.yearColKey, objKey, realmGet$year, false);
            }
        }
    }

    public static long insertOrUpdate(Realm realm, com.example.hissabbookapp.realmmodals.RecordDataRealm object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && !RealmObject.isFrozen(object) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getObjectKey();
        }
        Table table = realm.getTable(com.example.hissabbookapp.realmmodals.RecordDataRealm.class);
        long tableNativePtr = table.getNativePtr();
        RecordDataRealmColumnInfo columnInfo = (RecordDataRealmColumnInfo) realm.getSchema().getColumnInfo(com.example.hissabbookapp.realmmodals.RecordDataRealm.class);
        long pkColumnKey = columnInfo.idColKey;
        String primaryKeyValue = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$id();
        long objKey = Table.NO_MATCH;
        if (primaryKeyValue == null) {
            objKey = Table.nativeFindFirstNull(tableNativePtr, pkColumnKey);
        } else {
            objKey = Table.nativeFindFirstString(tableNativePtr, pkColumnKey, primaryKeyValue);
        }
        if (objKey == Table.NO_MATCH) {
            objKey = OsObject.createRowWithPrimaryKey(table, pkColumnKey, primaryKeyValue);
        }
        cache.put(object, objKey);
        String realmGet$remark = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$remark();
        if (realmGet$remark != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.remarkColKey, objKey, realmGet$remark, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.remarkColKey, objKey, false);
        }
        String realmGet$totalBalance = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$totalBalance();
        if (realmGet$totalBalance != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.totalBalanceColKey, objKey, realmGet$totalBalance, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.totalBalanceColKey, objKey, false);
        }
        String realmGet$time = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$time();
        if (realmGet$time != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.timeColKey, objKey, realmGet$time, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.timeColKey, objKey, false);
        }
        String realmGet$balance = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$balance();
        if (realmGet$balance != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.balanceColKey, objKey, realmGet$balance, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.balanceColKey, objKey, false);
        }
        String realmGet$date = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$date();
        if (realmGet$date != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.dateColKey, objKey, realmGet$date, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.dateColKey, objKey, false);
        }
        String realmGet$type = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$type();
        if (realmGet$type != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.typeColKey, objKey, realmGet$type, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.typeColKey, objKey, false);
        }
        String realmGet$day = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$day();
        if (realmGet$day != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.dayColKey, objKey, realmGet$day, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.dayColKey, objKey, false);
        }
        String realmGet$month = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$month();
        if (realmGet$month != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.monthColKey, objKey, realmGet$month, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.monthColKey, objKey, false);
        }
        String realmGet$year = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$year();
        if (realmGet$year != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.yearColKey, objKey, realmGet$year, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.yearColKey, objKey, false);
        }
        return objKey;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.example.hissabbookapp.realmmodals.RecordDataRealm.class);
        long tableNativePtr = table.getNativePtr();
        RecordDataRealmColumnInfo columnInfo = (RecordDataRealmColumnInfo) realm.getSchema().getColumnInfo(com.example.hissabbookapp.realmmodals.RecordDataRealm.class);
        long pkColumnKey = columnInfo.idColKey;
        com.example.hissabbookapp.realmmodals.RecordDataRealm object = null;
        while (objects.hasNext()) {
            object = (com.example.hissabbookapp.realmmodals.RecordDataRealm) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && !RealmObject.isFrozen(object) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getObjectKey());
                continue;
            }
            String primaryKeyValue = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$id();
            long objKey = Table.NO_MATCH;
            if (primaryKeyValue == null) {
                objKey = Table.nativeFindFirstNull(tableNativePtr, pkColumnKey);
            } else {
                objKey = Table.nativeFindFirstString(tableNativePtr, pkColumnKey, primaryKeyValue);
            }
            if (objKey == Table.NO_MATCH) {
                objKey = OsObject.createRowWithPrimaryKey(table, pkColumnKey, primaryKeyValue);
            }
            cache.put(object, objKey);
            String realmGet$remark = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$remark();
            if (realmGet$remark != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.remarkColKey, objKey, realmGet$remark, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.remarkColKey, objKey, false);
            }
            String realmGet$totalBalance = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$totalBalance();
            if (realmGet$totalBalance != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.totalBalanceColKey, objKey, realmGet$totalBalance, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.totalBalanceColKey, objKey, false);
            }
            String realmGet$time = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$time();
            if (realmGet$time != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.timeColKey, objKey, realmGet$time, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.timeColKey, objKey, false);
            }
            String realmGet$balance = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$balance();
            if (realmGet$balance != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.balanceColKey, objKey, realmGet$balance, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.balanceColKey, objKey, false);
            }
            String realmGet$date = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$date();
            if (realmGet$date != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.dateColKey, objKey, realmGet$date, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.dateColKey, objKey, false);
            }
            String realmGet$type = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$type();
            if (realmGet$type != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.typeColKey, objKey, realmGet$type, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.typeColKey, objKey, false);
            }
            String realmGet$day = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$day();
            if (realmGet$day != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.dayColKey, objKey, realmGet$day, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.dayColKey, objKey, false);
            }
            String realmGet$month = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$month();
            if (realmGet$month != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.monthColKey, objKey, realmGet$month, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.monthColKey, objKey, false);
            }
            String realmGet$year = ((com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) object).realmGet$year();
            if (realmGet$year != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.yearColKey, objKey, realmGet$year, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.yearColKey, objKey, false);
            }
        }
    }

    public static com.example.hissabbookapp.realmmodals.RecordDataRealm createDetachedCopy(com.example.hissabbookapp.realmmodals.RecordDataRealm realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        com.example.hissabbookapp.realmmodals.RecordDataRealm unmanagedObject;
        if (cachedObject == null) {
            unmanagedObject = new com.example.hissabbookapp.realmmodals.RecordDataRealm();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        } else {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (com.example.hissabbookapp.realmmodals.RecordDataRealm) cachedObject.object;
            }
            unmanagedObject = (com.example.hissabbookapp.realmmodals.RecordDataRealm) cachedObject.object;
            cachedObject.minDepth = currentDepth;
        }
        com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface unmanagedCopy = (com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) unmanagedObject;
        com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface realmSource = (com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) realmObject;
        unmanagedCopy.realmSet$remark(realmSource.realmGet$remark());
        unmanagedCopy.realmSet$totalBalance(realmSource.realmGet$totalBalance());
        unmanagedCopy.realmSet$time(realmSource.realmGet$time());
        unmanagedCopy.realmSet$balance(realmSource.realmGet$balance());
        unmanagedCopy.realmSet$date(realmSource.realmGet$date());
        unmanagedCopy.realmSet$type(realmSource.realmGet$type());
        unmanagedCopy.realmSet$day(realmSource.realmGet$day());
        unmanagedCopy.realmSet$month(realmSource.realmGet$month());
        unmanagedCopy.realmSet$year(realmSource.realmGet$year());
        unmanagedCopy.realmSet$id(realmSource.realmGet$id());

        return unmanagedObject;
    }

    static com.example.hissabbookapp.realmmodals.RecordDataRealm update(Realm realm, RecordDataRealmColumnInfo columnInfo, com.example.hissabbookapp.realmmodals.RecordDataRealm realmObject, com.example.hissabbookapp.realmmodals.RecordDataRealm newObject, Map<RealmModel, RealmObjectProxy> cache, Set<ImportFlag> flags) {
        com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface realmObjectTarget = (com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) realmObject;
        com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface realmObjectSource = (com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxyInterface) newObject;
        Table table = realm.getTable(com.example.hissabbookapp.realmmodals.RecordDataRealm.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, flags);
        builder.addString(columnInfo.remarkColKey, realmObjectSource.realmGet$remark());
        builder.addString(columnInfo.totalBalanceColKey, realmObjectSource.realmGet$totalBalance());
        builder.addString(columnInfo.timeColKey, realmObjectSource.realmGet$time());
        builder.addString(columnInfo.balanceColKey, realmObjectSource.realmGet$balance());
        builder.addString(columnInfo.dateColKey, realmObjectSource.realmGet$date());
        builder.addString(columnInfo.typeColKey, realmObjectSource.realmGet$type());
        builder.addString(columnInfo.dayColKey, realmObjectSource.realmGet$day());
        builder.addString(columnInfo.monthColKey, realmObjectSource.realmGet$month());
        builder.addString(columnInfo.yearColKey, realmObjectSource.realmGet$year());
        builder.addString(columnInfo.idColKey, realmObjectSource.realmGet$id());

        builder.updateExistingTopLevelObject();
        return realmObject;
    }

    @Override
    @SuppressWarnings("ArrayToString")
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("RecordDataRealm = proxy[");
        stringBuilder.append("{remark:");
        stringBuilder.append(realmGet$remark() != null ? realmGet$remark() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{totalBalance:");
        stringBuilder.append(realmGet$totalBalance() != null ? realmGet$totalBalance() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{time:");
        stringBuilder.append(realmGet$time() != null ? realmGet$time() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{balance:");
        stringBuilder.append(realmGet$balance() != null ? realmGet$balance() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{date:");
        stringBuilder.append(realmGet$date() != null ? realmGet$date() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{type:");
        stringBuilder.append(realmGet$type() != null ? realmGet$type() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{day:");
        stringBuilder.append(realmGet$day() != null ? realmGet$day() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{month:");
        stringBuilder.append(realmGet$month() != null ? realmGet$month() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{year:");
        stringBuilder.append(realmGet$year() != null ? realmGet$year() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{id:");
        stringBuilder.append(realmGet$id() != null ? realmGet$id() : "null");
        stringBuilder.append("}");
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    @Override
    public ProxyState<?> realmGet$proxyState() {
        return proxyState;
    }

    @Override
    public int hashCode() {
        String realmName = proxyState.getRealm$realm().getPath();
        String tableName = proxyState.getRow$realm().getTable().getName();
        long objKey = proxyState.getRow$realm().getObjectKey();

        int result = 17;
        result = 31 * result + ((realmName != null) ? realmName.hashCode() : 0);
        result = 31 * result + ((tableName != null) ? tableName.hashCode() : 0);
        result = 31 * result + (int) (objKey ^ (objKey >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxy aRecordDataRealm = (com_example_hissabbookapp_realmmodals_RecordDataRealmRealmProxy)o;

        BaseRealm realm = proxyState.getRealm$realm();
        BaseRealm otherRealm = aRecordDataRealm.proxyState.getRealm$realm();
        String path = realm.getPath();
        String otherPath = otherRealm.getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;
        if (realm.isFrozen() != otherRealm.isFrozen()) return false;
        if (!realm.sharedRealm.getVersionID().equals(otherRealm.sharedRealm.getVersionID())) {
            return false;
        }

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aRecordDataRealm.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getObjectKey() != aRecordDataRealm.proxyState.getRow$realm().getObjectKey()) return false;

        return true;
    }
}
