package com.tenpo.challenge.repository;

import com.tenpo.challenge.model.RequestAudit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestsAuditRepository extends JpaRepository<RequestAudit, Long>  {
    RequestAudit save(RequestAudit requestAudit);
}
