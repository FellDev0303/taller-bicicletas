package co.edu.uao.tallerbicicletas.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CitaRequestDTO {
    @NotBlank(message = "El cliente es obligatorio")
    private String clienteId;

    @NotBlank(message = "La bicicleta es obligatoria")
    private String bicicletaId;

    @NotNull(message = "La fecha y hora de la cita son obligatorias")
    @Future(message = "La fecha y hora de la cita deben ser futuras")
    private LocalDateTime fechaHora;

    @NotBlank(message = "El motivo de la visita es obligatorio")
    private String motivo;

    // Opcional: problemas que el cliente observa en la bicicleta
    private String problemasObservados;

    // Opcional: ids de los servicios del catalogo que le interesan
    private List<String> serviciosInteres;
}