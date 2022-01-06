package liuru.world.vote_master.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import org.springframework.stereotype.Repository;

@Repository
@Data
public class Performance {
    @ExcelProperty(index = 0)
    private Integer id;
    @ExcelProperty(index = 1)
    private String name;
    @ExcelProperty(index = 2)
    private String actors;
    @ExcelProperty(index = 3)
    private String kind;
    @ExcelProperty(index = 4)
    private String date;
    @ExcelProperty(index = 5)
    private Integer score;
    @ExcelProperty(index = 6)
    private Integer rankNo;

}
