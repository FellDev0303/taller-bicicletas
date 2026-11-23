package co.edu.uao.tallerbicicletas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CambioEstadoRequestDTO {
    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "recibida|en_diagnostico|pendiente_autorizacion|en_ejecucion|lista_entrega|entregada|cancelada",
             message = "El estado debe ser recibida, en_diagnostico, pendiente_autorizacion, en_ejecucion, lista_entrega, entregada o cancelada")
    private String estado;

    @NotBlank(message = "El usuario responsable es obligatorio")
    private String usuarioId;

    @NotBlank(message = "La observación es obligatoria")
    private String observacion;
}
