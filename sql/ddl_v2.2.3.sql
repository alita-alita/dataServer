### 更新产品表name字段
update record_app_product rap
set enterprise_name = (select raeics.enterprise_name
                       from record_app_enterprise_industry_chain_suspected raeics
                       where raeics.enterprise_id = rap.enterprise_id limit 1)
where rap.enterprise_name is null;

CREATE TABLE `record_product_matches_dissociated`
(
    `id`                  bigint        NOT NULL AUTO_INCREMENT COMMENT '主键',
    `version`             varchar(128)  NOT NULL COMMENT '版本号',
    `chain_id`            bigint        NOT NULL COMMENT '产业链ID',
    `enterprise_id`       varchar(255)  NOT NULL COMMENT '企业id',
    `enterprise_name`     varchar(255)  NOT NULL COMMENT '企业名称',
    `enterprise_uni_code` varchar(255)  NOT NULL COMMENT '企业统一社会信用代码',
    `product_id`          varchar(255)  NOT NULL COMMENT '产品ID',
    `product_name`        varchar(255)  NOT NULL COMMENT '产品名称',
    `product_url`         text COMMENT '产品URL',
    `product_description` text COMMENT '产品描述',
    `product_purpose`     text COMMENT '产品用途',
    `node_id`             bigint        NOT NULL COMMENT '节点ID',
    `node_name`           varchar(255)  NOT NULL COMMENT '节点名称',
    `label_id`            bigint        NULL COMMENT '标签ID',
    `label_name`          varchar(255)  NULL COMMENT '匹配标签名称',
    `matched_score`       decimal(5, 2) NULL COMMENT '匹配分数',
    `match_reason`        varchar(1024)          DEFAULT NULL COMMENT '匹配理由',
    `status`              tinyint       NOT NULL DEFAULT '0' COMMENT '0 未处理 1 已处理 2 忽略',
    `data_source`         varchar(128)  NOT NULL COMMENT '来源',
    `deleted`             tinyint       NOT NULL DEFAULT '0' COMMENT '逻辑删除标志,1:删除 0:未删除',
    `gmt_create`          datetime               DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modify`          datetime               DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`           varchar(255)           DEFAULT NULL COMMENT '创建者',
    `update_by`           varchar(255)           DEFAULT NULL COMMENT '更新者',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk` (`version`, `chain_id`, `product_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='游离企业产品表';