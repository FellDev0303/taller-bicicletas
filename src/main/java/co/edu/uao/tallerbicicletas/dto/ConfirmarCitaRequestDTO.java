package co.edu.uao.tallerbicicletas.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmarCitaRequestDTO {
    @NotBlank(message = "El coordinador es obligatorio")
    private String coordinadorId;
}