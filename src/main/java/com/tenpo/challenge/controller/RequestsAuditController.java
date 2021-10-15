package com.tenpo.challenge.controller;

import com.tenpo.challenge.model.RequestAudit;
import com.tenpo.challenge.resources.RequestsAuditResource;
import com.tenpo.challenge.services.RequestsAuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/search")
public class RequestsAuditController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestsAuditController.class);

    @Autowired
    private RequestsAuditService requestsAuditService;

    @GetMapping()
    public ResponseEntity<RequestsAuditResource> searchRequests(@Valid @RequestParam @Min(1)  Integer offset,
                                                       @RequestParam(required = false, defaultValue = "100") Integer limit) {
        List<RequestAudit> response = requestsAuditService.getAllRequests(offset, limit);
        return new ResponseEntity(new RequestsAuditResource(response), HttpStatus.OK);
    }

}
