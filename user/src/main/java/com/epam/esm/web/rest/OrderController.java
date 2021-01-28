package com.epam.esm.web.rest;

import com.epam.esm.dto.OrderDtoFull;
import com.epam.esm.dto.UserDtoWithoutOrders;
import com.epam.esm.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<UserDtoWithOrders> readUser(@PathVariable long id) {
//        UserDtoWithOrders user = userService.read(id);
//        return ResponseEntity.status(HttpStatus.OK).body(user);
//    }
//
//    @GetMapping
//    @ResponseStatus(HttpStatus.OK)
//    public List<UserDtoWithoutOrders> readUsers() {
//        return userService.readAll();
//    }

    @PostMapping
    @RequestMapping("/users/{id}/orders")
    public ResponseEntity<OrderDtoFull> createOrder(@PathVariable long id,
            @RequestBody OrderDtoFull order) {
        UserDtoWithoutOrders user=new UserDtoWithoutOrders();
        user.setId(id);
        order.setUser(user);
        OrderDtoFull createdOrder = orderService.create(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

}
