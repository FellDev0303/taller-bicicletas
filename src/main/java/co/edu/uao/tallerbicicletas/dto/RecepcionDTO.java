package co.edu.uao.tallerbicicletas.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecepcionDTO {
    @NotBlank(message = "El problema reportado es obligatorio")
    private String problemaReportado;

    @NotBlank(message = "Las condiciones observadas son obligatorias")
    private String condicionesObservadas;

    // Opcional: si no viene, se guarda una lista vacia
    private List<String> accesorios;

    // Opcional: si viene, se valida cada fotografia
    @Valid
    private List<AdjuntoDTO> fotografias;
}