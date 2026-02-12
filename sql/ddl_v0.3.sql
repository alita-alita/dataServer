-- 知识库管理
ALTER TABLE `inst_research_institution`
DROP KEY `idx_code`,
	ADD KEY `idx_code`(`code`,`deleted`) USING BTREE,
	DROP KEY `uk_rd_institution`,
	ADD Unique KEY `uk_rd_institution`(`name`,`deleted`) USING BTREE
;

ALTER TABLE `policy`
DROP KEY `uk`,
	ADD Unique KEY `uk`(`policy_md5`,`deleted`) USING BTREE
;

ALTER TABLE `policy`
    MODIFY COLUMN `admin_unit` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '政府政策发布单位名称' AFTER `content`
;