package com.dumpdiary.app.di;

import android.content.ContentResolver;
import android.content.Context;
import androidx.room.Room;
import androidx.work.WorkManager;
import com.dumpdiary.app.data.local.AppDatabase;
import com.dumpdiary.app.data.local.LogDao;
import com.dumpdiary.app.data.local.ProfileDao;
import com.dumpdiary.app.data.local.UserPreferencesRepository;
import com.dumpdiary.app.data.remote.DumpDiaryApi;
import com.dumpdiary.app.data.remote.SupabaseApi;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Named;
import javax.inject.Singleton;
import okhttp3.Authenticator;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import org.json.JSONObject;
import retrofit2.Retrofit;

@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000r\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u0003\u001a\u00020\u00042\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u0007J\u0012\u0010\u0007\u001a\u00020\b2\b\b\u0001\u0010\t\u001a\u00020\nH\u0007J\u0012\u0010\u000b\u001a\u00020\f2\b\b\u0001\u0010\t\u001a\u00020\nH\u0007J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\fH\u0007J\u0010\u0010\u0010\u001a\u00020\u00062\u0006\u0010\u0011\u001a\u00020\u0012H\u0007J\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u000f\u001a\u00020\fH\u0007J\u0012\u0010\u0015\u001a\u00020\u00162\b\b\u0001\u0010\u0017\u001a\u00020\u0006H\u0007J\u0010\u0010\u0018\u001a\u00020\u00062\u0006\u0010\u0011\u001a\u00020\u0012H\u0007J\u0012\u0010\u0019\u001a\u00020\u001a2\b\b\u0001\u0010\t\u001a\u00020\nH\u0007J\u0018\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u001cH\u0002J\u0018\u0010 \u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u001cH\u0002J\u0010\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020$H\u0002J\u001e\u0010%\u001a\u00020&*\u00020\'2\u0006\u0010(\u001a\u00020\u001e2\b\u0010)\u001a\u0004\u0018\u00010\u001eH\u0002\u00a8\u0006*"}, d2 = {"Lcom/dumpdiary/app/di/AppModule;", "", "()V", "provideApi", "Lcom/dumpdiary/app/data/remote/DumpDiaryApi;", "okHttpClient", "Lokhttp3/OkHttpClient;", "provideContentResolver", "Landroid/content/ContentResolver;", "context", "Landroid/content/Context;", "provideDatabase", "Lcom/dumpdiary/app/data/local/AppDatabase;", "provideLogDao", "Lcom/dumpdiary/app/data/local/LogDao;", "database", "provideOkHttp", "preferencesRepository", "Lcom/dumpdiary/app/data/local/UserPreferencesRepository;", "provideProfileDao", "Lcom/dumpdiary/app/data/local/ProfileDao;", "provideSupabaseApi", "Lcom/dumpdiary/app/data/remote/SupabaseApi;", "supabaseOkHttpClient", "provideSupabaseOkHttp", "provideWorkManager", "Landroidx/work/WorkManager;", "resolveRuntimeUrl", "Lokhttp3/HttpUrl;", "baseUrl", "", "originalUrl", "resolveSupabaseUrl", "responseCount", "", "response", "Lokhttp3/Response;", "addEncodedPathParameter", "", "Lokhttp3/HttpUrl$Builder;", "name", "value", "app_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class AppModule {
    @org.jetbrains.annotations.NotNull()
    public static final com.dumpdiary.app.di.AppModule INSTANCE = null;
    
    private AppModule() {
        super();
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.dumpdiary.app.data.local.AppDatabase provideDatabase(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.dumpdiary.app.data.local.ProfileDao provideProfileDao(@org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.local.AppDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.dumpdiary.app.data.local.LogDao provideLogDao(@org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.local.AppDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final android.content.ContentResolver provideContentResolver(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Named(value = "rest")
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final okhttp3.OkHttpClient provideOkHttp(@org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.local.UserPreferencesRepository preferencesRepository) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.dumpdiary.app.data.remote.DumpDiaryApi provideApi(@javax.inject.Named(value = "rest")
    @org.jetbrains.annotations.NotNull()
    okhttp3.OkHttpClient okHttpClient) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Named(value = "supabase")
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final okhttp3.OkHttpClient provideSupabaseOkHttp(@org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.local.UserPreferencesRepository preferencesRepository) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.dumpdiary.app.data.remote.SupabaseApi provideSupabaseApi(@javax.inject.Named(value = "supabase")
    @org.jetbrains.annotations.NotNull()
    okhttp3.OkHttpClient supabaseOkHttpClient) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final androidx.work.WorkManager provideWorkManager(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    private final int responseCount(okhttp3.Response response) {
        return 0;
    }
    
    private final okhttp3.HttpUrl resolveRuntimeUrl(java.lang.String baseUrl, okhttp3.HttpUrl originalUrl) {
        return null;
    }
    
    private final okhttp3.HttpUrl resolveSupabaseUrl(java.lang.String baseUrl, okhttp3.HttpUrl originalUrl) {
        return null;
    }
    
    private final void addEncodedPathParameter(okhttp3.HttpUrl.Builder $this$addEncodedPathParameter, java.lang.String name, java.lang.String value) {
    }
}