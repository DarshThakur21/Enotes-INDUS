package com.enotes.Enotes_INDUS.config;

import com.enotes.Enotes_INDUS.model.User;
import com.enotes.Enotes_INDUS.utils.CommonUtil;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditConfig implements AuditorAware<Integer> {

    @Override
    public Optional<Integer> getCurrentAuditor() {
        User userLogged= CommonUtil.getLoggedInUser();
        return Optional.of(userLogged.getId());
    }
}
