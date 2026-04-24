package com.dumpdiary.app.di;

import com.dumpdiary.app.data.local.AppDatabase;
import com.dumpdiary.app.data.local.ProfileDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideProfileDaoFactory implements Factory<ProfileDao> {
  private final Provider<AppDatabase> databaseProvider;

  public AppModule_ProvideProfileDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public ProfileDao get() {
    return provideProfileDao(databaseProvider.get());
  }

  public static AppModule_ProvideProfileDaoFactory create(Provider<AppDatabase> databaseProvider) {
    return new AppModule_ProvideProfileDaoFactory(databaseProvider);
  }

  public static ProfileDao provideProfileDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideProfileDao(database));
  }
}
