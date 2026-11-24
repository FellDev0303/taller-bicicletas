package co.edu.uao.tallerbicicletas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntregaRequestDTO {
    @NotBlank(message = "El usuarioId es obligatorio")
    private String usuarioId;

    @NotNull(message = "La confirmación de recepción del cliente es obligatoria")
    private Boolean confirmacionRecepcionCliente;
}