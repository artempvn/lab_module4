package com.epam.esm.web.rest;

import com.epam.esm.dto.*;
import com.epam.esm.service.UserService;
import com.epam.esm.web.service.HateoasHandler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;
  private final HateoasHandler hateoasHandler;

  public UserController(UserService userService, HateoasHandler hateoasHandler) {
    this.userService = userService;
    this.hateoasHandler = hateoasHandler;
  }

  @GetMapping("/{id}")
  public ResponseEntity<EntityModel<UserDtoWithOrders>> readUser(@PathVariable long id) {
    EntityModel<UserDtoWithOrders> user = EntityModel.of(userService.read(id));
    user.add(takeUserLinks(user.getContent().getId()));
    return ResponseEntity.status(HttpStatus.OK).body(user);
  }

  @GetMapping
  public ResponseEntity<EntityModel<PageData<EntityModel<UserDto>>>> readUsers(
      @Valid PaginationParameter parameter) {
    PageData<UserDto> page = userService.readAll(parameter);

    EntityModel<PageData<EntityModel<UserDto>>> hateoasPage =
            hateoasHandler.wrapPageWithEntityModel(page);
    hateoasPage
            .getContent()
            .getContent()
            .forEach(
                    certificate -> certificate.add(takeUserLinks(certificate.getContent().getId())));

    hateoasPage.add(
            hateoasHandler.takeLinksForPagination(
                    UserController.class, parameter, page.getNumberOfPages()));
    hateoasPage.add(takeUsersLinks());
    return ResponseEntity.status(HttpStatus.OK).body(hateoasPage);
  }

  @GetMapping("/most-popular-tag")
  public ResponseEntity<TagDto> readMostWidelyTagFromUserWithHighestCostOrders() {
    TagDto tag = userService.takeMostWidelyTagFromUserWithHighestCostOrders();
    return ResponseEntity.status(HttpStatus.OK).body(tag);
  }

  List<Link> takeUserLinks(long id) {
    return List.of(linkTo(UserController.class).slash(id).withSelfRel());
  }

  List<Link> takeUsersLinks() {
    return List.of(
        linkTo(methodOn(UserController.class).readMostWidelyTagFromUserWithHighestCostOrders())
            .withRel("read most popular tag"));
  }
}
