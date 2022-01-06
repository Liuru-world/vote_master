package liuru.world.vote_master.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import org.springframework.stereotype.Repository;

@Data
@Repository
public class ExcelPR {
    @ExcelProperty(value = "节目",index = 0)
    private String name;
    @ExcelProperty(value = "演员",index = 1)
    private String actors;
    @ExcelProperty(value = "分类",index = 2)
    private String kind;
    @ExcelProperty(value =  "得分",index = 3)
    private Integer score;
    @ExcelProperty(value = "排名",index = 4)
    private Integer rank;
}
