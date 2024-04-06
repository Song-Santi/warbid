package com.auctiononline.warbidrestful.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "image")
    private String image;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    @Column(name = "deleted")
    private Boolean deleted;

    public Post(String title, String description, String image){
        this.title = title;
        this.description = description;
        this.image = image;
    }

    public Post(Long id, String title, String description, String image, Boolean deleted){
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
        this.deleted = deleted;
    }
}
