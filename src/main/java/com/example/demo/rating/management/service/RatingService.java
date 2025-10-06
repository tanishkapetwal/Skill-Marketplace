package com.example.demo.rating.management.service;

import com.example.demo.order.management.model.Orders;
import com.example.demo.seller.management.model.Seller;
import com.example.demo.skill.management.model.SkillsListing;
import com.example.demo.order.management.repository.OrdersRepo;
import com.example.demo.seller.management.repository.SellerRepo;
import com.example.demo.skill.management.repository.SkillsListingRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RatingService {
    @Autowired
    private OrdersRepo ordersRepo;
    @Autowired
    private SkillsListingRepo listingRepo;
    @Autowired
    private SellerRepo sellerRepo;

    @Transactional
    public void updateRatingsAfterOrder(int orderId) {
        Orders order = ordersRepo.findById(orderId).orElseThrow();

        // update listing avg
        int listingId = order.getSkillslisting().getId();
        Double listingAvg = ordersRepo.findAvgByListing(listingId);
        SkillsListing listing = listingRepo.findById(listingId).orElseThrow();
        listing.setAvgRating(listingAvg);
        listingRepo.save(listing);

        // update seller avg
        int sellerId = listing.getSeller().getId();
        Double sellerAvg = ordersRepo.findAvgBySeller(sellerId);
        Seller seller = sellerRepo.findById(sellerId).orElseThrow();
        seller.setAvgRating(sellerAvg);
        sellerRepo.save(seller);
    }
}

