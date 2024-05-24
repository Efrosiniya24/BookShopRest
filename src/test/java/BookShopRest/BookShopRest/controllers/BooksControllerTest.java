package BookShopRest.BookShopRest.controllers;

import BookShopRest.BookShopRest.Model.Books;
import BookShopRest.BookShopRest.repositories.BooksRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BooksControllerTest {

    @InjectMocks
    private BooksController booksController;

    @Mock
    private BooksRepository booksRepository;

    @Mock
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(booksController).build();
    }

    @Test
    void allBooks() throws Exception {
        when(booksRepository.findAll()).thenReturn(Collections.singletonList(new Books()));
        when(objectMapper.writeValueAsString(any())).thenReturn("[]");

        mockMvc.perform(get("/BookShop/allBooks"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));

        verify(booksRepository, times(1)).findAll();
        verify(objectMapper, times(1)).writeValueAsString(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addBook() throws Exception {
        Books book = new Books();
        when(booksRepository.save(any())).thenReturn(book);

        mockMvc.perform(post("/BookShop/addBook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());

        verify(booksRepository, times(1)).save(any());
    }


    @Test
    void getBook() throws Exception {
        Books book = new Books();
        when(booksRepository.findById(anyLong())).thenReturn(Optional.of(book));

        mockMvc.perform(get("/BookShop/book/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").doesNotExist());

        verify(booksRepository, times(1)).findById(anyLong());
    }

    @Test
    void deleteBook() throws Exception {
        doNothing().when(booksRepository).deleteById(anyLong());

        mockMvc.perform(delete("/BookShop/book/1"))
                .andExpect(status().isOk());

        verify(booksRepository, times(1)).deleteById(anyLong());
    }

}
