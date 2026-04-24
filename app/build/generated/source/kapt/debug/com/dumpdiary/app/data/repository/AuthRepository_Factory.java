package com.dumpdiary.app.data.repository;

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
public final class AuthRepository_Factory implements Factory<AuthRepository> {
  private final Provider<DumpDiaryApi> apiProvider;

  private final Provider<UserPreferencesRepository> preferencesRepositoryProvider;

  private final Provider<ProfileDao> profileDaoProvider;

  private final Provider<LogDao> logDaoProvider;

  public AuthRepository_Factory(Provider<DumpDiaryApi> apiProvider,
      Provider<UserPreferencesRepository> preferencesRepositoryProvider,
      Provider<ProfileDao> profileDaoProvider, Provider<LogDao> logDaoProvider) {
    this.apiProvider = apiProvider;
    this.preferencesRepositoryProvider = preferencesRepositoryProvider;
    this.profileDaoProvider = profileDaoProvider;
    this.logDaoProvider = logDaoProvider;
  }

  @Override
  public AuthRepository get() {
    return newInstance(apiProvider.get(), preferencesRepositoryProvider.get(), profileDaoProvider.get(), logDaoProvider.get());
  }

  public static AuthRepository_Factory create(Provider<DumpDiaryApi> apiProvider,
      Provider<UserPreferencesRepository> preferencesRepositoryProvider,
      Provider<ProfileDao> profileDaoProvider, Provider<LogDao> logDaoProvider) {
    return new AuthRepository_Factory(apiProvider, preferencesRepositoryProvider, profileDaoProvider, logDaoProvider);
  }

  public static AuthRepository newInstance(DumpDiaryApi api,
      UserPreferencesRepository preferencesRepository, ProfileDao profileDao, LogDao logDao) {
    return new AuthRepository(api, preferencesRepository, profileDao, logDao);
  }
}
