package liuru.world.vote_master.mapper;

import liuru.world.vote_master.entity.ExcelPR;
import liuru.world.vote_master.entity.Performance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Mapper
public interface PerformanceMapper {
    public List<Performance> findShows();

    public List<ExcelPR> getRankPerf();

    public List<Performance> findShowsM();

    public Integer getCounts();

    public Integer insertPerformance(Performance performance);

    public List<Performance> findShowsByNameOrId(@Param("id") Integer id,@Param("name") String name);

    public Integer updatePerformance(@Param("name") String name,@Param("score") Integer score);

}
