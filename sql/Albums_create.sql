CREATE TABLE  `gallery`.`Albums` (
  `id`          int(11)         NOT NULL AUTO_INCREMENT,
  `user`        int(11)         DEFAULT NULL,
  `title`       varchar(128)    DEFAULT NULL,
  `description` varchar(255)    DEFAULT NULL,
  `preview`     int(11)         DEFAULT NULL,
  `addDate`     datetime        DEFAULT NULL,
  PRIMARY KEY (`id`)
)
ENGINE=InnoDB DEFAULT CHARSET=UTF8;