DROP TABLE USER_INFO  if exists;
--用户表
CREATE TABLE `USER_INFO` (
`USER_ID`  int(11)  primary key,
`USER_NAME`  varchar(128) NOT NULL COMMENT '用户名',
`PASS_WORD`  varchar(128) NOT NULL COMMENT '密码',
`REAL_NAME`  varchar(32) NULL COMMENT '用户真实姓名',
`DESCRIBE_TEXT`  varchar(2000) NULL COMMENT '用户备注说明',
`PHONE`  varchar(32) NULL COMMENT '电话',
`EMAIL`  varchar(255) NOT NULL COMMENT '邮箱',
`CORPORATE_NAME`  varchar(255) NULL COMMENT '公司名称',
`POSITION_NAME`  varchar(255) NULL COMMENT '职位名称',
`JOB_NAME`  varchar(255) NULL COMMENT '职务名称',
`ADDRESS`  varchar(1000) NULL COMMENT '地址',
`USER_STATE`  int(1)  NOT NULL default 1 COMMENT '用户状态(1、正常，2、停用)',
`CREATE_DATE`  timestamp NOT NULL default CURRENT_TIMESTAMP COMMENT '创建时间',
`OPERATOR`  int(11) NULL COMMENT '修改用户ID',
`UPDATE_DATE`  timestamp NULL COMMENT '修改时间',
`DEL_STATE`  varchar(16) NOT NULL default 'F' COMMENT '是否删除' 
);

DROP TABLE GROUP_INFO  if exists;
--分组表
CREATE TABLE `GROUP_INFO` (
`GROUP_ID`  int(11)  primary key  auto_increment,
`GROUP_NAME`  varchar(128) NOT NULL COMMENT '分组名称',
`DESCRIBE_TEXT`  varchar(2000) NULL COMMENT '分组备注说明',
`CREATOR`  int(11) NOT NULL COMMENT '创建用户ID',
`CREATE_DATE`  timestamp NOT NULL default CURRENT_TIMESTAMP COMMENT '创建时间',
`OPERATOR`  int(11) NULL COMMENT '修改用户ID',
`UPDATE_DATE`  timestamp NULL COMMENT '修改时间',
`DEL_STATE`  varchar(16) NOT NULL default 'F' COMMENT '是否删除' 
);

DROP TABLE USER_GROUP_INFO  if exists;
--用户及分组中间表
CREATE TABLE `USER_GROUP_INFO` (
`ID`  int(11)  primary key auto_increment,
`USER_ID`  int(11) NOT NULL COMMENT '用户ID' , 
`GROUP_ID`  int(11) NOT NULL COMMENT '分组ID' ,
);

DROP TABLE PROJECT_INFO  if exists;
--项目表
CREATE TABLE `PROJECT_INFO` (
`PROJECT_ID`  int(11)  primary key auto_increment,
`PROJECT_NUMBER`  varchar(50) NOT NULL COMMENT '项目编号',
`PROJECT_NAME`  varchar(256) NOT NULL COMMENT '项目名称' ,
`DESCRIBE_TEXT`  varchar(2000) NULL COMMENT '项目备注说明',
`IS_SHARE` int(1) NOT NULL default 1 COMMENT '是否共享（1、不共享，2、共享）' ,
`CREATOR`  int(11) NOT NULL COMMENT '创建用户ID',
`CREATE_DATE`  timestamp NOT NULL default CURRENT_TIMESTAMP COMMENT '创建时间',
`OPERATOR`  int(11) NULL COMMENT '修改用户ID',
`UPDATE_DATE`  timestamp NULL COMMENT '修改时间',
`DEL_STATE`  varchar(16) NOT NULL default 'F' COMMENT '是否删除'
);

DROP TABLE POWER_INFO  if exists;
--权限表
CREATE TABLE `POWER_INFO` (
`POWER_ID`  int(11)  primary key auto_increment,
`CONTROL_TYPE`  varchar(64) NOT NULL COMMENT '控制类型，如：项目填写project',
`CONTROL_ID`  int(11)  NOT NULL COMMENT '控制ID,如：项目填写PROJECT_ID',
`EFFECT_TYPE`  varchar(64) NOT NULL COMMENT '影响类型,如：用户填写user',
`EFFECT_ID`  int(11)  NOT NULL COMMENT '影响ID,如：用户填写USER_ID'
);

