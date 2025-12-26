package com.tunawicara.app.domain.usecase;

import com.tunawicara.app.domain.repository.ExerciseRepository;
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
public final class GetExercisesUseCase_Factory implements Factory<GetExercisesUseCase> {
  private final Provider<ExerciseRepository> exerciseRepositoryProvider;

  public GetExercisesUseCase_Factory(Provider<ExerciseRepository> exerciseRepositoryProvider) {
    this.exerciseRepositoryProvider = exerciseRepositoryProvider;
  }

  @Override
  public GetExercisesUseCase get() {
    return newInstance(exerciseRepositoryProvider.get());
  }

  public static GetExercisesUseCase_Factory create(
      Provider<ExerciseRepository> exerciseRepositoryProvider) {
    return new GetExercisesUseCase_Factory(exerciseRepositoryProvider);
  }

  public static GetExercisesUseCase newInstance(ExerciseRepository exerciseRepository) {
    return new GetExercisesUseCase(exerciseRepository);
  }
}
