package com.dumpdiary.app.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile ProfileDao _profileDao;

  private volatile LogDao _logDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(3) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `profile` (`userId` TEXT NOT NULL, `email` TEXT NOT NULL, `displayName` TEXT NOT NULL, `avatarUrl` TEXT, `updatedAt` TEXT NOT NULL, PRIMARY KEY(`userId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `bowel_logs` (`id` TEXT NOT NULL, `userId` TEXT NOT NULL, `occurredAt` TEXT NOT NULL, `dateKey` TEXT NOT NULL, `durationSeconds` INTEGER NOT NULL, `feeling` TEXT NOT NULL, `stoolForm` INTEGER NOT NULL, `symptomTagsRaw` TEXT NOT NULL, `detailNote` TEXT NOT NULL, `snapshotDisplayName` TEXT NOT NULL, `snapshotAvatarUrl` TEXT, `createdAt` TEXT NOT NULL, `updatedAt` TEXT NOT NULL, `isDeleted` INTEGER NOT NULL, `pendingSyncAction` TEXT, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '6878f4c16bf33cb769ba401fb3d367a5')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `profile`");
        db.execSQL("DROP TABLE IF EXISTS `bowel_logs`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsProfile = new HashMap<String, TableInfo.Column>(5);
        _columnsProfile.put("userId", new TableInfo.Column("userId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProfile.put("email", new TableInfo.Column("email", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProfile.put("displayName", new TableInfo.Column("displayName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProfile.put("avatarUrl", new TableInfo.Column("avatarUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProfile.put("updatedAt", new TableInfo.Column("updatedAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysProfile = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesProfile = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoProfile = new TableInfo("profile", _columnsProfile, _foreignKeysProfile, _indicesProfile);
        final TableInfo _existingProfile = TableInfo.read(db, "profile");
        if (!_infoProfile.equals(_existingProfile)) {
          return new RoomOpenHelper.ValidationResult(false, "profile(com.dumpdiary.app.data.model.UserProfileEntity).\n"
                  + " Expected:\n" + _infoProfile + "\n"
                  + " Found:\n" + _existingProfile);
        }
        final HashMap<String, TableInfo.Column> _columnsBowelLogs = new HashMap<String, TableInfo.Column>(15);
        _columnsBowelLogs.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBowelLogs.put("userId", new TableInfo.Column("userId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBowelLogs.put("occurredAt", new TableInfo.Column("occurredAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBowelLogs.put("dateKey", new TableInfo.Column("dateKey", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBowelLogs.put("durationSeconds", new TableInfo.Column("durationSeconds", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBowelLogs.put("feeling", new TableInfo.Column("feeling", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBowelLogs.put("stoolForm", new TableInfo.Column("stoolForm", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBowelLogs.put("symptomTagsRaw", new TableInfo.Column("symptomTagsRaw", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBowelLogs.put("detailNote", new TableInfo.Column("detailNote", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBowelLogs.put("snapshotDisplayName", new TableInfo.Column("snapshotDisplayName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBowelLogs.put("snapshotAvatarUrl", new TableInfo.Column("snapshotAvatarUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBowelLogs.put("createdAt", new TableInfo.Column("createdAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBowelLogs.put("updatedAt", new TableInfo.Column("updatedAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBowelLogs.put("isDeleted", new TableInfo.Column("isDeleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBowelLogs.put("pendingSyncAction", new TableInfo.Column("pendingSyncAction", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBowelLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBowelLogs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBowelLogs = new TableInfo("bowel_logs", _columnsBowelLogs, _foreignKeysBowelLogs, _indicesBowelLogs);
        final TableInfo _existingBowelLogs = TableInfo.read(db, "bowel_logs");
        if (!_infoBowelLogs.equals(_existingBowelLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "bowel_logs(com.dumpdiary.app.data.model.BowelLogEntity).\n"
                  + " Expected:\n" + _infoBowelLogs + "\n"
                  + " Found:\n" + _existingBowelLogs);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "6878f4c16bf33cb769ba401fb3d367a5", "9a1a757f0301fb5c71772670ff9ec669");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "profile","bowel_logs");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `profile`");
      _db.execSQL("DELETE FROM `bowel_logs`");
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
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(ProfileDao.class, ProfileDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(LogDao.class, LogDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public ProfileDao profileDao() {
    if (_profileDao != null) {
      return _profileDao;
    } else {
      synchronized(this) {
        if(_profileDao == null) {
          _profileDao = new ProfileDao_Impl(this);
        }
        return _profileDao;
      }
    }
  }

  @Override
  public LogDao logDao() {
    if (_logDao != null) {
      return _logDao;
    } else {
      synchronized(this) {
        if(_logDao == null) {
          _logDao = new LogDao_Impl(this);
        }
        return _logDao;
      }
    }
  }
}
