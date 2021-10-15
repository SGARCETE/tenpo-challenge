package com.tenpo.challenge.services.impl;

import com.tenpo.challenge.model.RequestAudit;
import com.tenpo.challenge.repository.RequestsAuditRepository;
import com.tenpo.challenge.services.RequestsAuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Positive;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultRequestsAuditService implements RequestsAuditService {
    @Autowired
    private final RequestsAuditRepository requestsAuditRepository;

    public List<RequestAudit> getAllRequests(@Positive(message = "Please provide a valid offset attribute in JSON request") Integer offset,
                                                   @Positive(message = "Please provide a valid limit attribute in JSON request") Integer limit) {
        Pageable size = PageRequest.of(0,limit);
        List<RequestAudit> requests = requestsAuditRepository.findAll(size).toList();
        return getListWithOffset(requests, offset);
    }

    private List<RequestAudit> getListWithOffset(List<RequestAudit> requests, Integer offset) {
        if(offset <= 0 || offset > requests.size() || requests.isEmpty()) {
            return requests;
        } else {
            return requests.subList(offset - 1, requests.size());
        }
    }
}
