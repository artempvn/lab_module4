package com.epam.esm.dao;

import com.epam.esm.dto.TagDto;

import java.util.Optional;

public interface TagDao {

  TagDto create(TagDto tag);

  Optional<TagDto> read(String name);
}
