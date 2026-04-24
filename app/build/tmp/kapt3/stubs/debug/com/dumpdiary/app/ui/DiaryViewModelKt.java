package com.dumpdiary.app.ui;

import android.net.Uri;
import androidx.lifecycle.ViewModel;
import com.dumpdiary.app.data.model.BowelLogEntity;
import com.dumpdiary.app.data.repository.BowelLogInput;
import com.dumpdiary.app.data.repository.FriendRepository;
import com.dumpdiary.app.data.repository.FriendUi;
import com.dumpdiary.app.data.repository.LogRepository;
import com.dumpdiary.app.data.repository.ProfileRepository;
import com.dumpdiary.app.data.repository.UserProfileUi;
import java.time.Duration;
import dagger.hilt.android.lifecycle.HiltViewModel;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import javax.inject.Inject;
import kotlinx.coroutines.flow.SharingStarted;
import kotlinx.coroutines.flow.StateFlow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\u0018\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\"\n\u0002\b\u0002\u001a\u001e\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u00062\u0006\u0010\u0007\u001a\u00020\u0004H\u0002\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"dateFormatter", "Ljava/time/format/DateTimeFormatter;", "dateTimeFormatter", "deriveFeeling", "", "symptomTags", "", "fallback", "app_debug"})
public final class DiaryViewModelKt {
    @org.jetbrains.annotations.NotNull()
    private static final java.time.format.DateTimeFormatter dateFormatter = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.time.format.DateTimeFormatter dateTimeFormatter = null;
    
    private static final java.lang.String deriveFeeling(java.util.Set<java.lang.String> symptomTags, java.lang.String fallback) {
        return null;
    }
}