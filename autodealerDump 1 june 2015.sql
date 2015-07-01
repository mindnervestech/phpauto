-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.6.19 - MySQL Community Server (GPL)
-- Server OS:                    Win32
-- HeidiSQL Version:             8.0.0.4396
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Dumping structure for table autodealer.auth_user
CREATE TABLE IF NOT EXISTS `auth_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `avatar_url` varchar(255) DEFAULT NULL,
  `provider` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=336920058 DEFAULT CHARSET=utf8;

-- Dumping data for table autodealer.auth_user: ~0 rows (approximately)
/*!40000 ALTER TABLE `auth_user` DISABLE KEYS */;
INSERT INTO `auth_user` (`id`, `email`, `first_name`, `last_name`, `password`, `avatar_url`, `provider`) VALUES
	(-1361609913, 'mindnervesdemo@gmail.com', 'mindnerve', 'tech', NULL, 'https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg?sz=50', 'google'),
	(336920057, 'sanghapal.ahankare@gmail.com', 'Sanghapal', 'Ahankare', NULL, 'https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg?sz=50', 'google');
/*!40000 ALTER TABLE `auth_user` ENABLE KEYS */;


-- Dumping structure for table autodealer.featured_image
CREATE TABLE IF NOT EXISTS `featured_image` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `path` varchar(300) DEFAULT NULL,
  `thumb_path` varchar(300) DEFAULT NULL,
  `img_name` varchar(300) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_featured_image_auth_user` (`user_id`),
  CONSTRAINT `FK_featured_image_auth_user` FOREIGN KEY (`user_id`) REFERENCES `auth_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- Dumping data for table autodealer.featured_image: ~6 rows (approximately)
/*!40000 ALTER TABLE `featured_image` DISABLE KEYS */;
INSERT INTO `featured_image` (`id`, `path`, `thumb_path`, `img_name`, `user_id`, `description`) VALUES
	(1, '\\336920057\\Featured Images\\Jellyfish.jpg', '\\336920057\\Featured Images\\thumbnail_Jellyfish.jpg', 'Jellyfish.jpg', 336920057, NULL),
	(2, '\\336920057\\Featured Images\\Tulips.jpg', '\\336920057\\Featured Images\\thumbnail_Tulips.jpg', 'Tulips.jpg', 336920057, NULL),
	(6, '\\336920057\\Featured Images\\Hydrangeas.jpg', '\\336920057\\Featured Images\\thumbnail_Hydrangeas.jpg', 'Hydrangeas.jpg', 336920057, NULL),
	(7, '\\336920057\\Featured Images\\Koala.jpg', '\\336920057\\Featured Images\\thumbnail_Koala.jpg', 'Koala.jpg', 336920057, NULL),
	(8, '\\336920057\\Featured Images\\Desert.jpg', '\\336920057\\Featured Images\\thumbnail_Desert.jpg', 'Desert.jpg', 336920057, NULL);
/*!40000 ALTER TABLE `featured_image` ENABLE KEYS */;


-- Dumping structure for table autodealer.play_evolutions
CREATE TABLE IF NOT EXISTS `play_evolutions` (
  `id` int(11) NOT NULL,
  `hash` varchar(255) NOT NULL,
  `applied_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `apply_script` text,
  `revert_script` text,
  `state` varchar(255) DEFAULT NULL,
  `last_problem` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table autodealer.play_evolutions: ~1 rows (approximately)
