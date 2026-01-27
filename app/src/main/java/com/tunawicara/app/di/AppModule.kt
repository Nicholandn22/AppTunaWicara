package com.tunawicara.app.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tunawicara.app.data.repository.ExerciseRepositoryImpl
import com.tunawicara.app.data.repository.FirebaseAuthRepository
import com.tunawicara.app.data.repository.MateriWicaraRepositoryImpl
import com.tunawicara.app.domain.repository.AuthRepository
import com.tunawicara.app.domain.repository.ExerciseRepository
import com.tunawicara.app.domain.repository.MateriWicaraRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing app-level dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    
    @Binds
    @Singleton
    abstract fun bindExerciseRepository(
        exerciseRepositoryImpl: ExerciseRepositoryImpl
    ): ExerciseRepository
    
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        firebaseAuthRepository: FirebaseAuthRepository
    ): AuthRepository
    
    @Binds
    @Singleton
    abstract fun bindMateriWicaraRepository(
        materiWicaraRepositoryImpl: MateriWicaraRepositoryImpl
    ): MateriWicaraRepository
}

/**
 * Hilt module for providing Firebase instances
 */
@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
    
    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }
}
