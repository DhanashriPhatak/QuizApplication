package com.dhanashri.Response.Service.Dao;

import com.dhanashri.Response.Service.Module.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponseDao extends JpaRepository<Response,Integer> {
}
