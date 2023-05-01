-- phpMyAdmin SQL Dump
-- version 3.0.1.1
-- http://www.phpmyadmin.net
--
-- 服务器版本: 5.1.29
-- PHP 版本: 5.2.6

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

-- --------------------------------------------------------

-- ----------------------------
-- Table structure for `admin`
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `username` varchar(20) NOT NULL DEFAULT '',
  `password` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_admin
-- ----------------------------
INSERT INTO `admin` VALUES ('a', 'a'); 

CREATE TABLE IF NOT EXISTS `t_classInfo` (
  `classNumber` varchar(20)  NOT NULL COMMENT 'classNumber',
  `className` varchar(20)  NOT NULL COMMENT '班级名称',
  `classSpecialFieldNumber` varchar(50)  NOT NULL COMMENT '所属专业',
  `classBirthDate` varchar(20)  NULL COMMENT '成立日期',
  `classTeacherCharge` varchar(12)  NULL COMMENT '班主任',
  `classTelephone` varchar(20)  NULL COMMENT '联系电话',
  `classMemo` varchar(100)  NULL COMMENT '附加信息',
  PRIMARY KEY (`classNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `t_student` (
  `user_name` varchar(20)  NOT NULL COMMENT 'user_name',
  `studentNumber` varchar(30)  NOT NULL COMMENT '学号',
  `password` varchar(30)  NOT NULL COMMENT '登录密码',
  `studentName` varchar(12)  NOT NULL COMMENT '姓名',
  `studentSex` varchar(2)  NOT NULL COMMENT '性别',
  `studentClassNumber` varchar(20)  NOT NULL COMMENT '所在班级',
  `studentBirthday` varchar(20)  NULL COMMENT '出生日期',
  `studentState` varchar(20)  NULL COMMENT '政治面貌',
  `studentPhoto` varchar(60)  NOT NULL COMMENT '学生照片',
  `studentTelephone` varchar(20)  NULL COMMENT '联系电话',
  `studentEmail` varchar(30)  NULL COMMENT '学生邮箱',
  `studentQQ` varchar(20)  NULL COMMENT '联系qq',
  `studentAddress` varchar(100)  NULL COMMENT '家庭地址',
  `studentMemo` varchar(100)  NULL COMMENT '附加信息',
  `openid` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `t_teacher` (
  `teacherNumber` varchar(20)  NOT NULL COMMENT 'teacherNumber',
  `teacherName` varchar(12)  NOT NULL COMMENT '教师姓名',
  `teacherSex` varchar(2)  NOT NULL COMMENT '性别',
  `teacherBirthday` varchar(20)  NULL COMMENT '出生日期',
  `teacherArriveDate` varchar(20)  NULL COMMENT '入职日期',
  `teacherCardNumber` varchar(20)  NULL COMMENT '身份证号',
  `teacherPhone` varchar(20)  NULL COMMENT '联系电话',
  `teacherPhoto` varchar(60)  NOT NULL COMMENT '教师照片',
  `teacherAddress` varchar(100)  NULL COMMENT '家庭地址',
  `teacherMemo` varchar(8000)  NOT NULL COMMENT '教师简介',
  PRIMARY KEY (`teacherNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `t_courseInfo` (
  `courseNumber` varchar(20)  NOT NULL COMMENT 'courseNumber',
  `courseName` varchar(20)  NOT NULL COMMENT '课程名称',
  `courseTeacher` varchar(20)  NOT NULL COMMENT '上课老师',
  `courseTime` varchar(40)  NULL COMMENT '上课时间',
  `coursePlace` varchar(40)  NULL COMMENT '上课地点',
  `courseScore` float NOT NULL COMMENT '课程学分',
  `courseMemo` varchar(8000)  NOT NULL COMMENT '课程简介',
  PRIMARY KEY (`courseNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `t_courseSelect` (
  `selectId` int(11) NOT NULL AUTO_INCREMENT COMMENT '记录编号',
  `studentNumber` varchar(20)  NOT NULL COMMENT '选课学生',
  `courseNumber` varchar(20)  NOT NULL COMMENT '选择课程',
  `selectTime` varchar(20)  NULL COMMENT '选课时间',
  PRIMARY KEY (`selectId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS `t_scoreInfo` (
  `scoreId` int(11) NOT NULL AUTO_INCREMENT COMMENT '记录编号',
  `studentNumber` varchar(20)  NOT NULL COMMENT '学生',
  `courseNumber` varchar(20)  NOT NULL COMMENT '课程',
  `scoreValue` float NOT NULL COMMENT '成绩得分',
  `studentEvaluate` varchar(30)  NULL COMMENT '学生评价',
  PRIMARY KEY (`scoreId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS `t_notice` (
  `noticeId` int(11) NOT NULL AUTO_INCREMENT COMMENT '公告id',
  `title` varchar(80)  NOT NULL COMMENT '标题',
  `content` varchar(800)  NOT NULL COMMENT '公告内容',
  `publishDate` varchar(20)  NULL COMMENT '发布时间',
  PRIMARY KEY (`noticeId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;

ALTER TABLE t_student ADD CONSTRAINT FOREIGN KEY (studentClassNumber) REFERENCES t_classInfo(classNumber);
ALTER TABLE t_courseInfo ADD CONSTRAINT FOREIGN KEY (courseTeacher) REFERENCES t_teacher(teacherNumber);
ALTER TABLE t_courseSelect ADD CONSTRAINT FOREIGN KEY (studentNumber) REFERENCES t_student(user_name);
ALTER TABLE t_courseSelect ADD CONSTRAINT FOREIGN KEY (courseNumber) REFERENCES t_courseInfo(courseNumber);
ALTER TABLE t_scoreInfo ADD CONSTRAINT FOREIGN KEY (studentNumber) REFERENCES t_student(user_name);
ALTER TABLE t_scoreInfo ADD CONSTRAINT FOREIGN KEY (courseNumber) REFERENCES t_courseInfo(courseNumber);


