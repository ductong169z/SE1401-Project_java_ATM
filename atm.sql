/*
 Navicat Premium Data Transfer

 Source Server         : atm
 Source Server Type    : MySQL
 Source Server Version : 100138
 Source Host           : localhost:3306
 Source Schema         : atm

 Target Server Type    : MySQL
 Target Server Version : 100138
 File Encoding         : 65001

 Date: 09/12/2019 19:05:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `id` int(11) NOT NULL,
  `slug` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Records of roles
-- ----------------------------
BEGIN;
INSERT INTO `roles` VALUES (1, 'admin', 'Admin'), (2, 'user', 'User');
COMMIT;

-- ----------------------------
-- Table structure for setting
-- ----------------------------
DROP TABLE IF EXISTS `setting`;
CREATE TABLE `setting`  (
  `deposit_lim` bigint(255) NULL DEFAULT NULL COMMENT 'tien gui limit',
  `num_deposit_lim` int(255) NULL DEFAULT NULL COMMENT 'so tien gui limit',
  `withdraw_lim` bigint(255) NULL DEFAULT NULL,
  `num_withdraw_lim` int(255) NULL DEFAULT NULL,
  `num_trans_display` int(255) NULL DEFAULT NULL,
  `master_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `id` int(11) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Records of setting
-- ----------------------------
BEGIN;
INSERT INTO `setting` VALUES (25000, 5, 25000, 10, 5, 'wibu1234', 1);
COMMIT;

-- ----------------------------
-- Table structure for user_deposit
-- ----------------------------
DROP TABLE IF EXISTS `user_deposit`;
CREATE TABLE `user_deposit`  (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NULL DEFAULT NULL,
  `deposit_money` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `created_at` timestamp(0) NULL DEFAULT NULL,
  `type` int(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Records of user_deposit
-- ----------------------------
BEGIN;
INSERT INTO `user_deposit` VALUES (3, 16, '25000', '2019-12-08 00:00:00', 0), (4, 16, '25000', '2019-12-09 00:00:00', 0), (5, 15, '25000', '2019-12-09 00:00:00', 0), (6, 16, '20000', '2019-12-09 00:00:00', 0), (7, 16, '10000', '2019-12-09 00:00:00', 0);
COMMIT;

-- ----------------------------
-- Table structure for user_money
-- ----------------------------
DROP TABLE IF EXISTS `user_money`;
CREATE TABLE `user_money`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NULL DEFAULT NULL,
  `total_money` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Records of user_money
-- ----------------------------
BEGIN;
INSERT INTO `user_money` VALUES (6, 13, '25000'), (7, 14, '0'), (8, 15, '25000'), (9, 16, '24996');
COMMIT;

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Records of user_role
-- ----------------------------
BEGIN;
INSERT INTO `user_role` VALUES (13, 1), (14, 2), (15, 2), (16, 2);
COMMIT;

-- ----------------------------
-- Table structure for user_withdraw
-- ----------------------------
DROP TABLE IF EXISTS `user_withdraw`;
CREATE TABLE `user_withdraw`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NULL DEFAULT NULL,
  `withdraw_money` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `created_at` timestamp(0) NULL DEFAULT NULL,
  `type` int(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Records of user_withdraw
-- ----------------------------
BEGIN;
INSERT INTO `user_withdraw` VALUES (3, 16, '20000', '2019-12-09 00:00:00', 1), (4, 16, '10000', '2019-12-09 00:00:00', NULL), (5, 16, '2', '2019-12-09 00:00:00', 1), (6, 16, '2', '2019-12-09 00:00:00', 1), (7, 16, '25000', '2019-12-09 00:00:00', 1);
COMMIT;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `card_id` int(11) NOT NULL,
  `pin` int(255) NOT NULL,
  `contact_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `gender` tinyint(4) NULL DEFAULT NULL COMMENT '1 nam 0 nu',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Records of users
-- ----------------------------
BEGIN;
INSERT INTO `users` VALUES (13, 10011001, 2909, '0787816999', 1, 'FPTU', 'Nguyen Duc Tong'), (14, 10038214, 3839, '0808081508', 1, 'FPTU SE1401', 'Tran Minh Thang'), (15, 10086694, 5817, '1223561313141', 1, 'FPT', 'Tran Minh Thang'), (16, 10040899, 6524, '097686467548', 1, 'FPTUUUU', 'Dang Le Thanh Nam');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
