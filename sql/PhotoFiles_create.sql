DROP TABLE IF EXISTS `PhotoFiles`;

CREATE TABLE `PhotoFiles` (
  `id`              int(11)     NOT NULL AUTO_INCREMENT,
  `deleted`         bit(1)      DEFAULT NULL,
  `fileAddDate`     datetime    DEFAULT NULL,
  `filename`        varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `photoHeight`     int(11)     DEFAULT NULL,
  `photoWidth`      int(11)     DEFAULT NULL,
  `parentPhoto_id`  int(11)     DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK572B77856CBF4CBD` (`parentPhoto_id`)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;