package com.epam.esm.web.rest;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.OrderWithCertificatesDto;
import com.epam.esm.dto.OrderWithCertificatesWithTagsForCreationDto;
import com.epam.esm.dto.PageData;
import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.service.OrderService;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/** The type Order resource. */
@RestController
@RequestMapping("/users")
@SecurityScheme(type = SecuritySchemeType.HTTP, scheme = "bearer", name = "Authorization")
@SecurityRequirement(name = AUTHORIZATION)
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
   * Read user order by user id and order id.
   *
   * @param userId the user id
   * @param orderId the order id
   * @return the response entity of found order
   */
  @Secured({"ROLE_ADMIN", "ROLE_USER"})
  @GetMapping(value = "/{userId}/order/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<EntityModel<OrderWithCertificatesDto>> readUserOrder(
      @PathVariable long userId, @PathVariable long orderId) {
    EntityModel<OrderWithCertificatesDto> order =
        EntityModel.of(orderService.readOrderByUser(userId, orderId));
    order.add(buildOrderLinks(userId, order.getContent().getId()));
    return ResponseEntity.status(HttpStatus.OK).body(order);
  }

  /**
   * Read user orders meet pagination parameters.
   *
   * @param userId the user id
   * @param parameter the parameter of pagination
   * @return the response entity of found orders
   */
  @Secured({"ROLE_ADMIN", "ROLE_USER"})
  @GetMapping(value = "/{userId}/orders", produces = MediaType.APPLICATION_JSON_VALUE)
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
   * Persist order with certificates.
   *
   * @param userId the user id
   * @param order the order
   * @return the response entity of saved order
   */
  @Secured({"ROLE_ADMIN", "ROLE_USER"})
  @PostMapping(value = "/{userId}/order", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<OrderWithCertificatesWithTagsForCreationDto> createOrder(
      @PathVariable long userId,
      @Valid @RequestBody OrderWithCertificatesWithTagsForCreationDto order) {
    order.setUserId(userId);
    OrderWithCertificatesWithTagsForCreationDto createdOrder = orderService.create(order);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
  }

  /**
   * Build order links list.
   *
   * @param userId the user id
   * @param id the id of order
   * @return the list of links
   */
  List<Link> buildOrderLinks(long userId, long id) {
    return List.of(
        linkTo(OrderResource.class).slash(userId).slash("order").slash(id).withSelfRel());
  }

  /**
   * Build orders links list.
   *
   * @param userId the user id
   * @return the list of links
   */
  List<Link> buildOrdersLinks(long userId) {
    return List.of(
        linkTo(
                methodOn(OrderResource.class)
                    .createOrder(userId, new OrderWithCertificatesWithTagsForCreationDto()))
            .withRel("post")
            .withName("create order for user"));
  }
}
