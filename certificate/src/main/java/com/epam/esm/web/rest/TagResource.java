package com.epam.esm.web.rest;

import com.epam.esm.dto.PageData;
import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.dto.TagAction;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/** The type Tag resource. */
@RestController
@RequestMapping("/tags")
public class TagResource {

  private final TagService tagService;
  private final HateoasHandler hateoasHandler;

  /**
   * Instantiates a new Tag resource.
   *
   * @param tagService the tag service
   * @param hateoasHandler the hateoas handler
   */
  public TagResource(TagService tagService, HateoasHandler hateoasHandler) {
    this.tagService = tagService;
    this.hateoasHandler = hateoasHandler;
  }

  /**
   * Read tag by id.
   *
   * @param id the id of tag
   * @return the response entity of tag
   */
  @Secured({"ROLE_ADMIN", "ROLE_USER"})
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<EntityModel<TagDto>> readTag(@PathVariable long id) {
    EntityModel<TagDto> tag = EntityModel.of(tagService.read(id));
    tag.add(buildTagLinks(id));
    return ResponseEntity.status(HttpStatus.OK).body(tag);
  }

  /**
   * Read tags with pagination parameters.
   *
   * @param parameter the parameter of pagination
   * @return the response entity of found tags
   */
  @Secured({"ROLE_ADMIN", "ROLE_USER"})
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<EntityModel<PageData<EntityModel<TagDto>>>> readTags(
      @Valid PaginationParameter parameter) {
    PageData<TagDto> page = tagService.readAll(parameter);

    EntityModel<PageData<EntityModel<TagDto>>> hateoasPage =
        hateoasHandler.wrapPageWithEntityModel(page);

    for (EntityModel<TagDto> tag : hateoasPage.getContent().getContent()) {
      long id = tag.getContent().getId();
      List<Link> links = buildTagLinks(id);
      tag.add(links);
    }

    hateoasPage.add(
        hateoasHandler.buildLinksForPagination(
            TagResource.class, parameter, page.getNumberOfPages()));
    hateoasPage.add(buildTagsLinks());
    return ResponseEntity.status(HttpStatus.OK).body(hateoasPage);
  }

  /**
   * Persist tag.
   *
   * @param tag the tag
   * @return saved tag
   */
  @Secured("ROLE_ADMIN")
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public TagDto createTag(@RequestBody @Valid TagDto tag) {
    return tagService.create(tag);
  }

  /**
   * Process tag action response entity.
   *
   * @param action the action
   * @return the response entity
   */
  @Secured("ROLE_ADMIN")
  @PostMapping(value = "/action", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<Void> processTagAction(@RequestBody TagAction action) {
    tagService.processTagAction(action);
    return ResponseEntity.ok().build();
  }

  /**
   * Delete tag by id.
   *
   * @param id the id of tag
   */
  @Secured("ROLE_ADMIN")
  @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteTag(@PathVariable long id) {
    tagService.delete(id);
  }

  /**
   * Build tag links list.
   *
   * @param id the id of tag
   * @return the list of links
   */
  List<Link> buildTagLinks(long id) {
    return List.of(
        linkTo(TagResource.class).slash(id).withSelfRel(),
        linkTo(TagResource.class).slash(id).withRel("delete").withName("delete tag"));
  }

  /**
   * Build tags links list.
   *
   * @return the list of links
   */
  List<Link> buildTagsLinks() {
    return List.of(
        linkTo(TagResource.class).withRel("post").withName("create tag"),
        linkTo(methodOn(TagResource.class).processTagAction(new TagAction()))
            .withRel("post")
            .withName("tag action with certificate"));
  }
}
