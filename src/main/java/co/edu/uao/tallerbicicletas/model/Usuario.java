package co.edu.uao.tallerbicicletas.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


@Data 
@Document (collection = "usuarios")

public class Usuario {
    @Id 
    private String id;


    @NotBlank 
    private String nombreCompleto;

    @NotBlank 
    @Indexed (unique =  true)
    private String documento;
    
    
    @NotBlank
    @Email 
    @Indexed (unique= true)
    private  String correo;

    @NotBlank 
    private  String telefono;

    @NotBlank 
    @Pattern (regexp = "cliente|tecnico|coordinador",
        message = "El rol debe ser, cliente, tecnico o coordinador"
    )
    private String rol;

    private  String direccion;


}
