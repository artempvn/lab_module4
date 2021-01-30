package com.epam.esm.exception;

import java.util.function.Supplier;

public class OrderException extends ResourceException{

    public OrderException(String message, Long resourceId) {
        super(message, resourceId);
    }

    public static Supplier<ResourceException> validationWithEmptyOrder(Long id) {
        String message = String.format("Order must not be empty, user id = %s", id);
        return () -> new OrderException(message, id);
    }
}
