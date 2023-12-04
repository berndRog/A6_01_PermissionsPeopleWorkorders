package de.rogallab.mobile.domain
import de.rogallab.mobile.domain.usecases.people.AddWorkorder
import de.rogallab.mobile.domain.usecases.people.ReadPeople
import de.rogallab.mobile.domain.usecases.people.RemoveWorkorder

interface IPeopleUseCases {
   val readPeople: ReadPeople
   val addWorkorder: AddWorkorder
   val removeWorkorder: RemoveWorkorder
}