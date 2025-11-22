package com.example.pruebachn.service.impl;

import com.example.pruebachn.dto.AprobarRechazarSolicitudDTO;
import com.example.pruebachn.dto.SolicitudPrestamoDTO;
import com.example.pruebachn.entity.Cliente;
import com.example.pruebachn.entity.Prestamo;
import com.example.pruebachn.entity.SolicitudPrestamo;
import com.example.pruebachn.repository.ClienteRepository;
import com.example.pruebachn.repository.PrestamoRepository;
import com.example.pruebachn.repository.SolicitudPrestamoRepository;
import com.example.pruebachn.service.SolicitudPrestamoService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SolicitudPrestamoServiceImpl implements SolicitudPrestamoService {

    @Autowired
    private SolicitudPrestamoRepository solicitudPrestamoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Override
    public SolicitudPrestamoDTO crearSolicitud(Long clienteId, SolicitudPrestamoDTO solicitudDTO) {
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + clienteId));

        SolicitudPrestamo solicitud = new SolicitudPrestamo(
            cliente,
            solicitudDTO.getMontoSolicitado(),
            solicitudDTO.getPlazoMeses(),
            solicitudDTO.getDescripcion()
        );

        solicitud = solicitudPrestamoRepository.save(solicitud);
        return convertirAEntity(solicitud);
    }

    @Override
    public SolicitudPrestamoDTO obtenerSolicitudPorId(Long id) {
        SolicitudPrestamo solicitud = solicitudPrestamoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Solicitud no encontrada con ID: " + id));
        return convertirAEntity(solicitud);
    }

    @Override
    public List<SolicitudPrestamoDTO> obtenerSolicitudesPorCliente(Long clienteId) {
        return solicitudPrestamoRepository.findByClienteId(clienteId).stream()
            .map(this::convertirAEntity)
            .collect(Collectors.toList());
    }

    @Override
    public List<SolicitudPrestamoDTO> obtenerTodasLasSolicitudes() {
        return solicitudPrestamoRepository.findAll().stream()
            .map(this::convertirAEntity)
            .collect(Collectors.toList());
    }

    @Override
    public SolicitudPrestamoDTO aprobarSolicitud(Long solicitudId, AprobarRechazarSolicitudDTO dto) {
        SolicitudPrestamo solicitud = solicitudPrestamoRepository.findById(solicitudId)
            .orElseThrow(() -> new RuntimeException("Solicitud no encontrada con ID: " + solicitudId));

        if (solicitud.getEstado() != SolicitudPrestamo.EstadoSolicitud.EN_PROCESO) {
            throw new RuntimeException("Solo se pueden aprobar solicitudes en proceso");
        }

        // Verificar si ya existe un préstamo para esta solicitud
        if (prestamoRepository.findBySolicitudId(solicitudId).isPresent()) {
            throw new RuntimeException("Ya existe un préstamo para esta solicitud");
        }

        BigDecimal montoAprobado = dto.getMontoAprobado() != null ? dto.getMontoAprobado() : solicitud.getMontoSolicitado();
        BigDecimal tasaInteres = dto.getTasaInteres() != null ? dto.getTasaInteres() : new BigDecimal("5.0");

        // Actualizar estado de la solicitud
        solicitud.setEstado(SolicitudPrestamo.EstadoSolicitud.APROBADO);
        solicitud.setFechaAprobacion(LocalDateTime.now());
        solicitud.setObservaciones(dto.getObservaciones());
        solicitud = solicitudPrestamoRepository.save(solicitud);

        // Crear el préstamo
        Prestamo prestamo = new Prestamo(
            solicitud.getCliente(),
            solicitud,
            montoAprobado,
            solicitud.getPlazoMeses(),
            tasaInteres
        );
        prestamoRepository.save(prestamo);

        return convertirAEntity(solicitud);
    }

    @Override
    public SolicitudPrestamoDTO rechazarSolicitud(Long solicitudId, AprobarRechazarSolicitudDTO dto) {
        SolicitudPrestamo solicitud = solicitudPrestamoRepository.findById(solicitudId)
            .orElseThrow(() -> new RuntimeException("Solicitud no encontrada con ID: " + solicitudId));

        if (solicitud.getEstado() != SolicitudPrestamo.EstadoSolicitud.EN_PROCESO) {
            throw new RuntimeException("Solo se pueden rechazar solicitudes en proceso");
        }

        solicitud.setEstado(SolicitudPrestamo.EstadoSolicitud.RECHAZADO);
        solicitud.setFechaRechazo(LocalDateTime.now());
        solicitud.setObservaciones(dto.getObservaciones());
        solicitud = solicitudPrestamoRepository.save(solicitud);

        return convertirAEntity(solicitud);
    }

    private SolicitudPrestamoDTO convertirAEntity(SolicitudPrestamo solicitud) {
        SolicitudPrestamoDTO dto = new SolicitudPrestamoDTO();
        dto.setId(solicitud.getId());
        dto.setClienteId(solicitud.getCliente().getId());
        dto.setClienteNombre(solicitud.getCliente().getNombre());
        dto.setClienteApellido(solicitud.getCliente().getApellido());
        dto.setMontoSolicitado(solicitud.getMontoSolicitado());
        dto.setPlazoMeses(solicitud.getPlazoMeses());
        dto.setDescripcion(solicitud.getDescripcion());
        dto.setEstado(solicitud.getEstado());
        dto.setObservaciones(solicitud.getObservaciones());
        dto.setFechaSolicitud(solicitud.getFechaSolicitud());
        dto.setFechaAprobacion(solicitud.getFechaAprobacion());
        dto.setFechaRechazo(solicitud.getFechaRechazo());
        return dto;
    }
}

