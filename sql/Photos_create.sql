DROP TABLE IF EXISTS `Photos`;

CREATE TABLE `Photos` (
  `id`              int(11)     NOT NULL AUTO_INCREMENT,
  `addDate`         datetime    DEFAULT NULL,
  `deleted`         bit(1)      DEFAULT NULL,
  `description`     varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `fileName`        varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `title`           varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `album_id`        int(11)     DEFAULT NULL,
  `user_id`         int(11)     DEFAULT NULL,
  `positionInAlbum` int(11)     DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8E7174A1AB25F3FC` (`user_id`),
  KEY `FK8E7174A1909E48F3` (`album_id`)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;