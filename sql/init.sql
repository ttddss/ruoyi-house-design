
DROP TABLE IF EXISTS `blueprints`;
CREATE TABLE `blueprints` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(16) DEFAULT NULL COMMENT '图纸编码',
  `name` varchar(16) DEFAULT NULL COMMENT '图纸名称',
  `introduction` varchar(32) DEFAULT NULL COMMENT '简介',
  `image_url` varchar(128) DEFAULT NULL COMMENT '图纸图片',
  `price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '单价。单位元',
  `min_construction_cost` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '最小造价。单位万元',
  `max_construction_cost` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '最大造价。单位万元',
  `min_buildable_size` decimal(10,2) DEFAULT NULL COMMENT '最小可建尺寸。单位米',
  `max_buildable_size` decimal(10,2) DEFAULT NULL COMMENT '最大可建尺寸。单位米',
  `bay_size` decimal(10,2) DEFAULT NULL COMMENT '开间尺寸。单位米',
  `depth_size` decimal(10,2) DEFAULT NULL COMMENT '进深尺寸。单位米',
  `house_type` tinyint(4) NOT NULL COMMENT '房型：0-一层别墅 1-二层别墅 2-三层别墅 3-双拼别墅 4-其他',
  `bay_type` tinyint(4) NOT NULL COMMENT '室型：0-一开间 1-二开间 2-三开间 3-四开间 4-五开间 5-五开间以上',
  `style` tinyint(4) NOT NULL COMMENT '风格：0-复古 1-现代 2-抽象 3-其他',
  `bedroom_size` tinyint(4) DEFAULT NULL COMMENT '卧室数',
  `parlour_size` tinyint(4) DEFAULT NULL COMMENT '客厅数',
  `toilet_size` tinyint(4) DEFAULT NULL COMMENT '卫生间数',
  `del_flag` bigint(20) DEFAULT '0' COMMENT '删除标志: 0-代表存在 其他代表删除',
  `remark` varchar(32) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) ,
  UNIQUE KEY `uk_blueprints_code` (`code`, `del_flag`),
  KEY `idx_blueprints_create_time` (`create_time`)
) COMMENT='设计图纸表';

alter table blueprints add `user_id` bigint(20) NOT NULL DEFAULT '1' COMMENT '用户ID';
alter table blueprints add index idx_blueprints_user_id(`user_id`);
alter table blueprints add listing_status tinyint(4) NOT NULL DEFAULT '0' COMMENT '上架状态：0-已下架 1-已上架';
alter table blueprints add `view_num` int NOT NULL DEFAULT '0' COMMENT '浏览量';
alter table blueprints add `collect_num` int NOT NULL DEFAULT '0' COMMENT '收藏人数';
alter table blueprints add `sale_num` int(11) NOT NULL DEFAULT '0' COMMENT '销售量';

DROP TABLE IF EXISTS `blueprints_collect`;
CREATE TABLE `blueprints_collect` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `blueprints_id` bigint(20) NOT NULL COMMENT '图纸ID',
  `del_flag` bigint(20) DEFAULT '0' COMMENT '删除标志: 0-代表存在 其他代表删除',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_blueprints_user_id` (`user_id`),
  KEY `idx_blueprints_blueprints_id` (`blueprints_id`)
) COMMENT='用户设计图纸收藏表';

DROP TABLE IF EXISTS `blueprints_detail`;
CREATE TABLE `blueprints_detail` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `blueprints_id` bigint(20) NOT NULL COMMENT '图纸id',
   `introduction` varchar(32) DEFAULT NULL COMMENT '简介',
   `file_url` varchar(128) DEFAULT NULL COMMENT '图纸访问地址',
   `preview_image_url` varchar(1024) DEFAULT NULL COMMENT '图纸概览图片',
   `introduction_image_url` varchar(1024) DEFAULT NULL COMMENT '图纸说明图片',
   `remark` varchar(32) DEFAULT NULL COMMENT '备注',
   `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
   `create_time` datetime NOT NULL COMMENT '创建时间',
   `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
   `update_time` datetime DEFAULT NULL COMMENT '更新时间',
   `del_flag` bigint(20) DEFAULT '0' COMMENT '删除标志: 0-代表存在 其他代表删除',
   PRIMARY KEY (`id`),
   UNIQUE KEY `uk_blueprints_detail_blueprints_id` (`blueprints_id`, `del_flag`)
) COMMENT='设计图纸详情表';

