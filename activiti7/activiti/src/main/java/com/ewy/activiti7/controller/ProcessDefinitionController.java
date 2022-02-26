package com.ewy.activiti7.controller;

import com.ewy.activiti7.util.AjaxResponse;
import com.ewy.activiti7.util.GlobalConfig;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipInputStream;

@RestController
@RequestMapping("/processDefinition")
public class ProcessDefinitionController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    RepositoryService repositoryService;

    //    添加流程定义通过上传bpmn
    @PostMapping("/uploadStreamAndDeployment")
    public AjaxResponse uploadStreamAndDeployment(@RequestParam("processFile") MultipartFile multipartFile
                                                 ) {
        try {
            // 获取上传文件名
            String fileName = multipartFile.getOriginalFilename();
            // 获取文件扩展名
            String extension = FilenameUtils.getExtension(fileName);
            // 获取文件字节流对象
            InputStream fileInputStream = multipartFile.getInputStream();
            Deployment deployment = null;
            if ("zip".equals(extension)) {
                ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
                deployment = repositoryService.createDeployment()// 初始化部署
                        .addZipInputStream(zipInputStream)
//                        .name(deploymentName)
                        .deploy();
            } else {
                deployment = repositoryService.createDeployment()
                        .addInputStream(fileName, fileInputStream)
//                        .name(deploymentName)
                        .deploy();
            }
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(), deployment.getId() + ";" + fileName);
        } catch (Exception e) {
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    "部署流程失败", e.toString());
        }
    }

    //    添加流程定义通过在线提交bpmn的xml
    @PostMapping("/addDeploymentByString")
    public AjaxResponse addDeploymentByString(@RequestParam("stringBPMN") String stringBPMN) {
        try {
            Deployment deployment = repositoryService.createDeployment()
                    .addString("CreateWithBPMNJS.bpmn", stringBPMN)
//                    .name(deploymentName)
                    .deploy();
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(), deployment.getId());
        } catch (Exception e) {
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    "string部署流程失败", e.toString());
        }
    }

    //    获取流程定义列表
    @GetMapping("/getDefinitions")
    public AjaxResponse getDefinitions() {
        try {
            List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
            List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                    .list();
            int seq = 0;
            for (ProcessDefinition processDefinition : processDefinitions) {
                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                seq++;
                hashMap.put("seq",seq);
                hashMap.put("name", processDefinition.getName());
                hashMap.put("key", processDefinition.getKey());
                hashMap.put("resourceName", processDefinition.getResourceName());
                hashMap.put("processDefinitionID",processDefinition.getId());
                hashMap.put("deploymentId", processDefinition.getDeploymentId());
                hashMap.put("version", processDefinition.getVersion());
                list.add(hashMap);
            }
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(), list);
        } catch (Exception e) {
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    GlobalConfig.ResponseCode.ERROR.getDesc(), e.toString());
        }
    }

    //    获取流程定义xml
    @GetMapping("/getDefinitionXML")
    public void getDefinitionXML(HttpServletResponse response, @RequestParam("deploymentId") String deploymentId,
                                 @RequestParam("resourceName") String resourceName) {
        try {
            InputStream inputStream = repositoryService.getResourceAsStream(deploymentId, resourceName);
            int count = inputStream.available();
            byte[] bytes = new byte[count];
            response.setContentType("text/xml");
            OutputStream outputStream = response.getOutputStream();
            if (inputStream.read(bytes) != -1) {
                outputStream.write(bytes);
            }
            inputStream.close();
            outputStream.close();

        } catch (Exception e) {
            logger.error("获取流程定义xml失败，原因是：" + e.toString());
        }
    }

    //    获取流程部署列表
    @GetMapping("/getDeployments")
    public AjaxResponse getDeployments() {
        try {
            List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
            List<Deployment> deployments = repositoryService.createDeploymentQuery()
                    .list();
            for (Deployment deployment : deployments) {
                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                hashMap.put("name", deployment.getName());
                hashMap.put("id",deployment.getId());
                hashMap.put("deploymentTime",deployment.getDeploymentTime());
                list.add(hashMap);
            }
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(), list);
        } catch (Exception e) {
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                   "获取流程部署列表失败", e.toString());
        }
    }


    //    删除流程定义
    @GetMapping("/delDefinition")
    public AjaxResponse delDefinition(@RequestParam("pdID") String pdID) {
        try {
            repositoryService.deleteDeployment(pdID,true);
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(), null);
        } catch (Exception e) {
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    "删除流程定义失败", e.toString());
        }
    }


    //  上传文件
    @PostMapping("/upload")
    public AjaxResponse upload(HttpServletRequest request, @RequestParam("processFile") MultipartFile multipartFile) {
        try {
            if (multipartFile.isEmpty()){
                throw new Exception("文件为空");
            }
            String fileName = multipartFile.getOriginalFilename();// 文件名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));// 扩展名
            String filePath = GlobalConfig.BPMN_PathMapping;
            // 修改路径格式
//            filePath = filePath.replace("\\","/");
            filePath = filePath.replace("file:","");
            fileName = UUID.randomUUID() + suffixName;// 新的文件名
            File file = new File(filePath + fileName);
            if (!file.getParentFile().exists()){
                file.getParentFile().mkdir();
            }
            multipartFile.transferTo(file);
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(), fileName);
        } catch (Exception e) {
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    "上传文件失败", e.toString());
        }
    }
}
