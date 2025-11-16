package com.enotes.Enotes_INDUS.service;

import com.enotes.Enotes_INDUS.dto.TodoDto;

import java.util.List;

public interface TodoService {

    Boolean saveTodo(TodoDto todoDto);

    TodoDto getTodoById(Integer id) ;

    List<TodoDto> getTodoByUser();



}
