package com.epam.esm.web.rest;

import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.dto.TagAction;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagService;
import org.springframework.hateoas.CollectionModel;
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

  public TagController(TagService tagService) {
    this.tagService = tagService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<TagDto> readTag(@PathVariable long id) {
    TagDto tag = tagService.read(id);
    Link selfLink = linkTo(TagController.class).slash(id).withSelfRel();
    tag.add(selfLink);
    return ResponseEntity.status(HttpStatus.OK).body(tag);
  }

  @GetMapping(produces = {"application/hal+json"})
  @ResponseStatus(HttpStatus.OK)
  public CollectionModel<TagDto> readTags(@Valid PaginationParameter parameter) {
    List<TagDto> tags = tagService.readAll(parameter);
    tags.forEach(tag -> tag.add(linkTo(TagController.class).slash(tag.getId()).withSelfRel()));

    Link selfLink = linkTo(TagController.class).withSelfRel();
    ResponseEntity<Void> methodLinkBuilder =
        methodOn(TagController.class).processTagAction(new TagAction());
    Link tagAction = linkTo(methodLinkBuilder).withRel("tag action with certificate");
    return CollectionModel.of(tags, selfLink, tagAction);
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
    tagService.delete(id);
  }
}
