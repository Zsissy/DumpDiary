package com.dumpdiary.app.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.dumpdiary.app.data.model.BowelLogEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class LogDao_Impl implements LogDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BowelLogEntity> __insertionAdapterOfBowelLogEntity;

  private final SharedSQLiteStatement __preparedStmtOfClear;

  public LogDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBowelLogEntity = new EntityInsertionAdapter<BowelLogEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `bowel_logs` (`id`,`userId`,`occurredAt`,`dateKey`,`durationSeconds`,`feeling`,`stoolForm`,`symptomTagsRaw`,`detailNote`,`snapshotDisplayName`,`snapshotAvatarUrl`,`createdAt`,`updatedAt`,`isDeleted`,`pendingSyncAction`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BowelLogEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getUserId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getUserId());
        }
        if (entity.getOccurredAt() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getOccurredAt());
        }
        if (entity.getDateKey() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDateKey());
        }
        statement.bindLong(5, entity.getDurationSeconds());
        if (entity.getFeeling() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getFeeling());
        }
        statement.bindLong(7, entity.getStoolForm());
        if (entity.getSymptomTagsRaw() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getSymptomTagsRaw());
        }
        if (entity.getDetailNote() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getDetailNote());
        }
        if (entity.getSnapshotDisplayName() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getSnapshotDisplayName());
        }
        if (entity.getSnapshotAvatarUrl() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getSnapshotAvatarUrl());
        }
        if (entity.getCreatedAt() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getCreatedAt());
        }
        if (entity.getUpdatedAt() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getUpdatedAt());
        }
        final int _tmp = entity.isDeleted() ? 1 : 0;
        statement.bindLong(14, _tmp);
        if (entity.getPendingSyncAction() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getPendingSyncAction());
        }
      }
    };
    this.__preparedStmtOfClear = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM bowel_logs";
        return _query;
      }
    };
  }

  @Override
  public Object upsert(final BowelLogEntity log, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBowelLogEntity.insert(log);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertAll(final List<BowelLogEntity> logs,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBowelLogEntity.insert(logs);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object clear(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClear.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClear.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<BowelLogEntity>> observeActiveLogs() {
    final String _sql = "SELECT * FROM bowel_logs WHERE isDeleted = 0 ORDER BY occurredAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"bowel_logs"}, new Callable<List<BowelLogEntity>>() {
      @Override
      @NonNull
      public List<BowelLogEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfOccurredAt = CursorUtil.getColumnIndexOrThrow(_cursor, "occurredAt");
          final int _cursorIndexOfDateKey = CursorUtil.getColumnIndexOrThrow(_cursor, "dateKey");
          final int _cursorIndexOfDurationSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "durationSeconds");
          final int _cursorIndexOfFeeling = CursorUtil.getColumnIndexOrThrow(_cursor, "feeling");
          final int _cursorIndexOfStoolForm = CursorUtil.getColumnIndexOrThrow(_cursor, "stoolForm");
          final int _cursorIndexOfSymptomTagsRaw = CursorUtil.getColumnIndexOrThrow(_cursor, "symptomTagsRaw");
          final int _cursorIndexOfDetailNote = CursorUtil.getColumnIndexOrThrow(_cursor, "detailNote");
          final int _cursorIndexOfSnapshotDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "snapshotDisplayName");
          final int _cursorIndexOfSnapshotAvatarUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "snapshotAvatarUrl");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final int _cursorIndexOfPendingSyncAction = CursorUtil.getColumnIndexOrThrow(_cursor, "pendingSyncAction");
          final List<BowelLogEntity> _result = new ArrayList<BowelLogEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BowelLogEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpUserId;
            if (_cursor.isNull(_cursorIndexOfUserId)) {
              _tmpUserId = null;
            } else {
              _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            }
            final String _tmpOccurredAt;
            if (_cursor.isNull(_cursorIndexOfOccurredAt)) {
              _tmpOccurredAt = null;
            } else {
              _tmpOccurredAt = _cursor.getString(_cursorIndexOfOccurredAt);
            }
            final String _tmpDateKey;
            if (_cursor.isNull(_cursorIndexOfDateKey)) {
              _tmpDateKey = null;
            } else {
              _tmpDateKey = _cursor.getString(_cursorIndexOfDateKey);
            }
            final int _tmpDurationSeconds;
            _tmpDurationSeconds = _cursor.getInt(_cursorIndexOfDurationSeconds);
            final String _tmpFeeling;
            if (_cursor.isNull(_cursorIndexOfFeeling)) {
              _tmpFeeling = null;
            } else {
              _tmpFeeling = _cursor.getString(_cursorIndexOfFeeling);
            }
            final int _tmpStoolForm;
            _tmpStoolForm = _cursor.getInt(_cursorIndexOfStoolForm);
            final String _tmpSymptomTagsRaw;
            if (_cursor.isNull(_cursorIndexOfSymptomTagsRaw)) {
              _tmpSymptomTagsRaw = null;
            } else {
              _tmpSymptomTagsRaw = _cursor.getString(_cursorIndexOfSymptomTagsRaw);
            }
            final String _tmpDetailNote;
            if (_cursor.isNull(_cursorIndexOfDetailNote)) {
              _tmpDetailNote = null;
            } else {
              _tmpDetailNote = _cursor.getString(_cursorIndexOfDetailNote);
            }
            final String _tmpSnapshotDisplayName;
            if (_cursor.isNull(_cursorIndexOfSnapshotDisplayName)) {
              _tmpSnapshotDisplayName = null;
            } else {
              _tmpSnapshotDisplayName = _cursor.getString(_cursorIndexOfSnapshotDisplayName);
            }
            final String _tmpSnapshotAvatarUrl;
            if (_cursor.isNull(_cursorIndexOfSnapshotAvatarUrl)) {
              _tmpSnapshotAvatarUrl = null;
            } else {
              _tmpSnapshotAvatarUrl = _cursor.getString(_cursorIndexOfSnapshotAvatarUrl);
            }
            final String _tmpCreatedAt;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmpCreatedAt = null;
            } else {
              _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            final String _tmpUpdatedAt;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmpUpdatedAt = null;
            } else {
              _tmpUpdatedAt = _cursor.getString(_cursorIndexOfUpdatedAt);
            }
            final boolean _tmpIsDeleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp != 0;
            final String _tmpPendingSyncAction;
            if (_cursor.isNull(_cursorIndexOfPendingSyncAction)) {
              _tmpPendingSyncAction = null;
            } else {
              _tmpPendingSyncAction = _cursor.getString(_cursorIndexOfPendingSyncAction);
            }
            _item = new BowelLogEntity(_tmpId,_tmpUserId,_tmpOccurredAt,_tmpDateKey,_tmpDurationSeconds,_tmpFeeling,_tmpStoolForm,_tmpSymptomTagsRaw,_tmpDetailNote,_tmpSnapshotDisplayName,_tmpSnapshotAvatarUrl,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDeleted,_tmpPendingSyncAction);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getAllLogs(final Continuation<? super List<BowelLogEntity>> $completion) {
    final String _sql = "SELECT * FROM bowel_logs ORDER BY occurredAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<BowelLogEntity>>() {
      @Override
      @NonNull
      public List<BowelLogEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfOccurredAt = CursorUtil.getColumnIndexOrThrow(_cursor, "occurredAt");
          final int _cursorIndexOfDateKey = CursorUtil.getColumnIndexOrThrow(_cursor, "dateKey");
          final int _cursorIndexOfDurationSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "durationSeconds");
          final int _cursorIndexOfFeeling = CursorUtil.getColumnIndexOrThrow(_cursor, "feeling");
          final int _cursorIndexOfStoolForm = CursorUtil.getColumnIndexOrThrow(_cursor, "stoolForm");
          final int _cursorIndexOfSymptomTagsRaw = CursorUtil.getColumnIndexOrThrow(_cursor, "symptomTagsRaw");
          final int _cursorIndexOfDetailNote = CursorUtil.getColumnIndexOrThrow(_cursor, "detailNote");
          final int _cursorIndexOfSnapshotDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "snapshotDisplayName");
          final int _cursorIndexOfSnapshotAvatarUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "snapshotAvatarUrl");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final int _cursorIndexOfPendingSyncAction = CursorUtil.getColumnIndexOrThrow(_cursor, "pendingSyncAction");
          final List<BowelLogEntity> _result = new ArrayList<BowelLogEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BowelLogEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpUserId;
            if (_cursor.isNull(_cursorIndexOfUserId)) {
              _tmpUserId = null;
            } else {
              _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            }
            final String _tmpOccurredAt;
            if (_cursor.isNull(_cursorIndexOfOccurredAt)) {
              _tmpOccurredAt = null;
            } else {
              _tmpOccurredAt = _cursor.getString(_cursorIndexOfOccurredAt);
            }
            final String _tmpDateKey;
            if (_cursor.isNull(_cursorIndexOfDateKey)) {
              _tmpDateKey = null;
            } else {
              _tmpDateKey = _cursor.getString(_cursorIndexOfDateKey);
            }
            final int _tmpDurationSeconds;
            _tmpDurationSeconds = _cursor.getInt(_cursorIndexOfDurationSeconds);
            final String _tmpFeeling;
            if (_cursor.isNull(_cursorIndexOfFeeling)) {
              _tmpFeeling = null;
            } else {
              _tmpFeeling = _cursor.getString(_cursorIndexOfFeeling);
            }
            final int _tmpStoolForm;
            _tmpStoolForm = _cursor.getInt(_cursorIndexOfStoolForm);
            final String _tmpSymptomTagsRaw;
            if (_cursor.isNull(_cursorIndexOfSymptomTagsRaw)) {
              _tmpSymptomTagsRaw = null;
            } else {
              _tmpSymptomTagsRaw = _cursor.getString(_cursorIndexOfSymptomTagsRaw);
            }
            final String _tmpDetailNote;
            if (_cursor.isNull(_cursorIndexOfDetailNote)) {
              _tmpDetailNote = null;
            } else {
              _tmpDetailNote = _cursor.getString(_cursorIndexOfDetailNote);
            }
            final String _tmpSnapshotDisplayName;
            if (_cursor.isNull(_cursorIndexOfSnapshotDisplayName)) {
              _tmpSnapshotDisplayName = null;
            } else {
              _tmpSnapshotDisplayName = _cursor.getString(_cursorIndexOfSnapshotDisplayName);
            }
            final String _tmpSnapshotAvatarUrl;
            if (_cursor.isNull(_cursorIndexOfSnapshotAvatarUrl)) {
              _tmpSnapshotAvatarUrl = null;
            } else {
              _tmpSnapshotAvatarUrl = _cursor.getString(_cursorIndexOfSnapshotAvatarUrl);
            }
            final String _tmpCreatedAt;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmpCreatedAt = null;
            } else {
              _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            final String _tmpUpdatedAt;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmpUpdatedAt = null;
            } else {
              _tmpUpdatedAt = _cursor.getString(_cursorIndexOfUpdatedAt);
            }
            final boolean _tmpIsDeleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp != 0;
            final String _tmpPendingSyncAction;
            if (_cursor.isNull(_cursorIndexOfPendingSyncAction)) {
              _tmpPendingSyncAction = null;
            } else {
              _tmpPendingSyncAction = _cursor.getString(_cursorIndexOfPendingSyncAction);
            }
            _item = new BowelLogEntity(_tmpId,_tmpUserId,_tmpOccurredAt,_tmpDateKey,_tmpDurationSeconds,_tmpFeeling,_tmpStoolForm,_tmpSymptomTagsRaw,_tmpDetailNote,_tmpSnapshotDisplayName,_tmpSnapshotAvatarUrl,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDeleted,_tmpPendingSyncAction);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getPendingSyncLogs(final Continuation<? super List<BowelLogEntity>> $completion) {
    final String _sql = "SELECT * FROM bowel_logs WHERE pendingSyncAction IS NOT NULL ORDER BY updatedAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<BowelLogEntity>>() {
      @Override
      @NonNull
      public List<BowelLogEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfOccurredAt = CursorUtil.getColumnIndexOrThrow(_cursor, "occurredAt");
          final int _cursorIndexOfDateKey = CursorUtil.getColumnIndexOrThrow(_cursor, "dateKey");
          final int _cursorIndexOfDurationSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "durationSeconds");
          final int _cursorIndexOfFeeling = CursorUtil.getColumnIndexOrThrow(_cursor, "feeling");
          final int _cursorIndexOfStoolForm = CursorUtil.getColumnIndexOrThrow(_cursor, "stoolForm");
          final int _cursorIndexOfSymptomTagsRaw = CursorUtil.getColumnIndexOrThrow(_cursor, "symptomTagsRaw");
          final int _cursorIndexOfDetailNote = CursorUtil.getColumnIndexOrThrow(_cursor, "detailNote");
          final int _cursorIndexOfSnapshotDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "snapshotDisplayName");
          final int _cursorIndexOfSnapshotAvatarUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "snapshotAvatarUrl");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final int _cursorIndexOfPendingSyncAction = CursorUtil.getColumnIndexOrThrow(_cursor, "pendingSyncAction");
          final List<BowelLogEntity> _result = new ArrayList<BowelLogEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BowelLogEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpUserId;
            if (_cursor.isNull(_cursorIndexOfUserId)) {
              _tmpUserId = null;
            } else {
              _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            }
            final String _tmpOccurredAt;
            if (_cursor.isNull(_cursorIndexOfOccurredAt)) {
              _tmpOccurredAt = null;
            } else {
              _tmpOccurredAt = _cursor.getString(_cursorIndexOfOccurredAt);
            }
            final String _tmpDateKey;
            if (_cursor.isNull(_cursorIndexOfDateKey)) {
              _tmpDateKey = null;
            } else {
              _tmpDateKey = _cursor.getString(_cursorIndexOfDateKey);
            }
            final int _tmpDurationSeconds;
            _tmpDurationSeconds = _cursor.getInt(_cursorIndexOfDurationSeconds);
            final String _tmpFeeling;
            if (_cursor.isNull(_cursorIndexOfFeeling)) {
              _tmpFeeling = null;
            } else {
              _tmpFeeling = _cursor.getString(_cursorIndexOfFeeling);
            }
            final int _tmpStoolForm;
            _tmpStoolForm = _cursor.getInt(_cursorIndexOfStoolForm);
            final String _tmpSymptomTagsRaw;
            if (_cursor.isNull(_cursorIndexOfSymptomTagsRaw)) {
              _tmpSymptomTagsRaw = null;
            } else {
              _tmpSymptomTagsRaw = _cursor.getString(_cursorIndexOfSymptomTagsRaw);
            }
            final String _tmpDetailNote;
            if (_cursor.isNull(_cursorIndexOfDetailNote)) {
              _tmpDetailNote = null;
            } else {
              _tmpDetailNote = _cursor.getString(_cursorIndexOfDetailNote);
            }
            final String _tmpSnapshotDisplayName;
            if (_cursor.isNull(_cursorIndexOfSnapshotDisplayName)) {
              _tmpSnapshotDisplayName = null;
            } else {
              _tmpSnapshotDisplayName = _cursor.getString(_cursorIndexOfSnapshotDisplayName);
            }
            final String _tmpSnapshotAvatarUrl;
            if (_cursor.isNull(_cursorIndexOfSnapshotAvatarUrl)) {
              _tmpSnapshotAvatarUrl = null;
            } else {
              _tmpSnapshotAvatarUrl = _cursor.getString(_cursorIndexOfSnapshotAvatarUrl);
            }
            final String _tmpCreatedAt;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmpCreatedAt = null;
            } else {
              _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            final String _tmpUpdatedAt;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmpUpdatedAt = null;
            } else {
              _tmpUpdatedAt = _cursor.getString(_cursorIndexOfUpdatedAt);
            }
            final boolean _tmpIsDeleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp != 0;
            final String _tmpPendingSyncAction;
            if (_cursor.isNull(_cursorIndexOfPendingSyncAction)) {
              _tmpPendingSyncAction = null;
            } else {
              _tmpPendingSyncAction = _cursor.getString(_cursorIndexOfPendingSyncAction);
            }
            _item = new BowelLogEntity(_tmpId,_tmpUserId,_tmpOccurredAt,_tmpDateKey,_tmpDurationSeconds,_tmpFeeling,_tmpStoolForm,_tmpSymptomTagsRaw,_tmpDetailNote,_tmpSnapshotDisplayName,_tmpSnapshotAvatarUrl,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDeleted,_tmpPendingSyncAction);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getById(final String id, final Continuation<? super BowelLogEntity> $completion) {
    final String _sql = "SELECT * FROM bowel_logs WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, id);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<BowelLogEntity>() {
      @Override
      @Nullable
      public BowelLogEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfOccurredAt = CursorUtil.getColumnIndexOrThrow(_cursor, "occurredAt");
          final int _cursorIndexOfDateKey = CursorUtil.getColumnIndexOrThrow(_cursor, "dateKey");
          final int _cursorIndexOfDurationSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "durationSeconds");
          final int _cursorIndexOfFeeling = CursorUtil.getColumnIndexOrThrow(_cursor, "feeling");
          final int _cursorIndexOfStoolForm = CursorUtil.getColumnIndexOrThrow(_cursor, "stoolForm");
          final int _cursorIndexOfSymptomTagsRaw = CursorUtil.getColumnIndexOrThrow(_cursor, "symptomTagsRaw");
          final int _cursorIndexOfDetailNote = CursorUtil.getColumnIndexOrThrow(_cursor, "detailNote");
          final int _cursorIndexOfSnapshotDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "snapshotDisplayName");
          final int _cursorIndexOfSnapshotAvatarUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "snapshotAvatarUrl");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final int _cursorIndexOfPendingSyncAction = CursorUtil.getColumnIndexOrThrow(_cursor, "pendingSyncAction");
          final BowelLogEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpUserId;
            if (_cursor.isNull(_cursorIndexOfUserId)) {
              _tmpUserId = null;
            } else {
              _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            }
            final String _tmpOccurredAt;
            if (_cursor.isNull(_cursorIndexOfOccurredAt)) {
              _tmpOccurredAt = null;
            } else {
              _tmpOccurredAt = _cursor.getString(_cursorIndexOfOccurredAt);
            }
            final String _tmpDateKey;
            if (_cursor.isNull(_cursorIndexOfDateKey)) {
              _tmpDateKey = null;
            } else {
              _tmpDateKey = _cursor.getString(_cursorIndexOfDateKey);
            }
            final int _tmpDurationSeconds;
            _tmpDurationSeconds = _cursor.getInt(_cursorIndexOfDurationSeconds);
            final String _tmpFeeling;
            if (_cursor.isNull(_cursorIndexOfFeeling)) {
              _tmpFeeling = null;
            } else {
              _tmpFeeling = _cursor.getString(_cursorIndexOfFeeling);
            }
            final int _tmpStoolForm;
            _tmpStoolForm = _cursor.getInt(_cursorIndexOfStoolForm);
            final String _tmpSymptomTagsRaw;
            if (_cursor.isNull(_cursorIndexOfSymptomTagsRaw)) {
              _tmpSymptomTagsRaw = null;
            } else {
              _tmpSymptomTagsRaw = _cursor.getString(_cursorIndexOfSymptomTagsRaw);
            }
            final String _tmpDetailNote;
            if (_cursor.isNull(_cursorIndexOfDetailNote)) {
              _tmpDetailNote = null;
            } else {
              _tmpDetailNote = _cursor.getString(_cursorIndexOfDetailNote);
            }
            final String _tmpSnapshotDisplayName;
            if (_cursor.isNull(_cursorIndexOfSnapshotDisplayName)) {
              _tmpSnapshotDisplayName = null;
            } else {
              _tmpSnapshotDisplayName = _cursor.getString(_cursorIndexOfSnapshotDisplayName);
            }
            final String _tmpSnapshotAvatarUrl;
            if (_cursor.isNull(_cursorIndexOfSnapshotAvatarUrl)) {
              _tmpSnapshotAvatarUrl = null;
            } else {
              _tmpSnapshotAvatarUrl = _cursor.getString(_cursorIndexOfSnapshotAvatarUrl);
            }
            final String _tmpCreatedAt;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmpCreatedAt = null;
            } else {
              _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            final String _tmpUpdatedAt;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmpUpdatedAt = null;
            } else {
              _tmpUpdatedAt = _cursor.getString(_cursorIndexOfUpdatedAt);
            }
            final boolean _tmpIsDeleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp != 0;
            final String _tmpPendingSyncAction;
            if (_cursor.isNull(_cursorIndexOfPendingSyncAction)) {
              _tmpPendingSyncAction = null;
            } else {
              _tmpPendingSyncAction = _cursor.getString(_cursorIndexOfPendingSyncAction);
            }
            _result = new BowelLogEntity(_tmpId,_tmpUserId,_tmpOccurredAt,_tmpDateKey,_tmpDurationSeconds,_tmpFeeling,_tmpStoolForm,_tmpSymptomTagsRaw,_tmpDetailNote,_tmpSnapshotDisplayName,_tmpSnapshotAvatarUrl,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDeleted,_tmpPendingSyncAction);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
