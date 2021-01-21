CREATE TABLE IF NOT EXISTS `gift_certificates` (
  `id` BIGINT(5) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `description` VARCHAR(1000) NULL,
  `price` DECIMAL(8,2) NULL,
  `duration` INT NULL,
  `create_date` DATETIME NULL,
  `last_update_date` DATETIME NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) );

CREATE TABLE IF NOT EXISTS `tag` (
  `id` BIGINT(5) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
UNIQUE INDEX `id_UNIQUE1` (`id` ASC) );

CREATE TABLE IF NOT EXISTS `certificates_tags` (
  `tag_id` BIGINT(5) NOT NULL,
  `certificate_id` BIGINT(5) NOT NULL,
  CONSTRAINT `certificate_fk`
    FOREIGN KEY (`certificate_id`)
    REFERENCES `gift_certificates` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `tag_fk`
    FOREIGN KEY (`tag_id`)
    REFERENCES `tag` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);