/*!40000 ALTER TABLE `play_evolutions` DISABLE KEYS */;
INSERT INTO `play_evolutions` (`id`, `hash`, `applied_at`, `apply_script`, `revert_script`, `state`, `last_problem`) VALUES
	(1, 'de5c75b2068e94f4ddfbc9703557a228369a9785', '2015-06-22 12:12:48', 'create table auth_user (\nid                        integer auto_increment not null,\nemail                     varchar(255),\nfirst_name                varchar(255),\nlast_name                 varchar(255),\npassword                  varchar(255),\navatar_url                varchar(255),\nprovider                  varchar(255),\nconstraint pk_auth_user primary key (id))\n;\n\ncreate table site (\nid                        bigint auto_increment not null,\nname                      varchar(255),\nconstraint pk_site primary key (id))\n;\n\ncreate table vehicle (\nid                        bigint auto_increment not null,\ncategory                  varchar(255),\nvin                       varchar(255),\nyear                      varchar(255),\nmake                      varchar(255),\nmodel                     varchar(255),\ntrim                      varchar(255),\nlabel                     varchar(255),\nstock                     varchar(255),\nmileage                   varchar(255),\ncost                      varchar(255),\nprice                     varchar(255),\nexterior_color            varchar(255),\ninterior_color            varchar(255),\ncolor_description         varchar(255),\ndoors                     varchar(255),\nstereo                    varchar(255),\nengine                    varchar(255),\nfuel                      varchar(255),\ncity_mileage              varchar(255),\nhighway_mileage           varchar(255),\nbody_style                varchar(255),\ndrivetrain                varchar(255),\ntransmission              varchar(255),\nstandard_features1        varchar(255),\nstandard_features2        varchar(255),\ndescription               varchar(255),\nmade_in                   varchar(255),\nsteering_type             varchar(255),\nanti_brake_system         varchar(255),\nheight                    varchar(255),\nlength                    varchar(255),\nwidth                     varchar(255),\nstandard_seating          varchar(255),\noptional_seating          varchar(255),\nstatus                    varchar(255),\nuser_id                   integer,\nconstraint pk_vehicle primary key (id))\n;\n\ncreate table vehicle_image (\nid                        bigint auto_increment not null,\nvin                       varchar(255),\npath                      varchar(255),\ndefault_image             tinyint(1) default 0,\nrow                       integer,\ncol                       integer,\nimg_name                  varchar(255),\nthumb_path                varchar(255),\nuser_id                   integer,\nconstraint pk_vehicle_image primary key (id))\n;\n\n\ncreate table vehicle_site (\nvehicle_id                     bigint not null,\nsite_id                        bigint not null,\nconstraint pk_vehicle_site primary key (vehicle_id, site_id))\n;\nalter table vehicle add constraint fk_vehicle_user_1 foreign key (user_id) references auth_user (id) on delete restrict on update restrict;\ncreate index ix_vehicle_user_1 on vehicle (user_id);\nalter table vehicle_image add constraint fk_vehicle_image_user_2 foreign key (user_id) references auth_user (id) on delete restrict on update restrict;\ncreate index ix_vehicle_image_user_2 on vehicle_image (user_id);\n\n\n\nalter table vehicle_site add constraint fk_vehicle_site_vehicle_01 foreign key (vehicle_id) references vehicle (id) on delete restrict on update restrict;\n\nalter table vehicle_site add constraint fk_vehicle_site_site_02 foreign key (site_id) references site (id) on delete restrict on update restrict;', 'SET FOREIGN_KEY_CHECKS=0;\n\ndrop table auth_user;\n\ndrop table site;\n\ndrop table vehicle;\n\ndrop table vehicle_site;\n\ndrop table vehicle_image;\n\nSET FOREIGN_KEY_CHECKS=1;', 'applied', '');
/*!40000 ALTER TABLE `play_evolutions` ENABLE KEYS */;


-- Dumping structure for table autodealer.site
CREATE TABLE IF NOT EXISTS `site` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- Dumping data for table autodealer.site: ~8 rows (approximately)
/*!40000 ALTER TABLE `site` DISABLE KEYS */;
INSERT INTO `site` (`id`, `name`) VALUES
	(1, 'AutoTrader.com'),
	(2, 'Cars.com'),
	(3, 'CarsGuru'),
	(4, 'TrueCar'),
	(5, 'Carfax'),
	(6, 'Craigslist ($5)'),
	(7, 'Ebay'),
	(8, 'Kelly Blue Book');
/*!40000 ALTER TABLE `site` ENABLE KEYS */;


-- Dumping structure for table autodealer.slider_image
CREATE TABLE IF NOT EXISTS `slider_image` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `path` varchar(300) DEFAULT NULL,
  `thumb_path` varchar(300) DEFAULT NULL,
  `img_name` varchar(300) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_slider_image_auth_user` (`user_id`),
  CONSTRAINT `FK_slider_image_auth_user` FOREIGN KEY (`user_id`) REFERENCES `auth_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

