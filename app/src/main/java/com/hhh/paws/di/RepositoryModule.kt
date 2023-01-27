package com.hhh.paws.di

import com.google.firebase.firestore.FirebaseFirestore
import com.hhh.paws.database.dao.NotesDao
import com.hhh.paws.database.dao.PetDao
import com.hhh.paws.database.dao.UserDao
import com.hhh.paws.database.repository.NotesRepository
import com.hhh.paws.database.repository.PetRepository
import com.hhh.paws.database.repository.UserRepository
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
}