package com.epam.esm.controller;

import com.epam.esm.entity.TagAction;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/** The type Tag controller. */
@RestController
@RequestMapping("/tags")
public class TagController {

  private final TagService tagService;

  /**
   * Instantiates a new Tag controller.
   *
   * @param tagService the tag service
   */
  public TagController(TagService tagService) {
    this.tagService = tagService;
  }

  /**
   * Read tag response entity.
   *
   * @param id the id
   * @return the response entity
   */
  @GetMapping("/{id}")
  public ResponseEntity<TagDto> readTag(@PathVariable long id) {
    TagDto tag = tagService.read(id);
    return ResponseEntity.status(HttpStatus.OK).body(tag);
  }

  /**
   * Read tags list.
   *
   * @return the list
   */
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<TagDto> readTags() {
    return tagService.readAll();
  }

  /**
   * Create tag tag.
   *
   * @param tag the tag
   * @return the tag
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
}
