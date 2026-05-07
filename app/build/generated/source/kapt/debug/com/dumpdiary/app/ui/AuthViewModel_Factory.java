package com.dumpdiary.app.ui;

import com.dumpdiary.app.data.local.UserPreferencesRepository;
import com.dumpdiary.app.data.repository.AppUpdateRepository;
import com.dumpdiary.app.data.repository.AuthRepository;
import com.dumpdiary.app.data.repository.ServerConfigRepository;
import com.dumpdiary.app.data.repository.SupabaseAuthRepository;
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

  private final Provider<SupabaseAuthRepository> supabaseAuthRepositoryProvider;

  private final Provider<AppUpdateRepository> appUpdateRepositoryProvider;

  private final Provider<ServerConfigRepository> serverConfigRepositoryProvider;

  private final Provider<UserPreferencesRepository> preferencesRepositoryProvider;

  public AuthViewModel_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<SupabaseAuthRepository> supabaseAuthRepositoryProvider,
      Provider<AppUpdateRepository> appUpdateRepositoryProvider,
      Provider<ServerConfigRepository> serverConfigRepositoryProvider,
      Provider<UserPreferencesRepository> preferencesRepositoryProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.supabaseAuthRepositoryProvider = supabaseAuthRepositoryProvider;
    this.appUpdateRepositoryProvider = appUpdateRepositoryProvider;
    this.serverConfigRepositoryProvider = serverConfigRepositoryProvider;
    this.preferencesRepositoryProvider = preferencesRepositoryProvider;
  }

  @Override
  public AuthViewModel get() {
    return newInstance(authRepositoryProvider.get(), supabaseAuthRepositoryProvider.get(), appUpdateRepositoryProvider.get(), serverConfigRepositoryProvider.get(), preferencesRepositoryProvider.get());
  }

  public static AuthViewModel_Factory create(Provider<AuthRepository> authRepositoryProvider,
      Provider<SupabaseAuthRepository> supabaseAuthRepositoryProvider,
      Provider<AppUpdateRepository> appUpdateRepositoryProvider,
      Provider<ServerConfigRepository> serverConfigRepositoryProvider,
      Provider<UserPreferencesRepository> preferencesRepositoryProvider) {
    return new AuthViewModel_Factory(authRepositoryProvider, supabaseAuthRepositoryProvider, appUpdateRepositoryProvider, serverConfigRepositoryProvider, preferencesRepositoryProvider);
  }

  public static AuthViewModel newInstance(AuthRepository authRepository,
      SupabaseAuthRepository supabaseAuthRepository, AppUpdateRepository appUpdateRepository,
      ServerConfigRepository serverConfigRepository,
      UserPreferencesRepository preferencesRepository) {
    return new AuthViewModel(authRepository, supabaseAuthRepository, appUpdateRepository, serverConfigRepository, preferencesRepository);
  }
}
