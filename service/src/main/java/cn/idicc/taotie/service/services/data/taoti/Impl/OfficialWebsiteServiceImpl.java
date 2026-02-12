package cn.idicc.taotie.service.services.data.taoti.Impl;

import cn.idicc.taotie.infrastructment.entity.spider.Keyword;
import cn.idicc.taotie.infrastructment.entity.spider.OfficialWebsite;
import cn.idicc.taotie.infrastructment.entity.spider.OfficialWebsiteKafka;
import cn.idicc.taotie.infrastructment.mapper.spider.OfficialWebsiteMapper;
import cn.idicc.taotie.infrastructment.request.data.OfficialWebsiteReq;
import cn.idicc.taotie.service.services.data.taoti.OfficialWebsiteService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Log4j2
@Service
public class OfficialWebsiteServiceImpl implements OfficialWebsiteService {
    private static final Integer FAIL_CODE = 0;

    @Autowired
    private OfficialWebsiteMapper officialWebsiteMapper;

    @Override
    public PageInfo<OfficialWebsite> listOfficialWebsite(OfficialWebsiteReq officialWebsiteReq) {
        PageHelper.startPage(officialWebsiteReq.getPageNum(),officialWebsiteReq.getPageSize());
        List<OfficialWebsite> officialWebsites = officialWebsiteMapper.listOfficialWebsite(officialWebsiteReq.officialWebsiteName, officialWebsiteReq.officialWebsiteState);
        PageInfo<OfficialWebsite> listPageInfo = new PageInfo<>(officialWebsites);
        return listPageInfo;
    }

    @Override
    public List<OfficialWebsiteKafka> manualGather(List<Long> officialWebsiteId) {
        return officialWebsiteMapper.manualGather(officialWebsiteId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer addOfficialWebsite(OfficialWebsite officialWebsite) {
        if (officialWebsite == null){
            log.error("参数为空，无法添加关键词");
            return FAIL_CODE;
        }
        try {
            Integer addedOfficialWebsite = officialWebsiteMapper.addOfficialWebsite(officialWebsite);
            if (addedOfficialWebsite.equals(FAIL_CODE)) {
                log.error("添加关键词失败，请检查参数是否正确");
                return FAIL_CODE;
            }
            return addedOfficialWebsite;
        }  catch (Exception e) {
            e.printStackTrace();
            log.error("添加关键词失败，请检查参数是否正确");
            return FAIL_CODE;
        }

    }

    @Override
    public Integer addOfficialWebsiteBatch(List<OfficialWebsite> officialWebsiteDataList) {
        return officialWebsiteMapper.addOfficialWebsiteBatch(officialWebsiteDataList);
    }

    @Override
    public Integer updateOfficialWebsite(OfficialWebsite officialWebsite) {
        if (officialWebsite == null) {
            return 0;
        }
        try {
            Integer updateOfficialWebsite = officialWebsiteMapper.updateOfficialWebsite(officialWebsite);
            if (updateOfficialWebsite < 0) {
                return 0;
            }
            return updateOfficialWebsite;
        }  catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Integer updateOfficialWebsiteTime(Integer officialWebsiteId) {
        return officialWebsiteMapper.updateOfficialWebsiteTime(officialWebsiteId);
    }

    @Override
    public Integer delLogicOfficialWebsite(OfficialWebsite officialWebsite) {

        if (officialWebsite.getOfficialWebsiteId() == null) {
            return 0;
        }
        try {
            Integer delLogicOfficialWebsite = officialWebsiteMapper.delLogicOfficialWebsite(Math.toIntExact(officialWebsite.getOfficialWebsiteId()));
            if(delLogicOfficialWebsite < 0){
                return 0;
            }
            return  delLogicOfficialWebsite;
        }  catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
