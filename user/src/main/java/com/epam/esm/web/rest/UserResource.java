package com.epam.esm.web.rest;

import com.epam.esm.dto.*;
import com.epam.esm.service.UserService;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/** The type User resource. */
@RestController
@RequestMapping("/users")
public class UserResource {

  private final UserService userService;
  private final HateoasHandler hateoasHandler;

  /**
   * Instantiates a new User resource.
   *
   * @param userService the user service
   * @param hateoasHandler the hateoas handler
   */
  public UserResource(UserService userService, HateoasHandler hateoasHandler) {
    this.userService = userService;
    this.hateoasHandler = hateoasHandler;
  }

  /**
   * Read user response entity.
   *
   * @param id the id
   * @return the response entity
   */
  @GetMapping("/{id}")
  public ResponseEntity<EntityModel<UserDtoWithOrders>> readUser(@PathVariable long id) {
    EntityModel<UserDtoWithOrders> user = EntityModel.of(userService.read(id));
    user.add(buildUserLinks(user.getContent().getId()));
    return ResponseEntity.status(HttpStatus.OK).body(user);
  }

  /**
   * Read users response entity.
   *
   * @param parameter the parameter of pagination
   * @return the response entity
   */
  @GetMapping
  public ResponseEntity<EntityModel<PageData<EntityModel<UserDto>>>> readUsers(
      @Valid PaginationParameter parameter) {
    PageData<UserDto> page = userService.readAll(parameter);

    EntityModel<PageData<EntityModel<UserDto>>> hateoasPage =
        hateoasHandler.wrapPageWithEntityModel(page);

    for (EntityModel<UserDto> user : hateoasPage.getContent().getContent()) {
      long id = user.getContent().getId();
      List<Link> links = buildUserLinks(id);
      user.add(links);
    }

    hateoasPage.add(
        hateoasHandler.buildLinksForPagination(
            UserResource.class, parameter, page.getNumberOfPages()));
    hateoasPage.add(buildUsersLinks());
    return ResponseEntity.status(HttpStatus.OK).body(hateoasPage);
  }

  /**
   * Read most widely tag from user with highest cost orders response entity.
   *
   * @return the response entity
   */
  @GetMapping("/most-popular-tag")
  public ResponseEntity<TagDto> readMostWidelyTagFromUserWithHighestCostOrders() {
    TagDto tag = userService.takeMostWidelyTagFromUserWithHighestCostOrders();
    return ResponseEntity.status(HttpStatus.OK).body(tag);
  }

  /**
   * Build user links list.
   *
   * @param id the id
   * @return the list
   */
  List<Link> buildUserLinks(long id) {
    return List.of(linkTo(UserResource.class).slash(id).withSelfRel());
  }

  /**
   * Build users links list.
   *
   * @return the list
   */
  List<Link> buildUsersLinks() {
    return List.of(
        linkTo(methodOn(UserResource.class).readMostWidelyTagFromUserWithHighestCostOrders())
            .withRel("get")
            .withName("read most popular tag"));
  }
}
