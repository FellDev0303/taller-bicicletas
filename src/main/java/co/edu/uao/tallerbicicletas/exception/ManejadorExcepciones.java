package co.edu.uao.tallerbicicletas.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.UncategorizedMongoDbException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
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

    // JSON mal formado, tipos incompatibles o fechas LocalDateTime inválidas
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorRespuestaDTO> manejarJsonInvalido(HttpMessageNotReadableException ex) {
        return construirRespuesta(HttpStatus.BAD_REQUEST,
                "El formato del JSON enviado es inválido o contiene tipos de datos incorrectos",
                List.of(ex.getMostSpecificCause().getMessage()));
    }

    // Parámetros obligatorios faltantes en la URL (@RequestParam)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorRespuestaDTO> manejarParametroFaltante(MissingServletRequestParameterException ex) {
        return construirRespuesta(HttpStatus.BAD_REQUEST,
                "Falta el parámetro obligatorio '" + ex.getParameterName() + "'",
                null);
    }

    // Violación de un índice único en MongoDB (código 11000)
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ErrorRespuestaDTO> manejarDuplicado(DuplicateKeyException ex) {
        return construirRespuesta(HttpStatus.CONFLICT,
                "Ya existe un registro con esos datos únicos",
                List.of(ex.getMostSpecificCause().getMessage()));
    }

    // Documento rechazado por la validación ($jsonSchema) de MongoDB en inserciones
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorRespuestaDTO> manejarIntegridad(DataIntegrityViolationException ex) {
        return construirRespuesta(HttpStatus.BAD_REQUEST,
                "El documento no cumple el esquema de la base de datos",
                List.of(ex.getMostSpecificCause().getMessage()));
    }

    // Documento rechazado por MongoDB en comandos/actualizaciones directas (ej. código 121)
    @ExceptionHandler(UncategorizedMongoDbException.class)
    public ResponseEntity<ErrorRespuestaDTO> manejarErrorMongo(UncategorizedMongoDbException ex) {
        String detalle = ex.getMostSpecificCause().getMessage();
        if (detalle != null && detalle.contains("Document failed validation")) {
            return construirRespuesta(HttpStatus.BAD_REQUEST,
                    "El documento no cumple el esquema de la base de datos",
                    List.of(detalle));
        }
        return construirRespuesta(HttpStatus.INTERNAL_SERVER_ERROR,
                "Error al ejecutar la operación en la base de datos",
                detalle != null ? List.of(detalle) : null);
    }

    // Respaldo general para excepciones no controladas
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRespuestaDTO> manejarErrorGeneral(Exception ex) {
        return construirRespuesta(HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrió un error interno inesperado en el servidor",
                List.of(ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName()));
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
