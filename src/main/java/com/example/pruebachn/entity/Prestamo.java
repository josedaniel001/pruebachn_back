package com.example.pruebachn.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "prestamos")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud_id", nullable = false, unique = true)
    private SolicitudPrestamo solicitud;

    @NotNull
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal montoAprobado;

    @NotNull
    @Column(nullable = false)
    private Integer plazoMeses;

    @Column(precision = 5, scale = 2)
    private BigDecimal tasaInteres;

    @Column(nullable = false)
    private LocalDate fechaAprobacion;

    @Column(nullable = false)
    private LocalDate fechaVencimiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPrestamo estado = EstadoPrestamo.ACTIVO;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoPendiente;

    @OneToMany(mappedBy = "prestamo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pago> pagos = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    // Constructores
    public Prestamo() {
    }

    public Prestamo(Cliente cliente, SolicitudPrestamo solicitud, BigDecimal montoAprobado, 
                   Integer plazoMeses, BigDecimal tasaInteres) {
        this.cliente = cliente;
        this.solicitud = solicitud;
        this.montoAprobado = montoAprobado;
        this.plazoMeses = plazoMeses;
        this.tasaInteres = tasaInteres;
        this.fechaAprobacion = LocalDate.now();
        this.fechaVencimiento = fechaAprobacion.plusMonths(plazoMeses);
        this.estado = EstadoPrestamo.ACTIVO;
        this.saldoPendiente = montoAprobado;
        this.fechaCreacion = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public SolicitudPrestamo getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(SolicitudPrestamo solicitud) {
        this.solicitud = solicitud;
    }

    public BigDecimal getMontoAprobado() {
        return montoAprobado;
    }

    public void setMontoAprobado(BigDecimal montoAprobado) {
        this.montoAprobado = montoAprobado;
    }

    public Integer getPlazoMeses() {
        return plazoMeses;
    }

    public void setPlazoMeses(Integer plazoMeses) {
        this.plazoMeses = plazoMeses;
    }

    public BigDecimal getTasaInteres() {
        return tasaInteres;
    }

    public void setTasaInteres(BigDecimal tasaInteres) {
        this.tasaInteres = tasaInteres;
    }

    public LocalDate getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(LocalDate fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public EstadoPrestamo getEstado() {
        return estado;
    }

    public void setEstado(EstadoPrestamo estado) {
        this.estado = estado;
    }

    public BigDecimal getSaldoPendiente() {
        return saldoPendiente;
    }

    public void setSaldoPendiente(BigDecimal saldoPendiente) {
        this.saldoPendiente = saldoPendiente;
    }

    public List<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public enum EstadoPrestamo {
        ACTIVO, PAGADO, VENCIDO, CANCELADO
    }
}

