package com.epam.esm.dao;

import com.epam.esm.entity.Tag;
import com.epam.esm.entity.TagDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagDao {

  Tag create(TagDto tag);

  Optional<Tag> read(long id);

  List<Tag> readAll();

  void delete(long id);

  Optional<Tag> read(String name);
}
