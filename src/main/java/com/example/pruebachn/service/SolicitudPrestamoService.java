package com.example.pruebachn.service;

import com.example.pruebachn.dto.AprobarRechazarSolicitudDTO;
import com.example.pruebachn.dto.SolicitudPrestamoDTO;
import java.util.List;

public interface SolicitudPrestamoService {
    SolicitudPrestamoDTO crearSolicitud(Long clienteId, SolicitudPrestamoDTO solicitudDTO);
    SolicitudPrestamoDTO obtenerSolicitudPorId(Long id);
    List<SolicitudPrestamoDTO> obtenerSolicitudesPorCliente(Long clienteId);
    List<SolicitudPrestamoDTO> obtenerTodasLasSolicitudes();
    SolicitudPrestamoDTO aprobarSolicitud(Long solicitudId, AprobarRechazarSolicitudDTO dto);
    SolicitudPrestamoDTO rechazarSolicitud(Long solicitudId, AprobarRechazarSolicitudDTO dto);
}

