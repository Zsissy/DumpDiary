package com.dumpdiary.app.ui;

import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.format.DateFormat;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.util.Base64;
import android.widget.Toast;
import androidx.core.content.FileProvider;
import androidx.activity.result.contract.ActivityResultContracts;
import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
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

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\u0098\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u0007\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0017\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b%\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a.\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u000e2\u001c\u0010\u0014\u001a\u0018\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\u00120\u0015\u00a2\u0006\u0002\b\u0017\u00a2\u0006\u0002\b\u0018H\u0003\u001a0\u0010\u0019\u001a\u00020\u00122\u0006\u0010\u001a\u001a\u00020\u001b2\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00120\u001d2\u0006\u0010\u001e\u001a\u00020\u001f2\b\b\u0002\u0010 \u001a\u00020!H\u0003\u001a,\u0010\"\u001a\u00020\u00122\u0006\u0010#\u001a\u00020$2\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00120\u001d2\f\u0010&\u001a\b\u0012\u0004\u0012\u00020\u00120\u001dH\u0003\u001aO\u0010\'\u001a\u00020\u00122\u0006\u0010(\u001a\u00020\u000e2\u0006\u0010)\u001a\u00020\u000e2\u0006\u0010*\u001a\u00020\u00012\u0006\u0010+\u001a\u00020\u00012\b\b\u0002\u0010 \u001a\u00020!2\u0011\u0010,\u001a\r\u0012\u0004\u0012\u00020\u00120\u001d\u00a2\u0006\u0002\b\u0017H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b-\u0010.\u001a:\u0010/\u001a\u00020\u00122\u0006\u0010#\u001a\u00020$2\f\u00100\u001a\b\u0012\u0004\u0012\u0002010\b2\u0006\u00102\u001a\u00020\u000e2\u0012\u00103\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00120\u0015H\u0003\u001a\u001e\u00104\u001a\u00020\u00122\u0006\u0010,\u001a\u0002052\f\u00106\u001a\b\u0012\u0004\u0012\u00020\u00120\u001dH\u0003\u001a&\u00107\u001a\u00020\u00122\u0006\u0010)\u001a\u00020\u000e2\u0006\u00108\u001a\u00020\u001f2\f\u00106\u001a\b\u0012\u0004\u0012\u00020\u00120\u001dH\u0003\u001a<\u00109\u001a\u00020\u00122\u0006\u00102\u001a\u00020\u000e2\u0006\u0010:\u001a\u00020\u000e2\f\u0010;\u001a\b\u0012\u0004\u0012\u00020<0\b2\u0014\u0010=\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u000e\u0012\u0004\u0012\u00020\u00120\u0015H\u0003\u001a\u0082\u0001\u0010>\u001a\u00020\u00122\b\b\u0002\u0010 \u001a\u00020!2\u0006\u0010?\u001a\u00020@2\u0006\u00102\u001a\u00020\u000e2\u0012\u0010A\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00120\u00152\u0012\u00103\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00120\u00152\f\u0010B\u001a\b\u0012\u0004\u0012\u00020\u00120\u001d2\u0012\u0010C\u001a\u000e\u0012\u0004\u0012\u00020D\u0012\u0004\u0012\u00020\u00120\u00152\u0014\u0010E\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u000e\u0012\u0004\u0012\u00020\u00120\u0015H\u0003\u001a\u0010\u0010F\u001a\u00020\u00122\u0006\u0010G\u001a\u00020\u000fH\u0003\u001a8\u0010H\u001a\u00020\u00122\u0006\u0010?\u001a\u00020I2\b\b\u0002\u0010J\u001a\u00020K2\b\b\u0002\u0010L\u001a\u00020M2\b\b\u0002\u0010N\u001a\u00020O2\b\b\u0002\u0010P\u001a\u00020QH\u0007\u001a>\u0010R\u001a\u00020\u00122\u0006\u0010S\u001a\u00020\u000e2\u0006\u0010T\u001a\u00020\u001f2\f\u0010U\u001a\b\u0012\u0004\u0012\u00020\u00120\u001d2\f\u0010V\u001a\b\u0012\u0004\u0012\u00020\u00120\u001d2\b\b\u0002\u0010 \u001a\u00020!H\u0003\u001a\u0098\u0001\u0010W\u001a\u00020\u00122\u0006\u0010?\u001a\u00020@2\u0012\u0010X\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00120\u00152\f\u0010Y\u001a\b\u0012\u0004\u0012\u00020\u00120\u001d2\f\u0010Z\u001a\b\u0012\u0004\u0012\u00020\u00120\u001d2\u0012\u0010[\u001a\u000e\u0012\u0004\u0012\u00020\\\u0012\u0004\u0012\u00020\u00120\u00152\u0012\u0010]\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00120\u00152\u0012\u0010^\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00120\u00152\f\u0010_\u001a\b\u0012\u0004\u0012\u00020\u00120\u001d2\f\u0010`\u001a\b\u0012\u0004\u0012\u00020\u00120\u001dH\u0003\u001a\u001a\u0010a\u001a\u00020\u00122\u0006\u0010b\u001a\u00020\u000e2\b\b\u0002\u0010 \u001a\u00020!H\u0003\u001aR\u0010c\u001a\u00020\u00122\u0006\u0010d\u001a\u00020e2\u0012\u0010f\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00120\u00152\u001e\u0010g\u001a\u001a\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00120h2\f\u0010`\u001a\b\u0012\u0004\u0012\u00020\u00120\u001dH\u0003\u001a*\u0010i\u001a\u00020\u00122\f\u0010;\u001a\b\u0012\u0004\u0012\u00020<0\b2\u0012\u0010A\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00120\u0015H\u0003\u001a(\u0010j\u001a\u00020\u00122\u0006\u0010k\u001a\u00020\u000e2\f\u00106\u001a\b\u0012\u0004\u0012\u00020\u00120\u001d2\b\b\u0002\u0010l\u001a\u00020\u001fH\u0003\u001a$\u0010m\u001a\u00020\u00122\u0006\u0010n\u001a\u00020\u000f2\u0012\u0010=\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00120\u0015H\u0003\u001a\u0094\u0002\u0010o\u001a\u00020\u00122\u0006\u0010?\u001a\u00020I2\u0006\u0010p\u001a\u00020@2\f\u0010q\u001a\b\u0012\u0004\u0012\u00020\u00120\u001d2\u0012\u0010A\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00120\u00152\u0012\u00103\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00120\u00152\f\u0010B\u001a\b\u0012\u0004\u0012\u00020\u00120\u001d2\u0012\u0010C\u001a\u000e\u0012\u0004\u0012\u00020D\u0012\u0004\u0012\u00020\u00120\u00152\u0014\u0010E\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u000e\u0012\u0004\u0012\u00020\u00120\u00152\u0012\u0010X\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00120\u00152\f\u0010Y\u001a\b\u0012\u0004\u0012\u00020\u00120\u001d2\f\u0010Z\u001a\b\u0012\u0004\u0012\u00020\u00120\u001d2\u0012\u0010[\u001a\u000e\u0012\u0004\u0012\u00020\\\u0012\u0004\u0012\u00020\u00120\u00152\u0012\u0010]\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00120\u00152\u0012\u0010^\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00120\u00152\f\u0010r\u001a\b\u0012\u0004\u0012\u00020\u00120\u001d2\u0012\u0010s\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00120\u0015H\u0003\u001a<\u0010t\u001a\u00020\u00122\b\u0010u\u001a\u0004\u0018\u00010v2\u0006\u0010w\u001a\u00020\u000e2\u0012\u0010s\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00120\u00152\f\u0010r\u001a\b\u0012\u0004\u0012\u00020\u00120\u001dH\u0003\u001a\u0018\u0010x\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u000e2\u0006\u0010k\u001a\u00020\u000eH\u0003\u001a$\u0010y\u001a\u00020\u00122\u0006\u0010w\u001a\u00020\u000e2\u0012\u0010s\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00120\u0015H\u0003\u001a&\u0010z\u001a\u00020\u00122\u0006\u0010k\u001a\u00020\u000e2\u0006\u00108\u001a\u00020\u001f2\f\u00106\u001a\b\u0012\u0004\u0012\u00020\u00120\u001dH\u0003\u001a,\u0010{\u001a\u00020\u00122\u0006\u0010|\u001a\u0002012\f\u00103\u001a\b\u0012\u0004\u0012\u00020\u00120\u001d2\f\u0010}\u001a\b\u0012\u0004\u0012\u00020\u00120\u001dH\u0003\u001a\u00ba\u0001\u0010~\u001a\u00020\u00122\u0006\u0010d\u001a\u00020e2\u0019\u0010\u007f\u001a\u0015\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00120\u0080\u00012\u001a\u0010\u0081\u0001\u001a\u0015\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00120\u0080\u00012\u0013\u0010\u0082\u0001\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00120\u00152\u001a\u0010\u0083\u0001\u001a\u0015\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00120\u0080\u00012\u0013\u0010\u0084\u0001\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00120\u00152\r\u0010\u0085\u0001\u001a\b\u0012\u0004\u0012\u00020\u00120\u001d2\r\u0010\u0086\u0001\u001a\b\u0012\u0004\u0012\u00020\u00120\u001d2\r\u0010\u0087\u0001\u001a\b\u0012\u0004\u0012\u00020\u00120\u001dH\u0003\u001a\u0012\u0010\u0088\u0001\u001a\u00020\u00122\u0007\u0010\u0089\u0001\u001a\u00020\u000eH\u0003\u001aW\u0010\u008a\u0001\u001a\u00020\u00122\u0006\u0010)\u001a\u00020\u000e2\u0007\u0010\u008b\u0001\u001a\u00020\u000e2\u0007\u0010\u008c\u0001\u001a\u00020\u000e2\f\u00106\u001a\b\u0012\u0004\u0012\u00020\u00120\u001d2\u0007\u0010\u008d\u0001\u001a\u00020\u000e2\b\b\u0002\u0010 \u001a\u00020!2\u0011\u0010,\u001a\r\u0012\u0004\u0012\u00020\u00120\u001d\u00a2\u0006\u0002\b\u0017H\u0003\u001a\'\u0010\u008e\u0001\u001a\u00020\u00122\u0007\u0010\u008f\u0001\u001a\u00020\u000f2\u0007\u0010\u0090\u0001\u001a\u00020\u0001H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0006\b\u0091\u0001\u0010\u0092\u0001\u001a&\u0010\u0093\u0001\u001a\u00020\u00122\t\u0010\u0094\u0001\u001a\u0004\u0018\u00010\u000e2\u0006\u0010b\u001a\u00020\u000e2\b\b\u0002\u0010 \u001a\u00020!H\u0003\u001a\u00b2\u0001\u0010\u0095\u0001\u001a\u00020\u00122\u0006\u0010?\u001a\u00020@2\u0012\u0010X\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00120\u00152\f\u0010Y\u001a\b\u0012\u0004\u0012\u00020\u00120\u001d2\f\u0010Z\u001a\b\u0012\u0004\u0012\u00020\u00120\u001d2\u0012\u0010[\u001a\u000e\u0012\u0004\u0012\u00020\\\u0012\u0004\u0012\u00020\u00120\u00152\u0012\u0010]\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00120\u00152\u0012\u0010^\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00120\u00152\f\u0010_\u001a\b\u0012\u0004\u0012\u00020\u00120\u001d2\b\b\u0002\u0010 \u001a\u00020!2\u0006\u0010\u0013\u001a\u00020\u000e2\u0007\u0010\u0096\u0001\u001a\u00020\u000e2\n\b\u0002\u0010\u0097\u0001\u001a\u00030\u0098\u0001H\u0003\u001a|\u0010\u0099\u0001\u001a\u00020\u00122\u0006\u0010d\u001a\u00020e2\u0012\u0010f\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00120\u00152\u001f\u0010\u009a\u0001\u001a\u001a\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00120h2&\u0010\u009b\u0001\u001a!\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00120\u009c\u00012\f\u0010`\u001a\b\u0012\u0004\u0012\u00020\u00120\u001dH\u0003\u001a\u00e9\u0001\u0010\u009d\u0001\u001a\u00020\u00122\u0006\u0010?\u001a\u00020I2\b\u0010\u009e\u0001\u001a\u00030\u009f\u00012\u0013\u0010\u00a0\u0001\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00120\u00152\u0014\u0010\u00a1\u0001\u001a\u000f\u0012\u0005\u0012\u00030\u00a2\u0001\u0012\u0004\u0012\u00020\u00120\u00152\u0014\u0010\u00a3\u0001\u001a\u000f\u0012\u0005\u0012\u00030\u00a2\u0001\u0012\u0004\u0012\u00020\u00120\u00152\u0014\u0010\u00a4\u0001\u001a\u000f\u0012\u0005\u0012\u00030\u00a2\u0001\u0012\u0004\u0012\u00020\u00120\u00152\u0013\u0010\u00a5\u0001\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00120\u00152\u001a\u0010\u00a6\u0001\u001a\u0015\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00120\u0080\u00012\u0013\u0010\u0084\u0001\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00120\u00152\u0012\u0010s\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00120\u00152\r\u0010\u00a7\u0001\u001a\b\u0012\u0004\u0012\u00020\u00120\u001d2\f\u0010`\u001a\b\u0012\u0004\u0012\u00020\u00120\u001dH\u0003\u001a\u0019\u0010\u00a8\u0001\u001a\u00020\u00122\u000e\u0010\u00a9\u0001\u001a\t\u0012\u0005\u0012\u00030\u00aa\u00010\bH\u0003\u001a[\u0010\u00ab\u0001\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u000e2\u0006\u0010(\u001a\u00020\u000e2\u0007\u0010\u00ac\u0001\u001a\u00020\u000e2\u0006\u0010*\u001a\u00020\u00012\u0006\u0010+\u001a\u00020\u00012\b\b\u0002\u0010 \u001a\u00020!2\u0011\u0010,\u001a\r\u0012\u0004\u0012\u00020\u00120\u001d\u00a2\u0006\u0002\b\u0017H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0006\b\u00ad\u0001\u0010\u00ae\u0001\u001a(\u0010\u00af\u0001\u001a\u00020\u00122\b\u0010\u00b0\u0001\u001a\u00030\u00b1\u00012\u0013\u0010=\u001a\u000f\u0012\u0005\u0012\u00030\u00b1\u0001\u0012\u0004\u0012\u00020\u00120\u0015H\u0003\u001a*\u0010\u00b2\u0001\u001a\u00020\u00122\b\b\u0002\u0010 \u001a\u00020!2\f\u00100\u001a\b\u0012\u0004\u0012\u0002010\b2\u0007\u0010\u00b3\u0001\u001a\u00020$H\u0003\u001a3\u0010\u00b4\u0001\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u000e2\u0007\u0010\u0096\u0001\u001a\u00020\u000e2\u0007\u0010\u00b5\u0001\u001a\u00020\u000f2\u000e\u0010\u00a9\u0001\u001a\t\u0012\u0005\u0012\u00030\u00aa\u00010\bH\u0003\u001a=\u0010\u00b6\u0001\u001a\u00020\u00122\u0007\u0010\u00b7\u0001\u001a\u00020\u000b2\u0006\u00108\u001a\u00020\u001f2\f\u0010=\u001a\b\u0012\u0004\u0012\u00020\u00120\u001d2\b\b\u0002\u0010 \u001a\u00020!2\t\b\u0002\u0010\u00b8\u0001\u001a\u00020\u001fH\u0003\u001a\u0012\u0010\u00b9\u0001\u001a\u00020\u00122\u0007\u0010\u00ba\u0001\u001a\u00020\u000fH\u0003\u001a-\u0010\u00bb\u0001\u001a\u00020\u00122\u0006\u0010|\u001a\u0002012\f\u00103\u001a\b\u0012\u0004\u0012\u00020\u00120\u001d2\f\u0010`\u001a\b\u0012\u0004\u0012\u00020\u00120\u001dH\u0003\u001a%\u0010\u00bc\u0001\u001a\b\u0012\u0004\u0012\u00020\u001b0\b2\u0006\u0010#\u001a\u00020$2\f\u00100\u001a\b\u0012\u0004\u0012\u0002010\bH\u0002\u001a!\u0010\u00bd\u0001\u001a\u00020\u000e2\r\u0010\u00be\u0001\u001a\b\u0012\u0004\u0012\u0002010\b2\u0007\u0010\u00bf\u0001\u001a\u00020\u000fH\u0003\u001a*\u0010\u00c0\u0001\u001a\u00020\u000e2\f\u00100\u001a\b\u0012\u0004\u0012\u0002010\b2\b\u0010\u00c1\u0001\u001a\u00030\u00b1\u00012\u0007\u0010\u0089\u0001\u001a\u00020\u000eH\u0003\u001a&\u0010\u00c2\u0001\u001a\u00020\u000e2\u0007\u0010\u00c3\u0001\u001a\u00020\u000e2\t\u0010\u00c4\u0001\u001a\u0004\u0018\u00010\u000e2\u0007\u0010\u00c5\u0001\u001a\u00020DH\u0002\u001a\u0017\u0010\u00c6\u0001\u001a\u00020\u000e2\f\u00100\u001a\b\u0012\u0004\u0012\u0002010\bH\u0002\u001a#\u0010\u00c7\u0001\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u000f0\r2\f\u00100\u001a\b\u0012\u0004\u0012\u0002010\bH\u0002\u001a\u0019\u0010\u00c8\u0001\u001a\u00020\u000f2\u000e\u0010\u00a9\u0001\u001a\t\u0012\u0005\u0012\u00030\u00aa\u00010\bH\u0002\u001a&\u0010\u00c9\u0001\u001a\t\u0012\u0005\u0012\u00030\u00aa\u00010\b2\f\u00100\u001a\b\u0012\u0004\u0012\u0002010\b2\u0006\u0010#\u001a\u00020$H\u0002\u001a\'\u0010\u00ca\u0001\u001a\t\u0012\u0005\u0012\u00030\u00aa\u00010\b2\f\u00100\u001a\b\u0012\u0004\u0012\u0002010\b2\u0007\u0010\u00cb\u0001\u001a\u00020\u000fH\u0002\u001a\u0011\u0010\u00cc\u0001\u001a\u00020\u000e2\u0006\u0010G\u001a\u00020\u000fH\u0003\u001a\u0011\u0010\u00cd\u0001\u001a\u00020\u000e2\u0006\u0010#\u001a\u00020$H\u0002\u001a\u0012\u0010\u00ce\u0001\u001a\u00020\u000e2\u0007\u0010\u00cf\u0001\u001a\u00020\u000eH\u0002\u001a\u0012\u0010\u00d0\u0001\u001a\u00020\u000e2\u0007\u0010\u00d1\u0001\u001a\u00020\u000fH\u0002\u001a\u0012\u0010\u00d2\u0001\u001a\u00020\u000e2\u0007\u0010\u00cf\u0001\u001a\u00020\u000eH\u0002\u001a\u001b\u0010\u00d3\u0001\u001a\u00020\u000e2\u0007\u0010\u00d4\u0001\u001a\u00020\u000e2\u0007\u0010\u00d5\u0001\u001a\u00020\u000eH\u0002\u001a\u0014\u0010\u00d6\u0001\u001a\u0005\u0018\u00010\u00d7\u00012\u0006\u0010(\u001a\u00020\u000eH\u0002\u001a\u0017\u0010\u00d8\u0001\u001a\u0002052\f\u00100\u001a\b\u0012\u0004\u0012\u0002010\bH\u0002\u001a\u0014\u0010\u00d9\u0001\u001a\u0004\u0018\u00010\u000b2\u0007\u0010\u00da\u0001\u001a\u00020\u000fH\u0002\"\u0010\u0010\u0000\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0002\"\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0006\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\"\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\"\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\" \u0010\f\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000f0\r0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0010\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\u00db\u0001"}, d2 = {"calendarLoggedDayBackground", "Landroidx/compose/ui/graphics/Color;", "J", "displayDateFormatter", "Ljava/time/format/DateTimeFormatter;", "displayTimeFormatter", "editorDateTimeFormatter", "homeTabs", "", "Lcom/dumpdiary/app/ui/TabItem;", "stoolOptions", "Lcom/dumpdiary/app/ui/StoolOption;", "symptomItems", "Lkotlin/Pair;", "", "", "timeValueFormatter", "AuthScreenContainer", "", "title", "content", "Lkotlin/Function1;", "Landroidx/compose/foundation/layout/ColumnScope;", "Landroidx/compose/runtime/Composable;", "Lkotlin/ExtensionFunctionType;", "CalendarDayCell", "cell", "Lcom/dumpdiary/app/ui/CalendarCellData;", "onOpenLatest", "Lkotlin/Function0;", "canOpen", "", "modifier", "Landroidx/compose/ui/Modifier;", "CalendarHeader", "month", "Ljava/time/YearMonth;", "onPrevious", "onNext", "CalendarMetricCard", "value", "label", "containerColor", "contentColor", "icon", "CalendarMetricCard-9z6LAg8", "(Ljava/lang/String;Ljava/lang/String;JJLandroidx/compose/ui/Modifier;Lkotlin/jvm/functions/Function0;)V", "CalendarMonthCard", "logs", "Lcom/dumpdiary/app/data/model/BowelLogEntity;", "currentUserId", "onEdit", "CalendarNavButton", "Landroidx/compose/ui/graphics/vector/ImageVector;", "onClick", "CalendarOwnerChip", "selected", "CalendarOwnerToggle", "selectedUserId", "friends", "Lcom/dumpdiary/app/data/repository/FriendUi;", "onSelect", "CalendarScreen", "state", "Lcom/dumpdiary/app/ui/DiaryUiState;", "onAddFriend", "onRefresh", "onMoveMonth", "", "onSelectCalendarUser", "CalendarSummaryHero", "score", "DumpDiaryApp", "Lcom/dumpdiary/app/ui/MainUiState;", "mainViewModel", "Lcom/dumpdiary/app/ui/MainViewModel;", "authViewModel", "Lcom/dumpdiary/app/ui/AuthViewModel;", "diaryViewModel", "Lcom/dumpdiary/app/ui/DiaryViewModel;", "settingsViewModel", "Lcom/dumpdiary/app/ui/SettingsViewModel;", "DurationTrackerCard", "durationText", "isRunning", "onStart", "onStop", "EditorScaffold", "onOccurredAtChange", "onStartDurationTimer", "onStopDurationTimer", "onStoolFormChange", "", "onToggleSymptom", "onDetailsChange", "onSave", "onBack", "FallbackAvatar", "displayName", "ForgotPasswordScreen", "uiState", "Lcom/dumpdiary/app/ui/AuthUiState;", "onSendCode", "onReset", "Lkotlin/Function3;", "FriendSharePanel", "GradientActionButton", "text", "compact", "HomeBottomBar", "selectedTab", "HomeScreen", "diaryState", "onSaveLog", "onOpenSettings", "onLanguageChange", "HomeTopBar", "profile", "Lcom/dumpdiary/app/data/repository/UserProfileUi;", "languageTag", "InsightCard", "LanguageToggle", "LanguageToggleItem", "LogCard", "log", "onDelete", "LoginScreen", "onLogin", "Lkotlin/Function2;", "onSupabaseLogin", "onValidateServer", "onValidateSupabaseServer", "onSetServerType", "onNavigateRegister", "onNavigateForgot", "onDismissUpdate", "PeakInsightCard", "peakTime", "PickerInputCard", "displayValue", "selectionValue", "buttonText", "PoopCountIndicator", "count", "tint", "PoopCountIndicator-4WTKRHQ", "(IJ)V", "ProfileAvatar", "avatarUrl", "RecordEntryContent", "subtitle", "contentPadding", "Landroidx/compose/foundation/layout/PaddingValues;", "RegisterScreen", "onRegister", "onSupabaseRegister", "Lkotlin/Function4;", "SettingsScreen", "settingsState", "Lcom/dumpdiary/app/ui/SettingsUiState;", "onUpdateDisplayName", "onUploadAvatar", "Landroid/net/Uri;", "onExportLogs", "onImportLogs", "onValidateAndSwitchServer", "onValidateAndSwitchSupabaseServer", "onLogout", "StatsLineChart", "points", "Lcom/dumpdiary/app/ui/TrendPoint;", "StatsMetricBento", "suffix", "StatsMetricBento-ZkgLGzA", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JJLandroidx/compose/ui/Modifier;Lkotlin/jvm/functions/Function0;)V", "StatsRangeToggle", "selectedRange", "Lcom/dumpdiary/app/ui/StatsRange;", "StatsScreen", "selectedMonth", "StatsTrendCard", "trendDelta", "StoolOptionCard", "option", "fullWidth", "TrendDeltaChip", "delta", "ViewRecordScreen", "buildCalendarCells", "buildCalendarInsight", "monthLogs", "currentStreak", "buildStatsInsight", "range", "buildTrackedDurationLabel", "durationValue", "timerStartedAt", "nowMillis", "calculatePeakTimeLabel", "calculateStreak", "calculateTrendDelta", "calculateWeeklyTrend", "calculateYearlyTrend", "year", "consistencyTitle", "formatCalendarMonthTitle", "formatDateDisplay", "occurredAt", "formatDuration", "totalSeconds", "formatTimeDisplay", "mergeDateAndTime", "date", "time", "parseDateTimeOrNull", "Ljava/time/LocalDateTime;", "stoolIconForLogs", "stoolOptionForForm", "form", "app_debug"})
public final class DumpDiaryAppKt {
    @org.jetbrains.annotations.NotNull()
    private static final java.time.format.DateTimeFormatter editorDateTimeFormatter = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.time.format.DateTimeFormatter displayDateFormatter = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.time.format.DateTimeFormatter displayTimeFormatter = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.time.format.DateTimeFormatter timeValueFormatter = null;
    private static final long calendarLoggedDayBackground = 0L;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<com.dumpdiary.app.ui.StoolOption> stoolOptions = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<kotlin.Pair<java.lang.String, java.lang.Integer>> symptomItems = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<com.dumpdiary.app.ui.TabItem> homeTabs = null;
    
