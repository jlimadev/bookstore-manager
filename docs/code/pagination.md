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
