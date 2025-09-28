package com.enotes.Enotes_INDUS.config;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditConfig implements AuditorAware<Integer> {


    @Override
    public Optional<Integer> getCurrentAuditor() {
//        currently hardcoded but lateron will edit it with using user
        return Optional.of(1);
    }
}
