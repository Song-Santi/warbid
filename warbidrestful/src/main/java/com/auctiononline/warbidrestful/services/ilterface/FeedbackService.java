package com.auctiononline.warbidrestful.services.ilterface;

import com.auctiononline.warbidrestful.payload.response.GetAllResponse;
import com.auctiononline.warbidrestful.payload.response.MessageResponse;

public interface FeedbackService {
    GetAllResponse getAllFeedbackProduct(int page, int pageSize);
    GetAllResponse getAllFeedbackPost(int page, int pageSize);
    GetAllResponse getAllFeedbackContact(int page, int pageSize);

    GetAllResponse getFeedbackProductById(Long id);

    GetAllResponse getFeedbackPostById(Long id);

    GetAllResponse getFeedbackContactById(Long id);

    MessageResponse updateFeedback(Long id);

    MessageResponse deleteFeedback(Long id);
}
