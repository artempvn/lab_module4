package com.epam.esm.web.rest;

import com.epam.esm.dto.PageData;
import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.dto.TagAction;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
   * Read tag response entity.
   *
   * @param id the id
   * @return the response entity
   */
  @GetMapping("/{id}")
  public ResponseEntity<EntityModel<TagDto>> readTag(@PathVariable long id) {
    EntityModel<TagDto> tag = EntityModel.of(tagService.read(id));
    tag.add(buildTagLinks(id));
    return ResponseEntity.status(HttpStatus.OK).body(tag);
  }

  /**
   * Read tags response entity.
   *
   * @param parameter the parameter
   * @return the response entity
   */
  @GetMapping
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
   * Create tag tag dto.
   *
   * @param tag the tag
   * @return the tag dto
   */
  @PostMapping
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
  @PostMapping("/action")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<Void> processTagAction(@RequestBody TagAction action) {
    tagService.processTagAction(action);
    return ResponseEntity.ok().build();
  }

  /**
   * Delete tag.
   *
   * @param id the id
   */
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteTag(@PathVariable long id) {
    tagService.delete(id);
  }

  /**
   * Build tag links list.
   *
   * @param id the id
   * @return the list
   */
  List<Link> buildTagLinks(long id) {
    return List.of(
        linkTo(TagResource.class).slash(id).withSelfRel(),
        linkTo(TagResource.class).slash(id).withRel("delete").withName("delete tag"));
  }

  /**
   * Build tags links list.
   *
   * @return the list
   */
  List<Link> buildTagsLinks() {
    return List.of(
        linkTo(TagResource.class).withRel("post").withName("create tag"),
        linkTo(methodOn(TagResource.class).processTagAction(new TagAction()))
            .withRel("post")
            .withName("tag action with certificate"));
  }
}
