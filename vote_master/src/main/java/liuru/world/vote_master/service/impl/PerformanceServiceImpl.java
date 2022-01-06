package liuru.world.vote_master.service.impl;

import liuru.world.vote_master.entity.ExcelPR;
import liuru.world.vote_master.entity.Performance;
import liuru.world.vote_master.mapper.PerformanceMapper;
import liuru.world.vote_master.service.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PerformanceServiceImpl implements PerformanceService {
    @Autowired
    private PerformanceMapper performanceMapper;

    @Override
    public List<Performance> getShows() {
        return performanceMapper.findShows();
    }

    @Override
    public List<ExcelPR> getRankPerf() {
        return performanceMapper.getRankPerf();
    }

    @Override
    public List<Performance> getShowsM() {
        return performanceMapper.findShowsM();
    }

    @Override
    public Integer getCounts() {
        return performanceMapper.getCounts();
    }

    @Override
    public Integer insertPerformance(Performance performance) {
        return performanceMapper.insertPerformance(performance);
    }

    @Override
    public List<Performance> findShowsByNameOrId(Integer id,String name) {
        return performanceMapper.findShowsByNameOrId(id,name);
    }

    @Override
    public Integer updatePerformance(String name, Integer score) {
        return performanceMapper.updatePerformance(name,score);
    }
}
