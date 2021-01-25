package com.epam.esm.dao;

import com.epam.esm.entity.Tag;
import com.epam.esm.entity.TagDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagDao {

  TagDto create(TagDto tag);

  Optional<TagDto> read(long id);

  List<TagDto> readAll();

  void delete(long id);

  Optional<TagDto> read(String name);
}
