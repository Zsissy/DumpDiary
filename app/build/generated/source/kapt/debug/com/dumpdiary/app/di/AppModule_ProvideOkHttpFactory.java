package com.dumpdiary.app.di;

import com.dumpdiary.app.data.local.UserPreferencesRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import okhttp3.OkHttpClient;

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
public final class AppModule_ProvideOkHttpFactory implements Factory<OkHttpClient> {
  private final Provider<UserPreferencesRepository> preferencesRepositoryProvider;

  public AppModule_ProvideOkHttpFactory(
      Provider<UserPreferencesRepository> preferencesRepositoryProvider) {
    this.preferencesRepositoryProvider = preferencesRepositoryProvider;
  }

  @Override
  public OkHttpClient get() {
    return provideOkHttp(preferencesRepositoryProvider.get());
  }

  public static AppModule_ProvideOkHttpFactory create(
      Provider<UserPreferencesRepository> preferencesRepositoryProvider) {
    return new AppModule_ProvideOkHttpFactory(preferencesRepositoryProvider);
  }

  public static OkHttpClient provideOkHttp(UserPreferencesRepository preferencesRepository) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideOkHttp(preferencesRepository));
  }
}
