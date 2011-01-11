DROP TABLE IF EXISTS `UserRoles`;

CREATE TABLE `UserRoles` (
  `roleId`      int(11) NOT NULL,
  `userId`      int(11) NOT NULL,
  PRIMARY KEY (`userId`,`roleId`),
  KEY `FK8AF579927CC0E2A9` (`roleId`),
  CONSTRAINT `FK8AF579927CC0E2A9` FOREIGN KEY (`roleId`) REFERENCES `Roles` (`id`)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;