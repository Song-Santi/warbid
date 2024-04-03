package com.auctiononline.warbidrestful.services.implementation;

import com.auctiononline.warbidrestful.exception.AppException;
import com.auctiononline.warbidrestful.models.Auction;
import com.auctiononline.warbidrestful.models.Product;
import com.auctiononline.warbidrestful.models.User;
import com.auctiononline.warbidrestful.payload.dto.AuctionTheBestDTO;
import com.auctiononline.warbidrestful.payload.request.AuctionRegisterRequest;
import com.auctiononline.warbidrestful.payload.request.AuctionRequest;
import com.auctiononline.warbidrestful.payload.response.GetAllResponse;
import com.auctiononline.warbidrestful.payload.response.MessageResponse;
import com.auctiononline.warbidrestful.repository.AuctionRepository;
import com.auctiononline.warbidrestful.repository.ProductRepository;
import com.auctiononline.warbidrestful.repository.UserRepository;
import com.auctiononline.warbidrestful.services.ilterface.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Service
public class AuctionServiceImpl implements AuctionService {
    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public MessageResponse registerAuction(AuctionRegisterRequest auctionRegisterRequest){
        try {
            Auction auction = new Auction(auctionRegisterRequest.getProductId(), auctionRegisterRequest.getUserId());

            List<Product> products = productRepository.getAllProductActiveById(auctionRegisterRequest.getProductId());
            if(products.isEmpty()){
                return new MessageResponse(404, HttpStatus.NOT_FOUND, "Id product not found");
            }

            List<User> users = userRepository.findByIdActive(auctionRegisterRequest.getUserId());
            if(users.isEmpty()){
                return new MessageResponse(404, HttpStatus.NOT_FOUND,"Id user not found");
            }

            Product product = products.get(0);

            auction.setBidAmount(product.getStartPrice());
            auction.setBidTime(product.getAuctionTime());
            auction.setBidStatus(false);

            auctionRepository.save(auction);
            return new MessageResponse(200, HttpStatus.OK, "Auction registration successful!");
        } catch (AppException ex) {
            return new MessageResponse(500, ex.getHttpStatus(), ex.getMessage(), null);
        }
    }

    @Override
    public MessageResponse updatePriceIncrease(AuctionRequest auctionRequest){
        try {
            List<User> users = userRepository.findByIdActive(auctionRequest.getUserId());
            if(users.isEmpty()){
                return new MessageResponse(404, HttpStatus.NOT_FOUND,"Id user not found");
            }

            List<Product> products = productRepository.getAllProductActiveById(auctionRequest.getProductId());
            if(products.isEmpty()){
                return new MessageResponse(404, HttpStatus.NOT_FOUND, "Id product not found");
            }
            Product product = products.get(0);
            Auction auction = createAuctionFromRequest(auctionRequest);

            // compare time
            LocalDateTime bidTime = auction.getBidTime();
            LocalDateTime auctionEndTime = product.getAuctionEndTime();
            if (auctionEndTime == null) {
                return new MessageResponse(400, HttpStatus.BAD_REQUEST, "Auction end time is not specified");
            }

            int compareBidTime = bidTime.compareTo(auctionEndTime);
            if(compareBidTime > 0){ // bidTime after auctionEndTime --> invalid
                return new MessageResponse(400, HttpStatus.BAD_REQUEST,"The bet time has expired (invalid)");
            }

            // compare price
            List<Auction> auctions = auctionRepository.findAllByProductIdOrderByBidAmountDesc(auctionRequest.getProductId());
            if(auctions.isEmpty()){
                return new MessageResponse(404, HttpStatus.NOT_FOUND, "Auction not found by product id!");
            }
            Auction auctionBestPrice = auctions.get(0);
            BigDecimal bestPrice = auctionBestPrice.getBidAmount();

            int comparePrice = bestPrice.compareTo(auctionRequest.getBidAmount());

            if(comparePrice > 0){
                return new MessageResponse(400, HttpStatus.BAD_REQUEST,"Invalid price");
            }
            auction.setBidStatus(true);
            auctionRepository.save(auction);

            return new MessageResponse(200, HttpStatus.OK,"Submit auction successful!");
        } catch (AppException ex) {
            return new MessageResponse(500, ex.getHttpStatus(), ex.getMessage(), null);
        }
    }

    @Override
    public GetAllResponse bestAuctionProduct(Long productId){
        try{
            List<Auction> auctions = auctionRepository.findAllByProductIdOrderByBidAmountDesc(productId);
            if(auctions.isEmpty()){
                return new GetAllResponse(404, HttpStatus.NOT_FOUND.toString(),"Auction list not found by product id", null);
            }
            Auction auction = auctions.get(0);
            Long userId = auction.getUserId();

            if(userId == null){
                AuctionTheBestDTO auctionTheBestDTO = new AuctionTheBestDTO(auction.getBidAmount(), "Starting price");
                List<AuctionTheBestDTO> auctionTheBestDTOs = new ArrayList<>();
                auctionTheBestDTOs.add(auctionTheBestDTO);

                return new GetAllResponse(200, HttpStatus.OK.toString(),"Get auction best price successful!",auctionTheBestDTOs);
            }

            List<User> users = userRepository.findByIdActive(userId);
            if(users.isEmpty()){
                return new GetAllResponse(404, HttpStatus.NOT_FOUND.toString(),"Not user found by user id", null);
            }
            User user = users.get(0);
            AuctionTheBestDTO auctionTheBestDTO = new AuctionTheBestDTO(auction.getBidAmount(), user.getUsername());
            List<AuctionTheBestDTO> auctionTheBestDTOs = new ArrayList<>();
            auctionTheBestDTOs.add(auctionTheBestDTO);

            return new GetAllResponse(200, HttpStatus.OK.toString(),"Get auction best price successful!",auctionTheBestDTOs);
        }catch(AppException ex){
            return new GetAllResponse(500, ex.getHttpStatus().toString(), ex.getMessage(), null);
        }
    }

    private Auction createAuctionFromRequest(AuctionRequest auctionRequest) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime bidTime = LocalDateTime.parse(auctionRequest.getBidTime(), formatter);
        return new Auction(
                auctionRequest.getProductId(),
                auctionRequest.getUserId(),
                auctionRequest.getBidAmount(),
                bidTime
        );
    }
}
