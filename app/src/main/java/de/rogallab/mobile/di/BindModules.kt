package de.rogallab.mobile.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import de.rogallab.mobile.data.repositories.PeopleRepositoryImpl
import de.rogallab.mobile.data.repositories.WordordersRepositoryImpl
import de.rogallab.mobile.domain.IPeopleRepository
import de.rogallab.mobile.domain.IPeopleUseCases
import de.rogallab.mobile.domain.IWorkordersRepository
import de.rogallab.mobile.domain.usecases.people.PeopleUseCasesImpl

import javax.inject.Singleton

@InstallIn(ViewModelComponent::class)
@Module
abstract class BindViewModelModules {
   @ViewModelScoped
   @Binds
   abstract fun bindPeopleUseCases(
      peopleUseCases: PeopleUseCasesImpl
   ): IPeopleUseCases
}

@Module
@InstallIn(SingletonComponent::class)
interface BindSingletonModules {
   @Binds
   @Singleton
   fun bindPeopleRepository(
      peopleRepositoryImpl: PeopleRepositoryImpl
   ): IPeopleRepository

   @Binds
   @Singleton
   fun bindWorkordersRepository(
      wordOrdersRepositoryImpl: WordordersRepositoryImpl
   ): IWorkordersRepository
}