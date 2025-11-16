package com.enotes.Enotes_INDUS.service.impl;


import com.enotes.Enotes_INDUS.dto.TodoDto;
import com.enotes.Enotes_INDUS.exceptions.ResourceNotFound;
import com.enotes.Enotes_INDUS.model.Todo;
import com.enotes.Enotes_INDUS.repository.TodoRepo;
import com.enotes.Enotes_INDUS.service.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TodoServiceImpl implements TodoService {


    @Autowired
    private TodoRepo todoRepo;

    @Autowired
    private ModelMapper modelMapper;





    @Override
    public Boolean saveTodo(TodoDto todoDto) {
//        ObjectMapper objectMapper =new ObjectMapper();
        Todo todo =modelMapper.map(todoDto, Todo.class);
         Todo saveTools=   todoRepo.save(todo);
         if(!ObjectUtils.isEmpty(saveTools)){

     return true;
         }
     return false;





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
            int userId=1;

        List<Todo> todoList=todoRepo.findByCreatedBy(userId);
        List<TodoDto> todoDtoList=todoList.stream().map(todo -> modelMapper.map(todo, TodoDto.class)).toList();
        return  todoDtoList;
        }
        catch (Exception e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }



    }
}
