package com.dhanashri.Quiz_Service.Dao;

import com.dhanashri.Quiz_Service.Module.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizDao extends JpaRepository<Quiz,Integer> {
}
