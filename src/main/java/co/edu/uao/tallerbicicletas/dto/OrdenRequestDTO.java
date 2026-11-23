package co.edu.uao.tallerbicicletas.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdenRequestDTO {
    @NotBlank(message = "El cliente es obligatorio")
    private String clienteId;

    @NotBlank(message = "La bicicleta es obligatoria")
    private String bicicletaId;

    // Opcional: solo si la orden nace de una cita (tambien se reciben bicicletas sin cita)
    private String citaId;

    @NotBlank(message = "El coordinador responsable es obligatorio")
    private String coordinadorId;

    @NotEmpty(message = "Debe asignarse al menos un técnico")
    private List<String> tecnicosAsignados;

    @NotNull(message = "La fecha estimada de entrega es obligatoria")
    @Future(message = "La fecha estimada de entrega debe ser una fecha futura")
    private LocalDateTime fechaEstimadaEntrega;

    @NotNull(message = "Los datos de la recepción son obligatorios")
    @Valid
    private RecepcionDTO recepcion;
}
