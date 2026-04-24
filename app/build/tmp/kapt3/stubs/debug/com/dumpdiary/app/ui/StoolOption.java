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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0015\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u00002\u00020\u0001B-\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\t\u0010\u0014\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0015\u001a\u00020\u0003H\u00c6\u0003J\u0016\u0010\u0016\u001a\u00020\u0006H\u00c6\u0003\u00f8\u0001\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0017\u0010\fJ\u0016\u0010\u0018\u001a\u00020\u0006H\u00c6\u0003\u00f8\u0001\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0019\u0010\fJ\t\u0010\u001a\u001a\u00020\tH\u00c6\u0003JE\u0010\u001b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00062\b\b\u0002\u0010\b\u001a\u00020\tH\u00c6\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001c\u0010\u001dJ\u0013\u0010\u001e\u001a\u00020\u001f2\b\u0010 \u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010!\u001a\u00020\u0003H\u00d6\u0001J\t\u0010\"\u001a\u00020#H\u00d6\u0001R\u0019\u0010\u0005\u001a\u00020\u0006\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\n\n\u0002\u0010\r\u001a\u0004\b\u000b\u0010\fR\u0019\u0010\u0007\u001a\u00020\u0006\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\n\n\u0002\u0010\r\u001a\u0004\b\u000e\u0010\fR\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0012\u0082\u0002\u000b\n\u0005\b\u00a1\u001e0\u0001\n\u0002\b!\u00a8\u0006$"}, d2 = {"Lcom/dumpdiary/app/ui/StoolOption;", "", "value", "", "labelRes", "circleColor", "Landroidx/compose/ui/graphics/Color;", "contentColor", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "(IIJJLandroidx/compose/ui/graphics/vector/ImageVector;Lkotlin/jvm/internal/DefaultConstructorMarker;)V", "getCircleColor-0d7_KjU", "()J", "J", "getContentColor-0d7_KjU", "getIcon", "()Landroidx/compose/ui/graphics/vector/ImageVector;", "getLabelRes", "()I", "getValue", "component1", "component2", "component3", "component3-0d7_KjU", "component4", "component4-0d7_KjU", "component5", "copy", "copy-OoHUuok", "(IIJJLandroidx/compose/ui/graphics/vector/ImageVector;)Lcom/dumpdiary/app/ui/StoolOption;", "equals", "", "other", "hashCode", "toString", "", "app_debug"})
final class StoolOption {
    private final int value = 0;
    private final int labelRes = 0;
    private final long circleColor = 0L;
    private final long contentColor = 0L;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.ui.graphics.vector.ImageVector icon = null;
    
    private StoolOption(int value, int labelRes, long circleColor, long contentColor, androidx.compose.ui.graphics.vector.ImageVector icon) {
        super();
    }
    
    public final int getValue() {
        return 0;
    }
    
    public final int getLabelRes() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getIcon() {
        return null;
    }
    
    public final int component1() {
        return 0;
    }
    
    public final int component2() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector component5() {
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