-- Dumping data for table autodealer.slider_image: ~9 rows (approximately)
/*!40000 ALTER TABLE `slider_image` DISABLE KEYS */;
INSERT INTO `slider_image` (`id`, `path`, `thumb_path`, `img_name`, `user_id`, `description`) VALUES
	(9, '\\336920057\\Slider Images\\Lighthouse.jpg', '\\336920057\\Slider Images\\thumbnail_Lighthouse.jpg', 'Lighthouse.jpg', 336920057, NULL),
	(10, '\\336920057\\Slider Images\\Hydrangeas.jpg', '\\336920057\\Slider Images\\thumbnail_Hydrangeas.jpg', 'Hydrangeas.jpg', 336920057, NULL),
	(11, '\\336920057\\Slider Images\\Chrysanthemum.jpg', '\\336920057\\Slider Images\\thumbnail_Chrysanthemum.jpg', 'Chrysanthemum.jpg', 336920057, NULL),
	(12, '\\336920057\\Slider Images\\Desert.jpg', '\\336920057\\Slider Images\\thumbnail_Desert.jpg', 'Desert.jpg', 336920057, NULL),
	(13, '\\336920057\\Slider Images\\Jellyfish.jpg', '\\336920057\\Slider Images\\thumbnail_Jellyfish.jpg', 'Jellyfish.jpg', 336920057, NULL),
	(14, '\\336920057\\Slider Images\\Koala.jpg', '\\336920057\\Slider Images\\thumbnail_Koala.jpg', 'Koala.jpg', 336920057, NULL),
	(15, '\\336920057\\Slider Images\\Tulips.jpg', '\\336920057\\Slider Images\\thumbnail_Tulips.jpg', 'Tulips.jpg', 336920057, 'gffgbfffff'),
	(16, '\\336920057\\Slider Images\\Penguins.jpg', '\\336920057\\Slider Images\\thumbnail_Penguins.jpg', 'Penguins.jpg', 336920057, NULL),
	(17, '\\336920057\\Slider Images\\ADSCHELA.jpg', '\\336920057\\Slider Images\\thumbnail_ADSCHELA.jpg', 'ADSCHELA.jpg', 336920057, NULL);
/*!40000 ALTER TABLE `slider_image` ENABLE KEYS */;


-- Dumping structure for table autodealer.vehicle
CREATE TABLE IF NOT EXISTS `vehicle` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `category` varchar(255) DEFAULT NULL,
  `vin` varchar(255) DEFAULT NULL,
  `year` varchar(255) DEFAULT NULL,
  `make` varchar(255) DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL,
  `trim` varchar(255) DEFAULT NULL,
  `label` varchar(255) DEFAULT NULL,
  `stock` varchar(255) DEFAULT NULL,
  `mileage` varchar(255) DEFAULT NULL,
  `cost` int(11) DEFAULT NULL,
  `price` int(11) DEFAULT NULL,
  `exterior_color` varchar(255) DEFAULT NULL,
  `interior_color` varchar(255) DEFAULT NULL,
  `color_description` varchar(255) DEFAULT NULL,
  `doors` varchar(255) DEFAULT NULL,
  `stereo` varchar(255) DEFAULT NULL,
  `engine` varchar(255) DEFAULT NULL,
  `fuel` varchar(255) DEFAULT NULL,
  `city_mileage` varchar(255) DEFAULT NULL,
  `highway_mileage` varchar(255) DEFAULT NULL,
  `body_style` varchar(255) DEFAULT NULL,
  `drivetrain` varchar(255) DEFAULT NULL,
  `transmission` varchar(255) DEFAULT NULL,
  `standard_features1` varchar(255) DEFAULT NULL,
  `standard_features2` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `made_in` varchar(255) DEFAULT NULL,
  `steering_type` varchar(255) DEFAULT NULL,
  `anti_brake_system` varchar(255) DEFAULT NULL,
  `height` varchar(255) DEFAULT NULL,
  `length` varchar(255) DEFAULT NULL,
  `width` varchar(255) DEFAULT NULL,
  `standard_seating` varchar(255) DEFAULT NULL,
  `optional_seating` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ix_vehicle_user_1` (`user_id`),
  CONSTRAINT `fk_vehicle_user_1` FOREIGN KEY (`user_id`) REFERENCES `auth_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- Dumping data for table autodealer.vehicle: ~2 rows (approximately)
