DROP TABLE IF EXISTS `Roles`;

CREATE TABLE `Roles` (
  `id`          int(11)     NOT NULL AUTO_INCREMENT,
  `created`     datetime    DEFAULT NULL,
  `description` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `enabled`     bit(1)      DEFAULT NULL,
  `name`        varchar(20) COLLATE utf8_bin NOT NULL,
  `updated`     datetime    DEFAULT NULL,
  PRIMARY KEY (`id`)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;