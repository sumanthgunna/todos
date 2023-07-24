/*
 * You can use the following import statements
 *
 */

// Write your code here
package com.example.todo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

import com.example.todo.repository.TodoRepository;
import com.example.todo.model.*;
 
// Write your code here
@Service
public class TodoH2Service implements TodoRepository {

    @Autowired
    private JdbcTemplate db;

    @Override
    public ArrayList<Todo> getTodos() {
        List<Todo> todoList = db.query("SELECT * FROM todolist", new TodoRowMapper());
        ArrayList<Todo> todos = new ArrayList<>(todoList);
        return todos;
    }

    @Override 
    public Todo getTodoById(int id) {
        try {
            Todo todo = db.queryForObject("SELECT * FROM todolist WHERE id = ?", new TodoRowMapper(), id);
            return todo;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override 
    public Todo addTodo(Todo todo) {
        db.update("INSERT INTO todolist(todo, priority, status) values(?, ?, ?)", todo.getTodo(), todo.getPriority(), todo.getStatus());
        Todo savedTodo = db.queryForObject("SELECT * FROM todolist WHERE todo = ? AND priority = ? AND status = ?", new TodoRowMapper(), todo.getTodo(), todo.getPriority(), todo.getStatus());
        return savedTodo;
    }

    @Override 
    public Todo updateTodo(int id, Todo todo) {
        try {
            if (todo.getTodo() != null) {
                db.update("UPDATE todolist SET todo = ? WHERE id = ?", todo.getTodo(), id);
            }
            if (todo.getPriority() != null) {
                db.update("UPDATE todolist SET priority = ? WHERE id = ?", todo.getPriority(), id);
            }
            if (todo.getStatus() != null) {
                db.update("UPDATE todolist SET status = ? WHERE id = ?", todo.getStatus(), id);
            }
            return getTodoById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override 
    public void deleteTodo(int id) {
        try {
            db.update("DELETE FROM todolist WHERE id = ?", id);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}