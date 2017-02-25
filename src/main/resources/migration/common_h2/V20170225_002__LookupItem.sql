CREATE TABLE `lookup_item` (
  `itemKey` varchar(255) NOT NULL,
  `dateUpdated` datetime DEFAULT NULL,
  PRIMARY KEY (`itemKey`)
);

CREATE TABLE `lookup_item_attributes` (
  `itemKey` varchar(255) NOT NULL,
  `attributeValue` varchar(255) DEFAULT NULL,
  `attributeKey` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`itemKey`,`attributeKey`)
);
