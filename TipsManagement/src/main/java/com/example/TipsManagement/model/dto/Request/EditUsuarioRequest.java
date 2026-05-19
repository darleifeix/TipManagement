package com.example.TipsManagement.model.dto.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EditUsuarioRequest {
    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;
}
