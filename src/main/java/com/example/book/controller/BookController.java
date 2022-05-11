package com.example.book.controller;

import com.example.book.model.Book;
import com.example.book.model.Category;
import com.example.book.service.book.IBookService;
import com.example.book.service.category.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@CrossOrigin("*")
@RequestMapping("/books")
public class BookController {
    @Autowired
    private IBookService bookService;
    @Autowired
    private ICategoryService categoryService;

    @ModelAttribute("categories")
    public Iterable<Category> listAllCategory() {
        return categoryService.findAll();
    }

    @GetMapping("")
    public ResponseEntity<Iterable<Book>> listAllBooks() {
        Iterable<Book> books = bookService.findAll();
        List<Book> bookList = (List<Book>) books;
        return new ResponseEntity<>(bookList, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        bookService.save(book);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        Optional<Book> bookOptional = bookService.findById(id);
        if (bookOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        book.setId(bookOptional.get().getId());
        bookService.save(book);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Book> deleteBook(@PathVariable Long id) {
        Optional<Book> bookOptional = bookService.findById(id);
        if (bookOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        bookService.remove(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/category")
    public ResponseEntity<Iterable<Category>> findAllCategory() {
        Iterable<Category> categories = categoryService.findAll();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> findBookById(@PathVariable Long id) {
        Optional<Book> bookOptional = bookService.findById(id);
        if (bookOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(bookOptional.get(), HttpStatus.OK);
    }
}