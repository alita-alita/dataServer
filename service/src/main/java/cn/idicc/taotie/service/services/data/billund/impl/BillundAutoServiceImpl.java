package cn.idicc.taotie.service.services.data.billund.impl;

import cn.idicc.billund.common.starter.config.BillundProfile;
import cn.idicc.billund.common.starter.config.table.TableFieldInfo;
import cn.idicc.billund.common.starter.config.table.TableInfo;
import cn.idicc.billund.common.starter.config.table.TableInfoBuilder;
import cn.idicc.billund.common.starter.mapper.DefaultMapper;
import cn.idicc.billund.common.starter.mapper.MapperManager;
import cn.idicc.billund.common.starter.service.BaseService;
import cn.idicc.billund.common.starter.service.ServiceManager;
import cn.idicc.billund.common.starter.utils.ConvertUtil;
import cn.idicc.taotie.infrastructment.enums.DataSyncActionEnum;
import cn.idicc.taotie.service.message.data.KafkaDataMessage;
import cn.idicc.taotie.service.services.data.billund.BillundAutoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * @Author: MengDa
 * @Date: 2023/8/15
 * @Description:
 * @version: 1.0
 */
@Service
@Slf4j
public class BillundAutoServiceImpl implements BillundAutoService {

    @Autowired
    private ServiceManager serviceManager;

    @Autowired
    private MapperManager<DefaultMapper> mapperManager;

    @Autowired
    private BillundProfile billundProfile;

    private ConcurrentMap<String,ConcurrentMap<String,Class>> columnTypeMap;

    private TableInfo getTableInfo(String tableName){
        return TableInfoBuilder.getCacheTableInfo(tableName);
    }

    private ConcurrentMap<String,Class> getColumnTypeMap(String tableName){
        if (columnTypeMap == null){
            columnTypeMap = new ConcurrentHashMap<>();
        }
        if (!columnTypeMap.containsKey(tableName)){
            ConcurrentMap<String,Class> map = getTableInfo(tableName).getFieldList()
                    .stream()
                    .collect(Collectors.toConcurrentMap(TableFieldInfo::getProperty,
                            TableFieldInfo::getPropertyType));
            map.put(getTableInfo(tableName).getKeyProperty(),getTableInfo(tableName).getKeyType());
            columnTypeMap.put(tableName,map);
        }
        return columnTypeMap.get(tableName);
    }

    private BaseService getService(String tableName){
        return serviceManager.getService(tableName);
    }

    private DefaultMapper getMapper(String tableName){
        return mapperManager.getMapper(tableName);
    }

    @Override
    public void consumeMessage(KafkaDataMessage message) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        DefaultMapper defaultMapper = getMapper(message.getBusiness_code());
        DataSyncActionEnum actionEnum = DataSyncActionEnum.valueOf(message.getAction().toUpperCase());
        switch (actionEnum){
            case INSERT:
                insert(message,defaultMapper);
                break;
            case UPDATE:
                update(message,defaultMapper);
                break;
            case DELETE:
                delete(message,defaultMapper);
                break;
            default:
                break;
        }
    }

    private void insert(KafkaDataMessage message,DefaultMapper defaultMapper) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        ConcurrentMap<String,Class> columnMap = getColumnTypeMap(message.getBusiness_code());
        if (columnMap == null || columnMap.isEmpty()){
            log.error("columnMap is null or empty message is {}",message);
        }
        updateLogicField(message.getData().getElement(),message.getBusiness_code());
        Map<String,Object> element = message.getData().getElement().entrySet().stream().collect(
                Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> ConvertUtil.convertFromString(entry.getValue(),columnMap.get(entry.getKey()))));
        for (String key:columnMap.keySet()){
            if (!element.containsKey(key)){
                element.put(key,null);
            }
        }
        if (message.getData().getKey_id() != null){
            String keyProperty = getTableInfo(message.getBusiness_code()).getKeyProperty();
            Class keyType = getTableInfo(message.getBusiness_code()).getKeyType();
            element.put(keyProperty,
                    ConvertUtil.convertFromString(message.getData().getKey_id().toString(),keyType));
        }
        defaultMapper.insertDuplicate(element);
    }

    private void update(KafkaDataMessage message,DefaultMapper defaultMapper) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        ConcurrentMap<String,Class> columnMap = getColumnTypeMap(message.getBusiness_code());
        if (columnMap == null || columnMap.isEmpty()){
            log.error("columnMap is null or empty message is {}",message);
        }
        updateLogicField(message.getData().getElement(),message.getBusiness_code());
        Map<String,Object> element = message.getData().getElement().entrySet().stream().collect(
                Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> ConvertUtil.convertFromString(entry.getValue(),columnMap.get(entry.getKey()))));
        for (String key:columnMap.keySet()){
            if (!element.containsKey(key)){
                element.put(key,null);
            }
        }
        if (message.getData().getKey_id() != null){
            String keyProperty = getTableInfo(message.getBusiness_code()).getKeyProperty();
            Class keyType = getTableInfo(message.getBusiness_code()).getKeyType();
            element.put(keyProperty,
                    ConvertUtil.convertFromString(message.getData().getKey_id().toString(),keyType));
        }else {
            throw new RuntimeException("主键不能为空");
        }
        defaultMapper.deleteById(message.getData().getKey_id());
        defaultMapper.insertDuplicate(element);
    }

    private void delete(KafkaDataMessage message,DefaultMapper defaultMapper){
        defaultMapper.deleteById(message.getData().getKey_id());
    }

    private void updateLogicField(Map<String, String> element, String tableName){
        TableInfo tableInfo = getTableInfo(tableName);
        if (tableInfo.isWithLogicDelete()){
            TableFieldInfo logicDeleteFieldInfo = tableInfo.getLogicDeleteFieldInfo();
            element.put(logicDeleteFieldInfo.getColumn(),logicDeleteFieldInfo.getLogicNotDeleteValue());
        }
    }

    private QueryWrapper buildWrapper(Map<String,String> conditions,
                                      Map<String,Class> columnClassMap){
        QueryWrapper wrapper = new QueryWrapper();
        for(Map.Entry<String,String> entry:conditions.entrySet()){
            if (entry.getValue() != null) {
                wrapper.eq(entry.getKey(),
                        ConvertUtil.convertFromString(entry.getValue(), columnClassMap.get(entry.getKey())));
            }
        }
        return wrapper;
    }


}
