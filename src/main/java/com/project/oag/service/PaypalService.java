package com.project.oag.service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.paypal.api.payments.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
@Service
public class PaypalService {
	@Autowired
	private APIContext apiContext;

	public Map<String, String> createPayment(Double total, String currency, String method, String intent,
											 String description, String cancelUrl, String successUrl) throws PayPalRESTException {
		Amount amount = new Amount();
		amount.setCurrency(currency);
		total = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP).doubleValue();
		amount.setTotal(String.format("%.2f", total));

		Transaction transaction = new Transaction();
		transaction.setDescription(description);
		transaction.setAmount(amount);

		List<Transaction> transactions = new ArrayList<>();
		transactions.add(transaction);

		Payer payer = new Payer();
		payer.setPaymentMethod(method);

		Payment payment = new Payment();
		payment.setIntent(intent);
		payment.setPayer(payer);
		payment.setTransactions(transactions);
		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setCancelUrl(cancelUrl);
		redirectUrls.setReturnUrl(successUrl);
		payment.setRedirectUrls(redirectUrls);

		Payment createdPayment = payment.create(apiContext);

		Map<String, String> response = new HashMap<>();
		for (Links link : createdPayment.getLinks()) {
			if (link.getRel().equals("approval_url")) {
				response.put("approvalUrl", link.getHref());
				response.put("paymentId", createdPayment.getId());
				break;
			}
		}

		return response;
	}

	public String executePayment(String paymentId, String payerId) throws PayPalRESTException {
		Payment payment = new Payment();
		payment.setId(paymentId);
		PaymentExecution paymentExecute = new PaymentExecution();
		paymentExecute.setPayerId(payerId);

		Payment executedPayment = payment.execute(apiContext, paymentExecute);
		if (executedPayment.getState().equals("approved")) {
			return "success";
		} else {
			return "failure";
		}
	}

//
//	public Payment createPayment(
//			Double total,
//			String currency,
//			String method,
//			String intent,
//			String description,
//			String cancelUrl,
//			String successUrl) throws PayPalRESTException{
//		Amount amount = new Amount();
//		amount.setCurrency(currency);
//		total = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP).doubleValue();
//		amount.setTotal(String.format("%.2f", total));
//		Transaction transaction = new Transaction();
//		transaction.setDescription(description);
//		transaction.setAmount(amount);
//		List<Transaction> transactions = new ArrayList<>();
//		transactions.add(transaction);
//		Payer payer = new Payer();
//		payer.setPaymentMethod(method.toString());
//
//		Payment payment = new Payment();
//		payment.setIntent(intent.toString());
//		payment.setPayer(payer);
//		payment.setTransactions(transactions);
//		RedirectUrls redirectUrls = new RedirectUrls();
//		redirectUrls.setCancelUrl(cancelUrl);
//		redirectUrls.setReturnUrl(successUrl);
//		payment.setRedirectUrls(redirectUrls);
//		return payment.create(apiContext);
//	}
//	public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException{
//		Payment payment = new Payment();
//		payment.setId(paymentId);
//		PaymentExecution paymentExecute = new PaymentExecution();
//		paymentExecute.setPayerId(payerId);
//		return payment.execute(apiContext, paymentExecute);
//	}
}

