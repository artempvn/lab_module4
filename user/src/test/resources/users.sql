CREATE TABLE IF NOT EXISTS `users` (
  `id` BIGINT(5) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(25) NULL,
  `surname` VARCHAR(25) NULL,
  PRIMARY KEY (`id`),
UNIQUE INDEX `id_UNIQUE1` (`id` ASC) );

CREATE TABLE IF NOT EXISTS `certificates_backup` (
  `id` BIGINT(5) NOT NULL AUTO_INCREMENT,
  `previous_id` BIGINT(5) NULL,
  `price` DECIMAL(8,2) NULL,
  `order_id` BIGINT(5) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) )
   CONSTRAINT `order_id_fk`
    FOREIGN KEY (`order_id`)
    REFERENCES `orders` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE IF NOT EXISTS `tags_backup` (
  `id` BIGINT(5) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
UNIQUE INDEX `id_UNIQUE1` (`id` ASC) );

CREATE TABLE IF NOT EXISTS `orders` (
  `id` BIGINT(5) NOT NULL AUTO_INCREMENT,
  `create_date` DATETIME NULL,
  `user_id` BIGINT(5) NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `user_fk`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE IF NOT EXISTS `certificates_tags_backup` (
  `tag_id` BIGINT(5) NOT NULL,
  `certificate_id` BIGINT(5) NOT NULL,
  CONSTRAINT `cert_backup_fk`
    FOREIGN KEY (`certificate_id`)
    REFERENCES `certificates_backup` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `tag_backup_fk`
    FOREIGN KEY (`tag_id`)
    REFERENCES `tags_backup` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