CREATE TABLE `blueprints_user` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `blueprints_id` bigint(20) NOT NULL COMMENT '图纸id',
   `user_id` bigint(20) NOT NULL COMMENT '用户ID',
   `del_flag` bigint(20) DEFAULT '0' COMMENT '删除标志: 0-代表存在 其他代表删除',
   `remark` varchar(32) DEFAULT NULL COMMENT '备注',
   `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
   `create_time` datetime NOT NULL COMMENT '创建时间',
   `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
   `update_time` datetime DEFAULT NULL COMMENT '更新时间',
   PRIMARY KEY (`id`),
   KEY `idx_blueprints_user_blueprints_id` (`blueprints_id`),
   KEY `idx_blueprints_user_user_id` (`user_id`)
)  COMMENT='用户图纸表';


DROP TABLE IF EXISTS `blueprints_view`;
CREATE TABLE `blueprints_view` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `blueprints_id` bigint(20) NOT NULL COMMENT '图纸id',
   `user_id` bigint(20) NOT NULL COMMENT '用户id',
   `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
   `create_time` datetime NOT NULL COMMENT '创建时间',
   `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
   `update_time` datetime DEFAULT NULL COMMENT '更新时间',
   `del_flag` bigint(20) DEFAULT '0' COMMENT '删除标志: 0-代表存在 其他代表删除',
   PRIMARY KEY (`id`),
   KEY `idx_blueprints_view_blueprints_id` (`blueprints_id`),
   KEY `idx_blueprints_view_user_id` (`user_id`)
) COMMENT='图纸浏览记录表';

DROP TABLE IF EXISTS `feedback`;
CREATE TABLE `feedback` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
    `contact_info` varchar(64) DEFAULT NULL COMMENT '联系方式',
    `status` tinyint default '0' COMMENT '反馈状态：0-待处理 1-已解决 2-已关闭',
    `image_url` varchar(256) DEFAULT NULL COMMENT '反馈图片',
    `content` varchar(256) DEFAULT NULL COMMENT '反馈内容',
    `response_user_id` bigint(20) DEFAULT NULL COMMENT '管理员用户id',
    `response` varchar(256) DEFAULT NULL COMMENT '管理员回复内容',
    `response_time` datetime DEFAULT NULL COMMENT '管理员回复时间',
    `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `del_flag` bigint(20) DEFAULT '0' COMMENT '删除标志: 0-代表存在 其他代表删除',
    PRIMARY KEY (`id`),
    KEY `idx_feedback_create_time` (`create_time`),
    KEY `idx_feedback_response_user_id` (`response_user_id`)
) COMMENT='反馈表';

alter table sys_user add `age` int  DEFAULT '0' COMMENT '年龄';

DROP TABLE IF EXISTS `vip`;
CREATE TABLE `vip` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `name` varchar(32) DEFAULT NULL COMMENT '名称',
   `status` tinyint default '1' COMMENT '状态：0-停用 1-启用',
   `image_url` varchar(128) DEFAULT NULL COMMENT '图片',
   `price` decimal(10,2) default '0' comment '价格',
   `color` varchar(8) DEFAULT NULL COMMENT '颜色',
   `remark` varchar(128) DEFAULT NULL COMMENT '备注',
   `valid_days` int DEFAULT '0' COMMENT '有效天数',
   `discount` int DEFAULT '10' COMMENT '折扣',
   `order_num` int DEFAULT '0' COMMENT '排序',
   `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
   `create_time` datetime NOT NULL COMMENT '创建时间',
   `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
   `update_time` datetime DEFAULT NULL COMMENT '更新时间',
   `del_flag` bigint(20) DEFAULT '0' COMMENT '删除标志: 0-代表存在 其他代表删除',
   PRIMARY KEY (`id`)
) COMMENT='vip表';

alter table vip add `icon_image_url` varchar(128) DEFAULT NULL COMMENT '图片';

DROP TABLE IF EXISTS `vip_user`;
CREATE TABLE `vip_user` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` bigint(20) NOT NULL COMMENT '用户id',
    `vip_id` bigint(20) NOT NULL COMMENT 'vip id',
    `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `del_flag` bigint(20) DEFAULT '0' COMMENT '删除标志: 0-代表存在 其他代表删除',
    PRIMARY KEY (`id`),
    KEY `idx_vip_user_user_id` (`user_id`),
    KEY `idx_vip_user_vip_id` (`vip_id`)
) COMMENT='vip用户表';

alter table vip_user add `expire_time` datetime NOT NULL COMMENT '到期时间';


DROP TABLE IF EXISTS `pay_config`;
CREATE TABLE `pay_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) NOT NULL COMMENT '配置名称',
  `channel` tinyint(4) NOT NULL COMMENT '支付渠道：0-支付宝 1-微信',
  `appid` varchar(32) NOT NULL COMMENT '应用id',
  `appname` varchar(100) NOT NULL COMMENT '应用名称',
  `mchid` varchar(32) NOT NULL COMMENT '商户号',
  `ali_type` tinyint(4) DEFAULT NULL COMMENT '支付宝应用类型：0-第三方应用 1-网页应用 2-小程序应用 3-移动应用',
  `wx_type` tinyint(4) DEFAULT NULL COMMENT '微信支付应用类型：0-服务号 1-小程序 2-开放平台',
  `public_key` varchar(2550) DEFAULT NULL COMMENT '公钥',
  `private_key` varchar(2550) DEFAULT NULL COMMENT '私钥',
  `app_secret` varchar(32) DEFAULT NULL COMMENT '开发者密钥',
  `version` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '版本号',
  `del_flag` bigint(20) DEFAULT '0' COMMENT '删除标志: 0-代表存在 其他代表删除',
  `remark` varchar(32) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) COMMENT='支付配置表';

