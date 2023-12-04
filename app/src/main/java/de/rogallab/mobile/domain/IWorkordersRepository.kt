package de.rogallab.mobile.domain

import de.rogallab.mobile.data.models.PersonDto
import de.rogallab.mobile.data.models.WorkorderDto
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface IWorkordersRepository {

   fun readAll(): Flow<List<WorkorderDto>>
   suspend fun findById(id: UUID): WorkorderDto?
   suspend fun count(): Int

   suspend fun add(workorderDto: WorkorderDto): Boolean
   suspend fun addAll(workorderDtos: List<WorkorderDto>): Boolean
   suspend fun update(workorderDto: WorkorderDto): Boolean
   suspend fun remove(workorderDto: WorkorderDto): Boolean

   suspend fun loadWorkorderWithPerson(id: UUID): Map<WorkorderDto, PersonDto?>

}