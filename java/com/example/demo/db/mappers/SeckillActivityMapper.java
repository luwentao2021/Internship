package com.example.demo.db.mappers;

import com.example.demo.db.po.SeckillActivity;

import java.util.List;

public interface SeckillActivityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SeckillActivity record);

    int insertSelective(SeckillActivity record);

    SeckillActivity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SeckillActivity record);

    int updateByPrimaryKey(SeckillActivity record);

    SeckillActivity Bycustomerid(Long commodityId);

    List<SeckillActivity> querySeckillActivitysByStatus(int activityStatus);

    int lockStock(Long id);

    int deductStock(Long id);

    int deletedata(long id);

    void revertStock(Long seckillActivityId);
}