DROP TABLE DATASET_INFO  if exists;
--数据集
CREATE TABLE `DATASET_INFO` (
`ID`  int(11)  primary key auto_increment,
`DATASET_ID` varchar(32) NOT NULL ,
`FILE_NAME` varchar(200) NOT NULL COMMENT '文件名称',
`FILE_TYPE` varchar(64) NOT NULL COMMENT '文件类型',
`FILE_SIZE` varchar(64) NOT NULL COMMENT '文件大小',
`DATASET_PATH` varchar(1000) NOT NULL COMMENT '路径',
`MODEL` varchar(200)  NULL COMMENT '算法类型',
`INDUSTRY` varchar(200)  NULL COMMENT '行业',
`DESCRIPTION` varchar(200)  NULL COMMENT '说明',
`TASK_ID` varchar(200)  NULL COMMENT '执行计划ID',
`PROJECT_ID` int(11)  NULL COMMENT '所属项目ID',
`TAG` varchar(200)  NULL COMMENT '标签',
`CREATOR`  int(11) NOT NULL COMMENT '创建用户ID',
`CREATE_DATE`  timestamp NOT NULL default CURRENT_TIMESTAMP COMMENT '创建时间',
`DEL_STATE`  varchar(16) NOT NULL default 'F' COMMENT '是否删除'
);

DROP TABLE COLLECTION_MODEL  if exists;
--模型收藏
CREATE TABLE `COLLECTION_MODEL` (
`ID` int(32)  primary key auto_increment,
`NAME` varchar(200) NOT NULL COMMENT 'NAME',
`algorithms_type` varchar(200) NOT NULL COMMENT 'algorithms_type',
`PROJECT_ID` varchar(200) NOT NULL COMMENT 'PROJECT_ID',
`DATA_SET_ID` varchar(200) NOT NULL COMMENT 'DATA_SET_ID',
`TASK_ID` varchar(200) NOT NULL COMMENT 'TASK_ID',
`FULL_MODEL_ID` varchar(200) NOT NULL COMMENT 'modelId',
`USER_ID` int(11) NOT NULL COMMENT '用户Id',
`ENABLE` varchar(20) NOT NULL default '1' COMMENT '是否正常，没被删除',
`COLLECTION_DATE`  timestamp NOT NULL default CURRENT_TIMESTAMP COMMENT '创建时间',
);


DROP TABLE MODEL_DEPLOY  if exists;
--模型收藏
CREATE TABLE `MODEL_DEPLOY` (
`ID` int(32)  primary key auto_increment,
`fullModelId` varchar(200) NOT NULL COMMENT 'fullModelId',
`projectId` varchar(200) NOT NULL COMMENT 'projectId',
`algorithms_type` varchar(200) NOT NULL COMMENT 'algorithms_type',
`datasetId` varchar(200) NOT NULL COMMENT 'datasetId',
`taskId` varchar(200) NOT NULL COMMENT 'taskId',
`model_Name` varchar(200) NOT NULL COMMENT 'model_Name',
`CREATOR`  int(11) NOT NULL COMMENT '创建用户ID',
`CREATE_DATE`  timestamp NOT NULL default CURRENT_TIMESTAMP COMMENT '创建时间',
);

--添加数据
insert into PROJECT_INFO (PROJECT_ID,PROJECT_NUMBER,PROJECT_NAME,DESCRIBE_TEXT,CREATOR) values(1,'1','default project','default','1');
insert into GROUP_INFO  (GROUP_ID,GROUP_NAME,DESCRIBE_TEXT,CREATOR) values(1,'Administrators','admin','1');
insert into GROUP_INFO  (GROUP_ID,GROUP_NAME,DESCRIBE_TEXT,CREATOR) values(2,'guest','guets','1');
insert into USER_GROUP_INFO  (ID,USER_ID,GROUP_ID) values(1,1,1);
insert into USER_GROUP_INFO  (ID,USER_ID,GROUP_ID) values(2,2,2);