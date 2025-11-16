package com.enotes.Enotes_INDUS.controller;


import com.enotes.Enotes_INDUS.dto.TodoDto;
import com.enotes.Enotes_INDUS.service.TodoService;
import com.enotes.Enotes_INDUS.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RequestMapping("/api/v1/todo")
@RestController
public class TodoController
{

    @Autowired
    private TodoService todoService;

            @GetMapping("/all")
            public ResponseEntity<?> getAllTodos(){
                try {

                int userId=1;
                List<TodoDto> todoDtoList =todoService.getTodoByUser();
                    if (CollectionUtils.isEmpty(todoDtoList)){
                        return ResponseEntity.noContent().build();
                    }
                return CommonUtil.createBuildResponse(todoDtoList, HttpStatus.OK);

                } catch (Exception e) {
                    e.printStackTrace();
                    return CommonUtil.createErrorResponseMessage("list not found",HttpStatus.NOT_FOUND);
                }

            }

            @PostMapping("/save-todo")
            public ResponseEntity<?> saveTodos(@RequestBody  TodoDto todoDto){
                try {

                Boolean saved=todoService.saveTodo(todoDto);
                if (!saved){
                    return CommonUtil.createErrorResponseMessage("cannnot save ",HttpStatus.NOT_ACCEPTABLE);

                }

                return CommonUtil.createBuildResponseMessage("saved success", HttpStatus.CREATED);

                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);

                }
            }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTodoById(@PathVariable Integer id){
        try {
                TodoDto todoDto=todoService.getTodoById(id);
                if(!ObjectUtils.isEmpty(todoDto)){
                    return CommonUtil.createBuildResponse(todoDto, HttpStatus.OK);

                }
                return CommonUtil.createErrorResponseMessage("Not found todo", HttpStatus.NOT_FOUND);




        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);


        }

    }
}
