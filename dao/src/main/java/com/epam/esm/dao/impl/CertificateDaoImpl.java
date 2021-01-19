package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.SqlHandler;
import com.epam.esm.entity.*;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class CertificateDaoImpl implements CertificateDao {
  private static final String SQL_CREATE =
      "INSERT INTO gift_certificates(name,description,price,duration, create_date,last_update_date) VALUES (?,?,?,?,?,?)";
  private static final String SQL_READ =
      "SELECT id,name,description,price,duration, create_date,last_update_date FROM gift_certificates WHERE id=?";
  private static final String SQL_READ_ALL =
      "SELECT id,name,description,price,duration, create_date,last_update_date FROM gift_certificates";
  private static final String SQL_UPDATE =
      "UPDATE gift_certificates SET name=?,description=?,price=?,duration=?, create_date=?,last_update_date=? WHERE id=?";
  private static final String SQL_DELETE = "DELETE FROM gift_certificates WHERE id=?";
  private static final String SQL_ADD_TAG =
      "INSERT INTO certificates_tags(tag_id,certificate_id) VALUES (?,?)";
  private static final String SQL_REMOVE_TAG =
      "DELETE FROM certificates_tags WHERE tag_id=? AND certificate_id=? ";
  private static final String SQL_READ_BONDING_TAGS =
      "SELECT id,name FROM tag JOIN certificates_tags ON tag_id=id WHERE certificate_id=?";
  private static final String SQL_DELETE_BONDING_TAGS_BY_TAG_ID =
      "DELETE FROM certificates_tags WHERE tag_id=?";
  private static final String SQL_DELETE_BONDING_TAGS_BY_CERTIFICATE_ID =
      "DELETE FROM certificates_tags WHERE certificate_id=?";
  private static final RowMapper<Certificate> CERTIFICATE_ROW_MAPPER =
      (rs, rowNum) -> {
        Certificate certificate = new Certificate();
        certificate.setId(rs.getLong(1));
        certificate.setName(rs.getString(2));
        certificate.setDescription(rs.getString(3));
        Double price = rs.getObject(4) == null ? null : rs.getDouble(4);
        certificate.setPrice(price);
        Integer duration = rs.getObject(5) == null ? null : rs.getInt(5);
        certificate.setDuration(duration);
        certificate.setCreateDate(rs.getObject(6, LocalDateTime.class));
        certificate.setLastUpdateDate(rs.getObject(7, LocalDateTime.class));
        return certificate;
      };
  private static final String SQL_KEY = "sql";
  private static final String ARGS_KEY = "args";
  private final JdbcTemplate jdbcTemplate;
  private final SqlHandler sqlHandler;

  public CertificateDaoImpl(JdbcTemplate jdbcTemplate, SqlHandler sqlHandler) {
    this.sqlHandler = sqlHandler;
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public Certificate create(Certificate certificate) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(
        connection -> {
          PreparedStatement ps =
              connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
          ps.setString(1, certificate.getName());
          ps.setString(2, certificate.getDescription());
          ps.setObject(3, certificate.getPrice());
          ps.setObject(4, certificate.getDuration());
          ps.setObject(5, certificate.getCreateDate());
          ps.setObject(6, certificate.getLastUpdateDate());
          return ps;
        },
        keyHolder);
    certificate.setId(keyHolder.getKey().longValue());
    return certificate;
  }

  @Override
  public Optional<Certificate> read(long id) {
    return jdbcTemplate.queryForStream(SQL_READ, CERTIFICATE_ROW_MAPPER, id).findAny();
  }

  @Override
  public List<Certificate> readAll(CertificatesRequest parameter) {
    SqlData sqlData = sqlHandler.generateSqlDataForReadAllRequest(parameter);
    return jdbcTemplate.query(
        sqlData.getRequest(), CERTIFICATE_ROW_MAPPER, sqlData.getArgs().toArray());
  }

  @Override
  public int update(Certificate certificate) {
    return jdbcTemplate.update(
        SQL_UPDATE,
        certificate.getName(),
        certificate.getDescription(),
        certificate.getPrice(),
        certificate.getDuration(),
        certificate.getCreateDate(),
        certificate.getLastUpdateDate(),
        certificate.getId());
  }

  @Override
  public int delete(long id) {
    return jdbcTemplate.update(SQL_DELETE, id);
  }

  @Override
  public void addTag(long tagId, long certificateId) {
    jdbcTemplate.update(SQL_ADD_TAG, tagId, certificateId);
  }

  @Override
  public int removeTag(long tagId, long certificateId) {
    return jdbcTemplate.update(SQL_REMOVE_TAG, tagId, certificateId);
  }

  @Override
  public List<Tag> readCertificateTags(long certificateId) {
    return jdbcTemplate.query(
        SQL_READ_BONDING_TAGS, new BeanPropertyRowMapper<>(Tag.class), certificateId);
  }

  @Override
  public void deleteCertificateTagsByTagId(long tagId) {
    jdbcTemplate.update(SQL_DELETE_BONDING_TAGS_BY_TAG_ID, tagId);
  }

  @Override
  public void deleteCertificateTagsByCertificateId(long certificateId) {
    jdbcTemplate.update(SQL_DELETE_BONDING_TAGS_BY_CERTIFICATE_ID, certificateId);
  }

  @Override
  public int updatePatch(CertificatePatch certificate) {
    SqlData sqlData = sqlHandler.generateSqlDataForUpdateRequest(certificate);
    return jdbcTemplate.update(sqlData.getRequest(), sqlData.getArgs().toArray());
  }
}
