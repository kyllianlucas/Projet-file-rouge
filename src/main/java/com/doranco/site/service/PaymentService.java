// PaymentService.java
package com.doranco.site.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

@Service
public class PaymentService {

	@Value("${stripe.api.key}")
    private String stripeApiKey;
	
    public PaymentIntent createPaymentIntent(double amount, String currency) throws StripeException {
    	Stripe.apiKey = stripeApiKey;
    	 
    	PaymentIntentCreateParams params =
                 PaymentIntentCreateParams.builder()
                         .setAmount((long) (amount * 100)) // Amount is in cents
                         .setCurrency(currency)
                         .addPaymentMethodType("card")
                         .build();

         return PaymentIntent.create(params);
    }
}
