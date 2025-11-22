package com.example.pruebachn.service;

import com.example.pruebachn.dto.PrestamoDTO;

import java.math.BigDecimal;
import java.util.List;

public interface PrestamoService {
    PrestamoDTO obtenerPrestamoPorId(Long id);
    List<PrestamoDTO> obtenerPrestamosPorCliente(Long clienteId);
    List<PrestamoDTO> obtenerTodosLosPrestamos();
    BigDecimal calcularSaldoPendiente(Long prestamoId);
}

