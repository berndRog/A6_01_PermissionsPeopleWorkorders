package de.rogallab.mobile.domain.usecases.people

import de.rogallab.mobile.data.models.PersonDto
import de.rogallab.mobile.domain.IPeopleRepository
import de.rogallab.mobile.domain.UiState
import de.rogallab.mobile.domain.entities.Person
import de.rogallab.mobile.domain.mapping.toPerson
import de.rogallab.mobile.domain.utilities.logDebug
import de.rogallab.mobile.domain.utilities.logError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ReadPeople @Inject constructor(
   private val _peopleRepository: IPeopleRepository,
   private val _dispatcher: CoroutineDispatcher,
   private val _exceptionHandler: CoroutineExceptionHandler
) {

   operator fun invoke(): Flow<UiState<List<Person>>> = flow {
      emit(UiState.Loading)

      _peopleRepository.selectAll().distinctUntilChanged().collect { peopleDto: List<PersonDto> ->
         delay(500)
         logDebug(tag, "ReadAll.invoke() emit success")
//       val filteredPeople = peopleDto.filter { peopleDto ->
//          peopleDto.lastName.toUpper)=.startsWith("A")
//       }
         val people = peopleDto.toPerson().sortedBy { it.lastName }
         emit(UiState.Success(data = people))
      }
   }.catch {
      val message = it.localizedMessage ?: it.stackTraceToString()
      logError(tag, message)
      emit(UiState.Error(message = message))
   }.flowOn(_dispatcher + _exceptionHandler)

   companion object {
                                     //12345678901234567890123
      private const val tag: String = "ok>PeopleReadAllUseCase"
   }
}