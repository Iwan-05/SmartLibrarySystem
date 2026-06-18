package com.csc3402.smartlibrarysystem.repository;
import com.csc3402.smartlibrarysystem.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long>    {
    @Query("SELECT l FROM Loan l WHERE l.user_id = :userId AND l.return_date IS NULL")
    List<Loan> findActiveLoansByUserId(@Param("userId") String userId);

    @Query("SELECT l FROM Loan l WHERE l.user_id = :userId AND l.return_date IS NOT NULL")
    List<Loan> findPastLoansByUserId(@Param("userId") String userId);

    @Query("SELECT l FROM Loan l WHERE l.return_date IS NULL ORDER BY l.due_date ASC")
    List<Loan> findAllActiveLoansOrderByDueDate();

    @Query("SELECT l FROM Loan l WHERE l.return_date IS NOT NULL ORDER BY l.return_date DESC")
    List<Loan> findRecentlyReturnedLoans();

    @Query("SELECT l FROM Loan l ORDER BY l.borrow_date DESC")
    List<Loan> findAllOrderedByDate();
}
