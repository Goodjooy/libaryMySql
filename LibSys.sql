-- MySQL dump 10.13  Distrib 8.0.22, for Win64 (x86_64)
--
-- Host: localhost    Database: libSys
-- ------------------------------------------------------
-- Server version	8.0.22

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `author`
--

DROP TABLE IF EXISTS `author`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `author` (
  `p_id` int unsigned NOT NULL AUTO_INCREMENT,
  `author_name` varchar(25) NOT NULL,
  PRIMARY KEY (`p_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `author`
--

LOCK TABLES `author` WRITE;
/*!40000 ALTER TABLE `author` DISABLE KEYS */;
INSERT INTO `author` VALUES (1,'鎴愭澃'),(2,'寮犻'),(3,'鐭抽偟缇?),(4,'鏋楃倻'),(5,'涓嶆槸鍚ч樋sir'),(6,'dssdfsdsd');
/*!40000 ALTER TABLE `author` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `book`
--

DROP TABLE IF EXISTS `book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book` (
  `b_id` int unsigned NOT NULL AUTO_INCREMENT,
  `book_name` varchar(100) NOT NULL,
  `is_rent` tinyint(1) NOT NULL,
  `author_id` int unsigned DEFAULT NULL,
  PRIMARY KEY (`b_id`),
  KEY `author_id` (`author_id`),
  CONSTRAINT `book_ibfk_1` FOREIGN KEY (`author_id`) REFERENCES `author` (`p_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book`
--

LOCK TABLES `book` WRITE;
/*!40000 ALTER TABLE `book` DISABLE KEYS */;
INSERT INTO `book` VALUES (1,'銆婃満鍣ㄥ涔犲疄鎴樸€?,0,1),(3,'銆婁粠鍏ラ棬鍒扮簿閫氱殑濂宠璺€?,0,4),(4,'銆婁吉闊崇殑瀹炴垬鍩虹銆?,0,4),(5,'銆婃灄鍝ュ摜鐨勯泴鍫曚箣璺€?,0,4),(6,'銆婄棝鑻﹀浜嗭紝灏卞揩涔愪簡銆?,0,4),(7,'銆婂彧瑕佸涓嶆锛屽氨寰€姝婚噷瀛︺€?,0,4),(8,'銆婂ぇ璇濆摝鏁版嵁缁撴瀯銆?,0,5),(9,'銆婁粈涔堥淇偧鎵嬪唽銆?,0,5),(10,'銆婂ぇ璇濆摝ds 鏁版嵁缁撴瀯銆?,0,5),(11,'銆妔ds s銆?,0,6);
/*!40000 ALTER TABLE `book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rent_record`
--

DROP TABLE IF EXISTS `rent_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rent_record` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `book_id` int unsigned DEFAULT NULL,
  `user_id` int unsigned DEFAULT NULL,
  `start_data` date DEFAULT NULL,
  `return_data` date DEFAULT NULL,
  `is_close` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `book_id` (`book_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `rent_record_ibfk_1` FOREIGN KEY (`book_id`) REFERENCES `book` (`b_id`),
  CONSTRAINT `rent_record_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rent_record`
--

LOCK TABLES `rent_record` WRITE;
/*!40000 ALTER TABLE `rent_record` DISABLE KEYS */;
INSERT INTO `rent_record` VALUES (1,1,1,'2020-01-14','2020-04-12',1),(2,5,1,'2020-12-22','2020-12-26',1),(3,5,1,'2020-12-22','2020-12-26',1);
/*!40000 ALTER TABLE `rent_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `uid` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(25) NOT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Johe');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-12-22 12:26:59
