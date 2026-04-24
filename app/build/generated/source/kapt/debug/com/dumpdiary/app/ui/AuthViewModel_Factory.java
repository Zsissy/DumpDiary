package com.dumpdiary.app.ui;

import com.dumpdiary.app.data.repository.AppUpdateRepository;
import com.dumpdiary.app.data.repository.AuthRepository;
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
public final class AuthViewModel_Factory implements Factory<AuthViewModel> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<AppUpdateRepository> appUpdateRepositoryProvider;

  public AuthViewModel_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<AppUpdateRepository> appUpdateRepositoryProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.appUpdateRepositoryProvider = appUpdateRepositoryProvider;
  }

  @Override
  public AuthViewModel get() {
    return newInstance(authRepositoryProvider.get(), appUpdateRepositoryProvider.get());
  }

  public static AuthViewModel_Factory create(Provider<AuthRepository> authRepositoryProvider,
      Provider<AppUpdateRepository> appUpdateRepositoryProvider) {
    return new AuthViewModel_Factory(authRepositoryProvider, appUpdateRepositoryProvider);
  }

  public static AuthViewModel newInstance(AuthRepository authRepository,
      AppUpdateRepository appUpdateRepository) {
    return new AuthViewModel(authRepository, appUpdateRepository);
  }
}
