package com.example.pruebachn.service;

import com.example.pruebachn.dto.PagoDTO;
import java.util.List;

public interface PagoService {
    PagoDTO registrarPago(PagoDTO pagoDTO);
    PagoDTO obtenerPagoPorId(Long id);
    List<PagoDTO> obtenerPagosPorPrestamo(Long prestamoId);
    List<PagoDTO> obtenerTodosLosPagos();
}

