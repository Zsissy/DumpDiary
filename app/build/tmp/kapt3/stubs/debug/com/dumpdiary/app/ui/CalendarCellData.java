package com.dumpdiary.app.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.text.format.DateFormat;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.foundation.layout.ColumnScope;
import androidx.compose.foundation.layout.ExperimentalLayoutApi;
import androidx.compose.foundation.text.KeyboardOptions;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.CardDefaults;
import androidx.compose.material3.ExperimentalMaterial3Api;
import androidx.compose.material3.FilterChipDefaults;
import androidx.compose.material3.OutlinedTextFieldDefaults;
import androidx.compose.material3.SnackbarHostState;
import androidx.compose.material3.TopAppBarDefaults;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.graphics.Brush;
import androidx.compose.ui.graphics.vector.ImageVector;
import androidx.compose.ui.graphics.StrokeCap;
import androidx.compose.ui.graphics.drawscope.Stroke;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.text.style.TextOverflow;
import com.dumpdiary.app.R;
import com.dumpdiary.app.data.model.BowelLogEntity;
import com.dumpdiary.app.data.repository.FriendUi;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u00002\u00020\u0001B+\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u0012\u0006\u0010\t\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\nJ\t\u0010\u0011\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0012\u001a\u00020\u0005H\u00c6\u0003J\u000f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\u00c6\u0003J\t\u0010\u0014\u001a\u00020\u0005H\u00c6\u0003J7\u0010\u0015\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\u000e\b\u0002\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\b\b\u0002\u0010\t\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\u0016\u001a\u00020\u00052\b\u0010\u0017\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0018\u001a\u00020\u0019H\u00d6\u0001J\t\u0010\u001a\u001a\u00020\u001bH\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\t\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u000eR\u0017\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010\u00a8\u0006\u001c"}, d2 = {"Lcom/dumpdiary/app/ui/CalendarCellData;", "", "date", "Ljava/time/LocalDate;", "inCurrentMonth", "", "logs", "", "Lcom/dumpdiary/app/data/model/BowelLogEntity;", "isHighlighted", "(Ljava/time/LocalDate;ZLjava/util/List;Z)V", "getDate", "()Ljava/time/LocalDate;", "getInCurrentMonth", "()Z", "getLogs", "()Ljava/util/List;", "component1", "component2", "component3", "component4", "copy", "equals", "other", "hashCode", "", "toString", "", "app_debug"})
final class CalendarCellData {
    @org.jetbrains.annotations.NotNull()
    private final java.time.LocalDate date = null;
    private final boolean inCurrentMonth = false;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dumpdiary.app.data.model.BowelLogEntity> logs = null;
    private final boolean isHighlighted = false;
    
    public CalendarCellData(@org.jetbrains.annotations.NotNull()
    java.time.LocalDate date, boolean inCurrentMonth, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dumpdiary.app.data.model.BowelLogEntity> logs, boolean isHighlighted) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.LocalDate getDate() {
        return null;
    }
    
    public final boolean getInCurrentMonth() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dumpdiary.app.data.model.BowelLogEntity> getLogs() {
        return null;
    }
    
    public final boolean isHighlighted() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.LocalDate component1() {
        return null;
    }
    
    public final boolean component2() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dumpdiary.app.data.model.BowelLogEntity> component3() {
        return null;
    }
    
    public final boolean component4() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dumpdiary.app.ui.CalendarCellData copy(@org.jetbrains.annotations.NotNull()
    java.time.LocalDate date, boolean inCurrentMonth, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dumpdiary.app.data.model.BowelLogEntity> logs, boolean isHighlighted) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}