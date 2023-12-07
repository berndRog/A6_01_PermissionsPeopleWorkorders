package de.rogallab.mobile.data.repositories

import de.rogallab.mobile.data.IPeopleDao
import de.rogallab.mobile.data.models.PersonDto
import de.rogallab.mobile.data.models.PersonDtoWithWorkorderDtos
import de.rogallab.mobile.data.models.WorkorderDto
import de.rogallab.mobile.domain.IPeopleRepository
import de.rogallab.mobile.domain.utilities.logDebug
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class PeopleRepositoryImpl @Inject constructor(
   private val _peopleDao: IPeopleDao,
   private val _dispatcher: CoroutineDispatcher,
   private val _exceptionHandler: CoroutineExceptionHandler
): IPeopleRepository {
   //12345678901234567890123
   val tag = "ok>PeopleRepositoryImpl"

   override fun selectAll(): Flow<List<PersonDto>> {
      // throw Exception("Error thrown in selectAll()")
      logDebug(tag, "suspend selectAll()")
      return _peopleDao.selectAll()
   }

   override suspend fun findById(id: UUID): PersonDto? =
      withContext(_dispatcher + _exceptionHandler) {
         val personDto = _peopleDao.selectById(id)
         logDebug(tag, "findById() ${personDto?.asString()}")
//       throw Exception("Error thrown in findById()")
         return@withContext personDto
      }

   override suspend fun count(): Int =
      withContext(_dispatcher + _exceptionHandler) {
         val records = _peopleDao.count()
         logDebug(tag, "count() $records")
//       throw Exception("Error thrown in count()")
         return@withContext records
      }

   override suspend fun add(personDto: PersonDto): Boolean =
      withContext(_dispatcher + _exceptionHandler) {
//       throw Exception("Error thrown in add()")
         _peopleDao.insert(personDto)
         logDebug(tag, "insert() ${personDto.asString()}")
         return@withContext true
      }

   override suspend fun addAll(peopleDtos: List<PersonDto>): Boolean =
      withContext(_dispatcher + _exceptionHandler) {
//       throw Exception("Error thrown in addAll()")
         _peopleDao.insertAll(peopleDtos)
         logDebug(tag, "addAll()")
         return@withContext true
      }

   override suspend fun update(personDto: PersonDto): Boolean =
      withContext(_dispatcher + _exceptionHandler) {
//       throw Exception("Error thrown in update()")
         _peopleDao.update(personDto)
         logDebug(tag, "update()")
         return@withContext true
      }

   override suspend fun remove(personDto: PersonDto): Boolean =
      withContext(_dispatcher + _exceptionHandler) {
//       throw Exception("Error thrown in remove()")
         _peopleDao.delete(personDto)
         logDebug(tag, "remove()")
         return@withContext true
      }

   override suspend fun selectByIdWithWorkorders(id: UUID): PersonDtoWithWorkorderDtos? =
      withContext(_dispatcher + _exceptionHandler) {
         val personDtoWithWorkorderDtos = _peopleDao.findbyIdWithWorkorders(id)
         logDebug(tag, "findByIdWithWorkorders() " +
            "${personDtoWithWorkorderDtos?.personDto?.asString()}")
//          throw Exception("Error thrown in findById()")
         return@withContext personDtoWithWorkorderDtos
      }

   override suspend fun findByIdWithWorkorders(id: UUID): Map<PersonDto, List<WorkorderDto>> =
      withContext(_dispatcher + _exceptionHandler) {
         val map = _peopleDao.loadPersonWithWorkorders(id)
         logDebug(tag, "findByIdWithWorkorders()")
         return@withContext map
      }
}
