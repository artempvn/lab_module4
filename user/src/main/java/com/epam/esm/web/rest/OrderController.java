package com.epam.esm.web.rest;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.OrderDtoFull;
import com.epam.esm.dto.OrderDtoFullCreation;
import com.epam.esm.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping
  @RequestMapping("/users/{userId}/order/{orderId}")
  public ResponseEntity<OrderDtoFull> readUserOrder(
      @PathVariable long userId, @PathVariable long orderId) {
    OrderDtoFull order = orderService.readOrderByUser(userId, orderId);
    return ResponseEntity.status(HttpStatus.CREATED).body(order);
  }

  @GetMapping
  @RequestMapping("/users/{userId}/orders")
  public ResponseEntity<List<OrderDto>> readUserOrders(@PathVariable long userId) {
    List<OrderDto> orders = orderService.readAllByUser(userId);
    return ResponseEntity.status(HttpStatus.CREATED).body(orders);
  }

  @PostMapping
  @RequestMapping("/users/{userId}/order")
  public ResponseEntity<OrderDtoFullCreation> createOrder(
      @PathVariable long userId, @RequestBody OrderDtoFullCreation order) {
    order.setUserId(userId);
    OrderDtoFullCreation createdOrder = orderService.create(order);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
  }
}
