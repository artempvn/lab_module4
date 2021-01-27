CREATE TABLE IF NOT EXISTS `users` (
  `id` BIGINT(5) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(25) NULL,
  `surname` VARCHAR(25) NULL,
  PRIMARY KEY (`id`),
UNIQUE INDEX `id_UNIQUE1` (`id` ASC) );