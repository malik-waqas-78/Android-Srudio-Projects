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
public class com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxy extends com.example.hissabbookapp.realmmodals.SubRecordDataRealm
    implements RealmObjectProxy, com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface {

    static final class SubRecordDataRealmColumnInfo extends ColumnInfo {
        long remarkColKey;
        long totalBalanceColKey;
        long timeColKey;
        long balanceColKey;
        long mainIdColKey;
        long dateColKey;
        long idColKey;

        SubRecordDataRealmColumnInfo(OsSchemaInfo schemaInfo) {
            super(7);
            OsObjectSchemaInfo objectSchemaInfo = schemaInfo.getObjectSchemaInfo("SubRecordDataRealm");
            this.remarkColKey = addColumnDetails("remark", "remark", objectSchemaInfo);
            this.totalBalanceColKey = addColumnDetails("totalBalance", "totalBalance", objectSchemaInfo);
            this.timeColKey = addColumnDetails("time", "time", objectSchemaInfo);
            this.balanceColKey = addColumnDetails("balance", "balance", objectSchemaInfo);
            this.mainIdColKey = addColumnDetails("mainId", "mainId", objectSchemaInfo);
            this.dateColKey = addColumnDetails("date", "date", objectSchemaInfo);
            this.idColKey = addColumnDetails("id", "id", objectSchemaInfo);
        }

        SubRecordDataRealmColumnInfo(ColumnInfo src, boolean mutable) {
            super(src, mutable);
            copy(src, this);
        }

        @Override
        protected final ColumnInfo copy(boolean mutable) {
            return new SubRecordDataRealmColumnInfo(this, mutable);
        }

        @Override
        protected final void copy(ColumnInfo rawSrc, ColumnInfo rawDst) {
            final SubRecordDataRealmColumnInfo src = (SubRecordDataRealmColumnInfo) rawSrc;
            final SubRecordDataRealmColumnInfo dst = (SubRecordDataRealmColumnInfo) rawDst;
            dst.remarkColKey = src.remarkColKey;
            dst.totalBalanceColKey = src.totalBalanceColKey;
            dst.timeColKey = src.timeColKey;
            dst.balanceColKey = src.balanceColKey;
            dst.mainIdColKey = src.mainIdColKey;
            dst.dateColKey = src.dateColKey;
            dst.idColKey = src.idColKey;
        }
    }

    private static final String NO_ALIAS = "";
    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();

    private SubRecordDataRealmColumnInfo columnInfo;
    private ProxyState<com.example.hissabbookapp.realmmodals.SubRecordDataRealm> proxyState;

    com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (SubRecordDataRealmColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<com.example.hissabbookapp.realmmodals.SubRecordDataRealm>(this);
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
    public String realmGet$mainId() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.mainIdColKey);
    }

    @Override
    public void realmSet$mainId(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.mainIdColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.mainIdColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.mainIdColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.mainIdColKey, value);
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
        OsObjectSchemaInfo.Builder builder = new OsObjectSchemaInfo.Builder(NO_ALIAS, "SubRecordDataRealm", false, 7, 0);
        builder.addPersistedProperty(NO_ALIAS, "remark", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "totalBalance", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "time", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "balance", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "mainId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "date", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "id", RealmFieldType.STRING, Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static SubRecordDataRealmColumnInfo createColumnInfo(OsSchemaInfo schemaInfo) {
        return new SubRecordDataRealmColumnInfo(schemaInfo);
    }

    public static String getSimpleClassName() {
        return "SubRecordDataRealm";
    }

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "SubRecordDataRealm";
    }

    @SuppressWarnings("cast")
    public static com.example.hissabbookapp.realmmodals.SubRecordDataRealm createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = Collections.<String> emptyList();
        com.example.hissabbookapp.realmmodals.SubRecordDataRealm obj = null;
        if (update) {
            Table table = realm.getTable(com.example.hissabbookapp.realmmodals.SubRecordDataRealm.class);
            SubRecordDataRealmColumnInfo columnInfo = (SubRecordDataRealmColumnInfo) realm.getSchema().getColumnInfo(com.example.hissabbookapp.realmmodals.SubRecordDataRealm.class);
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
                    objectContext.set(realm, table.getUncheckedRow(objKey), realm.getSchema().getColumnInfo(com.example.hissabbookapp.realmmodals.SubRecordDataRealm.class), false, Collections.<String> emptyList());
                    obj = new io.realm.com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxy();
                } finally {
                    objectContext.clear();
                }
            }
        }
        if (obj == null) {
            if (json.has("id")) {
                if (json.isNull("id")) {
                    obj = (io.realm.com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxy) realm.createObjectInternal(com.example.hissabbookapp.realmmodals.SubRecordDataRealm.class, null, true, excludeFields);
                } else {
                    obj = (io.realm.com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxy) realm.createObjectInternal(com.example.hissabbookapp.realmmodals.SubRecordDataRealm.class, json.getString("id"), true, excludeFields);
                }
            } else {
                throw new IllegalArgumentException("JSON object doesn't have the primary key field 'id'.");
            }
        }

        final com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface objProxy = (com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface) obj;
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
        if (json.has("mainId")) {
            if (json.isNull("mainId")) {
                objProxy.realmSet$mainId(null);
            } else {
                objProxy.realmSet$mainId((String) json.getString("mainId"));
            }
        }
        if (json.has("date")) {
            if (json.isNull("date")) {
                objProxy.realmSet$date(null);
            } else {
                objProxy.realmSet$date((String) json.getString("date"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static com.example.hissabbookapp.realmmodals.SubRecordDataRealm createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        boolean jsonHasPrimaryKey = false;
        final com.example.hissabbookapp.realmmodals.SubRecordDataRealm obj = new com.example.hissabbookapp.realmmodals.SubRecordDataRealm();
        final com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface objProxy = (com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface) obj;
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
            } else if (name.equals("mainId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$mainId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$mainId(null);
                }
            } else if (name.equals("date")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$date((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$date(null);
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

    static com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxy newProxyInstance(BaseRealm realm, Row row) {
        // Ignore default values to avoid creating unexpected objects from RealmModel/RealmList fields
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        objectContext.set(realm, row, realm.getSchema().getColumnInfo(com.example.hissabbookapp.realmmodals.SubRecordDataRealm.class), false, Collections.<String>emptyList());
        io.realm.com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxy obj = new io.realm.com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxy();
        objectContext.clear();
        return obj;
    }

    public static com.example.hissabbookapp.realmmodals.SubRecordDataRealm copyOrUpdate(Realm realm, SubRecordDataRealmColumnInfo columnInfo, com.example.hissabbookapp.realmmodals.SubRecordDataRealm object, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
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
            return (com.example.hissabbookapp.realmmodals.SubRecordDataRealm) cachedRealmObject;
        }

        com.example.hissabbookapp.realmmodals.SubRecordDataRealm realmObject = null;
        boolean canUpdate = update;
        if (canUpdate) {
            Table table = realm.getTable(com.example.hissabbookapp.realmmodals.SubRecordDataRealm.class);
            long pkColumnKey = columnInfo.idColKey;
            String value = ((com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface) object).realmGet$id();
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
                    realmObject = new io.realm.com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxy();
                    cache.put(object, (RealmObjectProxy) realmObject);
                } finally {
                    objectContext.clear();
                }
            }
        }

        return (canUpdate) ? update(realm, columnInfo, realmObject, object, cache, flags) : copy(realm, columnInfo, object, update, cache, flags);
    }

    public static com.example.hissabbookapp.realmmodals.SubRecordDataRealm copy(Realm realm, SubRecordDataRealmColumnInfo columnInfo, com.example.hissabbookapp.realmmodals.SubRecordDataRealm newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (com.example.hissabbookapp.realmmodals.SubRecordDataRealm) cachedRealmObject;
        }

        com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface unmanagedSource = (com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface) newObject;

        Table table = realm.getTable(com.example.hissabbookapp.realmmodals.SubRecordDataRealm.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, flags);

        // Add all non-"object reference" fields
        builder.addString(columnInfo.remarkColKey, unmanagedSource.realmGet$remark());
        builder.addString(columnInfo.totalBalanceColKey, unmanagedSource.realmGet$totalBalance());
        builder.addString(columnInfo.timeColKey, unmanagedSource.realmGet$time());
        builder.addString(columnInfo.balanceColKey, unmanagedSource.realmGet$balance());
        builder.addString(columnInfo.mainIdColKey, unmanagedSource.realmGet$mainId());
        builder.addString(columnInfo.dateColKey, unmanagedSource.realmGet$date());
        builder.addString(columnInfo.idColKey, unmanagedSource.realmGet$id());

        // Create the underlying object and cache it before setting any object/objectlist references
        // This will allow us to break any circular dependencies by using the object cache.
        Row row = builder.createNewObject();
        io.realm.com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxy managedCopy = newProxyInstance(realm, row);
        cache.put(newObject, managedCopy);

        return managedCopy;
    }

    public static long insert(Realm realm, com.example.hissabbookapp.realmmodals.SubRecordDataRealm object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && !RealmObject.isFrozen(object) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getObjectKey();
        }
        Table table = realm.getTable(com.example.hissabbookapp.realmmodals.SubRecordDataRealm.class);
        long tableNativePtr = table.getNativePtr();
        SubRecordDataRealmColumnInfo columnInfo = (SubRecordDataRealmColumnInfo) realm.getSchema().getColumnInfo(com.example.hissabbookapp.realmmodals.SubRecordDataRealm.class);
        long pkColumnKey = columnInfo.idColKey;
        String primaryKeyValue = ((com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface) object).realmGet$id();
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
        String realmGet$remark = ((com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface) object).realmGet$remark();
        if (realmGet$remark != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.remarkColKey, objKey, realmGet$remark, false);
        }
        String realmGet$totalBalance = ((com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface) object).realmGet$totalBalance();
        if (realmGet$totalBalance != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.totalBalanceColKey, objKey, realmGet$totalBalance, false);
        }
        String realmGet$time = ((com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface) object).realmGet$time();
        if (realmGet$time != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.timeColKey, objKey, realmGet$time, false);
        }
        String realmGet$balance = ((com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface) object).realmGet$balance();
        if (realmGet$balance != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.balanceColKey, objKey, realmGet$balance, false);
        }
        String realmGet$mainId = ((com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface) object).realmGet$mainId();
        if (realmGet$mainId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.mainIdColKey, objKey, realmGet$mainId, false);
        }
        String realmGet$date = ((com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface) object).realmGet$date();
        if (realmGet$date != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.dateColKey, objKey, realmGet$date, false);
        }
        return objKey;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.example.hissabbookapp.realmmodals.SubRecordDataRealm.class);
        long tableNativePtr = table.getNativePtr();
        SubRecordDataRealmColumnInfo columnInfo = (SubRecordDataRealmColumnInfo) realm.getSchema().getColumnInfo(com.example.hissabbookapp.realmmodals.SubRecordDataRealm.class);
        long pkColumnKey = columnInfo.idColKey;
        com.example.hissabbookapp.realmmodals.SubRecordDataRealm object = null;
        while (objects.hasNext()) {
            object = (com.example.hissabbookapp.realmmodals.SubRecordDataRealm) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && !RealmObject.isFrozen(object) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getObjectKey());
                continue;
            }
            String primaryKeyValue = ((com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface) object).realmGet$id();
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
            String realmGet$remark = ((com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface) object).realmGet$remark();
            if (realmGet$remark != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.remarkColKey, objKey, realmGet$remark, false);
            }
            String realmGet$totalBalance = ((com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface) object).realmGet$totalBalance();
            if (realmGet$totalBalance != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.totalBalanceColKey, objKey, realmGet$totalBalance, false);
            }
            String realmGet$time = ((com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface) object).realmGet$time();
            if (realmGet$time != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.timeColKey, objKey, realmGet$time, false);
            }
            String realmGet$balance = ((com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface) object).realmGet$balance();
            if (realmGet$balance != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.balanceColKey, objKey, realmGet$balance, false);
            }
            String realmGet$mainId = ((com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface) object).realmGet$mainId();
            if (realmGet$mainId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.mainIdColKey, objKey, realmGet$mainId, false);
            }
            String realmGet$date = ((com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface) object).realmGet$date();
            if (realmGet$date != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.dateColKey, objKey, realmGet$date, false);
            }
        }
    }

    public static long insertOrUpdate(Realm realm, com.example.hissabbookapp.realmmodals.SubRecordDataRealm object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && !RealmObject.isFrozen(object) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getObjectKey();
        }
        Table table = realm.getTable(com.example.hissabbookapp.realmmodals.SubRecordDataRealm.class);
        long tableNativePtr = table.getNativePtr();
        SubRecordDataRealmColumnInfo columnInfo = (SubRecordDataRealmColumnInfo) realm.getSchema().getColumnInfo(com.example.hissabbookapp.realmmodals.SubRecordDataRealm.class);
        long pkColumnKey = columnInfo.idColKey;
        String primaryKeyValue = ((com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface) object).realmGet$id();
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
        String realmGet$remark = ((com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface) object).realmGet$remark();
        if (realmGet$remark != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.remarkColKey, objKey, realmGet$remark, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.remarkColKey, objKey, false);
        }
        String realmGet$totalBalance = ((com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface) object).realmGet$totalBalance();
        if (realmGet$totalBalance != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.totalBalanceColKey, objKey, realmGet$totalBalance, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.totalBalanceColKey, objKey, false);
        }
        String realmGet$time = ((com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface) object).realmGet$time();
        if (realmGet$time != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.timeColKey, objKey, realmGet$time, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.timeColKey, objKey, false);
        }
        String realmGet$balance = ((com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface) object).realmGet$balance();
        if (realmGet$balance != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.balanceColKey, objKey, realmGet$balance, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.balanceColKey, objKey, false);
        }
        String realmGet$mainId = ((com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface) object).realmGet$mainId();
        if (realmGet$mainId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.mainIdColKey, objKey, realmGet$mainId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.mainIdColKey, objKey, false);
        }
        String realmGet$date = ((com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface) object).realmGet$date();
        if (realmGet$date != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.dateColKey, objKey, realmGet$date, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.dateColKey, objKey, false);
        }
        return objKey;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.example.hissabbookapp.realmmodals.SubRecordDataRealm.class);
        long tableNativePtr = table.getNativePtr();
        SubRecordDataRealmColumnInfo columnInfo = (SubRecordDataRealmColumnInfo) realm.getSchema().getColumnInfo(com.example.hissabbookapp.realmmodals.SubRecordDataRealm.class);
        long pkColumnKey = columnInfo.idColKey;
        com.example.hissabbookapp.realmmodals.SubRecordDataRealm object = null;
        while (objects.hasNext()) {
            object = (com.example.hissabbookapp.realmmodals.SubRecordDataRealm) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && !RealmObject.isFrozen(object) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getObjectKey());
                continue;
            }
            String primaryKeyValue = ((com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface) object).realmGet$id();
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
            String realmGet$remark = ((com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface) object).realmGet$remark();
            if (realmGet$remark != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.remarkColKey, objKey, realmGet$remark, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.remarkColKey, objKey, false);
            }
            String realmGet$totalBalance = ((com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface) object).realmGet$totalBalance();
            if (realmGet$totalBalance != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.totalBalanceColKey, objKey, realmGet$totalBalance, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.totalBalanceColKey, objKey, false);
            }
            String realmGet$time = ((com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface) object).realmGet$time();
            if (realmGet$time != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.timeColKey, objKey, realmGet$time, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.timeColKey, objKey, false);
            }
            String realmGet$balance = ((com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface) object).realmGet$balance();
            if (realmGet$balance != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.balanceColKey, objKey, realmGet$balance, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.balanceColKey, objKey, false);
            }
            String realmGet$mainId = ((com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface) object).realmGet$mainId();
            if (realmGet$mainId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.mainIdColKey, objKey, realmGet$mainId, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.mainIdColKey, objKey, false);
            }
            String realmGet$date = ((com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface) object).realmGet$date();
            if (realmGet$date != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.dateColKey, objKey, realmGet$date, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.dateColKey, objKey, false);
            }
        }
    }

    public static com.example.hissabbookapp.realmmodals.SubRecordDataRealm createDetachedCopy(com.example.hissabbookapp.realmmodals.SubRecordDataRealm realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        com.example.hissabbookapp.realmmodals.SubRecordDataRealm unmanagedObject;
        if (cachedObject == null) {
            unmanagedObject = new com.example.hissabbookapp.realmmodals.SubRecordDataRealm();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        } else {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (com.example.hissabbookapp.realmmodals.SubRecordDataRealm) cachedObject.object;
            }
            unmanagedObject = (com.example.hissabbookapp.realmmodals.SubRecordDataRealm) cachedObject.object;
            cachedObject.minDepth = currentDepth;
        }
        com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface unmanagedCopy = (com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface) unmanagedObject;
        com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface realmSource = (com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface) realmObject;
        unmanagedCopy.realmSet$remark(realmSource.realmGet$remark());
        unmanagedCopy.realmSet$totalBalance(realmSource.realmGet$totalBalance());
        unmanagedCopy.realmSet$time(realmSource.realmGet$time());
        unmanagedCopy.realmSet$balance(realmSource.realmGet$balance());
        unmanagedCopy.realmSet$mainId(realmSource.realmGet$mainId());
        unmanagedCopy.realmSet$date(realmSource.realmGet$date());
        unmanagedCopy.realmSet$id(realmSource.realmGet$id());

        return unmanagedObject;
    }

    static com.example.hissabbookapp.realmmodals.SubRecordDataRealm update(Realm realm, SubRecordDataRealmColumnInfo columnInfo, com.example.hissabbookapp.realmmodals.SubRecordDataRealm realmObject, com.example.hissabbookapp.realmmodals.SubRecordDataRealm newObject, Map<RealmModel, RealmObjectProxy> cache, Set<ImportFlag> flags) {
        com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface realmObjectTarget = (com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface) realmObject;
        com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface realmObjectSource = (com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxyInterface) newObject;
        Table table = realm.getTable(com.example.hissabbookapp.realmmodals.SubRecordDataRealm.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, flags);
        builder.addString(columnInfo.remarkColKey, realmObjectSource.realmGet$remark());
        builder.addString(columnInfo.totalBalanceColKey, realmObjectSource.realmGet$totalBalance());
        builder.addString(columnInfo.timeColKey, realmObjectSource.realmGet$time());
        builder.addString(columnInfo.balanceColKey, realmObjectSource.realmGet$balance());
        builder.addString(columnInfo.mainIdColKey, realmObjectSource.realmGet$mainId());
        builder.addString(columnInfo.dateColKey, realmObjectSource.realmGet$date());
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
        StringBuilder stringBuilder = new StringBuilder("SubRecordDataRealm = proxy[");
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
        stringBuilder.append("{mainId:");
        stringBuilder.append(realmGet$mainId() != null ? realmGet$mainId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{date:");
        stringBuilder.append(realmGet$date() != null ? realmGet$date() : "null");
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
        com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxy aSubRecordDataRealm = (com_example_hissabbookapp_realmmodals_SubRecordDataRealmRealmProxy)o;

        BaseRealm realm = proxyState.getRealm$realm();
        BaseRealm otherRealm = aSubRecordDataRealm.proxyState.getRealm$realm();
        String path = realm.getPath();
        String otherPath = otherRealm.getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;
        if (realm.isFrozen() != otherRealm.isFrozen()) return false;
        if (!realm.sharedRealm.getVersionID().equals(otherRealm.sharedRealm.getVersionID())) {
            return false;
        }

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aSubRecordDataRealm.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getObjectKey() != aSubRecordDataRealm.proxyState.getRow$realm().getObjectKey()) return false;

        return true;
    }
}
