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

  private final Provider<ServerConfigRepository> serverConfigRepositoryProvider;

  private final Provider<FriendRepository> friendRepositoryProvider;

  public AuthRepository_Factory(Provider<DumpDiaryApi> apiProvider,
      Provider<UserPreferencesRepository> preferencesRepositoryProvider,
      Provider<ProfileDao> profileDaoProvider, Provider<LogDao> logDaoProvider,
      Provider<ServerConfigRepository> serverConfigRepositoryProvider,
      Provider<FriendRepository> friendRepositoryProvider) {
    this.apiProvider = apiProvider;
    this.preferencesRepositoryProvider = preferencesRepositoryProvider;
    this.profileDaoProvider = profileDaoProvider;
    this.logDaoProvider = logDaoProvider;
    this.serverConfigRepositoryProvider = serverConfigRepositoryProvider;
    this.friendRepositoryProvider = friendRepositoryProvider;
  }

  @Override
  public AuthRepository get() {
    return newInstance(apiProvider.get(), preferencesRepositoryProvider.get(), profileDaoProvider.get(), logDaoProvider.get(), serverConfigRepositoryProvider.get(), friendRepositoryProvider.get());
  }

  public static AuthRepository_Factory create(Provider<DumpDiaryApi> apiProvider,
      Provider<UserPreferencesRepository> preferencesRepositoryProvider,
      Provider<ProfileDao> profileDaoProvider, Provider<LogDao> logDaoProvider,
      Provider<ServerConfigRepository> serverConfigRepositoryProvider,
      Provider<FriendRepository> friendRepositoryProvider) {
    return new AuthRepository_Factory(apiProvider, preferencesRepositoryProvider, profileDaoProvider, logDaoProvider, serverConfigRepositoryProvider, friendRepositoryProvider);
  }

  public static AuthRepository newInstance(DumpDiaryApi api,
      UserPreferencesRepository preferencesRepository, ProfileDao profileDao, LogDao logDao,
      ServerConfigRepository serverConfigRepository, FriendRepository friendRepository) {
    return new AuthRepository(api, preferencesRepository, profileDao, logDao, serverConfigRepository, friendRepository);
  }
}
