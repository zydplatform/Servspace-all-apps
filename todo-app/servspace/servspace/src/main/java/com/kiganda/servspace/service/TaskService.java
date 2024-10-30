package com.kiganda.servspace.service;

import com.kiganda.servspace.model.Task;
import com.kiganda.servspace.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    public List <Task> getAllTasks(){
        return taskRepository.findAll();
    }

    public Optional <Task> getTaskById(Long id){
        return taskRepository.findById(id);
    }

    public Task createTask(Task task){
        task.setCreatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task taskDetails){
        Task task = taskRepository.findById(id).orElseThrow();
        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setCompleted(taskDetails.isCompleted());


        return taskRepository.save(task);

    }

    public void deleteTask(Long id){
        taskRepository.deleteById(id);
    }


}
