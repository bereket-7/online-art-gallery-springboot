/*package com.project.oag.controller;
/**
public class PaymentController {
	
	/**@RequestMapping("/create-payment")
	@ResponseBody
	public Payment createPayment() throws PayPalRESTException {
	    Amount amount = new Amount();
	    amount.setCurrency("USD");
	    amount.setTotal("100.00");

	    Transaction transaction = new Transaction();
	    transaction.setAmount(amount);
	    transaction.setDescription("Payment for online art gallery");

	    List transactions = new ArrayList<>();
	    transactions.add(transaction);

	    Payer payer = new Payer();
	    payer.setPaymentMethod("paypal");

	    RedirectUrls redirectUrls = new RedirectUrls();
	    redirectUrls.setReturnUrl("http://localhost:8080/execute-payment");
	    redirectUrls.setCancelUrl("http://localhost:8080/cancel-payment");

	    Payment payment = new Payment();
	    payment.setIntent("sale");
	    payment.setPayer(payer);
	    payment.setRedirectUrls(redirectUrls);
	    payment.setTransactions(transactions);

	    return payment.create(apiContext());
	}
	
	@RequestMapping("/execute-payment")
	@ResponseBody
	public Payment executePayment(@RequestParam("paymentId") String paymentId,
	                               @RequestParam("PayerID") String payerId) throws PayPalRESTException {
	    Payment payment = new Payment();
	    payment.setId(paymentId);

	    PaymentExecution paymentExecution = new PaymentExecution();
	    paymentExecution.setPayerId(payerId);

	    return payment.execute(apiContext(), paymentExecution);
	}
	
	@RequestMapping("/cancel-payment")
	@ResponseBody
	public String cancelPayment() {
	    return "Payment cancelled";
	}



}*/
