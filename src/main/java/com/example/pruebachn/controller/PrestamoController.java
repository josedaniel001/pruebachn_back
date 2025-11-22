package com.example.pruebachn.controller;

import com.example.pruebachn.dto.PrestamoDTO;
import com.example.pruebachn.service.PrestamoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
@Tag(name = "Préstamos", description = "API para gestión de préstamos aprobados")
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;

    @GetMapping("/{id}")
    @Operation(summary = "Obtener préstamo por ID", description = "Retorna la información de un préstamo específico con detalles y estado de pago")
    public ResponseEntity<PrestamoDTO> obtenerPrestamoPorId(@PathVariable Long id) {
        PrestamoDTO prestamo = prestamoService.obtenerPrestamoPorId(id);
        return ResponseEntity.ok(prestamo);
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Listar préstamos por cliente", description = "Retorna todos los préstamos aprobados de un cliente específico")
    public ResponseEntity<List<PrestamoDTO>> obtenerPrestamosPorCliente(@PathVariable Long clienteId) {
        List<PrestamoDTO> prestamos = prestamoService.obtenerPrestamosPorCliente(clienteId);
        return ResponseEntity.ok(prestamos);
    }

    @GetMapping
    @Operation(summary = "Listar todos los préstamos", description = "Retorna todos los préstamos aprobados del sistema")
    public ResponseEntity<List<PrestamoDTO>> obtenerTodosLosPrestamos() {
        List<PrestamoDTO> prestamos = prestamoService.obtenerTodosLosPrestamos();
        return ResponseEntity.ok(prestamos);
    }

    @GetMapping("/{id}/saldo-pendiente")
    @Operation(summary = "Calcular saldo pendiente", description = "Calcula y retorna el saldo pendiente de un préstamo")
    public ResponseEntity<BigDecimal> calcularSaldoPendiente(@PathVariable Long id) {
        BigDecimal saldo = prestamoService.calcularSaldoPendiente(id);
        return ResponseEntity.ok(saldo);
    }
}

