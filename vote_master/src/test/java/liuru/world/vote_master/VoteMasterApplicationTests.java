package liuru.world.vote_master;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import liuru.world.vote_master.entity.DemoData;
import liuru.world.vote_master.entity.Performance;
import liuru.world.vote_master.listener.DemoDataListener;
import liuru.world.vote_master.listener.PerformanceListener;
import liuru.world.vote_master.service.PerformanceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
class VoteMasterApplicationTests {
    @Autowired
    private PerformanceService performanceService;

    @Test
    void contextLoads() {
    }

    @Test
    void performancesGet(){
        System.out.println(performanceService.getShows());
    }

    @Test
    public void read(){
        String fileName = "/Users/liantao/Downloads/fileRepository/demo.xlsx";
        EasyExcel.read(fileName, DemoData.class,new DemoDataListener()).sheet().doRead();
    }

    @Test
    public void simpleWrite(){
        String fileName = "/Users/liantao/Downloads/fileRepository/demo.xlsx";
        List<DemoData> data = new ArrayList<DemoData>();
        DemoData demoData1 = new DemoData();
        demoData1.setString("string1");
        demoData1.setDoubleData(100.0);
        demoData1.setDate(new Date());
        DemoData demoData2 = new DemoData();
        demoData2.setDoubleData(200.0);
        demoData2.setDate(new Date());
        demoData2.setString("string2");
        data.add(demoData1);
        data.add(demoData2);
        EasyExcel.write(fileName,DemoData.class).sheet("one").doWrite(data);
    }

    /**
     * 读取多个sheet
     */
    @Test
    public void repeatedRead(){
        String fileName = "/Users/liantao/Downloads/fileRepository/demo.xlsx";
//        EasyExcel.read(fileName,DemoData.class,new DemoDataListener()).doReadAll();
//        读取部分sheet
        ExcelReader excelReader = EasyExcel.read(fileName).build();
        ReadSheet readSheet1 = EasyExcel.readSheet(0).head(DemoData.class).registerReadListener(new DemoDataListener()).build();
        ReadSheet readSheet2 = EasyExcel.readSheet(1).head(Performance.class).registerReadListener(new PerformanceListener()).build();
        excelReader.read(readSheet1,readSheet2);
        excelReader.finish();

    }

}
