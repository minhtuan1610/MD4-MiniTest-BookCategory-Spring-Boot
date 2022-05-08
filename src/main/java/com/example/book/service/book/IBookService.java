package com.example.book.service.book;

import com.example.book.model.Book;
import com.example.book.model.Category;
import com.example.book.service.IGeneralService;

public interface IBookService extends IGeneralService<Book> {
    Iterable<Book> findAllByCategory(Category category);
}
