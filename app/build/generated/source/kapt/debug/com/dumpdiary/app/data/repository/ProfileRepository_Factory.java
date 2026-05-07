package com.dumpdiary.app.data.repository;

import android.content.ContentResolver;
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
public final class ProfileRepository_Factory implements Factory<ProfileRepository> {
  private final Provider<DumpDiaryApi> apiProvider;

  private final Provider<ProfileDao> profileDaoProvider;

  private final Provider<UserPreferencesRepository> preferencesRepositoryProvider;

  private final Provider<ContentResolver> contentResolverProvider;

  private final Provider<ServerConfigRepository> serverConfigRepositoryProvider;

  private final Provider<SupabaseAuthRepository> supabaseAuthRepositoryProvider;

  public ProfileRepository_Factory(Provider<DumpDiaryApi> apiProvider,
      Provider<ProfileDao> profileDaoProvider,
      Provider<UserPreferencesRepository> preferencesRepositoryProvider,
      Provider<ContentResolver> contentResolverProvider,
      Provider<ServerConfigRepository> serverConfigRepositoryProvider,
      Provider<SupabaseAuthRepository> supabaseAuthRepositoryProvider) {
    this.apiProvider = apiProvider;
    this.profileDaoProvider = profileDaoProvider;
    this.preferencesRepositoryProvider = preferencesRepositoryProvider;
    this.contentResolverProvider = contentResolverProvider;
    this.serverConfigRepositoryProvider = serverConfigRepositoryProvider;
    this.supabaseAuthRepositoryProvider = supabaseAuthRepositoryProvider;
  }

  @Override
  public ProfileRepository get() {
    return newInstance(apiProvider.get(), profileDaoProvider.get(), preferencesRepositoryProvider.get(), contentResolverProvider.get(), serverConfigRepositoryProvider.get(), supabaseAuthRepositoryProvider.get());
  }

  public static ProfileRepository_Factory create(Provider<DumpDiaryApi> apiProvider,
      Provider<ProfileDao> profileDaoProvider,
      Provider<UserPreferencesRepository> preferencesRepositoryProvider,
      Provider<ContentResolver> contentResolverProvider,
      Provider<ServerConfigRepository> serverConfigRepositoryProvider,
      Provider<SupabaseAuthRepository> supabaseAuthRepositoryProvider) {
    return new ProfileRepository_Factory(apiProvider, profileDaoProvider, preferencesRepositoryProvider, contentResolverProvider, serverConfigRepositoryProvider, supabaseAuthRepositoryProvider);
  }

  public static ProfileRepository newInstance(DumpDiaryApi api, ProfileDao profileDao,
      UserPreferencesRepository preferencesRepository, ContentResolver contentResolver,
      ServerConfigRepository serverConfigRepository,
      SupabaseAuthRepository supabaseAuthRepository) {
    return new ProfileRepository(api, profileDao, preferencesRepository, contentResolver, serverConfigRepository, supabaseAuthRepository);
  }
}
