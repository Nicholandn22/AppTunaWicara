package com.tunawicara.app.data.repository;

import com.google.firebase.firestore.FirebaseFirestore;
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
    "KotlinInternalInJava"
})
public final class ExerciseRepositoryImpl_Factory implements Factory<ExerciseRepositoryImpl> {
  private final Provider<FirebaseFirestore> firestoreProvider;

  public ExerciseRepositoryImpl_Factory(Provider<FirebaseFirestore> firestoreProvider) {
    this.firestoreProvider = firestoreProvider;
  }

  @Override
  public ExerciseRepositoryImpl get() {
    return newInstance(firestoreProvider.get());
  }

  public static ExerciseRepositoryImpl_Factory create(
      Provider<FirebaseFirestore> firestoreProvider) {
    return new ExerciseRepositoryImpl_Factory(firestoreProvider);
  }

  public static ExerciseRepositoryImpl newInstance(FirebaseFirestore firestore) {
    return new ExerciseRepositoryImpl(firestore);
  }
}
