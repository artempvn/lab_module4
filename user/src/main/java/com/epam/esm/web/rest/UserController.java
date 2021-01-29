package com.epam.esm.web.rest;

import com.epam.esm.dto.UserDtoFull;
import com.epam.esm.dto.UserDto;
import com.epam.esm.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserDtoFull> readUser(@PathVariable long id) {
    UserDtoFull user = userService.read(id);
    return ResponseEntity.status(HttpStatus.OK).body(user);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<UserDto> readUsers() {
    return userService.readAll();
  }
}
