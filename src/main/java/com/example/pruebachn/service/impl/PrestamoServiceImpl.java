package com.example.pruebachn.service.impl;

import com.example.pruebachn.dto.PrestamoDTO;
import com.example.pruebachn.entity.Prestamo;
import com.example.pruebachn.repository.PagoRepository;
import com.example.pruebachn.repository.PrestamoRepository;
import com.example.pruebachn.service.PrestamoService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PrestamoServiceImpl implements PrestamoService {

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @Override
    public PrestamoDTO obtenerPrestamoPorId(Long id) {
        Prestamo prestamo = prestamoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Préstamo no encontrado con ID: " + id));
        return convertirAEntity(prestamo);
    }

    @Override
    public List<PrestamoDTO> obtenerPrestamosPorCliente(Long clienteId) {
        return prestamoRepository.findByClienteId(clienteId).stream()
            .map(this::convertirAEntity)
            .collect(Collectors.toList());
    }

    @Override
    public List<PrestamoDTO> obtenerTodosLosPrestamos() {
        return prestamoRepository.findAll().stream()
            .map(this::convertirAEntity)
            .collect(Collectors.toList());
    }

    @Override
    public BigDecimal calcularSaldoPendiente(Long prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
            .orElseThrow(() -> new RuntimeException("Préstamo no encontrado con ID: " + prestamoId));

        BigDecimal totalPagado = prestamo.getPagos().stream()
            .map(pago -> pago.getMonto())
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal saldoPendiente = prestamo.getMontoAprobado().subtract(totalPagado);
        
        // Actualizar el saldo pendiente en la entidad
        prestamo.setSaldoPendiente(saldoPendiente);
        
        // Actualizar el estado si está pagado completamente
        if (saldoPendiente.compareTo(BigDecimal.ZERO) <= 0) {
            prestamo.setEstado(Prestamo.EstadoPrestamo.PAGADO);
        }
        
        prestamoRepository.save(prestamo);
        return saldoPendiente;
    }

    private PrestamoDTO convertirAEntity(Prestamo prestamo) {
        PrestamoDTO dto = new PrestamoDTO();
        dto.setId(prestamo.getId());
        dto.setClienteId(prestamo.getCliente().getId());
        dto.setClienteNombre(prestamo.getCliente().getNombre());
        dto.setClienteApellido(prestamo.getCliente().getApellido());
        dto.setSolicitudId(prestamo.getSolicitud().getId());
        dto.setMontoAprobado(prestamo.getMontoAprobado());
        dto.setPlazoMeses(prestamo.getPlazoMeses());
        dto.setTasaInteres(prestamo.getTasaInteres());
        dto.setFechaAprobacion(prestamo.getFechaAprobacion());
        dto.setFechaVencimiento(prestamo.getFechaVencimiento());
        dto.setEstado(prestamo.getEstado());
        dto.setFechaCreacion(prestamo.getFechaCreacion());
        
        // Calcular total pagado
        BigDecimal totalPagado = prestamo.getPagos().stream()
            .map(pago -> pago.getMonto())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        dto.setTotalPagado(totalPagado);
        
        // Calcular saldo pendiente
        BigDecimal saldoPendiente = prestamo.getMontoAprobado().subtract(totalPagado);
        dto.setSaldoPendiente(saldoPendiente);
        
        return dto;
    }
}

