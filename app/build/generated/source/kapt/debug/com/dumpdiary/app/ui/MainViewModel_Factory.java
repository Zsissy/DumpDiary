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
public final class MainViewModel_Factory implements Factory<MainViewModel> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<ProfileRepository> profileRepositoryProvider;

  private final Provider<LogRepository> logRepositoryProvider;

  private final Provider<UserPreferencesRepository> preferencesRepositoryProvider;

  public MainViewModel_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<ProfileRepository> profileRepositoryProvider,
      Provider<LogRepository> logRepositoryProvider,
      Provider<UserPreferencesRepository> preferencesRepositoryProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.profileRepositoryProvider = profileRepositoryProvider;
    this.logRepositoryProvider = logRepositoryProvider;
    this.preferencesRepositoryProvider = preferencesRepositoryProvider;
  }

  @Override
  public MainViewModel get() {
    return newInstance(authRepositoryProvider.get(), profileRepositoryProvider.get(), logRepositoryProvider.get(), preferencesRepositoryProvider.get());
  }

  public static MainViewModel_Factory create(Provider<AuthRepository> authRepositoryProvider,
      Provider<ProfileRepository> profileRepositoryProvider,
      Provider<LogRepository> logRepositoryProvider,
      Provider<UserPreferencesRepository> preferencesRepositoryProvider) {
    return new MainViewModel_Factory(authRepositoryProvider, profileRepositoryProvider, logRepositoryProvider, preferencesRepositoryProvider);
  }

  public static MainViewModel newInstance(AuthRepository authRepository,
      ProfileRepository profileRepository, LogRepository logRepository,
      UserPreferencesRepository preferencesRepository) {
    return new MainViewModel(authRepository, profileRepository, logRepository, preferencesRepository);
  }
}
