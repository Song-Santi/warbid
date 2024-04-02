package com.auctiononline.warbidrestful.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
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

//    @ManyToOne
//    @JoinColumn(name = "category_id", referencedColumnName = "id")
//    private Category category;
    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "start_price")
    private BigDecimal startPrice;

    @Column(name = "auction_time")
    private LocalDateTime auctionTime;

    @Column(name = "auction_endtime")
    private LocalDateTime auctionEndTime;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

//    @OneToMany(mappedBy = "auction", cascade = CascadeType.ALL)
//    private List<Auction> auctions;
//
//    @OneToMany(mappedBy = "feedback", cascade = CascadeType.ALL)
//    private List<Feedback> feedbacks;

    public Product(String title, String description, String image, Integer categoryId, BigDecimal startPrice, LocalDateTime auctionTime, LocalDateTime auctionEndTime){
        this.title = title;
        this.description = description;
        this.image = image;
        this.categoryId = categoryId;
        this.startPrice = startPrice;
        this.auctionTime = auctionTime;
        this.auctionEndTime = auctionEndTime;
    }
}
