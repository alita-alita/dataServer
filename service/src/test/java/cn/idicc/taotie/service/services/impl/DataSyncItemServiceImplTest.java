
package cn.idicc.taotie.service.services.impl;

import cn.idicc.taotie.infrastructment.entity.spider.DataSyncItemDO;
import cn.idicc.taotie.infrastructment.mapper.spider.DataSyncItemMapper;
import cn.idicc.taotie.infrastructment.request.spider.DataSyncItemRequest;
import cn.idicc.taotie.infrastructment.response.data.DataSyncItemResponse;
import cn.idicc.taotie.service.services.data.taoti.Impl.DataSyncItemServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 数据同步任务数据项表 - 服务实现类测试
 *
 * @author guyongliang
 * @date 2026-01-27
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class DataSyncItemServiceImplTest {

    @Mock
    private DataSyncItemMapper dataSyncItemMapper;

    @InjectMocks
    private DataSyncItemServiceImpl dataSyncItemService;

    // 移除了构造函数中的 Mockito 初始化

    @Test
    void testSave() {
        // 准备测试数据
        DataSyncItemRequest request = new DataSyncItemRequest();
        request.setItemName("测试数据项");
        request.setMartToPlatform(true);
        request.setPlatformToMart(false);
        request.setPlatformToProd(true);

        DataSyncItemDO mockDO = new DataSyncItemDO();
        mockDO.setId(1L);
        when(dataSyncItemMapper.insert(any(DataSyncItemDO.class))).thenReturn(1);
        when(dataSyncItemMapper.selectById(1L)).thenReturn(mockDO);

        // 执行测试
        Long result = dataSyncItemService.save(request);

        // 验证结果
        assertEquals(1L, result);
        verify(dataSyncItemMapper, times(1)).insert(any(DataSyncItemDO.class));
    }

    @Test
    void testDelete() {
        // 准备测试数据
        Long id = 1L;
        when(dataSyncItemMapper.updateById(any(DataSyncItemDO.class))).thenReturn(1);

        // 执行测试
        Boolean result = dataSyncItemService.delete(id);

        // 验证结果
        assertTrue(result);
        verify(dataSyncItemMapper, times(1)).updateById(any(DataSyncItemDO.class));
    }

    @Test
    void testUpdate() {
        // 准备测试数据
        DataSyncItemRequest request = new DataSyncItemRequest();
        request.setId(1L);
        request.setItemName("更新后的数据项");

        when(dataSyncItemMapper.updateById(any(DataSyncItemDO.class))).thenReturn(1);

        // 执行测试
        Boolean result = dataSyncItemService.update(request);

        // 验证结果
        assertTrue(result);
        verify(dataSyncItemMapper, times(1)).updateById(any(DataSyncItemDO.class));
    }

    @Test
    void testGetById() {
        // 准备测试数据
        Long id = 1L;
        DataSyncItemDO mockDO = new DataSyncItemDO();
        mockDO.setId(id);
        mockDO.setItemName("测试数据项");
        mockDO.setMartToPlatform(true);
        mockDO.setPlatformToMart(false);
        mockDO.setPlatformToProd(true);

        when(dataSyncItemMapper.selectById(id)).thenReturn(mockDO);

        // 执行测试
        DataSyncItemResponse result = dataSyncItemService.getById(id);

        // 验证结果
        assertNotNull(result);
        assertEquals("测试数据项", result.getItemName());
        assertEquals(true, result.getMartToPlatform());
        assertEquals(false, result.getPlatformToMart());
        assertEquals(true, result.getPlatformToProd());
        verify(dataSyncItemMapper, times(1)).selectById(id);
    }

    @Test
    void testListAll() {
        // 准备测试数据
        DataSyncItemDO item1 = new DataSyncItemDO();
        item1.setId(1L);
        item1.setItemName("数据项1");
        item1.setMartToPlatform(true);
        item1.setPlatformToMart(false);
        item1.setPlatformToProd(true);

        DataSyncItemDO item2 = new DataSyncItemDO();
        item2.setId(2L);
        item2.setItemName("数据项2");
        item2.setMartToPlatform(true);
        item2.setPlatformToMart(false);
        item2.setPlatformToProd(true);

        List<DataSyncItemDO> mockList = Arrays.asList(item1, item2);
        when(dataSyncItemMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(mockList);

        // 执行测试
        List<DataSyncItemResponse> result = dataSyncItemService.listAll();

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("数据项1", result.get(0).getItemName());
        assertEquals("数据项2", result.get(1).getItemName());
        verify(dataSyncItemMapper, times(1)).selectList(any(LambdaQueryWrapper.class));
    }

    @Test
    void testListByCondition() {
        // 准备测试数据
        DataSyncItemRequest request = new DataSyncItemRequest();
        request.setItemName("数据项1");

        DataSyncItemDO item1 = new DataSyncItemDO();
        item1.setId(1L);
        item1.setItemName("数据项1");
        item1.setPlatformToProd(true);
        item1.setPlatformToMart(false);
        item1.setPlatformToProd(true);

        List<DataSyncItemDO> mockList = Arrays.asList(item1);
        when(dataSyncItemMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(mockList);

        // 执行测试
        List<DataSyncItemResponse> result = dataSyncItemService.pageList(null, 1, 10).getRecords();

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("数据项1", result.get(0).getItemName());
        verify(dataSyncItemMapper, times(1)).selectList(any(LambdaQueryWrapper.class));
    }
}