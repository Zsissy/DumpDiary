package com.dumpdiary.app;

import androidx.hilt.work.HiltWorkerFactory;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class DumpDiaryApplication_MembersInjector implements MembersInjector<DumpDiaryApplication> {
  private final Provider<HiltWorkerFactory> workerFactoryProvider;

  public DumpDiaryApplication_MembersInjector(Provider<HiltWorkerFactory> workerFactoryProvider) {
    this.workerFactoryProvider = workerFactoryProvider;
  }

  public static MembersInjector<DumpDiaryApplication> create(
      Provider<HiltWorkerFactory> workerFactoryProvider) {
    return new DumpDiaryApplication_MembersInjector(workerFactoryProvider);
  }

  @Override
  public void injectMembers(DumpDiaryApplication instance) {
    injectWorkerFactory(instance, workerFactoryProvider.get());
  }

  @InjectedFieldSignature("com.dumpdiary.app.DumpDiaryApplication.workerFactory")
  public static void injectWorkerFactory(DumpDiaryApplication instance,
      HiltWorkerFactory workerFactory) {
    instance.workerFactory = workerFactory;
  }
}