alter table pay_config add `gateway_url` varchar(128) DEFAULT NULL COMMENT '网关地址';
alter table pay_config add `sub_channel` tinyint(4) NOT NULL DEFAULT '0' COMMENT '子支付渠道：0-支付宝扫码付 1-支付宝手机网站支付 20-微信native支付 21-微信jsapi网页支付  22-微信小程序';
alter table pay_config add `serial_num` varchar(64) DEFAULT NULL COMMENT '序列号';
alter table pay_config add `api_key` varchar(32) DEFAULT NULL COMMENT 'aliv3密钥';
alter table pay_config add `expire_time` datetime DEFAULT NULL COMMENT '失效时间';
alter table pay_config add `plat_cert_path` varchar(128) DEFAULT NULL COMMENT '平台证书路径';


DROP TABLE IF EXISTS `pay_order`;
CREATE TABLE `pay_order` (
     `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
     `order_no` varchar(64) NOT NULL COMMENT '订单号',
     `channel` tinyint(4) NOT NULL COMMENT '支付渠道：0-支付宝 1-微信 99-其他',
     `sub_channel` tinyint(4) NOT NULL COMMENT '子支付渠道：0-支付宝扫码付 1-支付宝手机网站支付 20-微信native支付 21-微信jsapi网页支付  22-微信小程序支付 99-其他',
     `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
     `vip_id` bigint(20) DEFAULT NULL COMMENT 'vipID',
     `blueprints_id` bigint(20) DEFAULT NULL COMMENT '图纸ID',
     `status` tinyint(4) NOT NULL COMMENT '订单状态：0-支付中 1-支付成功 2-取消支付 3-关闭 4-支付异常 5-已退款',
     `amount` decimal(12,2) NOT NULL COMMENT '订单金额',
     `discount` decimal(4,2) NOT NULL COMMENT '折扣',
     `actual_amount` decimal(12,2) NOT NULL COMMENT '实付金额',
     `subject` varchar(200) DEFAULT NULL COMMENT '订单标题',
     `outer_trade_no` varchar(32) DEFAULT NULL COMMENT '外部交易流水号',
     `appid` varchar(64) DEFAULT NULL COMMENT '应用id',
     `mchid` varchar(32) DEFAULT NULL COMMENT '商户号',
     `code` varchar(20) DEFAULT NULL COMMENT '响应编码',
     `message` varchar(255) DEFAULT NULL COMMENT '响应报文',
     `del_flag` bigint(20) DEFAULT '0' COMMENT '删除标志: 0-代表存在 其他代表删除',
     `remark` varchar(32) DEFAULT NULL COMMENT '备注',
     `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
     `create_time` datetime NOT NULL COMMENT '创建时间',
     `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
     `update_time` datetime DEFAULT NULL COMMENT '更新时间',
     PRIMARY KEY (`id`) ,
     UNIQUE KEY `uk_pay_order_order_no` (`order_no`) ,
     KEY `idx_pay_order_create_time` (`create_time`) ,
     KEY `idx_pay_order_user_id` (`user_id`)
) COMMENT='支付订单';
alter table pay_order add  `discount_amount` decimal(12,2) NOT NULL COMMENT '优惠金额';
alter table pay_order add   `goods_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '商品类型：0-vip 1-图纸';
alter table pay_order add   `goods_num` int NOT NULL DEFAULT '0' COMMENT '商品数量';

DROP TABLE IF EXISTS `refund_order`;
CREATE TABLE `refund_order` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `refund_no` varchar(64) NOT NULL COMMENT '退款单号',
    `order_no` varchar(64) NOT NULL COMMENT '支付单号',
    `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
    `amount` decimal(18,2) NOT NULL COMMENT '退款金额',
    `description` varchar(255) DEFAULT NULL COMMENT '退款描述信息',
    `status` tinyint(4) NOT NULL COMMENT '退款状态：0-退款中 1-退款成功 2-取消退款 3-退款异常 4-关闭',
    `code` varchar(255) DEFAULT NULL COMMENT '响应编码',
    `message` varchar(255) DEFAULT NULL COMMENT '响应报文',
    `del_flag` bigint(20) DEFAULT '0' COMMENT '删除标志: 0-代表存在 其他代表删除',
    `remark` varchar(32) DEFAULT NULL COMMENT '备注',
    `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) ,
    UNIQUE KEY `uk_refund_order_order_no` (`refund_no`) ,
    KEY `idx_refund_order_create_time` (`create_time`) ,
    KEY `idx_refund_order_order_no` (`order_no`)
) COMMENT='退款单';

-- ----------------------------
-- 第三方授权表
-- ----------------------------
DROP TABLE IF EXISTS sys_auth_user;
CREATE TABLE sys_auth_user (
   auth_id           BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT '授权ID',
   uuid              VARCHAR(500)    NOT NULL                   COMMENT '第三方平台用户唯一ID',
   user_id           BIGINT(20)      NOT NULL                   COMMENT '系统用户ID',
   user_name         VARCHAR(30)     NOT NULL                   COMMENT '登录账号',
   nick_name         VARCHAR(30)     DEFAULT ''                 COMMENT '用户昵称',
   avatar            VARCHAR(500)    DEFAULT ''                 COMMENT '头像地址',
   email             VARCHAR(255)    DEFAULT ''                 COMMENT '用户邮箱',
   source            VARCHAR(255)    DEFAULT ''                 COMMENT '用户来源',
   create_time       DATETIME                                   COMMENT '创建时间',
   PRIMARY KEY (auth_id)
) ENGINE=INNODB AUTO_INCREMENT=100 COMMENT = '第三方授权表';

alter table sys_auth_user add `del_flag` bigint(20) DEFAULT '0' COMMENT '删除标志: 0-代表存在 其他代表删除';


DROP TABLE IF EXISTS `evaluate`;
CREATE TABLE `evaluate` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `score` int DEFAULT '5' COMMENT '评分',
    `order_no` varchar(64) NOT NULL COMMENT '订单号',
    `blueprints_id` bigint(20) NOT NULL COMMENT '图纸id',
    `user_id` bigint(20) NOT NULL COMMENT '用户id',
    `image_url` varchar(256) DEFAULT NULL COMMENT '图片',
    `anonymous` tinyint default '0' COMMENT '是否匿名：0-否 1-是',
    `content` varchar(128) DEFAULT NULL COMMENT '评价内容',
    `remark` varchar(128) DEFAULT NULL COMMENT '备注',
    `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `del_flag` bigint(20) DEFAULT '0' COMMENT '删除标志: 0-代表存在 其他代表删除',
    PRIMARY KEY (`id`)
) COMMENT='评价表';