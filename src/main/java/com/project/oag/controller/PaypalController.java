package com.project.oag.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.project.oag.entity.Order;
import com.project.oag.service.PaypalService;
@RestController
@RequestMapping("/paypal")
@CrossOrigin("http://localhost:8080/")
public class PaypalController {
	@Autowired
	PaypalService service;
	public static final String SUCCESS_URL = "paypalSuccess";
	public static final String CANCEL_URL = "paypalFail";
	@GetMapping("/")
	public String home() {
		return "home";
	}
	@PostMapping("/pay")
	@ResponseBody
	public Payment payment(@RequestBody Order order) {
		try {
			Payment payment = service.createPayment(order.getPrice(), order.getCurrency(), order.getMethod(),
					order.getIntent(), order.getDescription(), "http://localhost:9090/" + CANCEL_URL,
					"http://localhost:9090/" + SUCCESS_URL);
			return payment;
		} catch (PayPalRESTException e) {
			e.printStackTrace();
			return null;
		}
	}
	@GetMapping(value = CANCEL_URL)
	@ResponseBody
	public String cancelPay() {
		return "cancel";
	}
	@GetMapping(value = SUCCESS_URL)
	@ResponseBody
	public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
		try {
			Payment payment = service.executePayment(paymentId, payerId);
			System.out.println(payment.toJSON());
			if (payment.getState().equals("approved")) {
				return "success";
			}
		} catch (PayPalRESTException e) {
			System.out.println(e.getMessage());
		}
		return "failure";
	}
//	@PostMapping("/pay")
//	public String payment(@ModelAttribute("order") Order order) {
//		try {
//			Payment payment = service.createPayment(order.getPrice(), order.getCurrency(), order.getMethod(),
//					order.getIntent(), order.getDescription(), "http://localhost:8080/" + CANCEL_URL,
//					"http://localhost:8080/" + SUCCESS_URL);
//			for (Links link : payment.getLinks()) {
//				if (link.getRel().equals("approval_url")) {
//					return "redirect:" + link.getHref();
//				}
//			}
//		} catch (PayPalRESTException e) {
//
//			e.printStackTrace();
//		}
//		return "redirect:";
//	}
//	 @GetMapping(value = CANCEL_URL)
//	    public String cancelPay() {
//	        return "cancel";
//	    }
//	    @GetMapping(value = SUCCESS_URL)
//	    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
//	        try {
//	            Payment payment = service.executePayment(paymentId, payerId);
//	            System.out.println(payment.toJSON());
//	            if (payment.getState().equals("approved")) {
//	                return "success";
//	            }
//	        } catch (PayPalRESTException e) {
//	         System.out.println(e.getMessage());
//	        }
//	        return "redirect:/";
//	    }
}
