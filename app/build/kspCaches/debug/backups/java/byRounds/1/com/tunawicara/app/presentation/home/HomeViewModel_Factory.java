package com.tunawicara.app.presentation.home;

import com.google.firebase.auth.FirebaseAuth;
import com.tunawicara.app.data.audio.AudioPlayerManager;
import com.tunawicara.app.data.audio.AudioRecorderManager;
import com.tunawicara.app.domain.repository.MateriWicaraRepository;
import com.tunawicara.app.domain.usecase.GetExercisesUseCase;
import com.tunawicara.app.domain.usecase.GetMateriWithProgressUseCase;
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

  private final Provider<GetMateriWithProgressUseCase> getMateriWithProgressUseCaseProvider;

  private final Provider<MateriWicaraRepository> materiWicaraRepositoryProvider;

  private final Provider<AudioRecorderManager> audioRecorderManagerProvider;

  private final Provider<AudioPlayerManager> audioPlayerManagerProvider;

  private final Provider<FirebaseAuth> firebaseAuthProvider;

  public HomeViewModel_Factory(Provider<GetExercisesUseCase> getExercisesUseCaseProvider,
      Provider<GetMateriWithProgressUseCase> getMateriWithProgressUseCaseProvider,
      Provider<MateriWicaraRepository> materiWicaraRepositoryProvider,
      Provider<AudioRecorderManager> audioRecorderManagerProvider,
      Provider<AudioPlayerManager> audioPlayerManagerProvider,
      Provider<FirebaseAuth> firebaseAuthProvider) {
    this.getExercisesUseCaseProvider = getExercisesUseCaseProvider;
    this.getMateriWithProgressUseCaseProvider = getMateriWithProgressUseCaseProvider;
    this.materiWicaraRepositoryProvider = materiWicaraRepositoryProvider;
    this.audioRecorderManagerProvider = audioRecorderManagerProvider;
    this.audioPlayerManagerProvider = audioPlayerManagerProvider;
    this.firebaseAuthProvider = firebaseAuthProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(getExercisesUseCaseProvider.get(), getMateriWithProgressUseCaseProvider.get(), materiWicaraRepositoryProvider.get(), audioRecorderManagerProvider.get(), audioPlayerManagerProvider.get(), firebaseAuthProvider.get());
  }

  public static HomeViewModel_Factory create(
      Provider<GetExercisesUseCase> getExercisesUseCaseProvider,
      Provider<GetMateriWithProgressUseCase> getMateriWithProgressUseCaseProvider,
      Provider<MateriWicaraRepository> materiWicaraRepositoryProvider,
      Provider<AudioRecorderManager> audioRecorderManagerProvider,
      Provider<AudioPlayerManager> audioPlayerManagerProvider,
      Provider<FirebaseAuth> firebaseAuthProvider) {
    return new HomeViewModel_Factory(getExercisesUseCaseProvider, getMateriWithProgressUseCaseProvider, materiWicaraRepositoryProvider, audioRecorderManagerProvider, audioPlayerManagerProvider, firebaseAuthProvider);
  }

  public static HomeViewModel newInstance(GetExercisesUseCase getExercisesUseCase,
      GetMateriWithProgressUseCase getMateriWithProgressUseCase,
      MateriWicaraRepository materiWicaraRepository, AudioRecorderManager audioRecorderManager,
      AudioPlayerManager audioPlayerManager, FirebaseAuth firebaseAuth) {
    return new HomeViewModel(getExercisesUseCase, getMateriWithProgressUseCase, materiWicaraRepository, audioRecorderManager, audioPlayerManager, firebaseAuth);
  }
}
