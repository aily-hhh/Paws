package com.hhh.paws.di

import com.google.firebase.firestore.FirebaseFirestore
import com.hhh.paws.database.dao.*
import com.hhh.paws.database.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(database: FirebaseFirestore): UserDao {
        return UserRepository(database)
    }

    @Provides
    @Singleton
    fun providePetRepository(database: FirebaseFirestore): PetDao {
        return PetRepository(database)
    }

    @Provides
    @Singleton
    fun provideNotesRepository(database: FirebaseFirestore): NotesDao {
        return NotesRepository(database)
    }

    @Provides
    @Singleton
    fun provideIdentificationRepository(database: FirebaseFirestore): IdentificationDao {
        return IdentificationRepository(database)
    }

    @Provides
    @Singleton
    fun provideTreatmentRepository(database: FirebaseFirestore): TreatmentDao {
        return TreatmentRepository(database)
    }

    @Provides
    @Singleton
    fun provideVaccinesRepository(database: FirebaseFirestore): VaccinesDao {
        return VaccinesRepository(database)
    }

    @Provides
    @Singleton
    fun provideGalleryRepository(database: FirebaseFirestore): GalleryDao {
        return GalleryRepository(database)
    }
}