package com.dumpdiary.app.data.repository;

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
public final class FriendRepository_Factory implements Factory<FriendRepository> {
  private final Provider<DumpDiaryApi> apiProvider;

  private final Provider<UserPreferencesRepository> preferencesRepositoryProvider;

  public FriendRepository_Factory(Provider<DumpDiaryApi> apiProvider,
      Provider<UserPreferencesRepository> preferencesRepositoryProvider) {
    this.apiProvider = apiProvider;
    this.preferencesRepositoryProvider = preferencesRepositoryProvider;
  }

  @Override
  public FriendRepository get() {
    return newInstance(apiProvider.get(), preferencesRepositoryProvider.get());
  }

  public static FriendRepository_Factory create(Provider<DumpDiaryApi> apiProvider,
      Provider<UserPreferencesRepository> preferencesRepositoryProvider) {
    return new FriendRepository_Factory(apiProvider, preferencesRepositoryProvider);
  }

  public static FriendRepository newInstance(DumpDiaryApi api,
      UserPreferencesRepository preferencesRepository) {
    return new FriendRepository(api, preferencesRepository);
  }
}
