package com.auctiononline.warbidrestful.services.implementation;

import com.auctiononline.warbidrestful.exception.AppException;
import com.auctiononline.warbidrestful.models.*;
import com.auctiononline.warbidrestful.payload.dto.FeedbackContactDTO;
import com.auctiononline.warbidrestful.payload.dto.FeedbackPostDTO;
import com.auctiononline.warbidrestful.payload.dto.FeedbackProductDTO;
import com.auctiononline.warbidrestful.payload.dto.Paging;
import com.auctiononline.warbidrestful.payload.response.GetAllResponse;
import com.auctiononline.warbidrestful.payload.response.MessageResponse;
import com.auctiononline.warbidrestful.repository.FeedbackRepository;
import com.auctiononline.warbidrestful.repository.PostRepository;
import com.auctiononline.warbidrestful.repository.ProductRepository;
import com.auctiononline.warbidrestful.repository.UserRepository;
import com.auctiononline.warbidrestful.services.ilterface.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public GetAllResponse getAllFeedbackProduct(int page, int pageSize){
        try{
            List<Feedback> feedbacks = feedbackRepository.getAllFeedbackProduct();
            if(feedbacks.isEmpty()){
                return new GetAllResponse(404,HttpStatus.NOT_FOUND.toString(),"There are no comment for the product!",feedbacks);
            }
            List<FeedbackProductDTO> feedbackProductDTOs = new ArrayList<>();
            for(Feedback feedback : feedbacks){
                FeedbackProductDTO feedbackProductDTO = new FeedbackProductDTO();
                feedbackProductDTO.setId(feedback.getId());
                feedbackProductDTO.setProductId(feedback.getProductId());

                List<Product> products = productRepository.getAllProductActiveById(feedback.getProductId());
                if(products.isEmpty()){
                    continue;
                }
                Product product = products.get(0);
                feedbackProductDTO.setProductTitle(product.getTitle());
                feedbackProductDTO.setContent(feedback.getContent());
                feedbackProductDTO.setUpdateTime(feedback.getUpdated_time());
                List<User> users = userRepository.findByIdActive(feedback.getUserId());
                if(users.isEmpty()){
                   feedbackProductDTO.setUserName(null);
                   feedbackProductDTO.setEmail(null);
                    feedbackProductDTOs.add(feedbackProductDTO);
                    continue;
                }
                User user = users.get(0);
                feedbackProductDTO.setUserName(user.getUsername());
                feedbackProductDTO.setEmail(user.getEmail());
                feedbackProductDTOs.add(feedbackProductDTO);
            }

            int totalItems = feedbackProductDTOs.size();
            int totalPages = (int) Math.ceil((double) totalItems / pageSize);
            int startIndex = (page - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, totalItems);

            List<FeedbackProductDTO> feedbackProductDTOsOnPage = new ArrayList<>(feedbackProductDTOs.subList(startIndex, endIndex));

            Paging paging = new Paging(page, totalPages, pageSize, totalItems);

            return new GetAllResponse(200,HttpStatus.OK.toString(),"Get all comments for the product!",paging, feedbackProductDTOsOnPage);
        }catch (AppException ex){
            return new GetAllResponse(500, ex.getHttpStatus().toString(), ex.getMessage(), null);
        }
    }

    @Override
    public GetAllResponse getAllFeedbackPost(int page, int pageSize){
        try{
            List<Feedback> feedbacks = feedbackRepository.getAllFeedbackPost();
            if(feedbacks.isEmpty()){
                return new GetAllResponse(404,HttpStatus.NOT_FOUND.toString(),"There are no comment for the post!",feedbacks);
            }
            List<FeedbackPostDTO> feedbackPostDTOs = new ArrayList<>();
            for(Feedback feedback : feedbacks){
                FeedbackPostDTO feedbackPostDTO = new FeedbackPostDTO();
                feedbackPostDTO.setId(feedback.getId());
                feedbackPostDTO.setPostId(feedback.getPostId());

                List<Post> posts = postRepository.findPostActiveById(feedback.getPostId());
                if(posts.isEmpty()){
                    continue;
                }
                Post post = posts.get(0);
                feedbackPostDTO.setPostTitle(post.getTitle());
                feedbackPostDTO.setContent(feedback.getContent());
                feedbackPostDTO.setUpdateTime(feedback.getUpdated_time());
                List<User> users = userRepository.findByIdActive(feedback.getUserId());
                if(users.isEmpty()){
                    feedbackPostDTO.setUserName(null);
                    feedbackPostDTO.setEmail(null);
                    feedbackPostDTOs.add(feedbackPostDTO);
                    continue;
                }
                User user = users.get(0);
                feedbackPostDTO.setUserName(user.getUsername());
                feedbackPostDTO.setEmail(user.getEmail());
                feedbackPostDTOs.add(feedbackPostDTO);
            }

            int totalItems = feedbackPostDTOs.size();
            int totalPages = (int) Math.ceil((double) totalItems / pageSize);
            int startIndex = (page - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, totalItems);

            List<FeedbackPostDTO> feedbackPostDTOsOnPage = new ArrayList<>(feedbackPostDTOs.subList(startIndex, endIndex));

            Paging paging = new Paging(page, totalPages, pageSize, totalItems);

            return new GetAllResponse(200,HttpStatus.OK.toString(),"Get all comments for the post!",paging, feedbackPostDTOsOnPage);
        }catch (AppException ex){
            return new GetAllResponse(500, ex.getHttpStatus().toString(), ex.getMessage(), null);
        }
    }
    @Override
    public GetAllResponse getAllFeedbackContact(int page, int pageSize){
        try{
            List<Feedback> feedbacks = feedbackRepository.getAllFeedbackContact();
            if(feedbacks.isEmpty()){
                return new GetAllResponse(404,HttpStatus.NOT_FOUND.toString(),"There are no contact!",feedbacks);
            }
            List<FeedbackContactDTO> feedbackContactDTOs = new ArrayList<>();
            for(Feedback feedback : feedbacks){
                FeedbackContactDTO feedbackContactDTO = new FeedbackContactDTO();
                feedbackContactDTO.setId(feedback.getId());
                feedbackContactDTO.setFullName(feedback.getContactName());
                feedbackContactDTO.setEmail(feedback.getEmail());
                feedbackContactDTO.setTitle(feedback.getTitle());
                feedbackContactDTO.setContent(feedback.getContent());
                feedbackContactDTO.setUpdateTime(feedback.getUpdated_time());

                feedbackContactDTOs.add(feedbackContactDTO);
            }

            int totalItems = feedbackContactDTOs.size();
            int totalPages = (int) Math.ceil((double) totalItems / pageSize);
            int startIndex = (page - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, totalItems);

            List<FeedbackContactDTO> feedbackContactDTOsOnPage = new ArrayList<>(feedbackContactDTOs.subList(startIndex, endIndex));

            Paging paging = new Paging(page, totalPages, pageSize, totalItems);

            return new GetAllResponse(200,HttpStatus.OK.toString(),"Get all contact successful!",paging, feedbackContactDTOsOnPage);
        }catch (AppException ex){
            return new GetAllResponse(500, ex.getHttpStatus().toString(), ex.getMessage(), null);
        }
    }

    @Override
    public GetAllResponse getFeedbackProductById(Long id){
        try{
            List<Feedback> feedbacks = feedbackRepository.getFeedbackById(id);
            if(feedbacks.isEmpty()){
                return new GetAllResponse(404,HttpStatus.NOT_FOUND.toString(),"There are no comment for the product by id!", null);
            }
            Feedback feedback = feedbacks.get(0);
            FeedbackProductDTO feedbackProductDTO = new FeedbackProductDTO();
            feedbackProductDTO.setId(feedback.getId());
            feedbackProductDTO.setProductId(feedback.getProductId());
            feedbackProductDTO.setContent(feedback.getContent());
            feedbackProductDTO.setUpdateTime(feedback.getUpdated_time());
            List<User> users = userRepository.findByIdActive(feedback.getUserId());
            if(users.isEmpty()){
                feedbackProductDTO.setUserName(null);
                feedbackProductDTO.setEmail(null);
            }
            User user = users.get(0);
            feedbackProductDTO.setUserName(user.getUsername());
            feedbackProductDTO.setEmail(user.getEmail());
            List<Product> products = productRepository.getAllProductActiveById(feedback.getProductId());
            if(products.isEmpty()){
                feedbackProductDTO.setProductTitle(null);
            }
            Product product = products.get(0);
            feedbackProductDTO.setProductTitle(product.getTitle());

            List<FeedbackProductDTO> feedbackProductDTOs = new ArrayList<>();
            feedbackProductDTOs.add(feedbackProductDTO);

            return new GetAllResponse(200,HttpStatus.OK.toString(),"Get comment for the product by id successful!", feedbackProductDTOs);
        }catch (AppException ex){
            return new GetAllResponse(500, ex.getHttpStatus().toString(), ex.getMessage(), null);
        }
    }

    @Override
    public GetAllResponse getFeedbackPostById(Long id){
        try{
            List<Feedback> feedbacks = feedbackRepository.getFeedbackById(id);
            if(feedbacks.isEmpty()){
                return new GetAllResponse(404,HttpStatus.NOT_FOUND.toString(),"There are no comment for the post by id!", null);
            }
            Feedback feedback = feedbacks.get(0);
            FeedbackPostDTO feedbackPostDTO = new FeedbackPostDTO();
            feedbackPostDTO.setId(feedback.getId());
            feedbackPostDTO.setPostId(feedback.getPostId());
            feedbackPostDTO.setContent(feedback.getContent());
            feedbackPostDTO.setUpdateTime(feedback.getUpdated_time());
            List<User> users = userRepository.findByIdActive(feedback.getUserId());
            if(users.isEmpty()){
                feedbackPostDTO.setUserName(null);
                feedbackPostDTO.setEmail(null);
            }
            User user = users.get(0);
            feedbackPostDTO.setUserName(user.getUsername());
            feedbackPostDTO.setEmail(user.getEmail());
            List<Post> posts = postRepository.findPostActiveById(feedback.getPostId());
            if(posts.isEmpty()){
                feedbackPostDTO.setPostTitle(null);
            }
            Post post = posts.get(0);
            feedbackPostDTO.setPostTitle(post.getTitle());

            List<FeedbackPostDTO> feedbackProductDTOs = new ArrayList<>();
            feedbackProductDTOs.add(feedbackPostDTO);

            return new GetAllResponse(200,HttpStatus.OK.toString(),"Get comment for the post by id successful!", feedbackProductDTOs);
        }catch (AppException ex){
            return new GetAllResponse(500, ex.getHttpStatus().toString(), ex.getMessage(), null);
        }
    }

    @Override
    public GetAllResponse getFeedbackContactById(Long id){
        try{
            List<Feedback> feedbacks = feedbackRepository.getFeedbackById(id);
            if(feedbacks.isEmpty()){
                return new GetAllResponse(404, HttpStatus.NOT_FOUND.toString(),"There are no contact by id!",null);
            }
            Feedback feedback = feedbacks.get(0);
            FeedbackContactDTO feedbackContactDTO = new FeedbackContactDTO(feedback.getId(),
                                                            feedback.getContactName(),
                                                            feedback.getEmail(),
                                                            feedback.getTitle(),
                                                            feedback.getContent(),
                                                            feedback.getUpdated_time()
                                                             );
            List<FeedbackContactDTO> feedbackContactDTOs = new ArrayList<>();
            feedbackContactDTOs.add(feedbackContactDTO);
            return new GetAllResponse(200,HttpStatus.OK.toString(),"Get contact by id successful!", feedbackContactDTOs);
        }catch (AppException ex){
            return new GetAllResponse(500, ex.getHttpStatus().toString(), ex.getMessage(), null);
        }
    }

    @Override
    public MessageResponse updateFeedback(Long id){
        try{
            return null;
        }catch (AppException ex){
            return new MessageResponse(500, ex.getHttpStatus(), ex.getMessage(), null);
        }
    }

    @Override
    public MessageResponse deleteFeedback(Long id){
        try{
            return null;
        }catch (AppException ex){
            return new MessageResponse(500, ex.getHttpStatus(), ex.getMessage(), null);
        }
    }

    //private createFeedbackFromRequest()

}
