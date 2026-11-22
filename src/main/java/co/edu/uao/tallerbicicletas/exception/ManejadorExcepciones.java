package co.edu.uao.tallerbicicletas.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import co.edu.uao.tallerbicicletas.dto.ErrorRespuestaDTO;

@RestControllerAdvice
public class ManejadorExcepciones {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorRespuestaDTO> manejarNoEncontrado(RecursoNoEncontradoException ex) {
        return construirRespuesta(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    @ExceptionHandler(ConflictoException.class)
    public ResponseEntity<ErrorRespuestaDTO> manejarConflicto(ConflictoException ex) {
        return construirRespuesta(HttpStatus.CONFLICT, ex.getMessage(), null);
    }

    @ExceptionHandler(SolicitudInvalidaException.class)
    public ResponseEntity<ErrorRespuestaDTO> manejarSolicitudInvalida(SolicitudInvalidaException ex) {
        return construirRespuesta(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }

    // Errores de @Valid sobre los DTO de entrada
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRespuestaDTO> manejarValidacion(MethodArgumentNotValidException ex) {
        List<String> detalles = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        return construirRespuesta(HttpStatus.BAD_REQUEST, "Los datos enviados no son válidos", detalles);
    }

    // Violacion de un indice unico en MongoDB (debe ir separado porque es mas especifica)
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ErrorRespuestaDTO> manejarDuplicado(DuplicateKeyException ex) {
        return construirRespuesta(HttpStatus.CONFLICT,
                "Ya existe un registro con esos datos únicos",
                List.of(ex.getMostSpecificCause().getMessage()));
    }

    // Documento rechazado por la validacion ($jsonSchema) de MongoDB
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorRespuestaDTO> manejarIntegridad(DataIntegrityViolationException ex) {
        return construirRespuesta(HttpStatus.BAD_REQUEST,
                "El documento no cumple el esquema de la base de datos",
                List.of(ex.getMostSpecificCause().getMessage()));
    }

    private ResponseEntity<ErrorRespuestaDTO> construirRespuesta(HttpStatus status, String mensaje, List<String> detalles) {
        ErrorRespuestaDTO cuerpo = new ErrorRespuestaDTO(
                LocalDateTime.now().toString(),
                status.value(),
                status.getReasonPhrase(),
                mensaje,
                detalles);
        return new ResponseEntity<ErrorRespuestaDTO>(cuerpo, status);
    }
}
