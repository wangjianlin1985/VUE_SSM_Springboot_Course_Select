-- phpMyAdmin SQL Dump
-- version 3.0.1.1
-- http://www.phpmyadmin.net
--
-- �������汾: 5.1.29
-- PHP �汾: 5.2.6

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
  `className` varchar(20)  NOT NULL COMMENT '�༶����',
  `classSpecialFieldNumber` varchar(50)  NOT NULL COMMENT '����רҵ',
  `classBirthDate` varchar(20)  NULL COMMENT '��������',
  `classTeacherCharge` varchar(12)  NULL COMMENT '������',
  `classTelephone` varchar(20)  NULL COMMENT '��ϵ�绰',
  `classMemo` varchar(100)  NULL COMMENT '������Ϣ',
  PRIMARY KEY (`classNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `t_student` (
  `user_name` varchar(20)  NOT NULL COMMENT 'user_name',
  `studentNumber` varchar(30)  NOT NULL COMMENT 'ѧ��',
  `password` varchar(30)  NOT NULL COMMENT '��¼����',
  `studentName` varchar(12)  NOT NULL COMMENT '����',
  `studentSex` varchar(2)  NOT NULL COMMENT '�Ա�',
  `studentClassNumber` varchar(20)  NOT NULL COMMENT '���ڰ༶',
  `studentBirthday` varchar(20)  NULL COMMENT '��������',
  `studentState` varchar(20)  NULL COMMENT '������ò',
  `studentPhoto` varchar(60)  NOT NULL COMMENT 'ѧ����Ƭ',
  `studentTelephone` varchar(20)  NULL COMMENT '��ϵ�绰',
  `studentEmail` varchar(30)  NULL COMMENT 'ѧ������',
  `studentQQ` varchar(20)  NULL COMMENT '��ϵqq',
  `studentAddress` varchar(100)  NULL COMMENT '��ͥ��ַ',
  `studentMemo` varchar(100)  NULL COMMENT '������Ϣ',
  `openid` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `t_teacher` (
  `teacherNumber` varchar(20)  NOT NULL COMMENT 'teacherNumber',
  `teacherName` varchar(12)  NOT NULL COMMENT '��ʦ����',
  `teacherSex` varchar(2)  NOT NULL COMMENT '�Ա�',
  `teacherBirthday` varchar(20)  NULL COMMENT '��������',
  `teacherArriveDate` varchar(20)  NULL COMMENT '��ְ����',
  `teacherCardNumber` varchar(20)  NULL COMMENT '���֤��',
  `teacherPhone` varchar(20)  NULL COMMENT '��ϵ�绰',
  `teacherPhoto` varchar(60)  NOT NULL COMMENT '��ʦ��Ƭ',
  `teacherAddress` varchar(100)  NULL COMMENT '��ͥ��ַ',
  `teacherMemo` varchar(8000)  NOT NULL COMMENT '��ʦ���',
  PRIMARY KEY (`teacherNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `t_courseInfo` (
  `courseNumber` varchar(20)  NOT NULL COMMENT 'courseNumber',
  `courseName` varchar(20)  NOT NULL COMMENT '�γ�����',
  `courseTeacher` varchar(20)  NOT NULL COMMENT '�Ͽ���ʦ',
  `courseTime` varchar(40)  NULL COMMENT '�Ͽ�ʱ��',
  `coursePlace` varchar(40)  NULL COMMENT '�Ͽεص�',
  `courseScore` float NOT NULL COMMENT '�γ�ѧ��',
  `courseMemo` varchar(8000)  NOT NULL COMMENT '�γ̼��',
  PRIMARY KEY (`courseNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `t_courseSelect` (
  `selectId` int(11) NOT NULL AUTO_INCREMENT COMMENT '��¼���',
  `studentNumber` varchar(20)  NOT NULL COMMENT 'ѡ��ѧ��',
  `courseNumber` varchar(20)  NOT NULL COMMENT 'ѡ��γ�',
  `selectTime` varchar(20)  NULL COMMENT 'ѡ��ʱ��',
  PRIMARY KEY (`selectId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS `t_scoreInfo` (
  `scoreId` int(11) NOT NULL AUTO_INCREMENT COMMENT '��¼���',
  `studentNumber` varchar(20)  NOT NULL COMMENT 'ѧ��',
  `courseNumber` varchar(20)  NOT NULL COMMENT '�γ�',
  `scoreValue` float NOT NULL COMMENT '�ɼ��÷�',
  `studentEvaluate` varchar(30)  NULL COMMENT 'ѧ������',
  PRIMARY KEY (`scoreId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS `t_notice` (
  `noticeId` int(11) NOT NULL AUTO_INCREMENT COMMENT '����id',
  `title` varchar(80)  NOT NULL COMMENT '����',
  `content` varchar(800)  NOT NULL COMMENT '��������',
  `publishDate` varchar(20)  NULL COMMENT '����ʱ��',
  PRIMARY KEY (`noticeId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;

ALTER TABLE t_student ADD CONSTRAINT FOREIGN KEY (studentClassNumber) REFERENCES t_classInfo(classNumber);
ALTER TABLE t_courseInfo ADD CONSTRAINT FOREIGN KEY (courseTeacher) REFERENCES t_teacher(teacherNumber);
ALTER TABLE t_courseSelect ADD CONSTRAINT FOREIGN KEY (studentNumber) REFERENCES t_student(user_name);
ALTER TABLE t_courseSelect ADD CONSTRAINT FOREIGN KEY (courseNumber) REFERENCES t_courseInfo(courseNumber);
ALTER TABLE t_scoreInfo ADD CONSTRAINT FOREIGN KEY (studentNumber) REFERENCES t_student(user_name);
ALTER TABLE t_scoreInfo ADD CONSTRAINT FOREIGN KEY (courseNumber) REFERENCES t_courseInfo(courseNumber);


