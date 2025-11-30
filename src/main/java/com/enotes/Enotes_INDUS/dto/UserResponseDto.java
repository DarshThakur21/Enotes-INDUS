package com.enotes.Enotes_INDUS.dto;

import com.enotes.Enotes_INDUS.model.Role;
import lombok.*;

import java.util.List;

public class UserResponseDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNo;
    private List<Role> role;
    private StatusDto accountStatus;

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

    @Data
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusDto{
        private Integer id;
        private Boolean isActive;


    }
}
