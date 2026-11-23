package co.edu.uao.tallerbicicletas.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uao.tallerbicicletas.dto.BicicletaRequestDTO;
import co.edu.uao.tallerbicicletas.dto.BicicletaRequestDTO.BateriaDTO;
import co.edu.uao.tallerbicicletas.dto.BicicletaRequestDTO.CaracteristicasDTO;
import co.edu.uao.tallerbicicletas.dto.BicicletaRequestDTO.MotorDTO;
import co.edu.uao.tallerbicicletas.dto.BicicletaRequestDTO.SuspensionDTO;
import co.edu.uao.tallerbicicletas.dto.BicicletaResponseDTO;
import co.edu.uao.tallerbicicletas.exception.RecursoNoEncontradoException;
import co.edu.uao.tallerbicicletas.exception.SolicitudInvalidaException;
import co.edu.uao.tallerbicicletas.model.BicicletasModel;
import co.edu.uao.tallerbicicletas.model.UsuariosModel;
import co.edu.uao.tallerbicicletas.repository.IBicicletasRepository;
import co.edu.uao.tallerbicicletas.repository.IUsuariosRepository;

@Service
public class BicicletaServiceImp implements IBicicletaService {

    @Autowired
    private IBicicletasRepository bicicletasRepository;

    @Autowired
    private IUsuariosRepository usuariosRepository;

    @Override
    public BicicletaResponseDTO crearBicicleta(BicicletaRequestDTO dto) {
        validarPropietario(dto.getPropietarioId());
        validarCaracteristicasPorTipo(dto);
        BicicletasModel guardada = bicicletasRepository.save(aModelo(dto));
        return aRespuesta(guardada);
    }

    @Override
    public List<BicicletaResponseDTO> listarBicicletas(String propietarioId) {
        List<BicicletasModel> bicicletas;
        if (propietarioId == null) {
            bicicletas = bicicletasRepository.findAll();
        } else {
            validarIdMongo(propietarioId);
            bicicletas = bicicletasRepository.findByPropietarioId(new ObjectId(propietarioId));
        }
        return bicicletas.stream().map(this::aRespuesta).toList();
    }

    @Override
    public BicicletaResponseDTO buscarBicicletaPorId(String id) {
        validarIdMongo(id);
        BicicletasModel bicicleta = bicicletasRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new RecursoNoEncontradoException("Bicicleta no encontrada: " + id));
        return aRespuesta(bicicleta);
    }

    private void validarPropietario(String propietarioId) {
        UsuariosModel propietario = usuariosRepository.findById(new ObjectId(propietarioId))
                .orElseThrow(() -> new SolicitudInvalidaException("El propietario no existe"));
        if (!"cliente".equals(propietario.getRol())) {
            throw new SolicitudInvalidaException("El propietario debe ser un usuario con rol cliente");
        }
    }

    private void validarCaracteristicasPorTipo(BicicletaRequestDTO dto) {
        CaracteristicasDTO c = dto.getCaracteristicas();
        switch (dto.getTipo()) {
            case "montana" -> {
                if (c.getSuspension() == null) {
                    throw new SolicitudInvalidaException("Una bicicleta de montaña requiere suspension");
                }
            }
            case "electrica" -> {
                if (c.getBateria() == null || c.getMotor() == null) {
                    throw new SolicitudInvalidaException("Una bicicleta eléctrica requiere bateria y motor");
                }
            }
            default -> { }
        }
    }

    private void validarIdMongo(String id) {
        if (!ObjectId.isValid(id)) {
            throw new SolicitudInvalidaException("Identificador inválido: " + id);
        }
    }

    private BicicletasModel aModelo(BicicletaRequestDTO dto) {
        CaracteristicasDTO c = dto.getCaracteristicas();
        BicicletasModel.Caracteristicas caracteristicas = new BicicletasModel.Caracteristicas(
                c.getTamanoMarco(), c.getMaterial(), c.getTamanoRueda(), c.getTipoFrenos(), c.getTransmision(),
                c.getSuspension() == null ? null
                        : new BicicletasModel.Suspension(c.getSuspension().getInformacion(),
                                c.getSuspension().getRecorrido()),
                c.getBateria() == null ? null
                        : new BicicletasModel.Bateria(c.getBateria().getCapacidadWh(),
                                c.getBateria().getVoltajeV()),
                c.getMotor() == null ? null
                        : new BicicletasModel.Motor(c.getMotor().getTipo(), c.getMotor().getPotenciaW()));
        return new BicicletasModel(null, new ObjectId(dto.getPropietarioId()), dto.getMarca(),
                dto.getModelo(), dto.getTipo(), dto.getColor(), dto.getNumeroSerie(), caracteristicas);
    }

    private BicicletaResponseDTO aRespuesta(BicicletasModel b) {
        BicicletasModel.Caracteristicas c = b.getCaracteristicas();
        CaracteristicasDTO caracteristicas = new CaracteristicasDTO(
                c.getTamanoMarco(), c.getMaterial(), c.getTamanoRueda(), c.getTipoFrenos(), c.getTransmision(),
                c.getSuspension() == null ? null
                        : new SuspensionDTO(c.getSuspension().getInformacion(), c.getSuspension().getRecorrido()),
                c.getBateria() == null ? null
                        : new BateriaDTO(c.getBateria().getCapacidadWh(), c.getBateria().getVoltajeV()),
                c.getMotor() == null ? null
                        : new MotorDTO(c.getMotor().getTipo(), c.getMotor().getPotenciaW()));
        return new BicicletaResponseDTO(b.getId().toHexString(), b.getPropietarioId().toHexString(),
                b.getMarca(), b.getModelo(), b.getTipo(), b.getColor(), b.getNumeroSerie(), caracteristicas);
    }
}