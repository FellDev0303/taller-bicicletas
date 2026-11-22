package co.edu.uao.tallerbicicletas.exception;

// Se lanza cuando la operacion choca con el estado actual o con una regla de negocio (responde 409)
public class ConflictoException extends RuntimeException {
    public ConflictoException(String mensaje) {
        super(mensaje);
    }
}