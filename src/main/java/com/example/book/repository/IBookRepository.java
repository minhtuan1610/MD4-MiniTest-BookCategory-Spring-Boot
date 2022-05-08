package com.example.book.repository;

import com.example.book.model.Book;
import com.example.book.model.Category;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBookRepository extends PagingAndSortingRepository<Book, Long> {
    Iterable<Book> findAllByCategory(Category category);
}