/*!40000 ALTER TABLE `vehicle` DISABLE KEYS */;
INSERT INTO `vehicle` (`id`, `category`, `vin`, `year`, `make`, `model`, `trim`, `label`, `stock`, `mileage`, `cost`, `price`, `exterior_color`, `interior_color`, `color_description`, `doors`, `stereo`, `engine`, `fuel`, `city_mileage`, `highway_mileage`, `body_style`, `drivetrain`, `transmission`, `standard_features1`, `standard_features2`, `description`, `made_in`, `steering_type`, `anti_brake_system`, `height`, `length`, `width`, `standard_seating`, `optional_seating`, `status`, `user_id`) VALUES
	(1, 'Mercedez', '4F2YU09161KM33122', 'abcd', NULL, 'TRIBUTE', 'LX', 'kdjfvjkfd', '405', NULL, 5464, 987987, 'red', 'red', 'gkjbng', '4', 'dfkjvbdf', '3.0L V6 DOHC 24V', NULL, '18 miles/gallon', '24 miles/gallon', NULL, 'kfjvnjdf', 'kxjvbhkjdsf', 'lkivhkldjfnvjkldf', 'dfljvkldfnbfkj', 'kfjhvnkjdfvnldfnvkdfvnlkdfnvk', 'UNITED STATES', 'R&P', 'Non-ABS | 4-Wheel ABS', '69.90 in.', '173.00 in.', '71.90 in.', '5', '5', '', 336920057),
	(4, 'cars', 'WDDNG7KB7DA494890', '2013', 'Mercedes-Benz', 'S-Class', 'S65 AMG', 'wedr', '345', NULL, 3456, 4567, 'red', 'green', 'descr', '4', '6', '6.0L V12 SOHC 36V TURBO', '23.80 gallon', '12 miles/gallon', '19 miles/gallon', 'SEDAN 4-DR', 'treyu', 'auto', 'dfvrgvg', 'regbrt', 'ergtrrt', 'GERMANY', 'R&P', '4-Wheel ABS', '58.00 in.', '206.50 in.', '73.70 in.', '5', '6', 'Sold', 336920057);
/*!40000 ALTER TABLE `vehicle` ENABLE KEYS */;


-- Dumping structure for table autodealer.vehicle_audio
CREATE TABLE IF NOT EXISTS `vehicle_audio` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `vin` varchar(255) DEFAULT NULL,
  `path` varchar(500) DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_vehicle_audio_auth_user` (`user_id`),
  CONSTRAINT `FK_vehicle_audio_auth_user` FOREIGN KEY (`user_id`) REFERENCES `auth_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- Dumping data for table autodealer.vehicle_audio: ~2 rows (approximately)
/*!40000 ALTER TABLE `vehicle_audio` DISABLE KEYS */;
INSERT INTO `vehicle_audio` (`id`, `vin`, `path`, `file_name`, `user_id`) VALUES
	(2, 'WDDNG7KB7DA494890', '\\WDDNG7KB7DA494890-336920057\\audio\\Kalimba.mp3', 'Kalimba.mp3', 336920057),
	(3, 'WDDNG7KB7DA494890', '\\WDDNG7KB7DA494890-336920057\\audio\\Sleep Away.mp3', 'Sleep Away.mp3', 336920057);
/*!40000 ALTER TABLE `vehicle_audio` ENABLE KEYS */;


-- Dumping structure for table autodealer.vehicle_image
CREATE TABLE IF NOT EXISTS `vehicle_image` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `vin` varchar(255) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `default_image` tinyint(1) DEFAULT '0',
  `row` int(11) DEFAULT NULL,
  `col` int(11) DEFAULT NULL,
  `img_name` varchar(255) DEFAULT NULL,
  `thumb_path` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ix_vehicle_image_user_2` (`user_id`),
  CONSTRAINT `fk_vehicle_image_user_2` FOREIGN KEY (`user_id`) REFERENCES `auth_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8;

