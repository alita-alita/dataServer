package cn.idicc.taotie.service.lucene;

import cn.idicc.taotie.infrastructment.entity.icm.RecordAppEnterpriseIndustryChainSuspectedDO;
import cn.idicc.taotie.infrastructment.mapper.icm.RecordAppEnterpriseIndustryChainSuspectedMapper;
import org.apache.dubbo.rpc.cluster.Directory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class LuceneIndexer {

    @Autowired
    private RecordAppEnterpriseIndustryChainSuspectedMapper recordMapper;

    public void buildIndex() throws IOException {
        RAMDirectory indexDirectory = new RAMDirectory();
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(indexDirectory, config);


        List<RecordAppEnterpriseIndustryChainSuspectedDO> records = recordMapper.selectAll(null, null);
        for (RecordAppEnterpriseIndustryChainSuspectedDO record : records) {
            Document document = new Document();
            document.add(new StringField("biz_id", record.getBizId(), Field.Store.YES));
            document.add(new StringField("enterprise_id", record.getEnterpriseId(), Field.Store.YES));
            document.add(new TextField("enterprise_name", record.getEnterpriseName(), Field.Store.YES));
            document.add(new StringField("enterprise_uni_code", record.getEnterpriseUniCode(), Field.Store.YES));
            document.add(new StringField("industry_chain_id", String.valueOf(record.getIndustryChainId()), Field.Store.YES));
            document.add(new StringField("industry_chain_name", record.getIndustryChainName(), Field.Store.YES));
            document.add(new StringField("data_source", record.getDataSource(), Field.Store.YES));
            document.add(new StringField("negative", String.valueOf(record.getNegative()), Field.Store.YES));
            // 使用 TextField 存储企业名称，方便进行模糊查找
            indexWriter.addDocument(document);
        }
        indexWriter.close();
    }
}
