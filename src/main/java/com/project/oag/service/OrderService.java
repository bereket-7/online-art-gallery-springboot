package com.project.oag.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.project.oag.controller.dto.CartDto;
import com.project.oag.controller.dto.CartItemDto;
import com.project.oag.entity.Order;
import com.project.oag.entity.OrderItem;
import com.project.oag.entity.User;
import com.project.oag.exceptions.OrderNotFoundException;
import com.project.oag.repository.OrderItemRepository;
import com.project.oag.repository.OrderRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderService {
	
	 	@Autowired
	    private CartService cartService;

	    @Autowired
	    OrderRepository orderRepository;

	    @Autowired
	    OrderItemRepository orderItemRepository;

	    @Value("${BASE_URL}")
	    private String baseURL;

	    @Value("${STRIPE_SECRET_KEY}")
	    private String apiKey;
/*
	    // create total price
	    SessionCreateParams.LineItem.PriceData createPriceData(CheckoutItemDto checkoutItemDto) {
	        return SessionCreateParams.LineItem.PriceData.builder()
	                .setCurrency("ETB")
	                .setUnitAmount((long)(checkoutItemDto.getPrice()*100))
	                .setProductData(
	                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
	                                .setName(checkoutItemDto.getArtworkName())
	                                .build())
	                .build();
	    }
	    // build each product in the stripe checkout page
	    SessionCreateParams.LineItem createSessionLineItem(CheckoutItemDto checkoutItemDto) {
	        return SessionCreateParams.LineItem.builder()
	                // set price for each product
	                .setPriceData(createPriceData(checkoutItemDto))
	                // set quantity for each product
	                .setQuantity(Long.parseLong(String.valueOf(checkoutItemDto.getQuantity())))
	                .build();
	    }

	    // create session from list of checkout items
	    public Session createSession(List<CheckoutItemDto> checkoutItemDtoList) throws StripeException {

	        // supply success and failure url for stripe
	        String successURL = baseURL + "payment/success";
	        String failedURL = baseURL + "payment/failed";

	        // set the private key
	        Stripe.apiKey = apiKey;

	        List<SessionCreateParams.LineItem> sessionItemsList = new ArrayList<>();

	        // for each product compute SessionCreateParams.LineItem
	        for (CheckoutItemDto checkoutItemDto : checkoutItemDtoList) {
	            sessionItemsList.add(createSessionLineItem(checkoutItemDto));
	        }

	        // build the session param
	        SessionCreateParams params = SessionCreateParams.builder()
	                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
	                .setMode(SessionCreateParams.Mode.PAYMENT)
	                .setCancelUrl(failedURL)
	                .addAllLineItem(sessionItemsList)
	                .setSuccessUrl(successURL)
	                .build();
	        return Session.create(params);
	    }*/

	    public void placeOrder(User user, String sessionId) {
	        // first let get cart items for the user
	        CartDto cartDto = cartService.listCartItems(user);

	        List<CartItemDto> cartItemDtoList = cartDto.getcartItems();

	        // create the order and save it
	        Order newOrder = new Order();
	        newOrder.setCreatedDate(new Date());
	        newOrder.setSessionId(sessionId);
	        newOrder.setUser(user);
	        newOrder.setTotalPrice(cartDto.getTotalCost());
	        orderRepository.save(newOrder);

	        for (CartItemDto cartItemDto : cartItemDtoList) {
	            // create orderItem and save each one
	            OrderItem orderItem = new OrderItem();
	            orderItem.setCreatedDate(new Date());
	            orderItem.setPrice(cartItemDto.getArtwork().getPrice());
	            orderItem.setArtwork(cartItemDto.getArtwork());
	            orderItem.setQuantity(cartItemDto.getQuantity());
	            orderItem.setOrder(newOrder);
	            // add to order item list
	            orderItemRepository.save(orderItem);
	        }
	        //
	        cartService.deleteUserCartItems(user);
	    }

	    public List<Order> listOrders(User user) {
	        return orderRepository.findAllByUserOrderByCreatedDateDesc(user);
	    }


	    public Order getOrder(Integer orderId) throws OrderNotFoundException {
	        Optional<Order> order = orderRepository.findById(orderId);
	        if (order.isPresent()) {
	            return order.get();
	        }
	        throw new OrderNotFoundException("Order not found");
	    }
}
