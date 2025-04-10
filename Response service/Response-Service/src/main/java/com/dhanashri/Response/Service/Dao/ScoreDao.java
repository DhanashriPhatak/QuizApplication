package com.dhanashri.Response.Service.Dao;

import com.dhanashri.Response.Service.Module.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreDao extends JpaRepository<Score,Integer> {

}
