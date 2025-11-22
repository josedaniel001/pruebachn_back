package com.example.pruebachn.service.impl;

import com.example.pruebachn.dto.ClienteDTO;
import com.example.pruebachn.entity.Cliente;
import com.example.pruebachn.repository.ClienteRepository;
import com.example.pruebachn.service.ClienteService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public ClienteDTO crearCliente(ClienteDTO clienteDTO) {
        // Verificar si ya existe un cliente con ese número de identificación
        if (clienteRepository.existsByNumeroIdentificacion(clienteDTO.getNumeroIdentificacion())) {
            throw new RuntimeException("Ya existe un cliente con el número de identificación: " + clienteDTO.getNumeroIdentificacion());
        }

        Cliente cliente = new Cliente(
            clienteDTO.getNombre(),
            clienteDTO.getApellido(),
            clienteDTO.getNumeroIdentificacion(),
            clienteDTO.getFechaNacimiento(),
            clienteDTO.getDireccion(),
            clienteDTO.getCorreoElectronico(),
            clienteDTO.getTelefono()
        );

        cliente = clienteRepository.save(cliente);
        return convertirAEntity(cliente);
    }

    @Override
    public ClienteDTO obtenerClientePorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
        return convertirAEntity(cliente);
    }

    @Override
    public List<ClienteDTO> obtenerTodosLosClientes() {
        return clienteRepository.findAll().stream()
            .map(this::convertirAEntity)
            .collect(Collectors.toList());
    }

    @Override
    public ClienteDTO actualizarCliente(Long id, ClienteDTO clienteDTO) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));

        // Verificar si el número de identificación ya existe en otro cliente
        if (!cliente.getNumeroIdentificacion().equals(clienteDTO.getNumeroIdentificacion())) {
            if (clienteRepository.existsByNumeroIdentificacion(clienteDTO.getNumeroIdentificacion())) {
                throw new RuntimeException("Ya existe otro cliente con el número de identificación: " + clienteDTO.getNumeroIdentificacion());
            }
        }

        cliente.setNombre(clienteDTO.getNombre());
        cliente.setApellido(clienteDTO.getApellido());
        cliente.setNumeroIdentificacion(clienteDTO.getNumeroIdentificacion());
        cliente.setFechaNacimiento(clienteDTO.getFechaNacimiento());
        cliente.setDireccion(clienteDTO.getDireccion());
        cliente.setCorreoElectronico(clienteDTO.getCorreoElectronico());
        cliente.setTelefono(clienteDTO.getTelefono());

        cliente = clienteRepository.save(cliente);
        return convertirAEntity(cliente);
    }

    @Override
    public void eliminarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
        
        // Las solicitudes y préstamos se eliminarán en cascada debido a orphanRemoval = true
        clienteRepository.delete(cliente);
    }

    @Override
    public ClienteDTO obtenerClientePorNumeroIdentificacion(String numeroIdentificacion) {
        Cliente cliente = clienteRepository.findByNumeroIdentificacion(numeroIdentificacion)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con número de identificación: " + numeroIdentificacion));
        return convertirAEntity(cliente);
    }

    private ClienteDTO convertirAEntity(Cliente cliente) {
        return new ClienteDTO(
            cliente.getId(),
            cliente.getNombre(),
            cliente.getApellido(),
            cliente.getNumeroIdentificacion(),
            cliente.getFechaNacimiento(),
            cliente.getDireccion(),
            cliente.getCorreoElectronico(),
            cliente.getTelefono()
        );
    }
}

