package liuru.world.vote_master.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import liuru.world.vote_master.entity.DemoData;

import java.util.ArrayList;
import java.util.List;

public class DemoDataListener extends AnalysisEventListener<DemoData> {
    List<DemoData> list = new ArrayList<DemoData>();
    public DemoDataListener(){}

    /**
     * 这个每一条数据解析都回来调用
     * @param demoData
     * @param analysisContext
     */
    @Override
    public void invoke(DemoData demoData, AnalysisContext analysisContext) {
        System.out.println("解析到一条数据：" + JSON.toJSONString(demoData));
        list.add(demoData);
    }

    /**
     * 所有数据完成了，都会来调用
     * @param analysisContext
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println(JSON.toJSONString(list));
    }
}
