package mate.academy.bookstoreapp.repository;

import mate.academy.bookstoreapp.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
