package com.example.pruebachn.controller;

import com.example.pruebachn.dto.PagoDTO;
import com.example.pruebachn.service.PagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@Tag(name = "Pagos", description = "API para gestión de pagos de préstamos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @PostMapping
    @Operation(summary = "Registrar pago", description = "Registra un pago en efectivo realizado por un cliente para un préstamo aprobado")
    public ResponseEntity<PagoDTO> registrarPago(@Valid @RequestBody PagoDTO pagoDTO) {
        PagoDTO pagoRegistrado = pagoService.registrarPago(pagoDTO);
        return new ResponseEntity<>(pagoRegistrado, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener pago por ID", description = "Retorna la información de un pago específico")
    public ResponseEntity<PagoDTO> obtenerPagoPorId(@PathVariable Long id) {
        PagoDTO pago = pagoService.obtenerPagoPorId(id);
        return ResponseEntity.ok(pago);
    }

    @GetMapping("/prestamo/{prestamoId}")
    @Operation(summary = "Listar pagos por préstamo", description = "Retorna todos los pagos realizados para un préstamo específico")
    public ResponseEntity<List<PagoDTO>> obtenerPagosPorPrestamo(@PathVariable Long prestamoId) {
        List<PagoDTO> pagos = pagoService.obtenerPagosPorPrestamo(prestamoId);
        return ResponseEntity.ok(pagos);
    }

    @GetMapping
    @Operation(summary = "Listar todos los pagos", description = "Retorna todos los pagos registrados en el sistema")
    public ResponseEntity<List<PagoDTO>> obtenerTodosLosPagos() {
        List<PagoDTO> pagos = pagoService.obtenerTodosLosPagos();
        return ResponseEntity.ok(pagos);
    }
}

