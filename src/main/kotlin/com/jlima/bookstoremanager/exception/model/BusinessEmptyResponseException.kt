package com.jlima.bookstoremanager.exception.model

import javax.persistence.EntityNotFoundException

class BusinessEmptyResponseException(entity: AvailableEntities) :
    EntityNotFoundException("No $entity(s) found.")
