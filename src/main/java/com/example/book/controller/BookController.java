package com.example.book.controller;

import com.example.book.model.Book;
import com.example.book.model.BookForm;
import com.example.book.model.Category;
import com.example.book.service.book.IBookService;
import com.example.book.service.category.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@CrossOrigin("*")
@RequestMapping("/books")
public class BookController {
    @Autowired
    Environment environment;
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
    public ResponseEntity<Book> createBook(@ModelAttribute BookForm bookForm) {
        MultipartFile multipartFile = bookForm.getImage();
        String fileName = multipartFile.getOriginalFilename();
        String fileUpload = environment.getProperty("upload.path");
        try {
            FileCopyUtils.copy(multipartFile.getBytes(), new File(fileUpload + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Book book = new Book(bookForm.getName(), bookForm.getPrice(), bookForm.getAuthor(), fileName,
                bookForm.getCategory());
        bookService.save(book);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @ModelAttribute BookForm bookForm) {
        Optional<Book> bookOptional = bookService.findById(id);
        bookForm.setId(bookOptional.get().getId());
        MultipartFile multipartFile = bookForm.getImage();
        String fileName = multipartFile.getOriginalFilename();
        String fileUpload = environment.getProperty("upload.path");
        Book existBook = new Book(bookForm.getName(), bookForm.getPrice(), bookForm.getAuthor(), fileName,
                bookForm.getCategory());
        try {
            FileCopyUtils.copy(multipartFile.getBytes(), new File(fileUpload + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bookOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (existBook.getImage().equals("fileName.jpg")) {
            existBook.setImage(bookOptional.get().getImage());
        }
        bookService.save(existBook);
        return new ResponseEntity<>(existBook, HttpStatus.ACCEPTED);
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