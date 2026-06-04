package com.csc3402.smartlibrarysystem.repository;
import com.csc3402.smartlibrarysystem.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>    {
}
