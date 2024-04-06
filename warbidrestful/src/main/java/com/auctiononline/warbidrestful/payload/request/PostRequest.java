package com.auctiononline.warbidrestful.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
    private Long id;

    private String title;

    private String description;

    private String image;

    public PostRequest(String title, String description, String image){
        this.title = title;
        this.description = description;
        this.image = image;
    }
}
