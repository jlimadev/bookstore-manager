package com.jlima.bookstoremanager.exception.model

import javax.persistence.EntityNotFoundException

class BusinessEntityNotFoundException(entity: AvailableEntities, identifier: String) :
    EntityNotFoundException("$entity $identifier not found.")
