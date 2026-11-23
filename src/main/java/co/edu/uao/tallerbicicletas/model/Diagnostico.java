package co.edu.uao.tallerbicicletas.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Diagnostico {
    private List<Hallazgo> hallazgos;
}