package com.dumpdiary.app.data.repository;

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
public final class AppUpdateRepository_Factory implements Factory<AppUpdateRepository> {
  private final Provider<DumpDiaryApi> apiProvider;

  public AppUpdateRepository_Factory(Provider<DumpDiaryApi> apiProvider) {
    this.apiProvider = apiProvider;
  }

  @Override
  public AppUpdateRepository get() {
    return newInstance(apiProvider.get());
  }

  public static AppUpdateRepository_Factory create(Provider<DumpDiaryApi> apiProvider) {
    return new AppUpdateRepository_Factory(apiProvider);
  }

  public static AppUpdateRepository newInstance(DumpDiaryApi api) {
    return new AppUpdateRepository(api);
  }
}
