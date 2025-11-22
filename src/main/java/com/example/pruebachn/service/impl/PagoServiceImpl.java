package com.example.pruebachn.service.impl;

import com.example.pruebachn.dto.PagoDTO;
import com.example.pruebachn.entity.Pago;
import com.example.pruebachn.entity.Prestamo;
import com.example.pruebachn.repository.PagoRepository;
import com.example.pruebachn.repository.PrestamoRepository;
import com.example.pruebachn.service.PagoService;
import com.example.pruebachn.service.PrestamoService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PagoServiceImpl implements PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private PrestamoService prestamoService;

    @Override
    public PagoDTO registrarPago(PagoDTO pagoDTO) {
        Prestamo prestamo = prestamoRepository.findById(pagoDTO.getPrestamoId())
            .orElseThrow(() -> new RuntimeException("Préstamo no encontrado con ID: " + pagoDTO.getPrestamoId()));

        if (prestamo.getEstado() == Prestamo.EstadoPrestamo.PAGADO) {
            throw new RuntimeException("El préstamo ya está completamente pagado");
        }

        // Verificar que el monto del pago no exceda el saldo pendiente
        BigDecimal saldoPendiente = prestamoService.calcularSaldoPendiente(prestamo.getId());
        if (pagoDTO.getMonto().compareTo(saldoPendiente) > 0) {
            throw new RuntimeException("El monto del pago (" + pagoDTO.getMonto() + ") excede el saldo pendiente (" + saldoPendiente + ")");
        }

        Pago pago = new Pago(prestamo, pagoDTO.getMonto(), pagoDTO.getObservaciones());
        pago = pagoRepository.save(pago);

        // Recalcular el saldo pendiente después del pago
        prestamoService.calcularSaldoPendiente(prestamo.getId());

        return convertirAEntity(pago);
    }

    @Override
    public PagoDTO obtenerPagoPorId(Long id) {
        Pago pago = pagoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));
        return convertirAEntity(pago);
    }

    @Override
    public List<PagoDTO> obtenerPagosPorPrestamo(Long prestamoId) {
        return pagoRepository.findByPrestamoId(prestamoId).stream()
            .map(this::convertirAEntity)
            .collect(Collectors.toList());
    }

    @Override
    public List<PagoDTO> obtenerTodosLosPagos() {
        return pagoRepository.findAll().stream()
            .map(this::convertirAEntity)
            .collect(Collectors.toList());
    }

    private PagoDTO convertirAEntity(Pago pago) {
        PagoDTO dto = new PagoDTO();
        dto.setId(pago.getId());
        dto.setPrestamoId(pago.getPrestamo().getId());
        dto.setMonto(pago.getMonto());
        dto.setObservaciones(pago.getObservaciones());
        dto.setFechaPago(pago.getFechaPago());
        return dto;
    }
}

