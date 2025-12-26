package com.tunawicara.app.presentation.home;

import com.tunawicara.app.data.audio.AudioPlayerManager;
import com.tunawicara.app.data.audio.AudioRecorderManager;
import com.tunawicara.app.domain.usecase.GetExercisesUseCase;
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
    "KotlinInternalInJava"
})
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<GetExercisesUseCase> getExercisesUseCaseProvider;

  private final Provider<AudioRecorderManager> audioRecorderManagerProvider;

  private final Provider<AudioPlayerManager> audioPlayerManagerProvider;

  public HomeViewModel_Factory(Provider<GetExercisesUseCase> getExercisesUseCaseProvider,
      Provider<AudioRecorderManager> audioRecorderManagerProvider,
      Provider<AudioPlayerManager> audioPlayerManagerProvider) {
    this.getExercisesUseCaseProvider = getExercisesUseCaseProvider;
    this.audioRecorderManagerProvider = audioRecorderManagerProvider;
    this.audioPlayerManagerProvider = audioPlayerManagerProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(getExercisesUseCaseProvider.get(), audioRecorderManagerProvider.get(), audioPlayerManagerProvider.get());
  }

  public static HomeViewModel_Factory create(
      Provider<GetExercisesUseCase> getExercisesUseCaseProvider,
      Provider<AudioRecorderManager> audioRecorderManagerProvider,
      Provider<AudioPlayerManager> audioPlayerManagerProvider) {
    return new HomeViewModel_Factory(getExercisesUseCaseProvider, audioRecorderManagerProvider, audioPlayerManagerProvider);
  }

  public static HomeViewModel newInstance(GetExercisesUseCase getExercisesUseCase,
      AudioRecorderManager audioRecorderManager, AudioPlayerManager audioPlayerManager) {
    return new HomeViewModel(getExercisesUseCase, audioRecorderManager, audioPlayerManager);
  }
}
