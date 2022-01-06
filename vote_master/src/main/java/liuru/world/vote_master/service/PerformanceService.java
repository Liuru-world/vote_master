package liuru.world.vote_master.service;


import liuru.world.vote_master.entity.ExcelPR;
import liuru.world.vote_master.entity.Performance;

import java.util.List;

public interface PerformanceService {

    public List<Performance> getShows();

    public List<ExcelPR> getRankPerf();

    public List<Performance> getShowsM();

    public Integer getCounts();

    public Integer insertPerformance(Performance performance);

    public List<Performance> findShowsByNameOrId(Integer id,String name);

    public  Integer updatePerformance(String name,Integer score);
}
