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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
        validations.todoValidation(todoDto);
        Todo todo =modelMapper.map(todoDto, Todo.class);

        if(!ObjectUtils.isEmpty(todoDto.getId())){
            Todo updateTodo=updateTodos(todoDto);
            todoRepo.save(updateTodo);
            return true;
        }

         Todo saveTools=  todoRepo.save(todo);
         if(!ObjectUtils.isEmpty(saveTools)){

     return true;
         }
     return false;


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
        Todo todo=todoRepo.findById(todoId).orElseThrow(()->new ResourceNotFound("dont have any id with this id"));
        
            TodoDto todoDto=modelMapper.map(todo,TodoDto.class);


            return  todoDto;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }



    @Override
    public List<TodoDto> getTodoByUser() {
        try {
            int userId= CommonUtil.getLoggedInUser().getId();

        List<Todo> todoList=todoRepo.findByCreatedBy(userId);
        List<TodoDto> todoDtoList=todoList.stream().map(todo -> modelMapper.map(todo, TodoDto.class)).toList();
        return  todoDtoList;
        }
        catch (Exception e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<TodoDto> getByStatus(String status) {
        Status statusValue=Status.valueOf(status.toUpperCase());
        List<Todo> todo=todoRepo.findByStatus(statusValue);
        List<TodoDto> todoDtoList=todo.stream().map(t -> modelMapper.map(t, TodoDto.class)).toList();

        return todoDtoList;

    }
}
