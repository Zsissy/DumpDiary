package com.dumpdiary.app.di;

import com.dumpdiary.app.data.local.AppDatabase;
import com.dumpdiary.app.data.local.LogDao;
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
public final class AppModule_ProvideLogDaoFactory implements Factory<LogDao> {
  private final Provider<AppDatabase> databaseProvider;

  public AppModule_ProvideLogDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public LogDao get() {
    return provideLogDao(databaseProvider.get());
  }

  public static AppModule_ProvideLogDaoFactory create(Provider<AppDatabase> databaseProvider) {
    return new AppModule_ProvideLogDaoFactory(databaseProvider);
  }

  public static LogDao provideLogDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideLogDao(database));
  }
}
