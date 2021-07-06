# Auditable classes

* [Tutorial of implementation](https://www.kindsonthegenius.com/auditing-in-spring-bootstep-by-step-tutorial/)

In order to track the changes in our classes, we can add auditable fields, in our classes, such as:

- `CreatedAt`, `CreatedBy`, `UpdatedAt`, `UpdatedBy`

To do it, we need to create an abstract class, to our entities implement it.

```kotlin
@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class AuditableEntity {
    
    @CreatedBy
    protected lateinit var createdBy: String
        private set
    
    @CreatedDate
    protected lateinit var createdAt: Date
        private set
    
    @LastModifiedBy
    protected lateinit var updatedBy: String
        private set

    @LastModifiedDate
    protected lateinit var updatedAt: Date
        private set
}
```
We have to create the configuration to enable the JpaAuditing.

This Configuration is enough if we are working only with dates (`CreatedAt`, `UpdatedAt`):

```kotlin
@Configuration
@EnableJpaAuditing
class AuditConfig
```

If we are working with all options (`CreatedAt`, `CreatedBy`, `UpdatedAt`, `UpdatedBy`), we need some more configurations:

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
@Table(schema = "domain", name = "user")
data class UserEntity(
    @Id
    @GeneratedValue
    @Column(name = "id")
    val id: UUID? = null,

    @Column(name = "name")
    val name: String,

    @Column(name = "gender", length = 6)
    @Enumerated(EnumType.STRING)
    val gender: Gender,

    @Column(name = "birth_date")
    @Temporal(TemporalType.TIMESTAMP)
    val birthDate: Date,

    @Column(name = "email")
    val email: String,

    @Column(name = "password")
    val password: String,

    @Column(name = "role")
    val role: String,

    @Column(name = "is_active")
    val isActive: Boolean = true,

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val books: List<BookEntity>
) : AuditableEntity()

```