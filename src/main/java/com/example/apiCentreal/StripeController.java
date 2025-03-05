package com.example.apiCentreal;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stripe")
@CrossOrigin(origins = "*") // Enable CORS
public class StripeController {
    private static final Logger logger = LoggerFactory.getLogger(StripeController.class);

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @PostMapping("/create-payment-intent")
    public ResponseEntity<Map<String, String>> createPaymentIntent(@RequestBody Map<String, Object> paymentRequest) {
        try {
            logger.info("Received payment request: {}", paymentRequest);
            Stripe.apiKey = stripeSecretKey;

            // Create payment intent
            long amount = Long.parseLong(paymentRequest.get("amount").toString());
            String currency = paymentRequest.get("currency").toString();
            String description = (String) paymentRequest.get("description");

            logger.info("Creating payment intent for amount: {}, currency: {}", amount, currency);

            PaymentIntentCreateParams createParams = PaymentIntentCreateParams.builder()
                    .setAmount(amount)
                    .setCurrency(currency)
                    .setDescription(description)
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .build()
                    )
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(createParams);
            logger.info("Payment intent created successfully");

            Map<String, String> response = new HashMap<>();
            response.put("clientSecret", paymentIntent.getClientSecret());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error creating payment intent", e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}