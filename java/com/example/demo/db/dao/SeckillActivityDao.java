package com.example.demo.db.dao;

import com.example.demo.db.po.SeckillActivity;

import java.util.List;

public interface SeckillActivityDao {

    public List<SeckillActivity> querySeckillActivitysByStatus(int activityStatus);

    public void inertSeckillActivity(SeckillActivity seckillActivity);

    public SeckillActivity querySeckillActivityById(long activityId);

    public SeckillActivity checkSeckillActivityBycustomerid(long id);

    public void updateSeckillActivity(SeckillActivity seckillActivity);

    boolean lockStock(Long seckillActivityId);

    int deleterow(long id);

    boolean deductStock(Long seckillActivityId);

    void revertStock(Long seckillActivityId);
}
