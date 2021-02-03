package com.epam.esm.web.rest;

import com.epam.esm.dto.PageData;
import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.dto.TagAction;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.ResourceIsBoundException;
import com.epam.esm.service.TagService;
import com.epam.esm.web.service.HateoasHandler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/tags")
public class TagController {

  private final TagService tagService;
  private final HateoasHandler hateoasHandler;

  public TagController(TagService tagService, HateoasHandler hateoasHandler) {
    this.tagService = tagService;
    this.hateoasHandler = hateoasHandler;
  }

  @GetMapping("/{id}")
  public ResponseEntity<EntityModel<TagDto>> readTag(@PathVariable long id) {
    EntityModel<TagDto> tag = EntityModel.of(tagService.read(id));
    tag.add(takeTagLinks(id));
    return ResponseEntity.status(HttpStatus.OK).body(tag);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<EntityModel<PageData<EntityModel<TagDto>>>> readTags(
      @Valid PaginationParameter parameter) {
    PageData<TagDto> page = tagService.readAll(parameter);

    EntityModel<PageData<EntityModel<TagDto>>> hateoasPage =
        hateoasHandler.wrapPageWithEntityModel(page);
    hateoasPage
        .getContent()
        .getContent()
        .forEach(tag -> tag.add(takeTagLinks(tag.getContent().getId())));

    hateoasPage.add(
        hateoasHandler.takeLinksForPagination(
            TagController.class, parameter, page.getNumberOfPages()));
    hateoasPage.add(takeTagsLinks());
    return ResponseEntity.status(HttpStatus.OK).body(hateoasPage);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public TagDto createTag(@RequestBody @Valid TagDto tag) {
    return tagService.create(tag);
  }

  @PostMapping("/action")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<Void> processTagAction(@RequestBody TagAction action) {
    tagService.processTagAction(action);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteTag(@PathVariable long id) {
    try {
      tagService.delete(id);
    } catch (Exception ex) {
      throw ResourceIsBoundException.isBound(id).get();
    }
  }

  List<Link> takeTagLinks(long id) {
    return List.of(
        linkTo(TagController.class).slash(id).withSelfRel(),
        linkTo(TagController.class).slash(id).withRel("delete tag"));
  }

  List<Link> takeTagsLinks() {
    return List.of(
        linkTo(methodOn(TagController.class).processTagAction(new TagAction()))
            .withRel("tag action with certificate"));
  }
}
