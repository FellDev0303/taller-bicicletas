package co.edu.uao.tallerbicicletas.exception;

// Se lanza cuando los datos enviados no son aceptables (responde 400)
public class SolicitudInvalidaException extends RuntimeException {
    public SolicitudInvalidaException(String mensaje) {
        super(mensaje);
    }
}