-- Dumping data for table autodealer.vehicle_image: ~8 rows (approximately)
/*!40000 ALTER TABLE `vehicle_image` DISABLE KEYS */;
INSERT INTO `vehicle_image` (`id`, `vin`, `path`, `default_image`, `row`, `col`, `img_name`, `thumb_path`, `user_id`) VALUES
	(39, 'WDDNG7KB7DA494890', '\\WDDNG7KB7DA494890-336920057\\Hydrangeas.jpg', 0, 0, 2, 'Hydrangeas.jpg', '\\WDDNG7KB7DA494890-336920057\\thumbnail_Hydrangeas.jpg', 336920057),
	(40, 'WDDNG7KB7DA494890', '\\WDDNG7KB7DA494890-336920057\\Lighthouse.jpg', 0, 0, 0, 'Lighthouse.jpg', '\\WDDNG7KB7DA494890-336920057\\thumbnail_Lighthouse.jpg', 336920057),
	(41, 'WDDNG7KB7DA494890', '\\WDDNG7KB7DA494890-336920057\\Jellyfish.jpg', 0, 0, 1, 'Jellyfish.jpg', '\\WDDNG7KB7DA494890-336920057\\thumbnail_Jellyfish.jpg', 336920057),
	(42, 'WDDNG7KB7DA494890', '\\WDDNG7KB7DA494890-336920057\\Chrysanthemum.jpg', 0, 0, 3, 'Chrysanthemum.jpg', '\\WDDNG7KB7DA494890-336920057\\thumbnail_Chrysanthemum.jpg', 336920057),
	(43, 'WDDNG7KB7DA494890', '\\WDDNG7KB7DA494890-336920057\\Koala.jpg', 0, 0, 4, 'Koala.jpg', '\\WDDNG7KB7DA494890-336920057\\thumbnail_Koala.jpg', 336920057),
	(44, 'WDDNG7KB7DA494890', '\\WDDNG7KB7DA494890-336920057\\Desert.jpg', 0, 0, 5, 'Desert.jpg', '\\WDDNG7KB7DA494890-336920057\\thumbnail_Desert.jpg', 336920057),
	(45, 'WDDNG7KB7DA494890', '\\WDDNG7KB7DA494890-336920057\\Tulips.jpg', 0, 1, 0, 'Tulips.jpg', '\\WDDNG7KB7DA494890-336920057\\thumbnail_Tulips.jpg', 336920057),
	(46, 'WDDNG7KB7DA494890', '\\WDDNG7KB7DA494890-336920057\\Penguins.jpg', 0, 1, 1, 'Penguins.jpg', '\\WDDNG7KB7DA494890-336920057\\thumbnail_Penguins.jpg', 336920057),
	(47, 'WDDNG7KB7DA494890', '\\WDDNG7KB7DA494890-336920057\\ADSCHELA.jpg', 0, 1, 2, 'ADSCHELA.jpg', '\\WDDNG7KB7DA494890-336920057\\thumbnail_ADSCHELA.jpg', 336920057);
/*!40000 ALTER TABLE `vehicle_image` ENABLE KEYS */;


-- Dumping structure for table autodealer.vehicle_site
CREATE TABLE IF NOT EXISTS `vehicle_site` (
  `vehicle_id` bigint(20) NOT NULL,
  `site_id` bigint(20) NOT NULL,
  PRIMARY KEY (`vehicle_id`,`site_id`),
  KEY `fk_vehicle_site_site_02` (`site_id`),
  CONSTRAINT `fk_vehicle_site_site_02` FOREIGN KEY (`site_id`) REFERENCES `site` (`id`),
  CONSTRAINT `fk_vehicle_site_vehicle_01` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicle` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table autodealer.vehicle_site: ~11 rows (approximately)
/*!40000 ALTER TABLE `vehicle_site` DISABLE KEYS */;
INSERT INTO `vehicle_site` (`vehicle_id`, `site_id`) VALUES
	(1, 1),
	(4, 1),
	(1, 2),
	(4, 2),
	(1, 3),
	(4, 3),
	(1, 4),
	(4, 4),
	(1, 5),
	(1, 6),
	(1, 7);
/*!40000 ALTER TABLE `vehicle_site` ENABLE KEYS */;


-- Dumping structure for table autodealer.virtual_tour
CREATE TABLE IF NOT EXISTS `virtual_tour` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `vin` varchar(255) DEFAULT NULL,
  `desktop_url` varchar(500) DEFAULT NULL,
  `mobile_url` varchar(500) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_virtual_tour_auth_user` (`user_id`),
  CONSTRAINT `FK_virtual_tour_auth_user` FOREIGN KEY (`user_id`) REFERENCES `auth_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- Dumping data for table autodealer.virtual_tour: ~0 rows (approximately)
/*!40000 ALTER TABLE `virtual_tour` DISABLE KEYS */;
INSERT INTO `virtual_tour` (`id`, `vin`, `desktop_url`, `mobile_url`, `user_id`) VALUES
	(1, 'WDDNG7KB7DA494890', 'aaa', 'bbb', 336920057);
/*!40000 ALTER TABLE `virtual_tour` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
