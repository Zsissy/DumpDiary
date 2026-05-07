package com.dumpdiary.app.data.repository;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.OpenableColumns;
import androidx.work.ExistingWorkPolicy;
import androidx.work.WorkManager;
import com.dumpdiary.app.data.local.LogDao;
import com.dumpdiary.app.data.local.ProfileDao;
import com.dumpdiary.app.data.local.UserPreferences;
import com.dumpdiary.app.data.local.UserPreferencesRepository;
import com.dumpdiary.app.data.model.AuthRequestDto;
import com.dumpdiary.app.data.model.BowelLogEntity;
import com.dumpdiary.app.data.model.AddFriendRequestDto;
import com.dumpdiary.app.data.model.FriendProfileDto;
import com.dumpdiary.app.data.model.MessageDto;
import com.dumpdiary.app.data.model.RefreshRequestDto;
import com.dumpdiary.app.data.model.RegisterRequestDto;
import com.dumpdiary.app.data.model.ResetPasswordRequestDto;
import com.dumpdiary.app.data.model.SendEmailCodeRequestDto;
import com.dumpdiary.app.data.model.UpdateProfileRequestDto;
import com.dumpdiary.app.data.model.UserProfileEntity;
import com.dumpdiary.app.data.model.VerificationPurposeDto;
import com.dumpdiary.app.data.remote.DumpDiaryApi;
import com.dumpdiary.app.data.remote.SupabaseApi;
import com.dumpdiary.app.worker.SyncWorker;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;
import kotlinx.coroutines.flow.Flow;
import kotlinx.datetime.Clock;
import okhttp3.MultipartBody;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00000\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010$\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u001a.\u0010\u0003\u001a\u00020\u0002*\b\u0012\u0004\u0012\u00020\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u00022\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00070\u0006H\u0002\u001a\f\u0010\b\u001a\u00020\u0002*\u00020\u0002H\u0002\u001a\u0018\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00020\u00010\u0001*\u00020\u0002H\u0002\u001a\u0014\u0010\n\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u000b\u001a\u00020\u0002H\u0002\u001a\f\u0010\f\u001a\u00020\u0002*\u00020\u0002H\u0002\u001a\f\u0010\r\u001a\u00020\u000e*\u00020\u000fH\u0002\u001a\u0014\u0010\u0010\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u000b\u001a\u00020\u0002H\u0002\u001a\u0014\u0010\u0011\u001a\u00020\u0012*\u00020\u00132\u0006\u0010\u000b\u001a\u00020\u0002H\u0002\"\u0014\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"csvHeaders", "", "", "csvValue", "name", "headerIndex", "", "", "normalizeImportedSymptomTags", "parseCsvRows", "resolveRemoteUrl", "serverBaseUrl", "toCsvField", "toFallbackProfile", "Lcom/dumpdiary/app/data/model/UserProfileEntity;", "Lcom/dumpdiary/app/data/local/UserPreferences;", "toResolvedDownloadUrl", "toUi", "Lcom/dumpdiary/app/data/repository/FriendUi;", "Lcom/dumpdiary/app/data/model/FriendProfileDto;", "app_debug"})
public final class AppRepositoriesKt {
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<java.lang.String> csvHeaders = null;
    
    private static final com.dumpdiary.app.data.model.UserProfileEntity toFallbackProfile(com.dumpdiary.app.data.local.UserPreferences $this$toFallbackProfile) {
        return null;
    }
    
    private static final com.dumpdiary.app.data.repository.FriendUi toUi(com.dumpdiary.app.data.model.FriendProfileDto $this$toUi, java.lang.String serverBaseUrl) {
        return null;
    }
    
    private static final java.lang.String resolveRemoteUrl(java.lang.String $this$resolveRemoteUrl, java.lang.String serverBaseUrl) {
        return null;
    }
    
    private static final java.lang.String toResolvedDownloadUrl(java.lang.String $this$toResolvedDownloadUrl, java.lang.String serverBaseUrl) {
        return null;
    }
    
    private static final java.lang.String toCsvField(java.lang.String $this$toCsvField) {
        return null;
    }
    
    private static final java.lang.String csvValue(java.util.List<java.lang.String> $this$csvValue, java.lang.String name, java.util.Map<java.lang.String, java.lang.Integer> headerIndex) {
        return null;
    }
    
    private static final java.lang.String normalizeImportedSymptomTags(java.lang.String $this$normalizeImportedSymptomTags) {
        return null;
    }
    
    private static final java.util.List<java.util.List<java.lang.String>> parseCsvRows(java.lang.String $this$parseCsvRows) {
        return null;
    }
}