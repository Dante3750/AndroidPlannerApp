package com.example.androidassignment4travelplannerapp.di

import com.example.androidassignment4travelplannerapp.data.repository.TravelRepositoryImpl
import com.example.androidassignment4travelplannerapp.domain.repository.ITravelRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTravelRepository(
        travelRepositoryImpl: TravelRepositoryImpl
    ): ITravelRepository
}
