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

  public ProfileRepository_Factory(Provider<DumpDiaryApi> apiProvider,
      Provider<ProfileDao> profileDaoProvider,
      Provider<UserPreferencesRepository> preferencesRepositoryProvider,
      Provider<ContentResolver> contentResolverProvider) {
    this.apiProvider = apiProvider;
    this.profileDaoProvider = profileDaoProvider;
    this.preferencesRepositoryProvider = preferencesRepositoryProvider;
    this.contentResolverProvider = contentResolverProvider;
  }

  @Override
  public ProfileRepository get() {
    return newInstance(apiProvider.get(), profileDaoProvider.get(), preferencesRepositoryProvider.get(), contentResolverProvider.get());
  }

  public static ProfileRepository_Factory create(Provider<DumpDiaryApi> apiProvider,
      Provider<ProfileDao> profileDaoProvider,
      Provider<UserPreferencesRepository> preferencesRepositoryProvider,
      Provider<ContentResolver> contentResolverProvider) {
    return new ProfileRepository_Factory(apiProvider, profileDaoProvider, preferencesRepositoryProvider, contentResolverProvider);
  }

  public static ProfileRepository newInstance(DumpDiaryApi api, ProfileDao profileDao,
      UserPreferencesRepository preferencesRepository, ContentResolver contentResolver) {
    return new ProfileRepository(api, profileDao, preferencesRepository, contentResolver);
  }
}
