package com.jlima.bookstoremanager.providers.entity

import java.util.Date

abstract class AuditableEntity {
    protected abstract val createdAt: Date?
    protected abstract val updatedAt: Date?
    protected abstract val deletedAt: Date?
}
