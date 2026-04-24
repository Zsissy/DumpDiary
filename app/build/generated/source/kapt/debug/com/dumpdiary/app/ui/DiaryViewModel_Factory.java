package com.dumpdiary.app.ui;

import com.dumpdiary.app.data.repository.FriendRepository;
import com.dumpdiary.app.data.repository.LogRepository;
import com.dumpdiary.app.data.repository.ProfileRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class DiaryViewModel_Factory implements Factory<DiaryViewModel> {
  private final Provider<LogRepository> logRepositoryProvider;

  private final Provider<ProfileRepository> profileRepositoryProvider;

  private final Provider<FriendRepository> friendRepositoryProvider;

  public DiaryViewModel_Factory(Provider<LogRepository> logRepositoryProvider,
      Provider<ProfileRepository> profileRepositoryProvider,
      Provider<FriendRepository> friendRepositoryProvider) {
    this.logRepositoryProvider = logRepositoryProvider;
    this.profileRepositoryProvider = profileRepositoryProvider;
    this.friendRepositoryProvider = friendRepositoryProvider;
  }

  @Override
  public DiaryViewModel get() {
    return newInstance(logRepositoryProvider.get(), profileRepositoryProvider.get(), friendRepositoryProvider.get());
  }

  public static DiaryViewModel_Factory create(Provider<LogRepository> logRepositoryProvider,
      Provider<ProfileRepository> profileRepositoryProvider,
      Provider<FriendRepository> friendRepositoryProvider) {
    return new DiaryViewModel_Factory(logRepositoryProvider, profileRepositoryProvider, friendRepositoryProvider);
  }

  public static DiaryViewModel newInstance(LogRepository logRepository,
      ProfileRepository profileRepository, FriendRepository friendRepository) {
    return new DiaryViewModel(logRepository, profileRepository, friendRepository);
  }
}
