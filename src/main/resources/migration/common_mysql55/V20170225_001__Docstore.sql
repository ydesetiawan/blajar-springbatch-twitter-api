CREATE TABLE `docstore` (
  `uuid` varchar(36) NOT NULL,
  `reference` varchar(50) NOT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `docstore_details` (
  `uuid` varchar(36) NOT NULL,
  `companyCity` varchar(35) DEFAULT NULL,
  `companyCountry` varchar(40) DEFAULT NULL,
  `companyCountryISO` varchar(3) DEFAULT NULL,
  `companyLanguageISO` varchar(3) DEFAULT NULL,
  `companyName` varchar(80) DEFAULT NULL,
  `companyPostalCode` varchar(80) DEFAULT NULL,
  `companyStreet1` varchar(80) DEFAULT NULL,
  `companyStreet2` varchar(80) DEFAULT NULL,
  `companyRegistrationNumber` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user_docstore_link` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `lastSelectedTime` datetime DEFAULT NULL,
  `docstore` varchar(36) NOT NULL,
  `user` varchar(36) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_d3opmprv4fa0jusaqlosgbuog` (`user`,`docstore`),
  KEY `FK_76piv7893ve2kk64bjuxfvnpn` (`docstore`),
  CONSTRAINT `FK_htmu961ndqi64pho58qe0i6sp` FOREIGN KEY (`user`) REFERENCES `user` (`uuid`),
  CONSTRAINT `FK_76piv7893ve2kk64bjuxfvnpn` FOREIGN KEY (`docstore`) REFERENCES `docstore` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `docstore_relation` (
  `uuid` varchar(36) NOT NULL,
  `partyDocstore` varchar(36) NOT NULL,
  `principalDocstore` varchar(36) NOT NULL,
  `relationType` varchar(50) NOT NULL,
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `UK_docstoreRelation` (`principalDocstore`,`partyDocstore`,`relationType`),
  KEY `DocstoreRelationFKParty_docstore` (`partyDocstore`),
  KEY `DocstoreRelationFKPrincipal_docstore` (`principalDocstore`),
  CONSTRAINT `DocstoreRelationFKPrincipal_docstore` FOREIGN KEY (`principalDocstore`) REFERENCES `docstore` (`uuid`),
  CONSTRAINT `DocstoreRelationFKParty_docstore` FOREIGN KEY (`partyDocstore`) REFERENCES `docstore` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