    @androidx.compose.runtime.Composable()
    public static final void DumpDiaryApp(@org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.ui.MainUiState state, @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.ui.MainViewModel mainViewModel, @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.ui.AuthViewModel authViewModel, @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.ui.DiaryViewModel diaryViewModel, @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.ui.SettingsViewModel settingsViewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void LoginScreen(com.dumpdiary.app.ui.AuthUiState uiState, kotlin.jvm.functions.Function2<? super java.lang.String, ? super java.lang.String, kotlin.Unit> onLogin, kotlin.jvm.functions.Function2<? super java.lang.String, ? super java.lang.String, kotlin.Unit> onSupabaseLogin, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onValidateServer, kotlin.jvm.functions.Function2<? super java.lang.String, ? super java.lang.String, kotlin.Unit> onValidateSupabaseServer, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onSetServerType, kotlin.jvm.functions.Function0<kotlin.Unit> onNavigateRegister, kotlin.jvm.functions.Function0<kotlin.Unit> onNavigateForgot, kotlin.jvm.functions.Function0<kotlin.Unit> onDismissUpdate) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void RegisterScreen(com.dumpdiary.app.ui.AuthUiState uiState, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onSendCode, kotlin.jvm.functions.Function3<? super java.lang.String, ? super java.lang.String, ? super java.lang.String, kotlin.Unit> onRegister, kotlin.jvm.functions.Function4<? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, kotlin.Unit> onSupabaseRegister, kotlin.jvm.functions.Function0<kotlin.Unit> onBack) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ForgotPasswordScreen(com.dumpdiary.app.ui.AuthUiState uiState, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onSendCode, kotlin.jvm.functions.Function3<? super java.lang.String, ? super java.lang.String, ? super java.lang.String, kotlin.Unit> onReset, kotlin.jvm.functions.Function0<kotlin.Unit> onBack) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void AuthScreenContainer(java.lang.String title, kotlin.jvm.functions.Function1<? super androidx.compose.foundation.layout.ColumnScope, kotlin.Unit> content) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void HomeScreen(com.dumpdiary.app.ui.MainUiState state, com.dumpdiary.app.ui.DiaryUiState diaryState, kotlin.jvm.functions.Function0<kotlin.Unit> onSaveLog, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onAddFriend, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onEdit, kotlin.jvm.functions.Function0<kotlin.Unit> onRefresh, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onMoveMonth, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onSelectCalendarUser, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onOccurredAtChange, kotlin.jvm.functions.Function0<kotlin.Unit> onStartDurationTimer, kotlin.jvm.functions.Function0<kotlin.Unit> onStopDurationTimer, kotlin.jvm.functions.Function1<? super java.lang.Float, kotlin.Unit> onStoolFormChange, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onToggleSymptom, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onDetailsChange, kotlin.jvm.functions.Function0<kotlin.Unit> onOpenSettings, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onLanguageChange) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void HomeTopBar(com.dumpdiary.app.data.repository.UserProfileUi profile, java.lang.String languageTag, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onLanguageChange, kotlin.jvm.functions.Function0<kotlin.Unit> onOpenSettings) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void LanguageToggle(java.lang.String languageTag, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onLanguageChange) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void LanguageToggleItem(java.lang.String text, boolean selected, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void HomeBottomBar(int selectedTab, kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onSelect) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class, androidx.compose.foundation.layout.ExperimentalLayoutApi.class})
    @androidx.compose.runtime.Composable()
    private static final void ViewRecordScreen(com.dumpdiary.app.data.model.BowelLogEntity log, kotlin.jvm.functions.Function0<kotlin.Unit> onEdit, kotlin.jvm.functions.Function0<kotlin.Unit> onBack) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void EditorScaffold(com.dumpdiary.app.ui.DiaryUiState state, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onOccurredAtChange, kotlin.jvm.functions.Function0<kotlin.Unit> onStartDurationTimer, kotlin.jvm.functions.Function0<kotlin.Unit> onStopDurationTimer, kotlin.jvm.functions.Function1<? super java.lang.Float, kotlin.Unit> onStoolFormChange, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onToggleSymptom, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onDetailsChange, kotlin.jvm.functions.Function0<kotlin.Unit> onSave, kotlin.jvm.functions.Function0<kotlin.Unit> onBack) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.foundation.layout.ExperimentalLayoutApi.class})
    @androidx.compose.runtime.Composable()
    private static final void RecordEntryContent(com.dumpdiary.app.ui.DiaryUiState state, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onOccurredAtChange, kotlin.jvm.functions.Function0<kotlin.Unit> onStartDurationTimer, kotlin.jvm.functions.Function0<kotlin.Unit> onStopDurationTimer, kotlin.jvm.functions.Function1<? super java.lang.Float, kotlin.Unit> onStoolFormChange, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onToggleSymptom, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onDetailsChange, kotlin.jvm.functions.Function0<kotlin.Unit> onSave, androidx.compose.ui.Modifier modifier, java.lang.String title, java.lang.String subtitle, androidx.compose.foundation.layout.PaddingValues contentPadding) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void PickerInputCard(java.lang.String label, java.lang.String displayValue, java.lang.String selectionValue, kotlin.jvm.functions.Function0<kotlin.Unit> onClick, java.lang.String buttonText, androidx.compose.ui.Modifier modifier, kotlin.jvm.functions.Function0<kotlin.Unit> icon) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void DurationTrackerCard(java.lang.String durationText, boolean isRunning, kotlin.jvm.functions.Function0<kotlin.Unit> onStart, kotlin.jvm.functions.Function0<kotlin.Unit> onStop, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void StoolOptionCard(com.dumpdiary.app.ui.StoolOption option, boolean selected, kotlin.jvm.functions.Function0<kotlin.Unit> onSelect, androidx.compose.ui.Modifier modifier, boolean fullWidth) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void GradientActionButton(java.lang.String text, kotlin.jvm.functions.Function0<kotlin.Unit> onClick, boolean compact) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CalendarScreen(androidx.compose.ui.Modifier modifier, com.dumpdiary.app.ui.DiaryUiState state, java.lang.String currentUserId, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onAddFriend, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onEdit, kotlin.jvm.functions.Function0<kotlin.Unit> onRefresh, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onMoveMonth, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onSelectCalendarUser) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CalendarOwnerToggle(java.lang.String currentUserId, java.lang.String selectedUserId, java.util.List<com.dumpdiary.app.data.repository.FriendUi> friends, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onSelect) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CalendarOwnerChip(java.lang.String label, boolean selected, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.foundation.layout.ExperimentalLayoutApi.class})
    @androidx.compose.runtime.Composable()
    private static final void CalendarHeader(java.time.YearMonth month, kotlin.jvm.functions.Function0<kotlin.Unit> onPrevious, kotlin.jvm.functions.Function0<kotlin.Unit> onNext) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CalendarNavButton(androidx.compose.ui.graphics.vector.ImageVector icon, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CalendarSummaryHero(int score) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void InsightCard(java.lang.String title, java.lang.String text) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.foundation.layout.ExperimentalLayoutApi.class})
    @androidx.compose.runtime.Composable()
    private static final void LogCard(com.dumpdiary.app.data.model.BowelLogEntity log, kotlin.jvm.functions.Function0<kotlin.Unit> onEdit, kotlin.jvm.functions.Function0<kotlin.Unit> onDelete) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void StatsScreen(androidx.compose.ui.Modifier modifier, java.util.List<com.dumpdiary.app.data.model.BowelLogEntity> logs, java.time.YearMonth selectedMonth) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void StatsRangeToggle(com.dumpdiary.app.ui.StatsRange selectedRange, kotlin.jvm.functions.Function1<? super com.dumpdiary.app.ui.StatsRange, kotlin.Unit> onSelect) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void StatsTrendCard(java.lang.String title, java.lang.String subtitle, int trendDelta, java.util.List<com.dumpdiary.app.ui.TrendPoint> points) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void TrendDeltaChip(int delta) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void PeakInsightCard(java.lang.String peakTime) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void SettingsScreen(com.dumpdiary.app.ui.MainUiState state, com.dumpdiary.app.ui.SettingsUiState settingsState, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onUpdateDisplayName, kotlin.jvm.functions.Function1<? super android.net.Uri, kotlin.Unit> onUploadAvatar, kotlin.jvm.functions.Function1<? super android.net.Uri, kotlin.Unit> onExportLogs, kotlin.jvm.functions.Function1<? super android.net.Uri, kotlin.Unit> onImportLogs, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onValidateAndSwitchServer, kotlin.jvm.functions.Function2<? super java.lang.String, ? super java.lang.String, kotlin.Unit> onValidateAndSwitchSupabaseServer, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onSetServerType, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onLanguageChange, kotlin.jvm.functions.Function0<kotlin.Unit> onLogout, kotlin.jvm.functions.Function0<kotlin.Unit> onBack) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ProfileAvatar(java.lang.String avatarUrl, java.lang.String displayName, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void FallbackAvatar(java.lang.String displayName, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CalendarMonthCard(java.time.YearMonth month, java.util.List<com.dumpdiary.app.data.model.BowelLogEntity> logs, java.lang.String currentUserId, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onEdit) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CalendarDayCell(com.dumpdiary.app.ui.CalendarCellData cell, kotlin.jvm.functions.Function0<kotlin.Unit> onOpenLatest, boolean canOpen, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void FriendSharePanel(java.util.List<com.dumpdiary.app.data.repository.FriendUi> friends, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onAddFriend) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void StatsLineChart(java.util.List<com.dumpdiary.app.ui.TrendPoint> points) {
    }
    
    private static final java.util.List<com.dumpdiary.app.ui.TrendPoint> calculateWeeklyTrend(java.util.List<com.dumpdiary.app.data.model.BowelLogEntity> logs, java.time.YearMonth month) {
        return null;
    }
    
    private static final java.util.List<com.dumpdiary.app.ui.TrendPoint> calculateYearlyTrend(java.util.List<com.dumpdiary.app.data.model.BowelLogEntity> logs, int year) {
        return null;
    }
    
    private static final int calculateTrendDelta(java.util.List<com.dumpdiary.app.ui.TrendPoint> points) {
        return 0;
    }
    
    private static final java.lang.String calculatePeakTimeLabel(java.util.List<com.dumpdiary.app.data.model.BowelLogEntity> logs) {
        return null;
    }
    
    @androidx.compose.runtime.Composable()
    private static final java.lang.String buildStatsInsight(java.util.List<com.dumpdiary.app.data.model.BowelLogEntity> logs, com.dumpdiary.app.ui.StatsRange range, java.lang.String peakTime) {
        return null;
    }
    
    private static final kotlin.Pair<java.lang.Integer, java.lang.Integer> calculateStreak(java.util.List<com.dumpdiary.app.data.model.BowelLogEntity> logs) {
        return null;
    }
    
    private static final java.util.List<com.dumpdiary.app.ui.CalendarCellData> buildCalendarCells(java.time.YearMonth month, java.util.List<com.dumpdiary.app.data.model.BowelLogEntity> logs) {
        return null;
    }
    
    private static final androidx.compose.ui.graphics.vector.ImageVector stoolIconForLogs(java.util.List<com.dumpdiary.app.data.model.BowelLogEntity> logs) {
        return null;
    }
    
    private static final com.dumpdiary.app.ui.StoolOption stoolOptionForForm(int form) {
        return null;
    }
    
    @androidx.compose.runtime.Composable()
    private static final java.lang.String consistencyTitle(int score) {
        return null;
    }
    
    @androidx.compose.runtime.Composable()
    private static final java.lang.String buildCalendarInsight(java.util.List<com.dumpdiary.app.data.model.BowelLogEntity> monthLogs, int currentStreak) {
        return null;
    }
    
    private static final java.lang.String formatCalendarMonthTitle(java.time.YearMonth month) {
        return null;
    }
    
    private static final java.lang.String formatDateDisplay(java.lang.String occurredAt) {
        return null;
    }
    
    private static final java.lang.String formatTimeDisplay(java.lang.String occurredAt) {
        return null;
    }
    
    private static final java.time.LocalDateTime parseDateTimeOrNull(java.lang.String value) {
        return null;
    }
    
    private static final java.lang.String mergeDateAndTime(java.lang.String date, java.lang.String time) {
        return null;
    }
    
    private static final java.lang.String buildTrackedDurationLabel(java.lang.String durationValue, java.lang.String timerStartedAt, long nowMillis) {
        return null;
    }
    
    private static final java.lang.String formatDuration(int totalSeconds) {
        return null;
    }
}