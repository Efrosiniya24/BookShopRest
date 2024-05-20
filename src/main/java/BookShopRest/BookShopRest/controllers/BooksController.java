package BookShopRest.BookShopRest.controllers;


import BookShopRest.BookShopRest.Model.Books;
import BookShopRest.BookShopRest.repositories.BooksRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
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

//    @PostMapping("/addBook")
//    public void addBook(@RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2,
//                        @RequestParam("file3") MultipartFile file3, Books books, Principal principal){
//        log.info("New row: " + booksService.saveProduct(principal, books, file1, file2, file3));
//    }

    @GetMapping("/book/{id}")
    public Books getBook(@PathVariable Long id){
        return booksRepository.findById(id).orElseThrow();
    }

    @DeleteMapping("/book/{id}")
    public void deleteBook(@PathVariable Long id){
        booksRepository.deleteById(id);
    }
}
