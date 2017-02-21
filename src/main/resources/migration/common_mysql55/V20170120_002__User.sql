CREATE TABLE `user` (
  `uuid` varchar(36) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(200) NOT NULL,
  `fullName` varchar(50) DEFAULT NULL,
  `jobTitle` varchar(50) DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  `emailAddress` varchar(50) DEFAULT NULL,
  `lastLoginTime` datetime DEFAULT NULL,
  `mobileNumber` varchar(20) DEFAULT NULL,
  `resultsPerPage` int(11) DEFAULT 15,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `role` (
  `role` varchar(50) NOT NULL,
  `permissionRole` bit(1) NOT NULL,
  PRIMARY KEY (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user_role` (
   `uuid` varchar(36) NOT NULL,
  `role` varchar(50) NOT NULL,
  `user` varchar(36) NOT NULL,
  PRIMARY KEY (`uuid`),
  CONSTRAINT `UserRoleFK_user` FOREIGN KEY (`user`) REFERENCES `user` (`uuid`),
  CONSTRAINT `UserRoleFK_role` FOREIGN KEY (`role`) REFERENCES `role` (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO `user` (`uuid`, `username`, `password`, `enabled`) VALUES ('60c07adc-dd75-11e6-8e71-0025ab69e4c2', 'admin', '$2a$10$E6Bpa4EsexSuJclB.87ziupCcY6xBIq0baVYxUwA0.6AtQlO/qGNq', 1);
INSERT INTO `role` (`role`, `permissionRole`) VALUES ('USER', 1);
INSERT INTO `user_role` (`uuid`, `role`, `user`) VALUES ('30805497-dd76-11e6-8e71-0025ab69e4c2', 'USER', '60c07adc-dd75-11e6-8e71-0025ab69e4c2');