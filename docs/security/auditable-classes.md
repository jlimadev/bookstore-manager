# Auditable classes

* [Tutorial of implementation](https://www.kindsonthegenius.com/auditing-in-spring-bootstep-by-step-tutorial/)

In order to track the changes in our classes, we can add auditable fields, in our classes, such as:

- `CreatedAt`, `CreatedBy`, `UpdatedAt`, `UpdatedBy`

To do it, we need to create an abstract class, to our entities implement it.

```kotlin
@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class AuditableEntity : BaseEntity() {
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    protected lateinit var createdBy: String
        private set

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    protected lateinit var createdAt: Date
        private set

    @LastModifiedBy
    @Column(name = "updated_by", updatable = true)
    protected lateinit var updatedBy: String
        private set

    @LastModifiedDate
    @Column(name = "updated_at", updatable = true)
    protected lateinit var updatedAt: Date
        private set
}
```

This abstract class implements a base entity, that contains the ID.

```kotlin
@MappedSuperclass
abstract class BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    val id: UUID? = null
}
```

We have to create the configuration to enable the JpaAuditing.

This Configuration is enough if we are working only with dates (`CreatedAt`, `UpdatedAt`):

```kotlin
@Configuration
@EnableJpaAuditing
class AuditConfig
```

If we are working with all options (`CreatedAt`, `CreatedBy`, `UpdatedAt`, `UpdatedBy`), we need some more
configurations:

- The first one to set `By` fields.

```kotlin
class CustomAuditAware : AuditorAware<String> {
    override fun getCurrentAuditor(): Optional<String> {
        return Optional.of("Jonathan").filter { it.isNotEmpty() }
        // here we can have integration with spring security to get the connected user
    }
}
```

- The configuration that enables JpaAuditing

```kotlin
@Configuration
@EnableJpaAuditing
class AuditConfig {
    @Bean
    fun auditorAware(): AuditorAware<String> {
        return CustomAuditAware()
    }
}
```

**Important**
Our database need all these fields:

```sql
ALTER TABLE domain.author
    ADD COLUMN created_by VARCHAR(150) NOT NULL,
    ADD COLUMN created_at TIMESTAMPTZ NOT NULL,
    ADD COLUMN updated_by VARCHAR(150) NOT NULL,
    ADD COLUMN updated_at TIMESTAMPTZ NOT NULL,
    ADD COLUMN is_active BOOLEAN NOT NULL DEFAULT TRUE; -- this is optional
```

Finally, this is an entity implementing this auditable class:

```kotlin
@Entity
@Table(schema = "domain", name = "author")
data class AuthorEntity(
    @Column(name = "name", length = 255)
    var name: String,

    @Column(name = "birth_date")
    @Temporal(TemporalType.TIMESTAMP)
    var birthDate: Date,

    @Column(name = "is_active")
    var isActive: Boolean = true,

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    val books: List<BookEntity>,
) : AuditableEntity()
```

## References

- [Baeldung Auditing JPA](https://www.baeldung.com/database-auditing-jpa)
- [Kindson Tutorial](https://www.kindsonthegenius.com/auditing-in-spring-bootstep-by-step-tutorial/)