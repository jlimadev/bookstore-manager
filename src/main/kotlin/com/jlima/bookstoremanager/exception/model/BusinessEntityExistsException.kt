package com.jlima.bookstoremanager.exception.model

import javax.persistence.EntityExistsException

data class BusinessEntityExistsException(val entity: AvailableEntities, val comparable: String) :
    EntityExistsException("Entity $entity already exists: $comparable")
