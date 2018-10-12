package com.xxx.notes.controller;

import com.xxx.notes.dto.SysResult;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipInputStream;

/**
 * @ClassName ActivitiController
 * @Description 审批流操作类
 * @Author l17561
 * @Date 2018/10/11 17:09
 * @Version V1.0
 */
@RestController
@RequestMapping("/activiti")
public class ActivitiController {

    /**
     * 日志记录.
     */
    private Logger log = LoggerFactory.getLogger(ActivitiController.class);

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private IdentityService identityService;

    /**
     * 上传流程图
     *   其实是添加一个审批的流程，这一步是管理员来做，流程图也需要改变
     * @return
     */
    @PostMapping("/deploy")
    public SysResult deploy(@RequestParam("file") MultipartFile file) {
        // 部署流程定义 -- 即将流程图导入到数据库中
        // 这段代码执行结束后会向 act_re_deployment 插入一条数据，返回的对象显然是和这个表对应的
        // 同时，流程定义表 act_re_procdef 会插入bpmn中填写的内容
        // 另外，act_ge_bytearray 中存储了bpmn和png两个内容
        try {
            String filename = file.getOriginalFilename();
            String suffix = filename.substring(filename.indexOf(".") + 1);
            if (!Objects.equals("zip", suffix)) {
                return SysResult.build(20000, "文件格式不正确，请上传zip包！");
            }
            Deployment deployment = repositoryService.createDeployment() // 创建部署构建器对象
                    .addZipInputStream(new ZipInputStream(file.getInputStream()))
                    //.addClasspathResource("processes/subscription.bpmn") // 读取流程定义文件
                    .deploy() // 部署流程定义
                    ;
            log.info("部署了流程{},时间是{}", deployment.getName(), deployment.getDeploymentTime());
            return SysResult.ok("流程部署成功");
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.build(30000, "部署流程失败！");
        }
    }


    @GetMapping("/process/definition")
    public SysResult queryAllProcessDefinition(int page, int row) {
        //获取流程定义
        List<ProcessDefinition> processDefinition = repositoryService.createProcessDefinitionQuery() // 查询流程定义 act_re_procdef
                .deploymentId("") // WHERE DEPLOYMENT_ID_ = ?
                .listPage(page, row);

        return SysResult.ok();
    }

    @GetMapping("/PlatApiBookTQry")
    public void firstDemo() {

        Deployment deployment = repositoryService.createDeployment() // 创建部署构建器对象
                //.addZipInputStream(new ZipInputStream(this.getClass().getClassLoader().getResourceAsStream("zip.zip")))
                .addClasspathResource("processes/subscription.bpmn") // 读取流程定义文件
                .deploy() // 部署流程定义
                ;

        deployment.getId();

        //获取流程定义 -- select * from act_re_procdef where deployment_id_ = ? limit 1
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery() // 查询流程定义 act_re_procdef
                .deploymentId(deployment.getId()) // WHERE DEPLOYMENT_ID_ = ?
                .singleResult();

        //启动流程定义，返回流程实例
        // 其实是在 act_ru_execution（流程实例表） 中插入一条记录
        // act_ru_task （任务表）
        ProcessInstance pi = runtimeService.startProcessInstanceById(processDefinition.getId());


        // GroupEntity
        String processId = pi.getId();
        System.out.println("流程创建成功，当前流程实例ID：" + processId);

        // 查询个人任务
        Task task = taskService.createTaskQuery() // 任务查询对象
                //.taskAssignee("") // 根据办理人过滤
                .processInstanceId(processId) // 根据流程定义来查询
                .orderByTaskCreateTime().desc() // 根据创建时间倒序
                //.list() // 插询所有
                //.listPage(0, 10) // 分页查询
                .singleResult(); // 少用这个
        System.out.println("第一次执行前，任务名称：" + task.getName());

        // 执行任务
        // act_ru_execution与act_ru_task 发生变化
        taskService.complete(task.getId());
    }

}
