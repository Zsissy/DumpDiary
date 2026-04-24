package com.dumpdiary.app.di;

import com.dumpdiary.app.data.remote.DumpDiaryApi;
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
public final class AppModule_ProvideApiFactory implements Factory<DumpDiaryApi> {
  private final Provider<OkHttpClient> okHttpClientProvider;

  public AppModule_ProvideApiFactory(Provider<OkHttpClient> okHttpClientProvider) {
    this.okHttpClientProvider = okHttpClientProvider;
  }

  @Override
  public DumpDiaryApi get() {
    return provideApi(okHttpClientProvider.get());
  }

  public static AppModule_ProvideApiFactory create(Provider<OkHttpClient> okHttpClientProvider) {
    return new AppModule_ProvideApiFactory(okHttpClientProvider);
  }

  public static DumpDiaryApi provideApi(OkHttpClient okHttpClient) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideApi(okHttpClient));
  }
}
