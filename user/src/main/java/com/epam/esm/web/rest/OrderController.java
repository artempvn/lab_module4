package com.epam.esm.web.rest;

import com.epam.esm.dto.*;
import com.epam.esm.service.OrderService;
import com.epam.esm.web.service.HateoasHandler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
public class OrderController {

  private final OrderService orderService;
  private final HateoasHandler hateoasHandler;

  public OrderController(OrderService orderService, HateoasHandler hateoasHandler) {
    this.orderService = orderService;
    this.hateoasHandler = hateoasHandler;
  }

  @GetMapping
  @RequestMapping("/{userId}/order/{orderId}")
  public ResponseEntity<EntityModel<OrderDtoWithCertificates>> readUserOrder(
      @PathVariable long userId, @PathVariable long orderId) {
    EntityModel<OrderDtoWithCertificates> order =
        EntityModel.of(orderService.readOrderByUser(userId, orderId));
    order.add(takeOrderLinks(userId, order.getContent().getId()));
    return ResponseEntity.status(HttpStatus.OK).body(order);
  }

  @GetMapping
  @RequestMapping("/{userId}/orders")
  public ResponseEntity<EntityModel<PageData<EntityModel<OrderDto>>>> readUserOrders(
      @PathVariable long userId, @Valid PaginationParameter parameter) {
    PageData<OrderDto> page = orderService.readAllByUser(userId, parameter);


    EntityModel<PageData<EntityModel<OrderDto>>> hateoasPage =
            hateoasHandler.wrapPageWithEntityModel(page);
    hateoasPage
            .getContent()
            .getContent()
            .forEach(
                    order -> order.add(takeOrderLinks(userId,order.getContent().getId())));

    hateoasPage.add(
            hateoasHandler.takeLinksForPaginationWithOuterResource(
                    OrderController.class, parameter, page.getNumberOfPages(),userId,"orders"));
    hateoasPage.add(takeOrdersLinks(userId));

    return ResponseEntity.status(HttpStatus.OK).body(hateoasPage);
  }

  @PostMapping
  @RequestMapping("/{userId}/order")
  public ResponseEntity<OrderDtoWithCertificatesWithTagsForCreation> createOrder(
      @PathVariable long userId, @RequestBody OrderDtoWithCertificatesWithTagsForCreation order) {
    order.setUserId(userId);
    OrderDtoWithCertificatesWithTagsForCreation createdOrder = orderService.create(order);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
  }

  List<Link> takeOrderLinks(long userId, long id) {
    return List.of(
        linkTo(OrderController.class).slash(userId).slash("order").slash(id).withSelfRel());
  }

  List<Link> takeOrdersLinks(long userId) {
    return List.of(

        linkTo(
                methodOn(OrderController.class)
                    .createOrder(userId, new OrderDtoWithCertificatesWithTagsForCreation()))
            .withRel("create order for user"));
  }
}
