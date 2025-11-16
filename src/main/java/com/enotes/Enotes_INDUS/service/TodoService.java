package com.enotes.Enotes_INDUS.service;

import com.enotes.Enotes_INDUS.dto.TodoDto;
import com.enotes.Enotes_INDUS.exceptions.ResourceNotFound;

import java.util.List;

public interface TodoService {

    Boolean saveTodo(TodoDto todoDto) throws ResourceNotFound;

    TodoDto getTodoById(Integer id) ;

    List<TodoDto> getTodoByUser();


    List<TodoDto> getByStatus(String status);
}
