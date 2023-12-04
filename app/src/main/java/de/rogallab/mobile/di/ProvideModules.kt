package de.rogallab.mobile.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.rogallab.mobile.AppStart
import de.rogallab.mobile.data.IPeopleDao
import de.rogallab.mobile.data.IWorkordersDao
import de.rogallab.mobile.data.database.AppDatabase
import de.rogallab.mobile.data.seed.Seed
import de.rogallab.mobile.domain.IPeopleRepository
import de.rogallab.mobile.domain.IWorkordersRepository
import de.rogallab.mobile.domain.utilities.logError
import de.rogallab.mobile.domain.utilities.logInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

// https://medium.com/androiddevelopers/dependency-injection-on-android-with-hilt-67b6031e62d

// @Provides: Adds a binding for a type that cannot be constructor injected

// open class BaseComponentActivity(
//    private val _tag: String
// ) : ComponentActivity() {
// property injection with Dagger/Hilt
// @Inject
// lateinit var _logger: ILogger

@Module
@InstallIn(SingletonComponent::class)
object ProvideModules {
                          //12345678901234567890123
   private const val tag = "ok>AppProvidesModules ."

   @Singleton
   @Provides
   fun provideContext(
      application: Application // provided by Hilt
   ): Context {
      logInfo(tag, "providesContext()")
      return application.applicationContext
   }

   @Singleton
   @Provides
   fun provideCoroutineExceptionHandler(
   ): CoroutineExceptionHandler {
      logInfo(tag, "providesCoroutineExceptionHandler()")
      return CoroutineExceptionHandler { _, exception ->
         exception.localizedMessage?.let {
            logError("ok>CoroutineException", it)
         } ?: run {
            exception.stackTrace.forEach {
               logError("ok>CoroutineException", it.toString())
            }
         }
      }
   }
   @Singleton
   @Provides
   fun provideCoroutineDispatcher(
   ): CoroutineDispatcher {
      logInfo(tag, "providesCoroutineDispatcher()")
      return Dispatchers.IO
   }

   @Provides
   @Singleton
   fun provideSeed(
      application: Application,
      peopleRepository: IPeopleRepository,
      workordersRepository: IWorkordersRepository,
      dispatcher: CoroutineDispatcher,
      exceptionHandler: CoroutineExceptionHandler
   ): Seed {
      logInfo(tag, "providesSeed()")
      return Seed(
         application,
         peopleRepository,
         workordersRepository,
         dispatcher,
         exceptionHandler
      )
   }

   @Provides
   @Singleton
   fun providePeopleDao(
      database: AppDatabase
   ): IPeopleDao {
      logInfo(tag, "providesIPeopleDao()")
      return database.createPeopleDao()
   }

   @Provides
   @Singleton
   fun provideWorkOrdersDao(
      database: AppDatabase
   ): IWorkordersDao {
      logInfo(tag, "providesIWorkordersDao()")
      return database.createWordordersDao()
   }

   @Provides
   @Singleton
   fun provideAppDatabase(
      application: Application // provided by Hilt
   ): AppDatabase {
      logInfo(tag, "providesAppDatabase()")
      return Room.databaseBuilder(
         application.applicationContext,
         AppDatabase::class.java,
         AppStart.database_name
      ).fallbackToDestructiveMigration()
         .build()
   }
}