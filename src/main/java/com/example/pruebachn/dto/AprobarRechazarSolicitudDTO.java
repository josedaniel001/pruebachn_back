package com.example.pruebachn.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class AprobarRechazarSolicitudDTO {

    @NotNull(message = "Las observaciones son obligatorias")
    private String observaciones;
    
    private BigDecimal montoAprobado; // Solo para aprobación
    private BigDecimal tasaInteres; // Solo para aprobación

    // Constructores
    public AprobarRechazarSolicitudDTO() {
    }

    // Getters y Setters
    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public BigDecimal getMontoAprobado() {
        return montoAprobado;
    }

    public void setMontoAprobado(BigDecimal montoAprobado) {
        this.montoAprobado = montoAprobado;
    }

    public BigDecimal getTasaInteres() {
        return tasaInteres;
    }

    public void setTasaInteres(BigDecimal tasaInteres) {
        this.tasaInteres = tasaInteres;
    }
}

