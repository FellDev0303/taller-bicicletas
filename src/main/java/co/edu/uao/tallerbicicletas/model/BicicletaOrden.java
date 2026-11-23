package co.edu.uao.tallerbicicletas.model;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Copia de los datos de la bicicleta al momento de crear la orden (historico)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BicicletaOrden {
    private ObjectId bicicletaId;
    private String marca;
    private String modelo;
}