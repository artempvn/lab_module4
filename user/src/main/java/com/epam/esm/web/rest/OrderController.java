package com.epam.esm.web.rest;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.OrderDtoWithCertificates;
import com.epam.esm.dto.OrderDtoWithCertificatesWithTagsForCreation;
import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class OrderController {

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping
  @RequestMapping("/users/{userId}/order/{orderId}")
  public ResponseEntity<OrderDtoWithCertificates> readUserOrder(
      @PathVariable long userId, @PathVariable long orderId) {
    OrderDtoWithCertificates order = orderService.readOrderByUser(userId, orderId);
    return ResponseEntity.status(HttpStatus.OK).body(order);
  }

  @GetMapping
  @RequestMapping("/users/{userId}/orders")
  public ResponseEntity<List<OrderDto>> readUserOrders(
      @PathVariable long userId, @Valid PaginationParameter parameter) {
    List<OrderDto> orders = orderService.readAllByUser(userId, parameter);
    return ResponseEntity.status(HttpStatus.OK).body(orders);
  }

  @PostMapping
  @RequestMapping("/users/{userId}/order")
  public ResponseEntity<OrderDtoWithCertificatesWithTagsForCreation> createOrder(
      @PathVariable long userId, @RequestBody OrderDtoWithCertificatesWithTagsForCreation order) {
    order.setUserId(userId);
    OrderDtoWithCertificatesWithTagsForCreation createdOrder = orderService.create(order);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
  }
}
