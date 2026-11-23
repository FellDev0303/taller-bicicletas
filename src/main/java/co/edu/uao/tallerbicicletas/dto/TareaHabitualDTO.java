package co.edu.uao.tallerbicicletas.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TareaHabitualDTO {
    @NotBlank(message = "El nombre de la tarea es obligatorio")
    private String nombre;

    @NotBlank(message = "La descripción de la tarea es obligatoria")
    private String descripcion;
}