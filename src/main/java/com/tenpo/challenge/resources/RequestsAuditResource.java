package com.tenpo.challenge.resources;

import com.tenpo.challenge.model.RequestAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestsAuditResource {
    List<RequestAudit> requests;
}
