package de.rogallab.mobile.domain.mapping

import de.rogallab.mobile.data.models.AddressDto
import de.rogallab.mobile.data.models.PersonDto
import de.rogallab.mobile.data.models.PersonDtoWithWorkorderDtos
import de.rogallab.mobile.data.models.WorkorderDto
import de.rogallab.mobile.domain.entities.Address
import de.rogallab.mobile.domain.entities.Person
import de.rogallab.mobile.domain.entities.Workorder
import de.rogallab.mobile.domain.utilities.toZonedDateTime
import de.rogallab.mobile.domain.utilities.toZuluString
import java.time.Duration

fun AddressDto.toAddress(): Address = Address(
   street = this.street,
   number = this.number,
   postal = this.postal,
   city = this.city
)
fun Address.toAddressDto(): AddressDto = AddressDto(
   street = this.street,
   number = this.number,
   postal = this.postal,
   city = this.city
)

fun PersonDto.toPerson(): Person = Person(
   firstName = this.firstName,
   lastName = this.lastName,
   email = this.email,
   phone = this.phone,
   imagePath = this.imagePath,
   id = this.id,
   workorders = mutableListOf(),
   address = this.address?.toAddress()
)

fun List<PersonDto>.toPerson(): List<Person> {
   val people = mutableListOf<Person>()
   this.forEach { personDto ->
      people.add(personDto.toPerson())
   }
   return people
}

fun Person.toPersonDto(): PersonDto = PersonDto(
   firstName = this.firstName,
   lastName = this.lastName,
   email = this.email,
   phone = this.phone,
   imagePath = this.imagePath,
   id = this.id,
   address = this.address?.toAddressDto()
)

fun List<Person>.toPersonDto(): List<PersonDto> {
   val peopleDto = mutableListOf<PersonDto>()
   this.forEach { person ->
      peopleDto.add(person.toPersonDto())
   }
   return peopleDto
}

fun WorkorderDto.toWorkorder(): Workorder = Workorder(
   title = this.title,
   description = this.description,
   created = toZonedDateTime(this.created),
   started = toZonedDateTime(this.started),
   completed = toZonedDateTime(this.completed),
   state = this.state,
   duration = Duration.ofMillis(this.duration),
   remark = this.remark,
   id = this.id,
   person = null,
   personId = this.personId,
   address = this.address?.toAddress()
)

fun List<WorkorderDto>.toWorkorder(): List<Workorder> {
   val workorders = mutableListOf<Workorder>()
   this.forEach { workorderDto ->
      workorders.add(workorderDto.toWorkorder())
   }
   return workorders
}

fun Workorder.toWorkorderDto(): WorkorderDto = WorkorderDto(
   title = this.title,
   description = this.description,
   created = toZuluString(this.created),
   started = toZuluString(this.started),
   completed = toZuluString(this.completed),
   state = this.state,
   duration = this.duration.toMillis(),
   remark = this.remark,
   id = this.id,
   personId = this.personId,
   address = this.address?.toAddressDto()
)

fun List<Workorder>.toWorkorderDto(): List<WorkorderDto> {
   val workorderDtos = mutableListOf<WorkorderDto>()
   this.forEach { workorder ->
      workorderDtos.add(workorder.toWorkorderDto())
   }
   return workorderDtos
}


fun PersonDtoWithWorkorderDtos.toPerson(): Person {
   val person = this.personDto.toPerson()
   person.workorders.addAll(this.workorderDtos.toWorkorder())
   return person
}

