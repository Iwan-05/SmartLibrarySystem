package com.csc3402.smartlibrarysystem.repository;
import com.csc3402.smartlibrarysystem.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer>    {
    @Query("SELECT SUM(r.star_score) FROM Rating r WHERE r.book.book_id = :bookId")
    Double findSumByBookId(@Param("bookId") Long bookId);

    @Query("SELECT COUNT(r) FROM Rating r WHERE r.book.book_id = :bookId")
    Long findCountByBookId(@Param("bookId") Long bookId);
}
