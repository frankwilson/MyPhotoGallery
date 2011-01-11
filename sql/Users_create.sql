DROP TABLE IF EXISTS `Users`;

CREATE TABLE `Users` (
  `id`                  int(11)                                 NOT NULL AUTO_INCREMENT,
  `activationCode`      varchar(255)    COLLATE utf8_bin    DEFAULT NULL,
  `blocked`             bit(1)                              DEFAULT NULL,
  `email`               varchar(255)    COLLATE utf8_bin    DEFAULT NULL,
  `firstName`           varchar(255)    COLLATE utf8_bin    DEFAULT NULL,
  `lastName`            varchar(255)    COLLATE utf8_bin    DEFAULT NULL,
  `login`               varchar(255)    COLLATE utf8_bin    DEFAULT NULL,
  `nickName`            varchar(255)    COLLATE utf8_bin    DEFAULT NULL,
  `password`            varchar(255)    COLLATE utf8_bin    DEFAULT NULL,
  `temporary`           bit(1)                              DEFAULT NULL,
  `created`             datetime                            DEFAULT NULL,
  `updated`             datetime                            DEFAULT NULL,
  PRIMARY KEY (`id`)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;