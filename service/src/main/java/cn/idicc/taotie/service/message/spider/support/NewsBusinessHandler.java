package cn.idicc.taotie.service.message.spider.support;

import cn.idicc.pangu.service.KeywordDictionaryRpcService;
import cn.idicc.taotie.infrastructment.mapper.spider.NewsDao;
import cn.idicc.taotie.infrastructment.entity.spider.News;
import cn.idicc.taotie.infrastructment.utils.DateUtil;
import cn.idicc.taotie.infrastructment.utils.MessageKeyUtils;
import cn.idicc.taotie.service.message.spider.BusinessHandler;
import cn.idicc.taotie.service.message.spider.MessageEntity;
import cn.idicc.taotie.service.message.spider.entity.NewsMessageDataEntity;
import com.alibaba.fastjson2.JSONObject;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 新闻类处理
 */
@Service
public class NewsBusinessHandler implements BusinessHandler {

    private static final Logger logger = LoggerFactory.getLogger(NewsBusinessHandler.class);

    @Autowired
    private NewsDao newsDao;

    @DubboReference(interfaceClass = KeywordDictionaryRpcService.class, check = false)
    private KeywordDictionaryRpcService keywordDictionaryRpcService;

    @Override
    public boolean handle(MessageEntity messageEntity) {

        NewsMessageDataEntity newsMessageDataEntity = JSONObject.parseObject(messageEntity.getData().toString(), NewsMessageDataEntity.class);
        News news = toNewsDO(messageEntity, newsMessageDataEntity);
        int effectRows = newsDao.countByUKey(news.getuKey());
        if (effectRows > 0) {
            logger.info("已存在:{}", messageEntity.toString());
            return true;
        }
        int insert = newsDao.insert(news);

        //调用pangu的rpc接口新增来源关键字
        keywordDictionaryRpcService.add(news.getSource(),3);
        return insert == 1;
    }

    private News toNewsDO(MessageEntity messageEntity, NewsMessageDataEntity newsMessageDataEntity) {
        News newsDO = new News();
        newsDO.setSource(messageEntity.getSource());
        newsDO.setUrl(messageEntity.getUrl());
        newsDO.setContent(newsMessageDataEntity.getContent());
        newsDO.setTitle(newsMessageDataEntity.getTitle());
        newsDO.setuKey(MessageKeyUtils.generateKey(messageEntity.getUrl()));
        newsDO.setPublishingDate(DateUtil.toSQLDate(newsMessageDataEntity.getPublishingDate()));
        return newsDO;
    }
}
