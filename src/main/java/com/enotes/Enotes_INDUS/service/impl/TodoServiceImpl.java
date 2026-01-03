package com.enotes.Enotes_INDUS.service.impl;


import com.enotes.Enotes_INDUS.dto.TodoDto;
import com.enotes.Enotes_INDUS.exceptions.ResourceNotFound;
import com.enotes.Enotes_INDUS.model.Todo;
import com.enotes.Enotes_INDUS.model.enums.Status;
import com.enotes.Enotes_INDUS.repository.TodoRepo;
import com.enotes.Enotes_INDUS.service.TodoService;
import com.enotes.Enotes_INDUS.utils.CommonUtil;
import com.enotes.Enotes_INDUS.utils.Validations;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TodoServiceImpl implements TodoService {


    @Autowired
    private TodoRepo todoRepo;

    @Autowired
    private ModelMapper modelMapper;


    @Autowired
    private Validations validations;




    @Override
    public Boolean saveTodo(TodoDto todoDto) throws ResourceNotFound {
//        ObjectMapper objectMapper =new ObjectMapper();
        log.info("TodoServiceImpl : saveTodo() : Start");
        validations.todoValidation(todoDto);
        Todo todo =modelMapper.map(todoDto, Todo.class);

        if(!ObjectUtils.isEmpty(todoDto.getId())){
            log.info("Updating the todo");
            Todo updateTodo=updateTodos(todoDto);
            todoRepo.save(updateTodo);
            log.info("Upadted Todo");
            return true;
        }

         Todo saveTools=  todoRepo.save(todo);
         if(ObjectUtils.isEmpty(saveTools)){

        log.info("Todo didnot saved");
        return false;
         }
        log.info("Todo saved");
        log.info("TodoServiceImpl : saveTodo() : End");
        return true;


    }

    private Todo updateTodos(TodoDto todoDto) throws ResourceNotFound {
        Todo existingTodo=todoRepo.findById(todoDto.getId()).orElseThrow(()->new ResourceNotFound("dont have any id with this id"));

        existingTodo.setTitle(todoDto.getTitle());
        existingTodo.setDescription(todoDto.getDescription());
        existingTodo.setStatus(todoDto.getStatus());
        existingTodo.setUpdatedBy(todoDto.getUpdatedBy());
        existingTodo.setUpdatedOn(new Date());
        return  existingTodo;
    }


    @Override
    public TodoDto getTodoById(Integer todoId)  {
        try{
            log.info("TodoServiceImpl : getTodoById() : Start");
        Todo todo=todoRepo.findById(todoId).orElseThrow(()-> {
            log.error("Cannot fetch todo with this id:{}",todoId);
            return new ResourceNotFound("dont have any id with this id");
        });
            TodoDto todoDto=modelMapper.map(todo,TodoDto.class);
            log.info("Success");
            log.info("TodoServiceImpl : getTodoById() : End");
            return  todoDto;

        } catch (Exception e) {
            log.error("Error getting todo");
            e.printStackTrace();
        }
        return null;
    }



    @Override
    public List<TodoDto> getTodoByUser() {
        try {
            log.info("TodoServiceImpl : getTodoByUser() : Start");
            int userId= CommonUtil.getLoggedInUser().getId();

        List<Todo> todoList=todoRepo.findByCreatedBy(userId);
        List<TodoDto> todoDtoList=todoList.stream().map(todo -> modelMapper.map(todo, TodoDto.class)).toList();

        log.info("Success");
        log.info("TodoServiceImpl : getTodoByUser() : End");
        return  todoDtoList;
        }
        catch (Exception e) {
        log.error("Cannot fetch todo by user");
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<TodoDto> getByStatus(String status) {
        log.info("TodoServiceImpl : getByStatus() : Start");
        Status statusValue=Status.valueOf(status.toUpperCase());
        int userId= CommonUtil.getLoggedInUser().getId();
        List<Todo> todo=todoRepo.findByStatusAndUserId(statusValue,userId);
        List<TodoDto> todoDtoList=todo.stream().map(t -> modelMapper.map(t, TodoDto.class)).toList();
        log.info("TodoServiceImpl : getByStatus() : End");
        return todoDtoList;

    }

    @Override
    public void deleteTodo(Integer todoId) {
        Todo todo=todoRepo.findById(todoId).orElseThrow(()->new RuntimeException("Todo not found"));
        if(ObjectUtils.isEmpty(todo)){
           return;
        }
        todoRepo.delete(todo);
    }

    @Override
    public Boolean changeStatus(Integer id, Status status) {
        Todo todo=todoRepo.findById(id).orElseThrow(()->new RuntimeException("Todo not found"));
        if(ObjectUtils.isEmpty(todo)){
            return false;
        }
        todo.setStatus(status);
        todoRepo.save(todo);
        return true;
    }
}
