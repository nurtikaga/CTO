package com.nurtikaga.CTO.repository;


import com.nurtikaga.CTO.model.Request;
import com.nurtikaga.CTO.model.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
     List<Request> findByClientPhone(String clientPhone);
     List<Request> findByStatus(RequestStatus status);
}


