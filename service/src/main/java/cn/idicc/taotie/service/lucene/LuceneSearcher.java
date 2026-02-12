package cn.idicc.taotie.service.lucene;

import cn.idicc.taotie.infrastructment.entity.icm.RecordAppEnterpriseIndustryChainSuspectedDO;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Component
public class LuceneSearcher {
    @Autowired
    private  LuceneIndexer indexer;
    public List<RecordAppEnterpriseIndustryChainSuspectedDO> fuzzySearch(String enterpriseName, Long industryChainId) throws IOException {
        Directory indexDirectory = new RAMDirectory();
        Analyzer analyzer = new StandardAnalyzer();
        indexer.buildIndex();

        IndexReader indexReader = DirectoryReader.open(indexDirectory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();
        if (enterpriseName!= null &&!enterpriseName.isEmpty()) {
            // 使用 FuzzyQuery 进行模糊查找
            FuzzyQuery enterpriseNameQuery = new FuzzyQuery(new Term("enterprise_name_text", enterpriseName));
            booleanQueryBuilder.add(enterpriseNameQuery, BooleanClause.Occur.MUST);
        }
        if (industryChainId!= null) {
            Query industryChainIdQuery = new TermQuery(new Term("industry_chain_id", String.valueOf(industryChainId)));
            booleanQueryBuilder.add(industryChainIdQuery, BooleanClause.Occur.MUST);
        }
        Query combinedQuery = booleanQueryBuilder.build();

        TopDocs topDocs = indexSearcher.search(combinedQuery, 10);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        List<RecordAppEnterpriseIndustryChainSuspectedDO> matchedRecords = new ArrayList<>();
        for (ScoreDoc scoreDoc : scoreDocs) {
            Document document = indexSearcher.doc(scoreDoc.doc);
            RecordAppEnterpriseIndustryChainSuspectedDO record = new RecordAppEnterpriseIndustryChainSuspectedDO(
                    document.get("biz_id"),
                    document.get("enterprise_id"),
                    document.get("enterprise_name"),
                    document.get("enterprise_uni_code"),
                    Long.valueOf(document.get("industry_chain_id")),
                    document.get("industry_chain_name"),
                    document.get("suspected_clue"),
                    Integer.parseInt(document.get("has_product")),
                    document.get("data_source"),
                    Integer.parseInt(document.get("status")),
                    Boolean.valueOf(document.get("negative")),
                    document.get("fail_reason")
            );
            matchedRecords.add(record);
        }
        indexReader.close();
        return matchedRecords;
    }
}
