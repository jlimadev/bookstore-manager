package com.jlima.bookstoremanager.exception.model

import java.util.UUID
import javax.persistence.EntityNotFoundException

class BusinessEntityNotFoundException(entity: AvailableEntities, id: UUID) :
    EntityNotFoundException("$entity with id $id not found. Please try again.")
