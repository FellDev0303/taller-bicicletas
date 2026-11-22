package co.edu.uao.tallerbicicletas.exception;

// Se lanza cuando un recurso buscado no existe (responde 404)
public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
