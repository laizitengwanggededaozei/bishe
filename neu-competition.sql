/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80034
 Source Host           : localhost:3306
 Source Schema         : neu-competition

 Target Server Type    : MySQL
 Target Server Version : 80034
 File Encoding         : 65001

 Date: 05/03/2025 04:59:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for competition
-- ----------------------------
DROP TABLE IF EXISTS `competition`;
CREATE TABLE `competition`  (
  `com_id` int NOT NULL AUTO_INCREMENT,
  `com_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `organizer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `com_level` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `discipline` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `com_website` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `com_profile` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `com_purpose` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `com_other` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `commend` int NOT NULL,
  PRIMARY KEY (`com_id`, `user_id`) USING BTREE,
  INDEX `com_id`(`com_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of competition
-- ----------------------------
INSERT INTO `competition` VALUES (1, '全国大学生程序设计竞赛', '中国大学生程序设计竞赛组委会', '国家级', '计算机科学与技术', 'http://www.acm.org.cn/', '本竞赛由中国大学生程序设计竞赛组委会主办，旨在促进大学生程序设计能力的提高，推动计算机科学与技术的发展。', '提高大学生程序设计能力', '无', '/image/01.png', '0', 1);
INSERT INTO `competition` VALUES (2, '全国大学生数学建模竞赛', '中国大学生数学建模竞赛组委会', '国家级', '数学与应用数学', 'http://www.comap.com/', '本竞赛旨在培养学生综合运用数学知识解决实际问题的能力，提高数学建模水平。', '培养学生综合运用数学知识解决实际问题的能力', '无', '/image/02.png', '0', 1);
INSERT INTO `competition` VALUES (3, '全国大学生英语演讲比赛', '中国大学生英语演讲比赛组委会', '国家级', '外语', 'http://www.efc.edu.cn/', '本竞赛旨在提高大学生英语口语表达能力，促进英语教学改革。', '提高大学生英语口语表达能力', '无', '/image/03.png', '0', 1);
INSERT INTO `competition` VALUES (5, '1', '1', '院级', '1', '1', '1', '1', '1', '/image/屏幕截图 2024-10-10 061551.png', 'B组织者', 0);

-- ----------------------------
-- Table structure for matches
-- ----------------------------
DROP TABLE IF EXISTS `matches`;
CREATE TABLE `matches`  (
  `match_id` int NOT NULL AUTO_INCREMENT,
  `match_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `match_local` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `sponsor_contact` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `match_leader` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `grade_instructor` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `match_start_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `match_end_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `join_deadline` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `judging_information` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `competition_standards` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `prize_description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `com_id` int NULL DEFAULT NULL,
  `user_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `commend` int NOT NULL,
  `publish` int NOT NULL,
  PRIMARY KEY (`match_id`, `user_id`) USING BTREE,
  INDEX `com_id`(`com_id`) USING BTREE,
  CONSTRAINT `matches_ibfk_1` FOREIGN KEY (`com_id`) REFERENCES `competition` (`com_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of matches
-- ----------------------------
INSERT INTO `matches` VALUES (1, '第一届 全国大学生程序设计竞赛', '中国大学生程序设计竞赛组委会', '李娜娜', '刘丽丽', '林玲玲', '2024-06-01 08:00:00', '2024-06-01 18:00:00', '2024-05-25 23:59:59', '比赛将按规则进行评比', '参赛队伍需按照规定完成题目', '一等奖：5000元，二等奖：3000元，三等奖：1000元', '/image/01.png', 1, 'B组织者', 0, 1);
INSERT INTO `matches` VALUES (2, '第一届 全国大学生数学建模竞赛', '中国大学生数学建模竞赛组委会', '张欢欢', '王珊珊', '孙莎莎', '2024-07-10 09:00:00', '2024-07-11 18:00:00', '2024-06-30 23:59:59', '比赛将在48小时内完成', '参赛队伍需根据题目要求进行数学建模', '一等奖：8000元，二等奖：5000元，三等奖：2000元', '/image/02.png', 2, 'B组织者', 0, 0);
INSERT INTO `matches` VALUES (3, '第一届 全国大学生英语演讲比赛', '中国大学生英语演讲比赛组委会', '孙欣欣', '王丹丹', '赵琳琳', '2024-06-01 08:00:00', '2024-06-01 18:00:00', '2024-05-25 23:59:59', '比赛将按规则进行评比', '参赛队伍需按照规定完成题目', '一等奖：6000元，二等奖：3000元，三等奖：2000元', '/image/03.png', 3, 'B组织者', 0, 0);
INSERT INTO `matches` VALUES (4, '第二届 全国大学生程序设计竞赛', '中国大学生程序设计竞赛组委会', '李娜娜', '刘丽丽', '林玲玲', '2025-06-01 08:00:00', '2025-06-01 18:00:00', '2025-05-25 23:59:59', '比赛将按规则进行评比', '参赛队伍需按照规定完成题目', '11一等奖：5000元，二等奖：3000元，三等奖：1000元', '/image/01.png', 1, 'B组织者', 0, 1);
INSERT INTO `matches` VALUES (5, '第二届 全国大学生数学建模竞赛', '中国大学生数学建模竞赛组委会', '张欢欢', '王珊珊', '孙莎莎', '2025-07-10 09:00:00', '2025-07-11 18:00:00', '2025-06-30 23:59:59', '比赛将在48小时内完成', '参赛队伍需根据题目要求进行数学建模', '一等奖：8000元，二等奖：5000元，三等奖：2000元', '/image/02.png', 2, 'B组织者', 0, 0);
INSERT INTO `matches` VALUES (6, '第二届 全国大学生英语演讲比赛', '中国大学生英语演讲比赛组委会', '孙欣欣', '王丹丹', '赵琳琳', '2025-06-01 08:00:00', '2025-06-01 18:00:00', '2025-05-25 23:59:59', '比赛将按规则进行评比', '参赛队伍需按照规定完成题目', '一等奖：6000元，二等奖：3000元，三等奖：2000元	', '/image/03.png', 3, 'B组织者', 0, 0);

-- ----------------------------
-- Table structure for participation_record
-- ----------------------------
DROP TABLE IF EXISTS `participation_record`;
CREATE TABLE `participation_record`  (
  `team_id` int NOT NULL COMMENT '团队ID',
  `match_id` int NOT NULL COMMENT '比赛ID',
  PRIMARY KEY (`team_id`, `match_id`) USING BTREE,
  INDEX `team_id_idx`(`team_id`) USING BTREE,
  INDEX `match_id_idx`(`match_id`) USING BTREE,
  CONSTRAINT `participation_record_ibfk_1` FOREIGN KEY (`team_id`) REFERENCES `team` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `participation_record_ibfk_2` FOREIGN KEY (`match_id`) REFERENCES `matches` (`match_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '参赛记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of participation_record
-- ----------------------------
INSERT INTO `participation_record` VALUES (1, 4);

-- ----------------------------
-- Table structure for team
-- ----------------------------
DROP TABLE IF EXISTS `team`;
CREATE TABLE `team`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '团队唯一标识',
  `tname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '团队名称',
  `logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '团队Logo',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '团队表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of team
-- ----------------------------
INSERT INTO `team` VALUES (1, '教改一队', '/image/team/1.png');

-- ----------------------------
-- Table structure for team_member
-- ----------------------------
DROP TABLE IF EXISTS `team_member`;
CREATE TABLE `team_member`  (
  `uid` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `team_id` int NOT NULL COMMENT '团队ID',
  `uname` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户角色',
  PRIMARY KEY (`uid`, `team_id`) USING BTREE,
  INDEX `team_id`(`team_id`) USING BTREE,
  INDEX `team_member_ibfk_3`(`uname`) USING BTREE,
  CONSTRAINT `team_member_ibfk_1` FOREIGN KEY (`team_id`) REFERENCES `team` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `team_member_ibfk_2` FOREIGN KEY (`uid`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `team_member_ibfk_3` FOREIGN KEY (`uname`) REFERENCES `user` (`uname`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '团队成员表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of team_member
-- ----------------------------
INSERT INTO `team_member` VALUES ('S21008020412', 1, '王睿', '队员');
INSERT INTO `team_member` VALUES ('S21008021019', 1, '刘洋', '队长');
INSERT INTO `team_member` VALUES ('Tshengyu', 1, '盛雨', '教师');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户唯一标识',
  `uname` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `umail` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户邮箱',
  `upwd` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '123456' COMMENT '用户密码',
  `uprofilepic` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '#' COMMENT '用户头像',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uname`(`uname`) USING BTREE,
  UNIQUE INDEX `uname_2`(`uname`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('21008020316', '白杨', '21008020316@neu.com', '@Baiyang123', '/image/profile/5.png');
INSERT INTO `user` VALUES ('B组织者', '组织者', '组织者@admin.com', 'admin', '/image/profile/10.png');
INSERT INTO `user` VALUES ('S20150322', '迟尚熙', '20150322@edu.cn', '@Shengyu123', '#');
INSERT INTO `user` VALUES ('S21008020303', '李志明', '21008020303@neu.com', '@Lizhiming123', '/image/profile/8.png');
INSERT INTO `user` VALUES ('S21008020320', '胡皓元', '21008020320@neu.com', '@Huhaoyuan123', '/image/profile/9.png');
INSERT INTO `user` VALUES ('S21008020326', '韩佳樾', '21008020326@neu.com', '@Hanjiayue123', '/image/profile/7.png');
INSERT INTO `user` VALUES ('S21008020411', '迟宽鹏', '21008020411@neu.com', '@Chikuanpeng123', '/image/profile/11.png');
INSERT INTO `user` VALUES ('S21008020412', '王睿', '21008020412@neu.com', '@Wangrui123', '/image/profile/6.png');
INSERT INTO `user` VALUES ('S21008020425', '杨子骏', '21008020425@neu.com', '@Yangzijun123', '/image/profile/12.png');
INSERT INTO `user` VALUES ('S21008021011', '杨皓东', '21008021011@neu.com', '@Haodong123', '/image/profile/13.png');
INSERT INTO `user` VALUES ('S21008021019', '刘洋', '21008021019@edu.com', '@Liuyang123', '/image/profile/14.png');
INSERT INTO `user` VALUES ('Tshengyu', '盛雨', 'shengyu@neu.com', '@Shengyu123', '/image/profile/15.png');

SET FOREIGN_KEY_CHECKS = 1;
