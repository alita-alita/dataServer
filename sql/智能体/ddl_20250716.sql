CREATE TABLE `agent` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `agent_hash_id` BIGINT NOT NULL COMMENT '智能体唯一标识',
  `agent_name` VARCHAR(256) COLLATE utf8mb4_general_ci NOT NULL COMMENT '智能体名称',
  `agent_type` INT NOT NULL DEFAULT 1 COMMENT '智能体类型，1通用智能体，2预置逻辑智能体',
  `agent_status` INT NOT NULL DEFAULT 1 COMMENT '启用状态，1启用，0禁用',
  `model` VARCHAR(256) COLLATE utf8mb4_general_ci NULL COMMENT '模型',
  `mcp_server` VARCHAR(4096) COLLATE utf8mb4_general_ci NULL COMMENT '服务地址',
  `prompt` VARCHAR(4096) COLLATE utf8mb4_general_ci NULL COMMENT '默认提示词',
  `bean_name` VARCHAR(256) COLLATE utf8mb4_general_ci NULL COMMENT '实现类名称',
  `agent_desc` MEDIUMTEXT COLLATE utf8mb4_general_ci NULL COMMENT '智能体描述',
  `deleted` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标志0否1是',
  `gmt_create` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modify` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` VARCHAR(32) NOT NULL DEFAULT 'system' COMMENT '创建人',
  `update_by` VARCHAR(32) NOT NULL DEFAULT 'system' COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='智能体';



