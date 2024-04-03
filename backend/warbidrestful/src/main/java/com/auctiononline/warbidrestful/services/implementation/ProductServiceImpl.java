package com.auctiononline.warbidrestful.services.implementation;

import com.auctiononline.warbidrestful.exception.AppException;
import com.auctiononline.warbidrestful.models.Auction;
import com.auctiononline.warbidrestful.models.Category;
import com.auctiononline.warbidrestful.models.Post;
import com.auctiononline.warbidrestful.models.Product;
import com.auctiononline.warbidrestful.payload.dto.PostLabelDTO;
import com.auctiononline.warbidrestful.payload.dto.ProductDTO;
import com.auctiononline.warbidrestful.payload.request.PostRequest;
import com.auctiononline.warbidrestful.payload.request.ProductRequest;
import com.auctiononline.warbidrestful.payload.response.GetAllResponse;
import com.auctiononline.warbidrestful.payload.response.MessageResponse;
import com.auctiononline.warbidrestful.repository.AuctionRepository;
import com.auctiononline.warbidrestful.repository.CategoryRepository;
import com.auctiononline.warbidrestful.repository.ProductRepository;
import com.auctiononline.warbidrestful.services.ilterface.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Override
    public GetAllResponse getAll(){
        try {
            //List<Category> categories = categoryRepository.findCategoryById().get(0);
           List<Product> products = productRepository.getAllProductActive();
           List<ProductDTO> productDTOs = convertListProductToProductDTO(products);

         return new GetAllResponse(200, "OK", "Get all product successful!", productDTOs);
        } catch (AppException ex) {
            return new GetAllResponse(500, ex.getHttpStatus().toString(), ex.getMessage(), null);
        }
    }

    @Override
    public GetAllResponse getById(Long id){
        try {
            List<Product> products = productRepository.getAllProductActiveById(id);
            if(products.isEmpty()){
                return new GetAllResponse(404, HttpStatus.NOT_FOUND.toString(), "Id not found",null);
            }
            List<ProductDTO> productDTOs = convertListProductToProductDTO(products);

            return new GetAllResponse(200, "OK", "Get product by id successful!", productDTOs);
        } catch (AppException ex) {
            return new GetAllResponse(500, ex.getHttpStatus().toString(), ex.getMessage(), null);
        }
    }

    @Override
    public GetAllResponse getBySearch(String search){
        try {
            List<Product> products = productRepository.searchProductByKeyword(search);
            if(products.isEmpty()){
                return new GetAllResponse(404, HttpStatus.NOT_FOUND.toString(), "No results seen",null);
            }
            List<ProductDTO> productDTOs = convertListProductToProductDTO(products);

            return new GetAllResponse(200, "OK", "Get all product by keyword successful!", productDTOs);
        } catch (AppException ex) {
            return new GetAllResponse(500, ex.getHttpStatus().toString(), ex.getMessage(), null);
        }
    }


    @Override
    public GetAllResponse getByCategory(Integer categoryId){
        try {
            List<Product> products = productRepository.getAllProductActiveByCategory(categoryId);
            if(products.isEmpty()){
                return new GetAllResponse(404, HttpStatus.NOT_FOUND.toString(), "Category Id not found",null);
            }
            List<ProductDTO> productDTOs = convertListProductToProductDTO(products);

            return new GetAllResponse(200, "OK", "Get product by category id successful!", productDTOs);
        } catch (AppException ex) {
            return new GetAllResponse(500, ex.getHttpStatus().toString(), ex.getMessage(), null);
        }
    }

    @Override
    public GetAllResponse getByYetAuction(){
        try {
            LocalDateTime currentTime = LocalDateTime.now();
            List<Product> products = productRepository.getAllActiveProductsNotAuctioned(currentTime);
            if(products.isEmpty()){
                return new GetAllResponse(404, HttpStatus.NOT_FOUND.toString(), "No results returned",null);
            }
            List<ProductDTO> productDTOs = convertListProductToProductDTO(products);

            return new GetAllResponse(200, "OK", "Get products that have been successfully auctioned!", productDTOs);
        } catch (AppException ex) {
            return new GetAllResponse(500, ex.getHttpStatus().toString(), ex.getMessage(), null);
        }
    }

    @Override
    public GetAllResponse getByAuctioning(){
        try {
            LocalDateTime currentTime = LocalDateTime.now();
            List<Product> products = productRepository.getAllActiveProductsAuctioning(currentTime);
            if(products.isEmpty()){
                return new GetAllResponse(404, HttpStatus.NOT_FOUND.toString(), "No results returned",null);
            }
            List<ProductDTO> productDTOs = convertListProductToProductDTO(products);

            return new GetAllResponse(200, "OK", "Get the product being auctioned", productDTOs);
        } catch (AppException ex) {
            return new GetAllResponse(500, ex.getHttpStatus().toString(), ex.getMessage(), null);
        }
    }

    @Override
    public GetAllResponse getByAuctioned(){
        try {
            LocalDateTime currentTime = LocalDateTime.now();
            List<Product> products = productRepository.getAllActiveProductsAuctioned(currentTime);
            if(products.isEmpty()){
                return new GetAllResponse(404, HttpStatus.NOT_FOUND.toString(), "No results returned",null);
            }
            List<ProductDTO> productDTOs = convertListProductToProductDTO(products);

            return new GetAllResponse(200, "OK", "Get product auctioning successful!", productDTOs);
        } catch (AppException ex) {
            return new GetAllResponse(500, ex.getHttpStatus().toString(), ex.getMessage(), null);
        }
    }

    @Override
    public MessageResponse add(ProductRequest productRequest){
        try {
            Product product = createProductFromRequest(productRequest);
            product.setDeleted(false);

            String resultCompareTime = compareTimeValid(product);
            if(resultCompareTime != null){
                return new MessageResponse(400, HttpStatus.BAD_REQUEST,resultCompareTime);
            }
            productRepository.save(product);

            Auction auction = new Auction(product.getId(),null,product.getStartPrice(),product.getAuctionTime());
            auction.setBidStatus(false);
            auctionRepository.save(auction);

            return new MessageResponse(200, HttpStatus.OK, "Add product successful!");
        } catch (AppException ex) {
            return new MessageResponse(500, ex.getHttpStatus(), ex.getMessage(), null);
        }
    }

    @Override
    public MessageResponse update(ProductRequest productRequest){
        try {
            Product product = createProductFromRequest(productRequest);
            product.setId(productRequest.getId());
            product.setDeleted(false);

            String resultCompareTime = compareTimeValid(product);
            if(resultCompareTime != null){
                return new MessageResponse(400, HttpStatus.BAD_REQUEST, resultCompareTime);
            }

            productRepository.save(product);
            return new MessageResponse(200, HttpStatus.OK, "Update product successful!");
        } catch (AppException ex) {
            return new MessageResponse(500, ex.getHttpStatus(), ex.getMessage(), null);
        }
    }

    @Override
    public MessageResponse delete(Long id){
        try {
            List<Product> products = productRepository.getAllProductActiveById(id);
            if(products.isEmpty()){
                return new MessageResponse(404, HttpStatus.NOT_FOUND, "Id not found");
            }

            Product product = products.get(0);
            product.setDeleted(true);

            productRepository.save(product);
            return new MessageResponse(200, HttpStatus.OK,"Product deleted successful");

        } catch (AppException ex) {
            return new MessageResponse(500, ex.getHttpStatus(), ex.getMessage(), null);
        }
    }

    private String convertIdtoNameCategory(Integer id){
        List<Category> categories = categoryRepository.findCategoryById(id);
        if(categories.isEmpty()){
            return null;
        }
        Category category = categories.get(0);
        return  category.getName();
    }

    private List<ProductDTO> convertListProductToProductDTO(List<Product> products){
        List<ProductDTO> productsDTO = new ArrayList<>();

        for (Product product : products) {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(product.getId());
            productDTO.setTitle(product.getTitle());
            productDTO.setDescription(product.getDescription());
            productDTO.setImage(product.getImage());

            String categoryName = convertIdtoNameCategory(product.getCategoryId());
            productDTO.setCategory(categoryName);

            productDTO.setStartPrice(product.getStartPrice());
            productDTO.setAuctionTime(product.getAuctionTime());
            productDTO.setAuctionEndTime(product.getAuctionEndTime());

            productsDTO.add(productDTO);
        }
        return productsDTO;
    }

    private Product createProductFromRequest(ProductRequest productRequest) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime auctionTime = LocalDateTime.parse(productRequest.getAuctionTime(), formatter);
        LocalDateTime auctionEndTime = LocalDateTime.parse(productRequest.getAuctionEndTime(), formatter);
        return new Product(
                productRequest.getTitle(),
                productRequest.getDescription(),
                productRequest.getImage(),
                productRequest.getCategoryId(),
                productRequest.getStartPrice(),
                auctionTime,
                auctionEndTime
        );
    }

    private String compareTimeValid(Product product){
        if(product.getAuctionTime() == null || product.getAuctionEndTime() == null){
            return "Auction time or auction end time don't null";
        }

        int compareCurrentTime = product.getAuctionTime().compareTo(LocalDateTime.now());
        if(compareCurrentTime < 0){
            return "Auction Time invalid!";
        }

        int compareEndTime = product.getAuctionTime().compareTo(product.getAuctionEndTime());
        if(compareEndTime > 0){ //auctionEndTime invalid
            return "Auction End Time invalid!";
        }
        return null;
    }

}
