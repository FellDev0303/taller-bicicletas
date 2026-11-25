package co.edu.uao.tallerbicicletas.service;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import co.edu.uao.tallerbicicletas.dto.OrdenesPorEstadoEstadisticaDTO;
import co.edu.uao.tallerbicicletas.dto.ServicioSolicitadoEstadisticaDTO;
import co.edu.uao.tallerbicicletas.dto.TiemposAtencionEstadisticaDTO;
import co.edu.uao.tallerbicicletas.dto.ValoracionesEstadisticaDTO;
import co.edu.uao.tallerbicicletas.dto.ValoracionesEstadisticaDTO.DistribucionCalificacionDTO;

@Service
public class EstadisticasServiceImp implements IEstadisticasService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<ServicioSolicitadoEstadisticaDTO> obtenerServiciosMasSolicitados() {
        Aggregation aggregation = Aggregation.newAggregation(
                // 1. Filtrar solo los trabajos que tienen un servicio de catálogo asociado
                Aggregation.match(Criteria.where("servicio").ne(null)
                        .and("servicio.servicioId").ne(null)),

                // 2. Agrupar por servicioId y nombre, contando totales y cuántos fueron autorizados
                Aggregation.group("servicio.servicioId", "servicio.nombre")
                        .count().as("cantidadSolicitudes")
                        .sum(ConditionalOperators
                                .when(Criteria.where("autorizacion.decision").is("autorizado"))
                                .then(1)
                                .otherwise(0))
                        .as("cantidadAutorizados"),

                // 3. Proyectar los campos limpios hacia el DTO (convirtiendo ObjectId a String)
                Aggregation.project("cantidadSolicitudes", "cantidadAutorizados")
                        .andExpression("toString(_id.servicioId)").as("servicioId")
                        .and("_id.nombre").as("nombreServicio"),

                // 4. Ordenar de mayor a menor demanda
                Aggregation.sort(Sort.Direction.DESC, "cantidadSolicitudes")
        );

        AggregationResults<ServicioSolicitadoEstadisticaDTO> results = mongoTemplate.aggregate(
                aggregation, "trabajosOrden", ServicioSolicitadoEstadisticaDTO.class);

        return results.getMappedResults();
    }

    @Override
    public List<OrdenesPorEstadoEstadisticaDTO> obtenerOrdenesPorEstado() {
        Aggregation aggregation = Aggregation.newAggregation(
                // 1. Agrupar todas las órdenes por su campo estado
                Aggregation.group("estado").count().as("cantidad"),

                // 2. Proyectar _id como estado
                Aggregation.project("cantidad").and("_id").as("estado"),

                // 3. Ordenar de mayor a menor cantidad
                Aggregation.sort(Sort.Direction.DESC, "cantidad")
        );

        AggregationResults<OrdenesPorEstadoEstadisticaDTO> results = mongoTemplate.aggregate(
                aggregation, "ordenesServicio", OrdenesPorEstadoEstadisticaDTO.class);

        return results.getMappedResults();
    }

    @Override
    public TiemposAtencionEstadisticaDTO obtenerTiemposAtencion() {
        Aggregation aggregation = Aggregation.newAggregation(
                // 1. Filtrar órdenes entregadas que tengan ambas fechas registradas
                Aggregation.match(Criteria.where("estado").is("entregada")
                        .and("fechaRecepcion").ne(null)
                        .and("entrega.fecha").ne(null)),

                // 2. Calcular la diferencia en milisegundos (entrega.fecha - fechaRecepcion) y pasarla a horas
                Aggregation.project()
                        .and(ArithmeticOperators.Divide.valueOf(
                                ArithmeticOperators.Subtract.valueOf("entrega.fecha").subtract("fechaRecepcion"))
                                .divideBy(3600000))
                        .as("horasAtencion"),

                // 3. Agrupar todas las órdenes para sacar promedio, mínimo y máximo
                Aggregation.group()
                        .count().as("totalOrdenesEntregadas")
                        .avg("horasAtencion").as("promedioHorasAtencion")
                        .min("horasAtencion").as("minimoHorasAtencion")
                        .max("horasAtencion").as("maximoHorasAtencion"),

                // 4. Proyectar redondeando promedios, mínimo, máximo y equivalente en días
                Aggregation.project("totalOrdenesEntregadas")
                        .andExpression("round(promedioHorasAtencion, 2)").as("promedioHorasAtencion")
                        .andExpression("round(promedioHorasAtencion / 24, 2)").as("promedioDiasAtencion")
                        .andExpression("toLong(round(minimoHorasAtencion, 0))").as("minimoHorasAtencion")
                        .andExpression("toLong(round(maximoHorasAtencion, 0))").as("maximoHorasAtencion")
        );

        AggregationResults<TiemposAtencionEstadisticaDTO> results = mongoTemplate.aggregate(
                aggregation, "ordenesServicio", TiemposAtencionEstadisticaDTO.class);

        TiemposAtencionEstadisticaDTO resumen = results.getUniqueMappedResult();
        if (resumen == null) {
            return new TiemposAtencionEstadisticaDTO(0L, 0.0, 0.0, 0L, 0L);
        }
        return resumen;
    }

    @Override
    public ValoracionesEstadisticaDTO obtenerEstadisticasValoraciones() {
        // Agregación 1: Resumen general (total, promedio, mínima y máxima)
        Aggregation resumenAgg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("valoracionServicio").ne(null)
                        .and("valoracionServicio.calificacion").ne(null)),
                Aggregation.group()
                        .count().as("totalValoraciones")
                        .avg("valoracionServicio.calificacion").as("promedioCalificacion")
                        .min("valoracionServicio.calificacion").as("calificacionMinima")
                        .max("valoracionServicio.calificacion").as("calificacionMaxima"),
                Aggregation.project("totalValoraciones", "calificacionMinima", "calificacionMaxima")
                        .andExpression("round(promedioCalificacion, 2)").as("promedioCalificacion")
        );

        Document resumenDoc = mongoTemplate.aggregate(
                resumenAgg, "ordenesServicio", Document.class).getUniqueMappedResult();

        if (resumenDoc == null) {
            return new ValoracionesEstadisticaDTO(0L, 0.0, 0, 0, List.of());
        }

        // Agregación 2: Distribución de calificaciones (cuántas de 5, 4, 3, 2, 1 estrellas)
        Aggregation distribucionAgg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("valoracionServicio").ne(null)
                        .and("valoracionServicio.calificacion").ne(null)),
                Aggregation.group("valoracionServicio.calificacion").count().as("cantidad"),
                Aggregation.project("cantidad").and("_id").as("calificacion"),
                Aggregation.sort(Sort.Direction.DESC, "calificacion")
        );

        List<DistribucionCalificacionDTO> distribucion = mongoTemplate.aggregate(
                distribucionAgg, "ordenesServicio", DistribucionCalificacionDTO.class).getMappedResults();

        long total = ((Number) resumenDoc.get("totalValoraciones")).longValue();
        double promedio = ((Number) resumenDoc.get("promedioCalificacion")).doubleValue();
        int minima = ((Number) resumenDoc.get("calificacionMinima")).intValue();
        int maxima = ((Number) resumenDoc.get("calificacionMaxima")).intValue();

        return new ValoracionesEstadisticaDTO(total, promedio, minima, maxima, distribucion);
    }
}