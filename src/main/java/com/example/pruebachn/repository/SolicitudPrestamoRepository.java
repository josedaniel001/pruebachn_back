package com.example.pruebachn.repository;

import com.example.pruebachn.entity.SolicitudPrestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudPrestamoRepository extends JpaRepository<SolicitudPrestamo, Long> {
    List<SolicitudPrestamo> findByClienteId(Long clienteId);
    List<SolicitudPrestamo> findByEstado(SolicitudPrestamo.EstadoSolicitud estado);
}

