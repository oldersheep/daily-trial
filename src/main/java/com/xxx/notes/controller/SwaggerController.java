package com.xxx.notes.controller;

import com.xxx.notes.entity.MemberEntity;
import com.xxx.notes.dto.SysResult;
import com.xxx.notes.service.SwaggerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/swagger")
public class SwaggerController {

    @Autowired
    private SwaggerService swaggerService;

    @ApiOperation(value = "查询所有的成员", notes = "GET查询所有")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    public SysResult list() {

        return SysResult.ok(swaggerService.listAll());
    }

    @ApiOperation(value = "查询指定成员", notes = "查询指定成员")
    @RequestMapping(value = "/findOne", method = RequestMethod.POST, produces = {"application/json"})
    public SysResult findOne(@RequestParam String mid) {
        MemberEntity memberEntity = swaggerService.queryById(mid);
        if (memberEntity == null) {
            return SysResult.build(201,"无此成员！");
        }
        return SysResult.ok(memberEntity);
    }

    @ApiOperation(value = "移除指定成员", notes = "移除某个成员")
    @RequestMapping(value = "/remove", method = RequestMethod.DELETE)
    public SysResult removeById(@RequestParam String mid) {
        Integer ret = swaggerService.deleteMemberById(mid);
        if (ret != null && ret == 1) {
            return SysResult.ok();
        } else {
            return SysResult.build(404,"成员找不到！");
        }
    }
    @ApiOperation(value = "新增成员", notes = "新增成员")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public SysResult save(@RequestBody MemberEntity memberEntity) {
        swaggerService.saveMember(memberEntity);
        return SysResult.ok();
    }
    private String unicodeToCn(String unicode) {
        /** 以 \ u 分割，因为java注释也能识别unicode，因此中间加了一个空格*/
        String[] strs = unicode.split("u");
        String returnStr = "";
        for (int i = 1; i < strs.length; i++) {
            returnStr += (char) Integer.valueOf(strs[i], 16).intValue();
        }
        return returnStr;
    }
}
