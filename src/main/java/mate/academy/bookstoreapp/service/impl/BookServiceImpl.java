package mate.academy.bookstoreapp.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreapp.dto.BookDto;
import mate.academy.bookstoreapp.dto.CreateBookRequestDto;
import mate.academy.bookstoreapp.mapper.BookMapper;
import mate.academy.bookstoreapp.model.Book;
import mate.academy.bookstoreapp.repository.BookRepository;
import mate.academy.bookstoreapp.service.BookService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public BookDto findById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Can`t find the book with id" + id));
    }

    @Override
    public List<BookDto> findAll() {
        return bookMapper.toDtoList(bookRepository.findAll());
    }
}
