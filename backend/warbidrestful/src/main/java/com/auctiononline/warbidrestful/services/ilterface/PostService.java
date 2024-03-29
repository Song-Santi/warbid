package com.auctiononline.warbidrestful.services.ilterface;

import com.auctiononline.warbidrestful.payload.request.PostRequest;
import com.auctiononline.warbidrestful.payload.response.GetAllResponse;
import com.auctiononline.warbidrestful.payload.response.MessageResponse;

public interface PostService {
    GetAllResponse getAllPostLabel();

    GetAllResponse getAllPost();

    GetAllResponse getPostById(Long id);

    MessageResponse addPost(PostRequest postRequest);

    MessageResponse deletePost(Long id);

    MessageResponse updatePost(PostRequest postRequest);

}
