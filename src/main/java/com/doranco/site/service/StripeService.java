package com.doranco.site.service;

import org.springframework.stereotype.Service;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.param.ChargeCreateParams;

@Service
public class StripeService {

    public Charge chargeCard(String token, long amount, String currency) throws StripeException {
        ChargeCreateParams params = ChargeCreateParams.builder()
                .setAmount(amount)
                .setCurrency(currency)
                .setSource(token)
                .setDescription("Order Payment")
                .build();

        return Charge.create(params);
    }
}