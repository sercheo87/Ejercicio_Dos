package com.example.demo.mapper;

import com.example.demo.model.dto.ClienteRequestDTO;
import com.example.demo.model.dto.ClienteResponseDTO;
import com.example.demo.model.entity.Cliente;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre entidades Cliente y DTOs
 */
@Component
public class ClienteMapper {
    
    /**
     * Convierte un DTO de request a una entidad Cliente
     * 
     * @param dto DTO de request
     * @return entidad Cliente
     */
    public Cliente toEntity(ClienteRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefono(dto.getTelefono());
        return cliente;
    }
    
    /**
     * Convierte una entidad Cliente a un DTO de respuesta
     * 
     * @param cliente entidad Cliente
     * @return DTO de respuesta
     */
    public ClienteResponseDTO toResponseDTO(Cliente cliente) {
        if (cliente == null) {
            return null;
        }
        
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(cliente.getId());
        dto.setNombre(cliente.getNombre());
        dto.setEmail(cliente.getEmail());
        dto.setTelefono(cliente.getTelefono());
        dto.setFechaRegistro(cliente.getFechaRegistro());
        dto.setActivo(cliente.getActivo());
        return dto;
    }
    
    /**
     * Convierte una lista de entidades Cliente a una lista de DTOs de respuesta
     * 
     * @param clientes lista de entidades Cliente
     * @return lista de DTOs de respuesta
     */
    public List<ClienteResponseDTO> toResponseDTOList(List<Cliente> clientes) {
        if (clientes == null) {
            return null;
        }
        
        return clientes.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}
