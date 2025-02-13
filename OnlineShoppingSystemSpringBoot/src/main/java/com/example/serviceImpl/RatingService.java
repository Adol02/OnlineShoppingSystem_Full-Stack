//package com.example.service;
//
//import java.util.ArrayList;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.example.model.Rating;
//import com.example.repository.RatingRepository;
//@Service
//public class RatingService {
//	
//	@Autowired
//	private RatingRepository ratingRepo;
//	
//	public void addRating(Rating rating) {
//        ratingRepo.save(rating);
//    }
//	public ArrayList<Rating> getRating(String p) {
//		return (ArrayList<Rating>) ratingRepo.findByProductName(p);
//	}
//	public ArrayList<Rating> getAllRating() {
//		return (ArrayList<Rating>) ratingRepo.findAll();
//	}
//}
//package com;


