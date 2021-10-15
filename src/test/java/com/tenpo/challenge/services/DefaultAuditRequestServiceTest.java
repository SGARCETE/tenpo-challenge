package com.tenpo.challenge.services;

import com.tenpo.challenge.model.RequestAudit;
import com.tenpo.challenge.model.User;
import com.tenpo.challenge.repository.RequestsAuditRepository;
import com.tenpo.challenge.services.impl.DefaultRequestsAuditService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DefaultAuditRequestServiceTest {
    private RequestsAuditService requestsAuditService;
    private RequestsAuditRepository requestsAuditRepository;
    private RequestAudit requestAudit;

    @Before
    public void setUp() {
        requestsAuditRepository = Mockito.mock(RequestsAuditRepository.class);
        requestsAuditService = new DefaultRequestsAuditService(requestsAuditRepository);
        requestAudit = buildNewRequestAudit();
    }

    @Test
    public void testSearchRequests() {
        List<RequestAudit> requestAudits = Arrays.asList(requestAudit);
        Page<RequestAudit> pagedResponse = new PageImpl(requestAudits);
        Mockito.when(requestsAuditRepository.findAll(Mockito.any(Pageable.class))).thenReturn(pagedResponse);

        List<RequestAudit> response = requestsAuditService.getAllRequests(0, 100);

        assertEquals(1, response.size());
        assertEquals(requestAudit.getId(), response.get(0).getId());
    }

    //todo: Test that evaluates errores and limits

    private RequestAudit buildNewRequestAudit() {
        RequestAudit requestAudit = new RequestAudit();
        requestAudit.setId(1L);
        requestAudit.setUser(new User().setId(1L));
        requestAudit.setUrl("/bla");
        return requestAudit;
    }

}
