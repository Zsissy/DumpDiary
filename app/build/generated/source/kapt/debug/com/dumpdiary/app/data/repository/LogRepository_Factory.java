package com.dumpdiary.app.data.repository;

import android.content.ContentResolver;
import androidx.work.WorkManager;
import com.dumpdiary.app.data.local.LogDao;
import com.dumpdiary.app.data.local.ProfileDao;
import com.dumpdiary.app.data.local.UserPreferencesRepository;
import com.dumpdiary.app.data.remote.DumpDiaryApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class LogRepository_Factory implements Factory<LogRepository> {
  private final Provider<DumpDiaryApi> apiProvider;

  private final Provider<LogDao> logDaoProvider;

  private final Provider<ProfileDao> profileDaoProvider;

  private final Provider<UserPreferencesRepository> preferencesRepositoryProvider;

  private final Provider<ContentResolver> contentResolverProvider;

  private final Provider<WorkManager> workManagerProvider;

  public LogRepository_Factory(Provider<DumpDiaryApi> apiProvider, Provider<LogDao> logDaoProvider,
      Provider<ProfileDao> profileDaoProvider,
      Provider<UserPreferencesRepository> preferencesRepositoryProvider,
      Provider<ContentResolver> contentResolverProvider,
      Provider<WorkManager> workManagerProvider) {
    this.apiProvider = apiProvider;
    this.logDaoProvider = logDaoProvider;
    this.profileDaoProvider = profileDaoProvider;
    this.preferencesRepositoryProvider = preferencesRepositoryProvider;
    this.contentResolverProvider = contentResolverProvider;
    this.workManagerProvider = workManagerProvider;
  }

  @Override
  public LogRepository get() {
    return newInstance(apiProvider.get(), logDaoProvider.get(), profileDaoProvider.get(), preferencesRepositoryProvider.get(), contentResolverProvider.get(), workManagerProvider.get());
  }

  public static LogRepository_Factory create(Provider<DumpDiaryApi> apiProvider,
      Provider<LogDao> logDaoProvider, Provider<ProfileDao> profileDaoProvider,
      Provider<UserPreferencesRepository> preferencesRepositoryProvider,
      Provider<ContentResolver> contentResolverProvider,
      Provider<WorkManager> workManagerProvider) {
    return new LogRepository_Factory(apiProvider, logDaoProvider, profileDaoProvider, preferencesRepositoryProvider, contentResolverProvider, workManagerProvider);
  }

  public static LogRepository newInstance(DumpDiaryApi api, LogDao logDao, ProfileDao profileDao,
      UserPreferencesRepository preferencesRepository, ContentResolver contentResolver,
      WorkManager workManager) {
    return new LogRepository(api, logDao, profileDao, preferencesRepository, contentResolver, workManager);
  }
}
