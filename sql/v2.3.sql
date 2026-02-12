-- record_agent_product_matches : 企业产品挂载结果表
-- record_app_product : 企业产品表
-- record_product_matches_ai_check : AI质检结果表
-- record_product_matches : 最终挂载结果表

-- 清楚产业链版本号
delete from record_industry_chain
where version = #{vvv};

ALTER TABLE `record_industry_chain`
DROP COLUMN `version`,
	DROP KEY `uk_id_version`,
	DROP KEY `uk_chain_name`,
	DROP KEY `uk_chain_code`
;

ALTER TABLE `record_industry_chain`
    ADD COLUMN `state` int(2) NULL DEFAULT 0 COMMENT '状态码说明：
        0:  正常（NORMAL）
        1:  待生产（PRODUCING）
        2:  专利找企业中（PATENT_PROCESS）
        3:  专利找企业完成（PATENT_PROCESS_END）
        4:  产品找企业中（LABEL_FIND_ENTERPRISE）
        5:  产品找企业完成（LABEL_FIND_ENTERPRISE_END）
        6:  企业找产品中（ENTERPRISE_FIND_LABEL）
        7:  企业找产品完成（ENTERPRISE_FIND_LABEL_END）
        8:  挂载企业中（MATCH_ENTERPRISE）
        9:  挂载企业完成（MATCH_ENTERPRISE_END）
        10: 待AI质检（WAITING_AI_QUALIFICATION） ← 默认值
        11: AI质检中（AI_QUALIFICATION_PROCESS）
        12: 待人工质检（WAITING_HUMAN_QUALIFICATION）
        13: 人工质检完成（HUMAN_QUALIFICATION_FINISH）
        14: 待同步集市（WAITING_SYNC_DW)
        15: 同步集市中（SYNCING_DW)
        16: 同步集市完成（SYNC_DW_END)
        17: 待同步生产中（WAITING_SYNC_PRODUCTION）
        18: 待同步生产中（SYNCING_PRODUCTION）
        19: 同步生产完成（SYNC_PRODUCTION_END）
        98: 失败（ERROR）
        99: 完成（FINISH）' AFTER `lib_file_id`
;


delete from record_industry_chain_node
where version = #{vvv};

ALTER TABLE `record_industry_chain_node`
DROP KEY `uk_id_version`
;
ALTER TABLE `record_industry_chain_node`
DROP COLUMN `version`
;




--???
delete from record_industry_chain_node_label_relation
where version = #{vvv};
ALTER TABLE `record_industry_chain_node_label_relation`
DROP KEY `uk`
;
ALTER TABLE `record_industry_chain_node_label_relation`
DROP COLUMN `version`
;
ALTER TABLE `record_industry_chain_node_label_relation`
    ADD Unique KEY `uk_node_label`(`chain_node_id`,`industry_label_id`) USING HASH
;


delete from record_industry_label
where version = #{vvv};

ALTER TABLE `record_industry_label`
	DROP KEY `uk_id_version`,
	DROP KEY `uk_lc`
;

ALTER TABLE `record_industry_label`
	     DROP COLUMN `version`;

ALTER TABLE `record_industry_label`
    ADD Unique KEY `uk_label_chain_id`(`chain_id`,`biz_id`) USING HASH
;

ALTER TABLE `record_industry_label`
    MODIFY COLUMN `status` int NOT NULL DEFAULT 0 COMMENT '0:  正常 1:  待生产    2:  专利找企业中  3:  专利找企业完成 4:  产品找企业中  5:  产品找企业完成' AFTER `chain_id`
;
update `record_industry_label`
set status = 5;




delete from record_industry_chain_category_relation
where version = #{vvv};

ALTER TABLE `record_industry_chain_category_relation`
DROP COLUMN `version`,
	DROP KEY `uk`
;
ALTER TABLE `record_industry_chain_category_relation`
    ADD Unique KEY `uk_chain_cat`(`chain_id`,`category_id`) USING HASH
;


ALTER TABLE `record_app_enterprise_industry_chain_suspected`
DROP COLUMN `version`
;
ALTER TABLE record_app_enterprise_industry_chain_suspected
    MODIFY COLUMN industry_chain_id INT NOT NULL COMMENT '产业链id';
