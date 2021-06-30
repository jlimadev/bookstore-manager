package com.jlima.bookstoremanager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BookstoreManagerApplication

fun main(args: Array<String>) {
    runApplication<BookstoreManagerApplication>(*args)
}
