package com.example.demo.service.impl;

import com.example.demo.exception.ClienteAlreadyExistsException;
import com.example.demo.exception.ClienteNotFoundException;
import com.example.demo.mapper.ClienteMapper;
import com.example.demo.model.dto.ClienteRequestDTO;
import com.example.demo.model.dto.ClienteResponseDTO;
import com.example.demo.model.entity.Cliente;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.service.ClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del servicio que gestiona la lógica de negocio de clientes
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ClienteServiceImpl implements ClienteService {
    
    private final ClienteRepository repository;
    private final ClienteMapper mapper;

    /**
     * Crea un nuevo cliente
     * 
     * @param requestDTO datos del cliente a crear
     * @return cliente creado
     * @throws ClienteAlreadyExistsException si ya existe un cliente con el mismo nombre
     */
    @Override
    public ClienteResponseDTO crear(ClienteRequestDTO requestDTO) {
        log.info("Creando cliente con nombre: {}", requestDTO.getNombre());
        
        if (repository.existsByNombre(requestDTO.getNombre())) {
            log.warn("Ya existe un cliente con el nombre: {}", requestDTO.getNombre());
            throw new ClienteAlreadyExistsException("Ya existe un cliente con el nombre: " + requestDTO.getNombre());
        }
        
        Cliente cliente = mapper.toEntity(requestDTO);
        Cliente clienteGuardado = repository.save(cliente);
        
        log.info("Cliente creado exitosamente con id: {}", clienteGuardado.getId());
        return mapper.toResponseDTO(clienteGuardado);
    }

    /**
     * Obtiene un cliente por su ID
     * 
     * @param id ID del cliente
     * @return cliente encontrado
     * @throws ClienteNotFoundException si el cliente no existe
     */
    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDTO obtenerPorId(Long id) {
        log.info("Buscando cliente con id: {}", id);
        
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException(id));
        
        return mapper.toResponseDTO(cliente);
    }

    /**
     * Obtiene un cliente por su nombre
     * 
     * @param nombre nombre del cliente
     * @return cliente encontrado
     * @throws ClienteNotFoundException si el cliente no existe
     */
    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDTO obtenerPorNombre(String nombre) {
        log.info("Buscando cliente con nombre: {}", nombre);
        
        Cliente cliente = repository.findByNombre(nombre)
                .orElseThrow(() -> new ClienteNotFoundException("nombre", nombre));
        
        return mapper.toResponseDTO(cliente);
    }

    /**
     * Lista todos los clientes activos
     * 
     * @return lista de clientes
     */
    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listar() {
        log.info("Listando todos los clientes activos");
        
        List<Cliente> clientes = repository.findByActivo(true);
        return mapper.toResponseDTOList(clientes);
    }

    /**
     * Actualiza un cliente existente
     * 
     * @param id ID del cliente a actualizar
     * @param requestDTO nuevos datos del cliente
     * @return cliente actualizado
     * @throws ClienteNotFoundException si el cliente no existe
     */
    @Override
    public ClienteResponseDTO actualizar(Long id, ClienteRequestDTO requestDTO) {
        log.info("Actualizando cliente con id: {}", id);
        
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException(id));
        
        // Verificar si el nuevo nombre ya existe en otro cliente
        if (!cliente.getNombre().equals(requestDTO.getNombre()) 
                && repository.existsByNombre(requestDTO.getNombre())) {
            throw new ClienteAlreadyExistsException("Ya existe un cliente con el nombre: " + requestDTO.getNombre());
        }
        
        cliente.setNombre(requestDTO.getNombre());
        cliente.setEmail(requestDTO.getEmail());
        cliente.setTelefono(requestDTO.getTelefono());
        
        Cliente clienteActualizado = repository.save(cliente);
        
        log.info("Cliente actualizado exitosamente con id: {}", clienteActualizado.getId());
        return mapper.toResponseDTO(clienteActualizado);
    }

    /**
     * Elimina (desactiva) un cliente
     * 
     * @param id ID del cliente a eliminar
     * @throws ClienteNotFoundException si el cliente no existe
     */
    @Override
    public void eliminar(Long id) {
        log.info("Eliminando cliente con id: {}", id);
        
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException(id));
        
        cliente.setActivo(false);
        repository.save(cliente);
        
        log.info("Cliente eliminado (desactivado) exitosamente con id: {}", id);
    }

    /**
     * Elimina permanentemente un cliente de la base de datos
     * 
     * @param id ID del cliente a eliminar
     * @throws ClienteNotFoundException si el cliente no existe
     */
    @Override
    public void eliminarPermanente(Long id) {
        log.info("Eliminando permanentemente cliente con id: {}", id);
        
        if (!repository.existsById(id)) {
            throw new ClienteNotFoundException(id);
        }
        
        repository.deleteById(id);
        
        log.info("Cliente eliminado permanentemente con id: {}", id);
    }
}