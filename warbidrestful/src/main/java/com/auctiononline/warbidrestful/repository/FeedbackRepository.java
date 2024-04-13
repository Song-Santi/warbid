package com.auctiononline.warbidrestful.repository;

import com.auctiononline.warbidrestful.models.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    @Query("SELECT f FROM Feedback f WHERE f.allowDisplay = TRUE")
    List<Feedback> getAllFeedback();

    @Query("SELECT f FROM Feedback f WHERE f.allowDisplay = TRUE AND f.productId IS NOT NULL")
    List<Feedback> getAllFeedbackProduct();

    @Query("SELECT f FROM Feedback f WHERE f.allowDisplay = TRUE AND f.postId IS NOT NULL")
    List<Feedback> getAllFeedbackPost();

    @Query("SELECT f FROM Feedback f WHERE f.allowDisplay = TRUE AND f.contactName IS NOT NULL")
    List<Feedback> getAllFeedbackContact();

    @Query("SELECT f FROM Feedback f WHERE f.allowDisplay = TRUE AND f.id = :id")
    List<Feedback> getFeedbackById(Long id);

}
