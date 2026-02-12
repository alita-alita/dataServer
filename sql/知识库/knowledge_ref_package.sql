CREATE TABLE knowledge_ref_package (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    package_id BIGINT NOT NULL,
    knowledge_id BIGINT NOT NULL,
    deleted BOOLEAN DEFAULT FALSE,
    gmt_create TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    gmt_modify TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    create_by VARCHAR(50),
    update_by VARCHAR(50),
    FOREIGN KEY (package_id) REFERENCES knowledge_package(id),
    FOREIGN KEY (knowledge_id) REFERENCES knowledge(id)
);
