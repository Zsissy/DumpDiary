package com.dumpdiary.app.worker;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.dumpdiary.app.data.local.UserPreferencesRepository;
import com.dumpdiary.app.data.repository.FriendRepository;
import com.dumpdiary.app.data.repository.LogRepository;
import com.dumpdiary.app.data.repository.ProfileRepository;
import com.dumpdiary.app.data.repository.SupabaseRoomRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class SyncWorker_Factory {
  private final Provider<LogRepository> logRepositoryProvider;

  private final Provider<ProfileRepository> profileRepositoryProvider;

  private final Provider<FriendRepository> friendRepositoryProvider;

  private final Provider<UserPreferencesRepository> preferencesRepositoryProvider;

  private final Provider<SupabaseRoomRepository> supabaseRoomRepositoryProvider;

  public SyncWorker_Factory(Provider<LogRepository> logRepositoryProvider,
      Provider<ProfileRepository> profileRepositoryProvider,
      Provider<FriendRepository> friendRepositoryProvider,
      Provider<UserPreferencesRepository> preferencesRepositoryProvider,
      Provider<SupabaseRoomRepository> supabaseRoomRepositoryProvider) {
    this.logRepositoryProvider = logRepositoryProvider;
    this.profileRepositoryProvider = profileRepositoryProvider;
    this.friendRepositoryProvider = friendRepositoryProvider;
    this.preferencesRepositoryProvider = preferencesRepositoryProvider;
    this.supabaseRoomRepositoryProvider = supabaseRoomRepositoryProvider;
  }

  public SyncWorker get(Context appContext, WorkerParameters params) {
    return newInstance(appContext, params, logRepositoryProvider.get(), profileRepositoryProvider.get(), friendRepositoryProvider.get(), preferencesRepositoryProvider.get(), supabaseRoomRepositoryProvider.get());
  }

  public static SyncWorker_Factory create(Provider<LogRepository> logRepositoryProvider,
      Provider<ProfileRepository> profileRepositoryProvider,
      Provider<FriendRepository> friendRepositoryProvider,
      Provider<UserPreferencesRepository> preferencesRepositoryProvider,
      Provider<SupabaseRoomRepository> supabaseRoomRepositoryProvider) {
    return new SyncWorker_Factory(logRepositoryProvider, profileRepositoryProvider, friendRepositoryProvider, preferencesRepositoryProvider, supabaseRoomRepositoryProvider);
  }

  public static SyncWorker newInstance(Context appContext, WorkerParameters params,
      LogRepository logRepository, ProfileRepository profileRepository,
      FriendRepository friendRepository, UserPreferencesRepository preferencesRepository,
      SupabaseRoomRepository supabaseRoomRepository) {
    return new SyncWorker(appContext, params, logRepository, profileRepository, friendRepository, preferencesRepository, supabaseRoomRepository);
  }
}