ALTER TABLE record_app_enterprise_industry_chain_suspected
DROP PRIMARY KEY,
ADD PRIMARY KEY (id, industry_chain_id);

ALTER TABLE `record_app_enterprise_industry_chain_suspected`
DROP KEY `uk_enterprise_chain_id`,
	DROP KEY `uk_biz_id`
;
ALTER TABLE record_app_enterprise_industry_chain_suspected
    PARTITION BY HASH(industry_chain_id)
    PARTITIONS 10;

update record_app_enterprise_industry_chain_suspected
set status = 9;



delete from record_app_typical_enterprise
where version = #{vvv};

ALTER TABLE `record_app_typical_enterprise`
DROP COLUMN `version`,
;

ALTER TABLE `record_app_typical_enterprise`
DROP KEY `uk`
;

ALTER TABLE `record_app_typical_enterprise`
    ADD Unique KEY `uk_cid_nid_label_uni`(`industry_chain_id`,`industry_node_id`,`industry_label_id`,`enterprise_uni_code`) USING HASH
;

delete from record_blacklist_keywords
where version = #{vvv};

ALTER TABLE `record_blacklist_keywords`
DROP COLUMN `version`
;



delete from record_ai_check_record
where version = #{vvv};

ALTER TABLE `record_ai_check_record`
DROP COLUMN `version`
;

ALTER TABLE `record_ai_check_record`
    ADD KEY `idx_chain_id`(`chain_id`) USING HASH
;

ALTER TABLE `record_app_product`
    ADD COLUMN `status` int(3) NULL DEFAULT 0 COMMENT '状态值：0-正常, 8-挂载企业中, 9-挂载企业完成, 99-完成' AFTER `data_source`
;


ALTER TABLE `record_agent_log`
    MODIFY COLUMN `result` longtext NULL COMMENT 'agent返回值' AFTER `type`
;

ALTER TABLE `record_agent_product_matches`
DROP KEY `idx_chain_product_id`,
	ADD Unique KEY `idx_chain_product_id`(`industry_chain_id`,`product_id`) USING BTREE
;
ALTER TABLE `record_agent_product_matches`
DROP COLUMN `file_id`,
	MODIFY COLUMN `status` int NOT NULL DEFAULT 0 COMMENT '0:  正常（NORMAL）8:  挂载企业中（MATCH_ENTERPRISE）         9:  挂载企业完成（MATCH_ENTERPRISE_END）         10: 待AI质检（WAITING_AI_QUALIFICATION）         11: AI质检中（AI_QUALIFICATION_PROCESS）         12: 待人工质检（WAITING_HUMAN_QUALIFICATION）         13: 人工质检完成（HUMAN_QUALIFICATION_FINISH）         14: 待同步集市（WAITING_SYNC_DW)         15: 同步集市中（SYNCING_DW)         16: 同步集市完成（SYNC_DW_END)         17: 待同步生产中（WAITING_SYNC_PRODUCTION）         18: 待同步生产中（SYNCING_PRODUCTION）         19: 同步生产完成（SYNC_PRODUCTION_END）         98: 失败（ERROR）         99: 完成（FINISH）' AFTER `suspected_clue`
;

ALTER TABLE record_agent_product_matches
    MODIFY COLUMN industry_chain_id BIGINT NOT NULL COMMENT '产业链id';

ALTER TABLE `record_agent_product_matches`
DROP KEY `uk_biz_id`
;

ALTER TABLE record_agent_product_matches
DROP PRIMARY KEY,
ADD PRIMARY KEY (id, industry_chain_id);

ALTER TABLE record_agent_product_matches
    PARTITION BY HASH(industry_chain_id)
    PARTITIONS 10;

update record_agent_product_matches
set `status` = 99 where 1=1;

ALTER TABLE `record_ai_check_record`
DROP COLUMN `version`
;

ALTER TABLE `record_product_matches_ai_check`
DROP COLUMN `version`
;
ALTER TABLE record_product_matches_ai_check
    MODIFY COLUMN chain_id BIGINT NOT NULL COMMENT '产业链id';

