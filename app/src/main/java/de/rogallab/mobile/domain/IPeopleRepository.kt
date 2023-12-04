package de.rogallab.mobile.domain

import de.rogallab.mobile.data.models.PersonDto
import de.rogallab.mobile.data.models.PersonDtoWithWorkorderDtos
import de.rogallab.mobile.data.models.WorkorderDto
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface IPeopleRepository {
   fun selectAll(): Flow<List<PersonDto>>
   suspend fun findById(id: UUID): PersonDto?
   suspend fun count(): Int
   suspend fun add(personDto: PersonDto): Boolean
   suspend fun addAll(peopleDtos: List<PersonDto>): Boolean
   suspend fun update(personDto: PersonDto): Boolean
   suspend fun remove(personDto: PersonDto): Boolean

   suspend fun findbyIdWithWorkorders(id: UUID): PersonDtoWithWorkorderDtos?
   suspend fun loadPersonWithWorkorders(id:UUID): Map<PersonDto, List<WorkorderDto>>
}

