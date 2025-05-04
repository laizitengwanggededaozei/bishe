
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for competitionsystem
-- ----------------------------
DROP TABLE IF EXISTS `competitionsystem`;
CREATE TABLE `competitionsystem` (
    `comsysId` int NOT NULL AUTO_INCREMENT COMMENT '赛制编号，采用自增长方式',
    `comsysName` varchar(20) NOT NULL UNIQUE COMMENT '赛制名称',
    `comId` int COMMENT '赛制所属的赛事编号',
    PRIMARY KEY (`comsysId`),
    FOREIGN KEY (`comId`) REFERENCES competitions(`comId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for competitions
-- ----------------------------
DROP TABLE IF EXISTS `competitions`;
CREATE TABLE `competitions` (
  `comId` int NOT NULL AUTO_INCREMENT COMMENT '赛事编号，采用自增长方式',
  `comName` varchar(50) NOT NULL UNIQUE COMMENT '赛事名称',
  `comProfile` mediumtext COMMENT '赛事简介',
  `comPurpose` varchar(200) COMMENT '赛事宗旨',
  `comLogo` varchar(100) NOT NULL COMMENT '赛事标识',
  `managerId` varchar(12) NOT NULL COMMENT '赛事负责人编号，与users表的id相对应',
  `sponsor` mediumtext COMMENT '赞助商，可能有多个，所以采用富文本形式编辑',
  `comSysId` int NOT NULL COMMENT '赛制编号',
  `lastest` date NOT NULL COMMENT '最近一次举办时间',
  `comOther` varchar(200) NOT NULL COMMENT '其它说明',
  PRIMARY KEY (`comId`),
  FOREIGN KEY (`comSysId`) REFERENCES competitionsystem(`comsysId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for matches
-- ----------------------------
DROP TABLE IF EXISTS `matches`;
CREATE TABLE `matches` (
  `matchId` int NOT NULL AUTO_INCREMENT COMMENT '比赛的届次编号，采用自增长方式',
  `comId` int NOT NULL COMMENT '比赛所属的赛事编号',
  `startDate` date NOT NULL COMMENT '开始时间',
  `endDate` date NOT NULL COMMENT '结束时间',
  `finalDate` date NOT NULL COMMENT '决赛时间',
  `finalLocation` varchar(20) NOT NULL COMMENT '决赛地点',
  `status` int(1) NOT NULL COMMENT '比赛状态（0：筹备中；1：已发布；2：已终止）',
  `joinDeadline` date NOT NULL COMMENT '报名截止时间',
  `sponsorContact` varchar(200) COMMENT '赞助企业联系人列表',
  `courseLeader` varchar(12) NOT NULL COMMENT '课程负责人，与users表的id相对应',
  `gradeInstructor` varchar(12) NOT NULL COMMENT '素质教师，与users表的id相对应',
  `enterpriseJudges` mediumtext COMMENT '裁判信息，采用富文本方式',
  `teacherJudges` mediumtext COMMENT '专业教师评委信息，采用富文本方式',
  `poster` mediumtext NOT NULL COMMENT '比赛海报',
  `scoringCriteria` varchar(200) NOT NULL COMMENT '评分标准的具体说明',
  `awardIntroduction` mediumtext NOT NULL COMMENT '奖品的具体说明',
  PRIMARY KEY (`matchId`,`comId`),
  FOREIGN KEY (`comId`) REFERENCES competitions(`comId`),
  FOREIGN KEY (`courseLeader`) REFERENCES users(`userId`),
  FOREIGN KEY (`gradeInstructor`) REFERENCES users(`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
    `userId` varchar(12) NOT NULL COMMENT '用户编号（学号或者老师的员工号）',
    `password` varchar(20) NOT NULL COMMENT '密码',
    `realName` varchar(20) NOT NULL COMMENT '真实姓名',
    `userType` int(1) NOT NULL COMMENT '用户类别（0：赛事组织者；1：教师；2：学生）',
    `email` varchar(256) NOT NULL COMMENT '用户的电子邮件',
    PRIMARY KEY (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for teams
-- ----------------------------
DROP TABLE IF EXISTS `teams`;
CREATE TABLE `teams` (
    `teamId` int(11) NOT NULL AUTO_INCREMENT COMMENT '团队编号，采用自增长方式',
    `teamName` varchar(20) NOT NULL UNIQUE COMMENT '团队名称',
    `teamCreateAt` date NOT NULL COMMENT '团队创建日期',
    PRIMARY KEY (`teamId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for teamMembers
-- ----------------------------
DROP TABLE IF EXISTS `teamMembers`;
CREATE TABLE `teamMembers` (
   `memberId` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号，采用自增长方式',
   `teamId` int(11) NOT NULL COMMENT '团队编号',
   `userId` varchar(12) NOT NULL COMMENT '用户编号（学号或者老师的员工号）',
   `memberType` int(1) NOT NULL COMMENT '成员类别（0：教师；1：队长；2：学生）',
   PRIMARY KEY (`memberId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for registration
-- ----------------------------
DROP TABLE IF EXISTS `registration`;
CREATE TABLE `registration` (
    `regId` int NOT NULL AUTO_INCREMENT COMMENT '报名信息编号，采用自增长方式',
    `regDate` date NOT NULL UNIQUE COMMENT '报名时间',
    `reviewResult` int NOT NULL COMMENT '审批状态（0：待审核；1：通过；2：驳回修改；3：拒绝）',
    `feedback` varchar(100) COMMENT '审批反馈意见',
    `teamId` int NOT NULL COMMENT '报名团队编号',
    `matchId` int NOT NULL COMMENT '报名比赛编号',
    `comId` int NOT NULL COMMENT '赛事编号',
    FOREIGN KEY (`teamId`) REFERENCES teams(`teamId`),
    FOREIGN KEY (`matchId`) REFERENCES matches(`matchId`),
    FOREIGN KEY (`comId`) REFERENCES matches(`comId`),
    PRIMARY KEY (`regId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;
