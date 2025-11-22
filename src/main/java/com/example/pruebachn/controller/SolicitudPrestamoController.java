package com.example.pruebachn.controller;

import com.example.pruebachn.dto.AprobarRechazarSolicitudDTO;
import com.example.pruebachn.dto.SolicitudPrestamoDTO;
import com.example.pruebachn.service.SolicitudPrestamoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes-prestamo")
@Tag(name = "Solicitudes de Préstamo", description = "API para gestión de solicitudes de préstamos")
public class SolicitudPrestamoController {

    @Autowired
    private SolicitudPrestamoService solicitudPrestamoService;

    @PostMapping("/cliente/{clienteId}")
    @Operation(summary = "Crear solicitud de préstamo", description = "Permite a un cliente solicitar un préstamo bancario")
    public ResponseEntity<SolicitudPrestamoDTO> crearSolicitud(
            @PathVariable Long clienteId,
            @Valid @RequestBody SolicitudPrestamoDTO solicitudDTO) {
        SolicitudPrestamoDTO solicitudCreada = solicitudPrestamoService.crearSolicitud(clienteId, solicitudDTO);
        return new ResponseEntity<>(solicitudCreada, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener solicitud por ID", description = "Retorna la información de una solicitud específica")
    public ResponseEntity<SolicitudPrestamoDTO> obtenerSolicitudPorId(@PathVariable Long id) {
        SolicitudPrestamoDTO solicitud = solicitudPrestamoService.obtenerSolicitudPorId(id);
        return ResponseEntity.ok(solicitud);
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Listar solicitudes por cliente", description = "Retorna todas las solicitudes de préstamo de un cliente específico")
    public ResponseEntity<List<SolicitudPrestamoDTO>> obtenerSolicitudesPorCliente(@PathVariable Long clienteId) {
        List<SolicitudPrestamoDTO> solicitudes = solicitudPrestamoService.obtenerSolicitudesPorCliente(clienteId);
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping
    @Operation(summary = "Listar todas las solicitudes", description = "Retorna todas las solicitudes de préstamo del sistema")
    public ResponseEntity<List<SolicitudPrestamoDTO>> obtenerTodasLasSolicitudes() {
        List<SolicitudPrestamoDTO> solicitudes = solicitudPrestamoService.obtenerTodasLasSolicitudes();
        return ResponseEntity.ok(solicitudes);
    }

    @PostMapping("/{id}/aprobar")
    @Operation(summary = "Aprobar solicitud de préstamo", description = "Aprueba una solicitud de préstamo y crea el préstamo correspondiente")
    public ResponseEntity<SolicitudPrestamoDTO> aprobarSolicitud(
            @PathVariable Long id,
            @Valid @RequestBody AprobarRechazarSolicitudDTO dto) {
        SolicitudPrestamoDTO solicitud = solicitudPrestamoService.aprobarSolicitud(id, dto);
        return ResponseEntity.ok(solicitud);
    }

    @PostMapping("/{id}/rechazar")
    @Operation(summary = "Rechazar solicitud de préstamo", description = "Rechaza una solicitud de préstamo con observaciones")
    public ResponseEntity<SolicitudPrestamoDTO> rechazarSolicitud(
            @PathVariable Long id,
            @Valid @RequestBody AprobarRechazarSolicitudDTO dto) {
        SolicitudPrestamoDTO solicitud = solicitudPrestamoService.rechazarSolicitud(id, dto);
        return ResponseEntity.ok(solicitud);
    }
}

