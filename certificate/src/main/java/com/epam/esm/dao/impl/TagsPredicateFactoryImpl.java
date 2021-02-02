package com.epam.esm.dao.impl;

import com.epam.esm.dao.PredicateFactory;
import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dao.entity.Tag;
import com.epam.esm.dto.CertificatesRequest;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class TagsPredicateFactoryImpl implements PredicateFactory {

  @Override
  public List<Predicate> buildPredicates(
      CriteriaBuilder builder, CertificatesRequest request, Root<Certificate> root) {
    List<Predicate> predicates = new ArrayList<>();
    List<String> tags = request.getTags();

    for (String tag : tags) {
      Join<Certificate, Tag> join = root.join("tags");
      predicates.add(builder.equal(join.get("name"), tag));
    }
    return predicates;
  }
}
