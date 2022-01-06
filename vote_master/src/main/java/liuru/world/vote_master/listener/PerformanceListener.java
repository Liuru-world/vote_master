package liuru.world.vote_master.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.fastjson.JSON;
import liuru.world.vote_master.entity.Performance;
import liuru.world.vote_master.entity.Rank;
import liuru.world.vote_master.service.PerformanceService;
import liuru.world.vote_master.service.RankService;
import liuru.world.vote_master.service.impl.PerformanceServiceImpl;
import liuru.world.vote_master.service.impl.RankServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Slf4j
public class PerformanceListener extends AnalysisEventListener<Performance> {

    private PerformanceService performanceService;
    public RankService rankService;
    List<Performance> list = new ArrayList<Performance>();

    public PerformanceListener(){}
    public PerformanceListener(PerformanceService performanceService,RankService rankService){
        this.rankService = rankService;
        this.performanceService = performanceService;
    }
    @Override
    public void invoke(Performance performance, AnalysisContext analysisContext) {
        log.info("解析到一条数据：" + JSON.toJSONString(performance));
        list.add(performance);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info(JSON.toJSONString(list));
        Rank rank = new Rank();
        for (Performance performance:list){
            performanceService.insertPerformance(performance);
            rank.setId(performance.getId());
            rank.setRank(performance.getRankNo());
            rankService.insertRank(rank);
        }
        log.info("数据已从Excel存入数据库");
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        log.info("解析到一条头数据:" + JSON.toJSONString(headMap));
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        log.error("解析失败，但是继续解析下一行：" + exception.getMessage());
        ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) exception;
        log.error("第" + excelDataConvertException.getRowIndex() + "行，+" + "第" + excelDataConvertException.getColumnIndex()
        + "列解析异常");
    }
}
