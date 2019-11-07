/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50711
 Source Host           : localhost:3306
 Source Schema         : tx-manager

 Target Server Type    : MySQL
 Target Server Version : 50711
 File Encoding         : 65001

 Date: 07/11/2019 19:56:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for hibernate_sequence
-- ----------------------------
DROP TABLE IF EXISTS `hibernate_sequence`;
CREATE TABLE `hibernate_sequence`  (
  `next_val` bigint(20) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of hibernate_sequence
-- ----------------------------
INSERT INTO `hibernate_sequence` VALUES (9);

-- ----------------------------
-- Table structure for t_logger
-- ----------------------------
DROP TABLE IF EXISTS `t_logger`;
CREATE TABLE `t_logger`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `unit_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `tag` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `content` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_time` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_tx_exception
-- ----------------------------
DROP TABLE IF EXISTS `t_tx_exception`;
CREATE TABLE `t_tx_exception`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `unit_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `mod_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `transaction_state` tinyint(4) NULL DEFAULT NULL,
  `registrar` tinyint(4) NULL DEFAULT NULL,
  `remark` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ex_state` tinyint(4) NULL DEFAULT NULL COMMENT '0 未解决 1已解决',
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_tx_exception
-- ----------------------------
INSERT INTO `t_tx_exception` VALUES (1, '5180b875129537', NULL, 'student', 1, 2, NULL, 0, '2019-10-12 17:05:01');
INSERT INTO `t_tx_exception` VALUES (2, '5180b875129537', NULL, 'student', 1, 2, NULL, 0, '2019-10-12 17:05:01');
INSERT INTO `t_tx_exception` VALUES (3, '520b0330116537', NULL, 'student', 1, 2, NULL, 0, '2019-10-14 09:21:39');
INSERT INTO `t_tx_exception` VALUES (4, '520b0330116537', NULL, 'student', 1, 2, NULL, 0, '2019-10-14 09:21:39');
INSERT INTO `t_tx_exception` VALUES (5, '5221b3d481a537', NULL, 'student', 1, 2, NULL, 0, '2019-10-14 15:58:06');
INSERT INTO `t_tx_exception` VALUES (6, '5221b3d481a537', NULL, 'student', 1, 2, NULL, 0, '2019-10-14 15:58:06');
INSERT INTO `t_tx_exception` VALUES (7, '52220da871a537', NULL, 'student', 1, 2, NULL, 0, '2019-10-14 16:04:33');
INSERT INTO `t_tx_exception` VALUES (8, '52220da871a537', NULL, 'student', 1, 2, NULL, 0, '2019-10-14 16:04:33');

SET FOREIGN_KEY_CHECKS = 1;
