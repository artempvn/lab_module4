package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.entity.Tag;
import com.epam.esm.dto.TagDto;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public class TagDaoImpl implements TagDao {

    private final SessionFactory sessionFactory;

    public TagDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public TagDto create(TagDto tagDto) {
        Tag tag = new Tag(tagDto);
        Session session = sessionFactory.getCurrentSession();
        session.save(tag);
        return new TagDto(tag);
    }

    @Override
    public Optional<TagDto> read(String name) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "from  Tag where name=:name";
        Query query = session.createQuery(hql).setParameter("name", name);
        Optional<Tag> tag = query.getResultStream().findFirst();
        return tag.map(TagDto::new);
    }
}