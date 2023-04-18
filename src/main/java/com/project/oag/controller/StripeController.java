package com.project.oag.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.oag.common.StripeResponse;
import com.project.oag.controller.dto.CheckoutItemDto;
import com.project.oag.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
@RestController
@RequestMapping("/api/payment")
public class StripeController {
 
  @Autowired
  private StripeService stripeService;
 
  @PostMapping("/create-checkout-session")
  public ResponseEntity<StripeResponse> checkoutList(@RequestBody List<CheckoutItemDto> checkoutItemDtoList) throws StripeException {
      // create the stripe session
      Session session = stripeService.createSession(checkoutItemDtoList);
      StripeResponse stripeResponse = new StripeResponse(session.getId());
      // send the stripe session id in response
      return new ResponseEntity<StripeResponse>(stripeResponse, HttpStatus.OK);
  }
  
}

