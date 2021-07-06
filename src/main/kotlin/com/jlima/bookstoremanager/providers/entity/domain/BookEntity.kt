package com.jlima.bookstoremanager.providers.entity.domain

import com.jlima.bookstoremanager.providers.entity.AuditableEntity
import java.util.Date
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "book", schema = "domain")
data class BookEntity(
    @Column(name = "name")
    var name: String,

    @Column(name = "isbn", length = 17)
    var isbn: String,

    @Column(name = "pages")
    var pages: Int,

    @Column(name = "chapters")
    var chapters: Int,

    @Column(name = "release_date")
    var releaseDate: Date,

    @Column(name = "is_active")
    var isActive: Boolean = true,

    @ManyToOne(cascade = [CascadeType.MERGE])
    @JoinColumn(name = "author_id")
    var author: AuthorEntity,

    @ManyToOne(cascade = [CascadeType.MERGE])
    @JoinColumn(name = "publisher_id")
    var publisher: PublisherEntity,

    @ManyToOne(cascade = [CascadeType.MERGE])
    @JoinColumn(name = "user_id")
    var user: UserEntity
) : AuditableEntity()
