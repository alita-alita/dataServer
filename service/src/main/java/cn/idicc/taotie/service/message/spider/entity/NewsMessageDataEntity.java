package cn.idicc.taotie.service.message.spider.entity;

import com.alibaba.fastjson2.annotation.JSONField;

import java.util.Objects;

public class NewsMessageDataEntity {

    private String title;
    private String content;
    @JSONField(name = "publishing_date")
    private String publishingDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublishingDate() {
        return publishingDate;
    }

    public void setPublishingDate(String publishingDate) {
        this.publishingDate = publishingDate;
    }

    @Override
    public String toString() {
        return "NewsMessageEntity{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", publishingDate='" + publishingDate + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NewsMessageDataEntity that = (NewsMessageDataEntity) o;
        return Objects.equals(title, that.title) && Objects.equals(content, that.content) && Objects.equals(publishingDate, that.publishingDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, content, publishingDate);
    }
}
