package com.epam.esm.web.rest;

import com.epam.esm.dto.*;
import com.epam.esm.service.OrderService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/** The type Order resource. */
@RestController
@RequestMapping("/users")
public class OrderResource {

  private final OrderService orderService;
  private final HateoasHandler hateoasHandler;

  /**
   * Instantiates a new Order resource.
   *
   * @param orderService the order service
   * @param hateoasHandler the hateoas handler
   */
  public OrderResource(OrderService orderService, HateoasHandler hateoasHandler) {
    this.orderService = orderService;
    this.hateoasHandler = hateoasHandler;
  }

  /**
   * Read user order response entity.
   *
   * @param userId the user id
   * @param orderId the order id
   * @return the response entity
   */
  @GetMapping
  @RequestMapping("/{userId}/order/{orderId}")
  public ResponseEntity<EntityModel<OrderDtoWithCertificates>> readUserOrder(
      @PathVariable long userId, @PathVariable long orderId) {
    EntityModel<OrderDtoWithCertificates> order =
        EntityModel.of(orderService.readOrderByUser(userId, orderId));
    order.add(buildOrderLinks(userId, order.getContent().getId()));
    return ResponseEntity.status(HttpStatus.OK).body(order);
  }

  /**
   * Read user orders response entity.
   *
   * @param userId the user id
   * @param parameter the parameter
   * @return the response entity
   */
  @GetMapping
  @RequestMapping("/{userId}/orders")
  public ResponseEntity<EntityModel<PageData<EntityModel<OrderDto>>>> readUserOrders(
      @PathVariable long userId, @Valid PaginationParameter parameter) {
    PageData<OrderDto> page = orderService.readAllByUser(userId, parameter);

    EntityModel<PageData<EntityModel<OrderDto>>> hateoasPage =
        hateoasHandler.wrapPageWithEntityModel(page);

    for (EntityModel<OrderDto> order : hateoasPage.getContent().getContent()) {
      long id = order.getContent().getId();
      List<Link> links = buildOrderLinks(userId, id);
      order.add(links);
    }

    hateoasPage.add(
        hateoasHandler.buildLinksForPaginationWithOuterResource(
            OrderResource.class, parameter, page.getNumberOfPages(), userId, "orders"));
    hateoasPage.add(buildOrdersLinks(userId));

    return ResponseEntity.status(HttpStatus.OK).body(hateoasPage);
  }

  /**
   * Create order response entity.
   *
   * @param userId the user id
   * @param order the order
   * @return the response entity
   */
  @PostMapping
  @RequestMapping("/{userId}/order")
  public ResponseEntity<OrderDtoWithCertificatesWithTagsForCreation> createOrder(
      @PathVariable long userId,
      @Valid @RequestBody OrderDtoWithCertificatesWithTagsForCreation order) {
    order.setUserId(userId);
    OrderDtoWithCertificatesWithTagsForCreation createdOrder = orderService.create(order);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
  }

  /**
   * Build order links list.
   *
   * @param userId the user id
   * @param id the id
   * @return the list
   */
  List<Link> buildOrderLinks(long userId, long id) {
    return List.of(
        linkTo(OrderResource.class).slash(userId).slash("order").slash(id).withSelfRel());
  }

  /**
   * Build orders links list.
   *
   * @param userId the user id
   * @return the list
   */
  List<Link> buildOrdersLinks(long userId) {
    return List.of(
        linkTo(
                methodOn(OrderResource.class)
                    .createOrder(userId, new OrderDtoWithCertificatesWithTagsForCreation()))
            .withRel("post")
            .withName("create order for user"));
  }
}
