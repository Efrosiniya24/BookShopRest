package BookShopRest.BookShopRest.controllers;


import BookShopRest.BookShopRest.Model.Books;
import BookShopRest.BookShopRest.repositories.BooksRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/BookShop")
public class BooksController {

    public final ObjectMapper objectMapper;
    public final BooksRepository booksRepository;

    @GetMapping("/allBooks")
    public String allBooks() throws JsonProcessingException {
        List<Books> allBooks = booksRepository.findAll();
        return objectMapper.writeValueAsString(allBooks);
    }

    @PostMapping("/addBook")
    public void addBook(@RequestBody Books book){
        log.info("New row: " + booksRepository.save(book));
    }

    @GetMapping("/book/{id}")
    public Books getBook(@PathVariable Long id){
        return booksRepository.findById(id).orElseThrow();
    }

    @DeleteMapping("/book/{id}")
    public void deleteBook(@PathVariable Long id){
        booksRepository.deleteById(id);
    }
}
