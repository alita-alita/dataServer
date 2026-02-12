package cn.idicc.taotie.infrastructment.entity.spider;

import java.sql.Date;
import java.util.Objects;

public class News {

    private long id;
    private String source;
    private String url;

    private String uKey;

    private String title;
    private String content;

    private Date publishingDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

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

    public Date getPublishingDate() {
        return publishingDate;
    }

    public void setPublishingDate(Date publishingDate) {
        this.publishingDate = publishingDate;
    }

    public String getuKey() {
        return uKey;
    }

    public void setuKey(String uKey) {
        this.uKey = uKey;
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", source='" + source + '\'' +
                ", url='" + url + '\'' +
                ", uKey='" + uKey + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", publishingDate=" + publishingDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        News news = (News) o;
        return id == news.id && Objects.equals(source, news.source) && Objects.equals(url, news.url) && Objects.equals(uKey, news.uKey) && Objects.equals(title, news.title) && Objects.equals(content, news.content) && Objects.equals(publishingDate, news.publishingDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, source, url, uKey, title, content, publishingDate);
    }
}
