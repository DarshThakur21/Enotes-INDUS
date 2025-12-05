package com.enotes.Enotes_INDUS.controller;


import com.enotes.Enotes_INDUS.dto.TodoDto;
import com.enotes.Enotes_INDUS.service.TodoService;
import com.enotes.Enotes_INDUS.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
            @PreAuthorize("hasRole('USER')")
            public ResponseEntity<?> getAllTodos(){
                try {
                log.info("TodoController : getAllTodos() : Start");
                int userId=CommonUtil.getLoggedInUser().getId();
                List<TodoDto> todoDtoList =todoService.getTodoByUser();
                    if (CollectionUtils.isEmpty(todoDtoList)){
                        return ResponseEntity.noContent().build();
                    }
                    log.info("List Found");
                    log.info("TodoController : getAllTodos() : End");
                return CommonUtil.createBuildResponse(todoDtoList, HttpStatus.OK);

                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("List Not found");
                    return CommonUtil.createErrorResponseMessage("list not found",HttpStatus.NOT_FOUND);
                }

            }

            @PostMapping("/save-todo")
            @PreAuthorize("hasRole('USER')")
            public ResponseEntity<?> saveTodos(@RequestBody  TodoDto todoDto){
                try {
                    log.info("TodoController : saveTodos() : Start");
                Boolean saved=todoService.saveTodo(todoDto);
                if (!saved){
                log.info("Cannot save the todo");
                    return CommonUtil.createErrorResponseMessage("cannnot save ",HttpStatus.NOT_ACCEPTABLE);

                }
                log.info("Save success");
                log.info("TodoController : saveTodos() : End");
                return CommonUtil.createBuildResponseMessage("saved success", HttpStatus.CREATED);

                } catch (Exception e) {

                    e.printStackTrace();
                    log.error("Error saving todo");
                    throw new RuntimeException(e);

                }
            }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getTodoById(@PathVariable Integer id){
        try {
            log.info("TodoController : getTodoById() : Start");
                TodoDto todoDto=todoService.getTodoById(id);
                if(ObjectUtils.isEmpty(todoDto)){
                log.info("Todo list not found");
                    return CommonUtil.createErrorResponseMessage("Not found todo", HttpStatus.NOT_FOUND);
                }
                log.info("Todo list found");
                log.info("TodoController : getTodoById() : End");

                    return CommonUtil.createBuildResponse(todoDto, HttpStatus.OK);



        } catch (Exception e) {
            log.error("Todo list not found");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/by-status/{status}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getTodoByStatus(@PathVariable String status){
        try {
            log.info("TodoController : getTodoByStatus() : Start");

            List<TodoDto> todoDto=todoService.getByStatus(status);
            if(ObjectUtils.isEmpty(todoDto)){
            log.info("Not found todo with the status");
                return CommonUtil.createErrorResponseMessage("Not found todo with this status", HttpStatus.NOT_FOUND);

            }
            log.info("Todo with the status:{}",status);
            log.info("TodoController : getTodoByStatus() : End");
            return CommonUtil.createBuildResponse(todoDto, HttpStatus.OK);

        } catch (Exception e) {

            log.error("Todo list not found");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
