package de.rogallab.mobile.domain.usecases.people

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import de.rogallab.mobile.domain.IWorkordersRepository
import de.rogallab.mobile.domain.UiState
import de.rogallab.mobile.domain.entities.Person
import de.rogallab.mobile.domain.entities.Workorder
import de.rogallab.mobile.domain.mapping.toWorkorderDto
import de.rogallab.mobile.domain.utilities.logError
import de.rogallab.mobile.ui.people.PeopleViewModel
import de.rogallab.mobile.domain.entities.WorkState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Add Workorder to person and update Workorder in database
 */
class AddWorkorder @Inject constructor(
   private val _workordersRepository: IWorkordersRepository,
   dispatcher: CoroutineDispatcher,
   exceptionHandler: CoroutineExceptionHandler
) {
   private val _coroutineContext = SupervisorJob() + dispatcher + exceptionHandler
   private val _coroutineScope = CoroutineScope(_coroutineContext)

   operator fun invoke(person: Person, workorder: Workorder): State<UiState<Person>> {

      val uiState: MutableState<UiState<Person>> = mutableStateOf(value = UiState.Empty)

      try {
         workorder.state = WorkState.Assigned
         workorder.personId = person.id
         workorder.person = person
         person.workorders.add(workorder)

         val workorderDto = workorder.toWorkorderDto()

         _coroutineScope.launch {
            val result = _coroutineScope.async {
               return@async _workordersRepository.update(workorderDto)
            }.await()
            if (result) {
               uiState.value =  UiState.Success(person)
            } else {
               val message = "Error in UC.AddWorkorder()"
               logError(tag, message)
               uiState.value = UiState.Error(message, false, true)
            }
         }
      } catch (e: Exception) {
         val message = e.localizedMessage ?: e.stackTraceToString()
         logError(PeopleViewModel.tag, message)
         uiState.value = UiState.Error(message)
      }
      return uiState
   }

   companion object {       // 12345678901234567890123
      private const val tag = "ok>UC.UpdateWorkorder ."
   }

}