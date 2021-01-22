package com.epam.esm.dao;

import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagDao {

  Tag create(Tag tag);

  Optional<Tag> read(long id);

  List<Tag> readAll();

  int delete(long id);

  Optional<Tag> read(String name);
}
