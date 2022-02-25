package personal.progresscompaninon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class PasswordChangeDto {

    @NotBlank
    private String oldPassword;
    @Size(min = 8, message = "Enter a stronger password")
    private String newPassword;
}
