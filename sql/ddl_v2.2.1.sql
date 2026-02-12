CREATE TABLE `record_app_typical_enterprise`
(
    `id`                  bigint  NOT NULL AUTO_INCREMENT COMMENT '主键',
    `version`             varchar(255)     NOT NULL COMMENT '版本',
    `enterprise_id`       varchar(255)     NOT NULL COMMENT '企业id',
    `enterprise_name`     varchar(255)     NOT NULL COMMENT '企业名称',
    `enterprise_uni_code` varchar(255)     NOT NULL COMMENT '企业社会统一信用代码',
    `industry_chain_id`   bigint           NOT NULL COMMENT '产业链id',
    `industry_chain_name` varchar(255)     NOT NULL COMMENT '产业链',
    `industry_node_id`    bigint           NOT NULL COMMENT '产业链节点id',
    `industry_node_name`  varchar(255)     NOT NULL COMMENT '产业链节点',
    `industry_label_id`   bigint           NOT NULL COMMENT '产业链标签id',
    `industry_label_name` varchar(255)     NOT NULL COMMENT '产业链标签',
    `data_source`         varchar(255)     DEFAULT NULL COMMENT '数据来源',
    `gmt_create`          datetime         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modify`          datetime         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`           varchar(255)     DEFAULT NULL COMMENT '创建者',
    `update_by`           varchar(255)     DEFAULT NULL COMMENT '更新者',
    `deleted`             tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除标志,1:删除 0:未删除',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk` (`version`, `industry_chain_id`, `industry_node_id`, `industry_label_id`, `enterprise_uni_code`),
    KEY `idx_uni_code` (`enterprise_uni_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='典型企业表';

CREATE TABLE `sync_task`
(
    `id`          bigint        NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `name`        varchar(128)  NOT NULL COMMENT '任务名称',
    `name_desc`   varchar(1024) NULL COMMENT '任务描述',
    `task_type`   varchar(128)  NOT NULL COMMENT '任务类型',
    `need_params` varchar(1024) NULL COMMENT '所需参数备注',
    `deleted`     tinyint(1)    NOT NULL DEFAULT '0' COMMENT '逻辑删除标志,0:删除 1:未删除',
    `gmt_create`  datetime      NOT NULL DEFAULT (now()) COMMENT '创建时间',
    `gmt_modify`  datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近一次更新时间',
    `create_by`   varchar(32)   NOT NULL DEFAULT 'system' COMMENT '创建者',
    `update_by`   varchar(32)   NOT NULL DEFAULT 'system' COMMENT '更新者',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='同步任务表';

CREATE TABLE `sync_task_instance`
(
    `id`            bigint        NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `task_id`       bigint        NOT NULL COMMENT '任务名称',
    `instance_desc` varchar(128)  NOT NULL COMMENT '运行备注',
    `param`         varchar(1024) NULL COMMENT '参数',
    `status`        varchar(128)  NULL COMMENT '任务状态',
    `deleted`       tinyint(1)    NOT NULL DEFAULT '0' COMMENT '逻辑删除标志,0:删除 1:未删除',
    `gmt_create`    datetime      NOT NULL DEFAULT (now()) COMMENT '创建时间',
    `gmt_modify`    datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近一次更新时间',
    `create_by`     varchar(32)   NOT NULL DEFAULT 'system' COMMENT '创建者',
    `update_by`     varchar(32)   NOT NULL DEFAULT 'system' COMMENT '更新者',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_task` (`task_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='同步任务实例';

CREATE TABLE `sync_task_instance_log`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `task_id`     bigint       NOT NULL COMMENT '任务ID',
    `instance_id` bigint       NOT NULL COMMENT '任务实例ID',
    `job_id`      varchar(128) NOT NULL COMMENT 'python job ID',
    `job_log`     longtext     NULL COMMENT 'job日志',
    `status`      tinyint      NOT NULL DEFAULT 0 COMMENT '状态',
    `deleted`     tinyint(1)   NOT NULL DEFAULT '0' COMMENT '逻辑删除标志,0:删除 1:未删除',
    `gmt_create`  datetime     NOT NULL DEFAULT (now()) COMMENT '创建时间',
    `gmt_modify`  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近一次更新时间',
    `create_by`   varchar(32)  NOT NULL DEFAULT 'system' COMMENT '创建者',
    `update_by`   varchar(32)  NOT NULL DEFAULT 'system' COMMENT '更新者',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_task_instance` (`task_id`, `instance_id`),
    KEY `idx_instance_id` (`instance_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='同步任务执行记录';