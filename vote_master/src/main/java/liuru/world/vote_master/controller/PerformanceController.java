package liuru.world.vote_master.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import liuru.world.vote_master.entity.Performance;
import liuru.world.vote_master.exception.ParamException;
import liuru.world.vote_master.service.PerformanceService;
import liuru.world.vote_master.utils.HttpCodeEnum;
import liuru.world.vote_master.utils.HttpReturn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/performance")
public class PerformanceController {
    @Autowired
    private PerformanceService performanceService;

    // 根据id获取用户(此方法可用于测试数据库连接、异常、RESTful风格、git、Swagger工具以及日志)
    @GetMapping("/performancesGet")
    @ApiOperation(value = "Get the Performances",notes = "no need params")
    @ApiResponses({
            @ApiResponse(code = 200,message = "请求正确"),
            @ApiResponse(code = 400,message = "参数错误"),
            @ApiResponse(code = 404,message = "页面没找到"),
            @ApiResponse(code = 500,message = "服务错误")
    })
    @CrossOrigin(value = "*", maxAge = 1800, allowedHeaders ="*")
    public HttpReturn getShows(){
        try {
            List<Performance> performances = performanceService.getShows();
            log.info("查询所有节目成功");
            return new HttpReturn(HttpCodeEnum.OK,performances);
        }catch (Exception e){
            log.error("查询所有节目失败了");
            e.printStackTrace();
            throw new ParamException(400,"查询所有节目失败");
        }
    }


    @GetMapping("/performancesGetM")
    @ApiOperation(value = "Get the Performances",notes = "no need params")
    @ApiResponses({
            @ApiResponse(code = 200,message = "请求正确"),
            @ApiResponse(code = 400,message = "参数错误"),
            @ApiResponse(code = 404,message = "页面没找到"),
            @ApiResponse(code = 500,message = "服务错误")
    })
    @CrossOrigin(value = "*", maxAge = 1800, allowedHeaders ="*")
    public HttpReturn getShowsM(){
        try {
            List<Performance> performances = performanceService.getShowsM();
            log.info("查询获奖节目成功");
            return new HttpReturn(HttpCodeEnum.OK,performances);
        }catch (Exception e){
            log.error("查询获奖节目失败了");
            e.printStackTrace();
            throw new ParamException(400,"查询获奖节目失败");
        }
    }

    @GetMapping("/countsGet")
    @ApiOperation(value = "Get the Counts",notes = "no need params")
    @ApiResponses({
            @ApiResponse(code = 200,message = "请求正确"),
            @ApiResponse(code = 400,message = "参数错误"),
            @ApiResponse(code = 404,message = "页面没找到"),
            @ApiResponse(code = 500,message = "服务错误")
    })
    @CrossOrigin(value = "*", maxAge = 1800, allowedHeaders ="*")
    public HttpReturn getCounts(){
        try {
           Integer num = performanceService.getCounts();
            log.info("查询节目数目成功");
            return new HttpReturn(HttpCodeEnum.OK,num);
        }catch (Exception e){
            log.error("查询所有节目数目失败了");
            e.printStackTrace();
            throw new ParamException(400,"查询所有数目失败");
        }
    }


    @PutMapping("/performanceInsert")
    @ApiOperation(value = "Insert the performance",notes = "need params like performance")
    @ApiResponses({
            @ApiResponse(code = 200,message = "请求正确"),
            @ApiResponse(code = 400,message = "参数错误"),
            @ApiResponse(code = 404,message = "页面没找到"),
            @ApiResponse(code = 500,message = "服务错误")
    })
    @CrossOrigin(value = "*", maxAge = 1800, allowedHeaders ="*")
    public HttpReturn insertPerformance(@RequestBody Map map){
        try {
            Date d = new Date();
            SimpleDateFormat sbf = new SimpleDateFormat("yyyy-MM-dd");
            Performance performance = new Performance();
            String date = String.valueOf(sbf.format(d));
            performance.setId(Integer.valueOf(String.valueOf(map.get("id"))));
            performance.setName((String) map.get("name"));
            performance.setKind((String) map.get("kind"));
            performance.setActors((String) map.get("actors"));
            performance.setDate(date);
            performance.setScore(0);
            Integer num = performanceService.insertPerformance(performance);
            log.info("新增节目成功");
            System.out.println(num);
            return new HttpReturn(HttpCodeEnum.OK,num);
        }catch (Exception e){
            log.error("新增节目失败了");
            e.printStackTrace();
            throw new ParamException(400,"新增节目失败");
        }
    }

    @PostMapping("/findShowsByNameOrId")
    @ApiOperation(value = "Get the Performances",notes = "id or name params")
    @ApiResponses({
            @ApiResponse(code = 200,message = "请求正确"),
            @ApiResponse(code = 400,message = "参数错误"),
            @ApiResponse(code = 404,message = "页面没找到"),
            @ApiResponse(code = 500,message = "服务错误")
    })
    @CrossOrigin(value = "*", maxAge = 1800, allowedHeaders ="*")
    public HttpReturn findShowsByNameOrId(@RequestBody(required=false) Map map){
        try {
            Integer id = null;
            String name = null;
            if (!"".equals(map.get("id"))){
                id = Integer.valueOf(String.valueOf(map.get("id")));
            }
            if (!"".equals(map.get("name"))) {
                name = String.valueOf(map.get("name"));
            }
            List<Performance> performances = performanceService.findShowsByNameOrId(id,name);
            log.info("查询指定节目成功");
            return new HttpReturn(HttpCodeEnum.OK,performances);
        }catch (Exception e){
            log.error("查询指定节目失败了");
            e.printStackTrace();
            throw new ParamException(400,"查询指定节目失败");
        }
    }

    @PostMapping("/updatePerformance")
    @ApiOperation(value = "Update the performance",notes = "need params name and score")
    @ApiResponses({
            @ApiResponse(code = 200,message = "请求正确"),
            @ApiResponse(code = 400,message = "参数错误"),
            @ApiResponse(code = 404,message = "页面没找到"),
            @ApiResponse(code = 500,message = "服务错误")
    })
    @CrossOrigin(value = "*", maxAge = 1800, allowedHeaders ="*")
    public HttpReturn updatePerformance(@RequestBody Map map){
        try {
            Integer score = null;
            String name = null;
            if (!"".equals(map.get("score"))){
                score = Integer.valueOf(String.valueOf(map.get("score")));
            }
            if (!"".equals(map.get("name"))) {
                name = String.valueOf(map.get("name"));
            }
            Integer num = performanceService.updatePerformance(name,score);
            log.info("节目评分成功");
            return new HttpReturn(HttpCodeEnum.OK,num);
        }catch (Exception e){
            log.error("节目评分失败了");
            e.printStackTrace();
            throw new ParamException(400,"节目评分失败");
        }
    }

}
