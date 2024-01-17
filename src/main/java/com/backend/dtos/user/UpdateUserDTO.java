package com.backend.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String dateOfBirth;
    @Nullable
    private String oldImage;
    private String oldPassword;
    @Nullable
    private String newPassword;

}
