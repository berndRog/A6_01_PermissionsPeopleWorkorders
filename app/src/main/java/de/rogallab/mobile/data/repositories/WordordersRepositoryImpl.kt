package de.rogallab.mobile.data.repositories

import de.rogallab.mobile.data.IWorkordersDao
import de.rogallab.mobile.data.models.PersonDto
import de.rogallab.mobile.data.models.WorkorderDto
import de.rogallab.mobile.domain.IWorkordersRepository
import de.rogallab.mobile.domain.utilities.logDebug
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class WordordersRepositoryImpl @Inject constructor(
   private val _workordersDao: IWorkordersDao,
   private val _dispatcher: CoroutineDispatcher,
   private val _exceptionHandler: CoroutineExceptionHandler
) : IWorkordersRepository {

            //12345678901234567890123
   val tag = "ok>WorkorderReposImpl ."

   override fun readAll(): Flow<List<WorkorderDto>> {
      // throw Exception("Error thrown in selectAll()")
      logDebug(tag,"selectAll()")
      return _workordersDao.selectAll()
   }

   override suspend fun findById(id: UUID): WorkorderDto? =
      withContext(_dispatcher+_exceptionHandler) {
         val workorderDto = _workordersDao.selectById(id)
         logDebug(tag,"findById() ${workorderDto?.asString()}")
//       throw Exception("Error thrown in findById()")
         return@withContext workorderDto
      }

   override suspend fun count(): Int =
      withContext(_dispatcher+_exceptionHandler) {
         val records = _workordersDao.count()
         logDebug(tag,"count() $records")
//       throw Exception("Error thrown in count()")
         return@withContext records
   }

   override suspend fun add(workorderDto: WorkorderDto): Boolean =
      withContext(_dispatcher + _exceptionHandler) {
//       throw Exception("Error thrown in add()")
         _workordersDao.insert(workorderDto)
         logDebug(tag, "insert() ${workorderDto.asString()}")
         return@withContext true
      }

   override suspend fun addAll(workorderDtos: List<WorkorderDto>): Boolean =
      withContext(_dispatcher + _exceptionHandler) {
//       throw Exception("Error thrown in addAll()")
         _workordersDao.insertAll(workorderDtos)
         logDebug(tag, "addAll()")
         return@withContext true
      }

   override suspend fun update(workorderDto: WorkorderDto): Boolean =
      withContext(_dispatcher + _exceptionHandler) {
//       throw Exception("Error thrown in update()")
         _workordersDao.update(workorderDto)
         logDebug(tag, "update()")
         return@withContext true
      }

   override suspend fun remove(workorderDto: WorkorderDto): Boolean =
      withContext(_dispatcher + _exceptionHandler) {
//       throw Exception("Error thrown in remove()")
         _workordersDao.delete(workorderDto)
         logDebug(tag, "remove()")
         return@withContext true
      }

   override suspend fun findByIdWithPerson(id: UUID): Map<WorkorderDto, PersonDto?> =
      withContext(_dispatcher + _exceptionHandler) {
         val map = _workordersDao.findByIdWithPerson(id)
         logDebug(tag, ",loadWorkorderWithPerson()")
         return@withContext map
      }
}