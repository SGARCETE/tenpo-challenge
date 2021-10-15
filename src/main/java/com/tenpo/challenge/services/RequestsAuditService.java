package com.tenpo.challenge.services;

import com.tenpo.challenge.model.RequestAudit;

import java.util.List;

public interface RequestsAuditService {
    List<RequestAudit> getAllRequests(Integer offset, Integer limit);
}
