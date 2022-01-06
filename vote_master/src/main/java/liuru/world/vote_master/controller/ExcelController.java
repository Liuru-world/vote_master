package liuru.world.vote_master.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import liuru.world.vote_master.entity.DemoData;
import liuru.world.vote_master.entity.ExcelPR;
import liuru.world.vote_master.entity.Performance;
import liuru.world.vote_master.exception.ParamException;
import liuru.world.vote_master.listener.DemoDataListener;
import liuru.world.vote_master.listener.PerformanceListener;
import liuru.world.vote_master.service.PerformanceService;
import liuru.world.vote_master.service.RankService;
import liuru.world.vote_master.utils.HttpCodeEnum;
import liuru.world.vote_master.utils.HttpReturn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/excel")
public class ExcelController {
    @Autowired
    private PerformanceService performanceService;
    @Autowired
    private RankService rankService;

    @GetMapping("/download")
    @ApiOperation(value = "download excel",notes = "no need params")
    @ApiResponses({
            @ApiResponse(code = 200,message = "请求正确"),
            @ApiResponse(code = 400,message = "参数错误"),
            @ApiResponse(code = 404,message = "页面没找到"),
            @ApiResponse(code = 500,message = "服务错误")
    })
    @CrossOrigin(value = "*", maxAge = 1800, allowedHeaders ="*")
    public void download(HttpServletResponse response){
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=/Users/liantao/Downloads/fileRepository/demo.xlsx");
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
            EasyExcel.write(response.getOutputStream(), DemoData.class).sheet("模版").doWrite(data);
            log.info("下载成功");
        }catch (Exception e){
            log.error("下载失败了");
            e.printStackTrace();
            throw new ParamException(400,"下载失败");
        }
    }


    @PostMapping("/upload")
    @ApiOperation(value = "upload excel",notes = "params multipartFile")
    @ApiResponses({
            @ApiResponse(code = 200,message = "请求正确"),
            @ApiResponse(code = 400,message = "参数错误"),
            @ApiResponse(code = 404,message = "页面没找到"),
            @ApiResponse(code = 500,message = "服务错误")
    })
    @CrossOrigin(value = "*", maxAge = 1800, allowedHeaders ="*")
    public HttpReturn upload(MultipartFile file){
        try {
            EasyExcel.read(file.getInputStream(),DemoData.class,new DemoDataListener()).sheet().doRead();
            log.info("上传成功");
            return new HttpReturn(HttpCodeEnum.OK,"success");
        }catch (Exception e){
            log.error("上传失败了");
            e.printStackTrace();
            throw new ParamException(400,"上传失败");
        }
    }

    @PutMapping("/input")
    @ApiOperation(value = "input excel",notes = "params fileName")
    @ApiResponses({
            @ApiResponse(code = 200,message = "请求正确"),
            @ApiResponse(code = 400,message = "参数错误"),
            @ApiResponse(code = 404,message = "页面没找到"),
            @ApiResponse(code = 500,message = "服务错误")
    })
    @CrossOrigin(value = "*", maxAge = 1800, allowedHeaders ="*")
    public HttpReturn input(@RequestBody Map map){
        try {
            String fileName = String.valueOf(map.get("fileName"));
            ExcelReader excelReader = EasyExcel.read(fileName).build();
            ReadSheet readSheet = EasyExcel.readSheet(0).head(Performance.class).registerReadListener(new PerformanceListener(performanceService,rankService)).build();
            excelReader.read(readSheet);
            excelReader.finish();
            log.info("导入成功");
            return new HttpReturn(HttpCodeEnum.OK,"input successfully");
        }catch (Exception e){
            log.error("导入失败了");
            e.printStackTrace();
            throw new ParamException(400,"导入失败");
        }
//        /Users/liantao/Downloads/fileRepository/工作簿1.xlsx
    }


    @GetMapping("/rankPerf")
    @ApiOperation(value = "Get the Performances And Ranks",notes = "no need params")
    @ApiResponses({
            @ApiResponse(code = 200,message = "请求正确"),
            @ApiResponse(code = 400,message = "参数错误"),
            @ApiResponse(code = 404,message = "页面没找到"),
            @ApiResponse(code = 500,message = "服务错误")
    })
    @CrossOrigin(value = "*", maxAge = 1800, allowedHeaders ="*")
    public HttpReturn getRankPerf(){
        try {
            List<ExcelPR> excelPRS = performanceService.getRankPerf();
            log.info("查询节目成功");
            return new HttpReturn(HttpCodeEnum.OK,excelPRS);
        }catch (Exception e){
            log.error("查询节目失败了");
            e.printStackTrace();
            throw new ParamException(400,"查询节目失败");
        }
    }

    @GetMapping("/exportRP")
    @ApiOperation(value = "Export the Performances And Ranks",notes = "no need params")
    @ApiResponses({
            @ApiResponse(code = 200,message = "请求正确"),
            @ApiResponse(code = 400,message = "参数错误"),
            @ApiResponse(code = 404,message = "页面没找到"),
            @ApiResponse(code = 500,message = "服务错误")
    })
    @CrossOrigin(value = "*", maxAge = 1800, allowedHeaders ="*")
    public String exportRP(HttpServletResponse response){
        try {
            Date date = new Date();
            Timestamp t = new Timestamp(date.getTime());
            String fileName =  t + ".xlsx";
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            List<ExcelPR> excelPRS = performanceService.getRankPerf();
            EasyExcel.write(response.getOutputStream(), ExcelPR.class).sheet("节目列表").doWrite(excelPRS);
            return "success";
        }catch (Exception e){
            log.error("导出节目失败了");
            e.printStackTrace();
            throw new ParamException(400,"导出节目失败");
        }
    }
}
