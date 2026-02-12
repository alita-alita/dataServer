alter table record_industry_label
    add label_desc text null comment '产品定义' after label_name;

alter table record_agent_product_matches
    add extra_reason varchar(1024) null comment '额外评论' after fail_reason;

# record_agent_prompt更新