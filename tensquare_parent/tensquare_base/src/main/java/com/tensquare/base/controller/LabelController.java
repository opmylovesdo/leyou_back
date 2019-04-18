package com.tensquare.base.controller;

import com.tensquare.base.pojo.Label;
import com.tensquare.base.service.LabelService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: LUOLUO
 * @Date: 2019/4/16
 * @Description: com.tensquare.base.controller
 * @version: 1.0
 */
@RestController
@CrossOrigin
@RequestMapping("/label")
public class LabelController {

    @Autowired
    private LabelService labelService;

    @GetMapping()
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", labelService.findAll());
    }

    @GetMapping("/{id}")
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "查询成功", labelService.findById(id));
    }

    @PostMapping
    public Result add(@RequestBody Label label) {
        labelService.add(label);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    @PutMapping("/{id}")
    public Result update(@RequestBody Label label, @PathVariable String id) {
        label.setId(id);
        labelService.update(label);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable String id) {
        labelService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    @PostMapping("/search")
    public Result findSearch(@RequestBody Label label) {

        return new Result(true, StatusCode.OK, "查询成功", labelService.findSearch(label));
    }

    @PostMapping("/search/{page}/{size}")
    public Result findSearch(@RequestBody Label label, @PathVariable int page, @PathVariable int size) {
        Page<Label> pageList = labelService.findSearch(label, page, size);
        return new Result(true, StatusCode.OK, "查询成功",
                new PageResult<>(pageList.getTotalElements(), pageList.getContent()));
    }


}
