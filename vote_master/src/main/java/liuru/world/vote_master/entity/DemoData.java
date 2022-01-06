package liuru.world.vote_master.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;
@Data
public class DemoData {
    @ExcelProperty(index = 0)
    private String string;
    @ExcelProperty(index = 1)
    private Date date;
    @ExcelProperty(index = 2)
    private Double doubleData;
}
