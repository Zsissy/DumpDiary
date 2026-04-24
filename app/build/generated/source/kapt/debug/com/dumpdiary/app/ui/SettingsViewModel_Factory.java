package com.dumpdiary.app.ui;

import com.dumpdiary.app.data.local.UserPreferencesRepository;
import com.dumpdiary.app.data.repository.AuthRepository;
import com.dumpdiary.app.data.repository.LogRepository;
import com.dumpdiary.app.data.repository.ProfileRepository;
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

  public SettingsViewModel_Factory(Provider<ProfileRepository> profileRepositoryProvider,
      Provider<LogRepository> logRepositoryProvider,
      Provider<AuthRepository> authRepositoryProvider,
      Provider<UserPreferencesRepository> preferencesRepositoryProvider) {
    this.profileRepositoryProvider = profileRepositoryProvider;
    this.logRepositoryProvider = logRepositoryProvider;
    this.authRepositoryProvider = authRepositoryProvider;
    this.preferencesRepositoryProvider = preferencesRepositoryProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(profileRepositoryProvider.get(), logRepositoryProvider.get(), authRepositoryProvider.get(), preferencesRepositoryProvider.get());
  }

  public static SettingsViewModel_Factory create(
      Provider<ProfileRepository> profileRepositoryProvider,
      Provider<LogRepository> logRepositoryProvider,
      Provider<AuthRepository> authRepositoryProvider,
      Provider<UserPreferencesRepository> preferencesRepositoryProvider) {
    return new SettingsViewModel_Factory(profileRepositoryProvider, logRepositoryProvider, authRepositoryProvider, preferencesRepositoryProvider);
  }

  public static SettingsViewModel newInstance(ProfileRepository profileRepository,
      LogRepository logRepository, AuthRepository authRepository,
      UserPreferencesRepository preferencesRepository) {
    return new SettingsViewModel(profileRepository, logRepository, authRepository, preferencesRepository);
  }
}
