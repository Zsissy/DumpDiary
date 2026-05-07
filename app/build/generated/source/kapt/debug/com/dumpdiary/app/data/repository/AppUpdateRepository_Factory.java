package com.dumpdiary.app.data.repository;

import com.dumpdiary.app.data.local.UserPreferencesRepository;
import com.dumpdiary.app.data.remote.DumpDiaryApi;
import com.dumpdiary.app.data.remote.SupabaseApi;
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
public final class AppUpdateRepository_Factory implements Factory<AppUpdateRepository> {
  private final Provider<DumpDiaryApi> apiProvider;

  private final Provider<SupabaseApi> supabaseApiProvider;

  private final Provider<ServerConfigRepository> serverConfigRepositoryProvider;

  private final Provider<UserPreferencesRepository> preferencesRepositoryProvider;

  public AppUpdateRepository_Factory(Provider<DumpDiaryApi> apiProvider,
      Provider<SupabaseApi> supabaseApiProvider,
      Provider<ServerConfigRepository> serverConfigRepositoryProvider,
      Provider<UserPreferencesRepository> preferencesRepositoryProvider) {
    this.apiProvider = apiProvider;
    this.supabaseApiProvider = supabaseApiProvider;
    this.serverConfigRepositoryProvider = serverConfigRepositoryProvider;
    this.preferencesRepositoryProvider = preferencesRepositoryProvider;
  }

  @Override
  public AppUpdateRepository get() {
    return newInstance(apiProvider.get(), supabaseApiProvider.get(), serverConfigRepositoryProvider.get(), preferencesRepositoryProvider.get());
  }

  public static AppUpdateRepository_Factory create(Provider<DumpDiaryApi> apiProvider,
      Provider<SupabaseApi> supabaseApiProvider,
      Provider<ServerConfigRepository> serverConfigRepositoryProvider,
      Provider<UserPreferencesRepository> preferencesRepositoryProvider) {
    return new AppUpdateRepository_Factory(apiProvider, supabaseApiProvider, serverConfigRepositoryProvider, preferencesRepositoryProvider);
  }

  public static AppUpdateRepository newInstance(DumpDiaryApi api, SupabaseApi supabaseApi,
      ServerConfigRepository serverConfigRepository,
      UserPreferencesRepository preferencesRepository) {
    return new AppUpdateRepository(api, supabaseApi, serverConfigRepository, preferencesRepository);
  }
}
