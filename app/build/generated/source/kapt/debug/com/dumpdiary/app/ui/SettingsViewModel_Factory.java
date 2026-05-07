package com.dumpdiary.app.ui;

import com.dumpdiary.app.data.local.UserPreferencesRepository;
import com.dumpdiary.app.data.repository.AuthRepository;
import com.dumpdiary.app.data.repository.FriendRepository;
import com.dumpdiary.app.data.repository.LogRepository;
import com.dumpdiary.app.data.repository.ProfileRepository;
import com.dumpdiary.app.data.repository.ServerConfigRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<ProfileRepository> profileRepositoryProvider;

  private final Provider<LogRepository> logRepositoryProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<UserPreferencesRepository> preferencesRepositoryProvider;

  private final Provider<ServerConfigRepository> serverConfigRepositoryProvider;

  private final Provider<FriendRepository> friendRepositoryProvider;

  public SettingsViewModel_Factory(Provider<ProfileRepository> profileRepositoryProvider,
      Provider<LogRepository> logRepositoryProvider,
      Provider<AuthRepository> authRepositoryProvider,
      Provider<UserPreferencesRepository> preferencesRepositoryProvider,
      Provider<ServerConfigRepository> serverConfigRepositoryProvider,
      Provider<FriendRepository> friendRepositoryProvider) {
    this.profileRepositoryProvider = profileRepositoryProvider;
    this.logRepositoryProvider = logRepositoryProvider;
    this.authRepositoryProvider = authRepositoryProvider;
    this.preferencesRepositoryProvider = preferencesRepositoryProvider;
    this.serverConfigRepositoryProvider = serverConfigRepositoryProvider;
    this.friendRepositoryProvider = friendRepositoryProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(profileRepositoryProvider.get(), logRepositoryProvider.get(), authRepositoryProvider.get(), preferencesRepositoryProvider.get(), serverConfigRepositoryProvider.get(), friendRepositoryProvider.get());
  }

  public static SettingsViewModel_Factory create(
      Provider<ProfileRepository> profileRepositoryProvider,
      Provider<LogRepository> logRepositoryProvider,
      Provider<AuthRepository> authRepositoryProvider,
      Provider<UserPreferencesRepository> preferencesRepositoryProvider,
      Provider<ServerConfigRepository> serverConfigRepositoryProvider,
      Provider<FriendRepository> friendRepositoryProvider) {
    return new SettingsViewModel_Factory(profileRepositoryProvider, logRepositoryProvider, authRepositoryProvider, preferencesRepositoryProvider, serverConfigRepositoryProvider, friendRepositoryProvider);
  }

  public static SettingsViewModel newInstance(ProfileRepository profileRepository,
      LogRepository logRepository, AuthRepository authRepository,
      UserPreferencesRepository preferencesRepository,
      ServerConfigRepository serverConfigRepository, FriendRepository friendRepository) {
    return new SettingsViewModel(profileRepository, logRepository, authRepository, preferencesRepository, serverConfigRepository, friendRepository);
  }
}
