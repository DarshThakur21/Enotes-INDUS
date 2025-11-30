package com.enotes.Enotes_INDUS.dto;

import com.enotes.Enotes_INDUS.model.Role;
import lombok.*;

import java.util.List;

public class UserRequestDto {

    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String mobileNo;
    private List<Role> role;

    @Data
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoleDto{
        private Integer id;

        private String role;
    }

}
