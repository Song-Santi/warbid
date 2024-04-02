package com.auctiononline.warbidrestful.services.implementation;

import com.auctiononline.warbidrestful.exception.AppException;
import com.auctiononline.warbidrestful.models.Post;
import com.auctiononline.warbidrestful.payload.dto.PostLabelDTO;
import com.auctiononline.warbidrestful.payload.request.PostRequest;
import com.auctiononline.warbidrestful.payload.response.GetAllResponse;
import com.auctiononline.warbidrestful.payload.response.MessageResponse;
import com.auctiononline.warbidrestful.repository.PostRepository;
import com.auctiononline.warbidrestful.services.ilterface.PostService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;

    //mapping dto vào entity
    private PostLabelDTO toDTO(Post entity){
        PostLabelDTO dto = new PostLabelDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    @Override
    public GetAllResponse getAllPostLabel(){
        try {
            List<PostLabelDTO> postLabelList = postRepository.findAllActivePosts()
                    .stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());

            return new GetAllResponse(200, "OK", "Get all post label", postLabelList);
        } catch (AppException ex) {
            return new GetAllResponse(500, ex.getHttpStatus().toString(), ex.getMessage(), null);
        }

    }

    @Override
    public GetAllResponse getAllPost(){
        try{
            List<Post> postList = postRepository.findAll();

            return new GetAllResponse(200,"OK", "Get all post", postList);
        }catch(AppException ex){
            return  new GetAllResponse(500, ex.getHttpStatus().toString(), ex.getMessage(), null);
        }
    }

    @Override
    public  GetAllResponse getPostById(Long id){
        try{
            List<Post> posts = postRepository.findPostActiveById(id);

            if(posts.isEmpty()){
                return new GetAllResponse(404, HttpStatus.NOT_FOUND.toString(), "Id not found!",null);
            }

            return new GetAllResponse(200, HttpStatus.OK.toString(), "Get post by id successfull!", posts);

        }catch(AppException ex){
            return new GetAllResponse(500, ex.getHttpStatus().toString(), ex.getMessage(), null);
        }
    }

    @Override
    public MessageResponse addPost(PostRequest postRequest){
        try{
            Post post = createPostFromRequest(postRequest);
            post.setDeleted(false);
            postRepository.save(post);
            return new MessageResponse(200, HttpStatus.OK,"Add post successful!");
        }catch (AppException ex){
            return new MessageResponse(500, ex.getHttpStatus(), ex.getMessage());
        }
    }

    @Override
    public MessageResponse updatePost(PostRequest postRequest){
        try{
            List<Post> posts = postRepository.findPostActiveById(postRequest.getId());
            if(posts.isEmpty()){
                return new MessageResponse(404, HttpStatus.NOT_FOUND,"Id not found");
            }

            Post post = createPostFromRequest(postRequest);
            post.setId(postRequest.getId());
            post.setDeleted(false);
            postRepository.save(post);

            return new MessageResponse(200, HttpStatus.OK,"Update post successful!");
        }catch(AppException ex){
            return new MessageResponse(500, ex.getHttpStatus(),ex.getMessage());
        }
    }

    @Override
    public MessageResponse deletePost(Long id){
        try{
            List<Post> posts = postRepository.findPostActiveById(id);
            if(posts.isEmpty()){
                return new MessageResponse(404, HttpStatus.NOT_FOUND, "Id not found");
            }

            Post post = posts.get(0);
            post.setDeleted(true);
            postRepository.save(post);
            return new MessageResponse(200, HttpStatus.OK, "Delete post successful!");

        }catch(AppException ex){
            return new MessageResponse(500, ex.getHttpStatus(), ex.getMessage());
        }
    }

    private Post createPostFromRequest(PostRequest postRequest) {
        return new Post(
                postRequest.getTitle(),
                postRequest.getDescription(),
                postRequest.getImage()
        );
    }
}
