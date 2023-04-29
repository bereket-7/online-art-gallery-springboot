package com.project.oag.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.oag.common.ApiResponse;
import com.project.oag.entity.Order;
import com.project.oag.entity.User;
import com.project.oag.exceptions.AuthenticationFailException;
import com.project.oag.exceptions.OrderNotFoundException;
import com.project.oag.service.AuthenticationService;
import com.project.oag.service.OrderService;

@RestController
@RequestMapping("/order")
@CrossOrigin("http://localhost:8080/")
public class OrderController {
	 	@Autowired
	    private OrderService orderService;
	    @Autowired
	    private AuthenticationService authenticationService;

	    // place order after checkout
	    @PostMapping("/add")
	    public ResponseEntity<ApiResponse> placeOrder(@RequestParam("token") String token, @RequestParam("sessionId") String sessionId)
	            throws AuthenticationFailException {
	        // validate token
	        authenticationService.authenticate(token);
	        // retrieve user
	        User user = authenticationService.getUser(token);
	        // place the order
	        orderService.placeOrder(user, sessionId);
	        return new ResponseEntity<>(new ApiResponse(true, "Order has been placed"), HttpStatus.CREATED);
	    }
	    // get all orders
	    @GetMapping("/all")
	    public ResponseEntity<List<Order>> getAllOrders(@RequestParam("token") String token) throws AuthenticationFailException {
	        // validate token
	        authenticationService.authenticate(token);
	        // retrieve user
	        User user = authenticationService.getUser(token);
	        // get orders
	        List<Order> orderDtoList = orderService.listOrders(user);

	        return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
	    }
	    // get orderitems for an order
	    @GetMapping("/{id}")
	    public ResponseEntity<Object> getOrderById(@PathVariable("id") Integer id, @RequestParam("token") String token)
	            throws AuthenticationFailException {
	        // validate token
	        authenticationService.authenticate(token);
	        try {
	            Order order = orderService.getOrder(id);
	            return new ResponseEntity<>(order,HttpStatus.OK);
	        }
	        catch (OrderNotFoundException e) {
	            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
	        }

	    }




}



