# Pagination, Sorting and Query

- Very Good [reference](https://reflectoring.io/spring-boot-paging/)

Usually we add pagination on FindAll methods. In order to add pagination to this project we need to modify our getAll
methods.

Our controller will be implemented this way:

- The default page will be 0 (zero), which means page one.
- The size (amount of data to be shown) is 10
- The default sorting is Name.
- call it simply: GET `baseUrl/authors`. This will use the default.

If we want to override these values, our requests will be something similar to it:

- GET `baseUrl/authors?page=0&size=5&sort=id,desc&sort=birthDate,asc`

```kotlin
@GetMapping
override fun findAll(
    @PageableDefault(page = 0, size = 10)
    @SortDefault.SortDefaults(
        SortDefault(sort = ["name"], direction = Sort.Direction.ASC)
    ) pageable: Pageable
): ResponseEntity<PaginationResponse<AuthorDTO>> {
    return ResponseEntity(authorService.findAll(pageable), HttpStatus.OK)
}
```

Finally, our controller will this way:

- Will receive the pageable
- Call repository with this data and return a Page.
- We can verify if have any data and then return

```kotlin
override fun findAll(pageable: Pageable): PaginationResponse<AuthorDTO> {
    val databaseResult = authorRepository.findAll(pageable)
    val authorsList = databaseResult.toList()

    if (authorsList.isEmpty()) {
        throw BusinessEmptyResponseException(AvailableEntities.AUTHOR)
    }

    return PaginationResponse(
        totalPages = databaseResult.totalPages,
        totalItems = databaseResult.totalElements.toInt(),
        currentPage = databaseResult.number,
        currentItems = databaseResult.numberOfElements,
        data = authorsList.map { it.toDTO() }
    )
}
```

If we have multiple controllers, we can create a function to transform the Page to Pagination Response and, apply DRY
principle as well.

- First: This is our PaginationResponse

```kotlin
data class PaginationResponse<T>(
    val totalPages: Int,
    val totalItems: Int,
    val currentPage: Int,
    val currentItems: Int,
    val data: List<T>
)
```

- Second: This is a possible solution to call/add pagination response

```kotlin
fun <T> Page<*>.toPaginationResponse(data: List<T>): PaginationResponse<T> {
    return PaginationResponse(
        totalPages = this.totalPages,
        totalItems = this.totalElements.toInt(),
        currentPage = this.number,
        currentItems = this.numberOfElements,
        data = data
    )
}
```

Then our controller can return just this line

```kotlin
return databaseResult.toPaginationResponse(authorsList.map { it.toDTO() })
```