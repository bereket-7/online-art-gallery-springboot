package com.project.oag.shopping.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@CrossOrigin("http://localhost:8080/")
public class OrderController {
	 	@Autowired
	    private OrderService orderService;

	    // place order after checkout
//	    @PostMapping("/add")
//	    public ResponseEntity<ApiResponse> placeOrder(@RequestParam("token") String token, @RequestParam("sessionId") String sessionId)
//	            throws AuthenticationFailException {
//	        // validate token
//	        authenticationService.authenticate(token);
//	        // retrieve user
//	        User user = authenticationService.getUser(token);
//	        // place the order
//	        orderService.placeOrder(user, sessionId);
//	        return new ResponseEntity<>(new ApiResponse(true, "Order has been placed"), HttpStatus.CREATED);
//	    }
	    // get all orders
//	    @GetMapping("/all")
//	    public ResponseEntity<List<Order>> getAllOrders(@RequestParam("token") String token) throws AuthenticationFailException {
//	        // validate token
//	        authenticationService.authenticate(token);
//	        // retrieve user
//	        User user = authenticationService.getUser(token);
//	        // get orders
//	        List<Order> orderDtoList = orderService.listOrders(user);
//
//	        return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
//	    }
	    // get orderitems for an order
//	    @GetMapping("/{id}")
//	    public ResponseEntity<Object> getOrderById(@PathVariable("id") Integer id, @RequestParam("token") String token)
//	            throws AuthenticationFailException {
//	        // validate token
//	        authenticationService.authenticate(token);
//	        try {
//	            Order order = orderService.getOrder(id);
//	            return new ResponseEntity<>(order,HttpStatus.OK);
//	        }
//	        catch (OrderNotFoundException e) {
//	            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
//	        }
//	    }




}