ALTER TABLE record_product_matches_ai_check
DROP PRIMARY KEY,
ADD PRIMARY KEY (id, chain_id);

ALTER TABLE record_product_matches_ai_check
    PARTITION BY HASH(chain_id)
    PARTITIONS 10;

update record_product_matches_ai_check
set `status` = 99 where 1=1;

ALTER TABLE `record_product_matches_dissociated`
DROP KEY `uk`
;
ALTER TABLE `record_product_matches_dissociated`
DROP COLUMN `version`
;



ALTER TABLE record_product_matches_dissociated
    MODIFY COLUMN chain_id BIGINT NOT NULL COMMENT '产业链id';

ALTER TABLE record_product_matches_dissociated
DROP PRIMARY KEY,
ADD PRIMARY KEY (id, chain_id);

ALTER TABLE record_product_matches_dissociated
    PARTITION BY HASH(chain_id)
    PARTITIONS 10;


-- 以下为产业链模块化生产

CREATE TABLE `industry_chain_atom_node` (
                                            `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                                            `atom_node_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '原子节点名称',
                                            `node_desc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '原子节点描述',
                                            `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '0:未删除 1: 已删除',
                                            `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                            `gmt_modify` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                            `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
                                            `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
                                            PRIMARY KEY (`id`)
) ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
COMMENT='原子节点'
ROW_FORMAT=DYNAMIC
AVG_ROW_LENGTH=0;



CREATE TABLE `atom_node_ref_industry_label` (
                                                `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                                                `atom_node_id` bigint NOT NULL COMMENT '原子节点ID',
                                                `industry_label_id` bigint NOT NULL COMMENT '产业链标签ID',
                                                `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否逻辑删除 0否1是',
                                                `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                                `gmt_modify` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近一次更新时间',
                                                `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'system' COMMENT '创建者',
                                                `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'system' COMMENT '更新者',
                                                PRIMARY KEY (`id`),
                                                Unique KEY `uk_node_label`(`atom_node_id`,`industry_label_id`) USING BTREE
) ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci
COMMENT='原子节点与产业链标签关系表'
ROW_FORMAT=DYNAMIC;

CREATE TABLE `chain_node_ref_atom_node` (
                                            `id` bigint(21) NOT NULL AUTO_INCREMENT  COMMENT 'id',
                                            `node_id` bigint(21) NOT NULL COMMENT '产业链节点ID',
                                            `atom_node_id` bigint(21) NOT NULL COMMENT '原子节点ID',
                                            `deleted` tinyint NULL default 0 COMMENT '0 : 未删除 1: 已删除',
                                            `create_by` varchar(32) NULL,
                                            `update_by` varchar(32) NULL,
                                            `gmt_create` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                            `gmt_modify` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                            PRIMARY KEY (`id`),
                                            Unique KEY `idx_node_atom`(`node_id`,`atom_node_id`) USING HASH
) ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8
COMMENT='产业链节点关联原子节点';

ALTER TABLE `record_agent_product_matches`
    ADD KEY `idx_chain_matched_product`(`industry_chain_id`,`matched_product`) USING HASH
;

ALTER TABLE `record_product_matches_ai_check`
    ADD KEY `idx_c_id_pid`(`chain_id`,`product_id`) USING HASH
;

--清除重复数据
select `biz_id` , count(1) as tt from `record_industry_chain_node`
group by `biz_id`
having tt > 1
;

insert into `industry_chain_atom_node` (id, `atom_node_name` , `node_desc` )
SELECT `biz_id` ,`node_name` , `node_desc`   FROM `record_industry_chain_node` where `is_leaf` = 1 and `deleted` = 0;


CREATE TABLE `industry_label_ref_product` (
                                              `id` bigint(21) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                              `industry_label_id` bigint(21) NOT NULL COMMENT '产业链标签ID',
                                              `product_id` varchar(32) NOT NULL COMMENT '产品ID',
                                              `create_by` varchar(32) NOT NULL DEFAULT 'admin',
                                              `update_by` varchar(32) NOT NULL DEFAULT 'admin',
                                              `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                              `gmt_modify` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                              PRIMARY KEY (`id`)
) ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8
COMMENT='产业链标签关联产品表';
