package com.example.demo.controller;

import com.example.demo.model.dto.ClienteRequestDTO;
import com.example.demo.model.dto.ClienteResponseDTO;
import com.example.demo.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar clientes
 */
@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
@Slf4j
public class ClienteController {
    
    private final ClienteService service;

    /**
     * Crea un nuevo cliente
     * 
     * @param requestDTO datos del cliente a crear
     * @return cliente creado con estado HTTP 201
     */
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> crear(@Valid @RequestBody ClienteRequestDTO requestDTO) {
        log.info("POST /api/v1/clientes - Crear cliente: {}", requestDTO.getNombre());
        
        ClienteResponseDTO response = service.crear(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Obtiene un cliente por su ID
     * 
     * @param id ID del cliente
     * @return cliente encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/v1/clientes/{} - Obtener cliente", id);
        
        ClienteResponseDTO response = service.obtenerPorId(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene un cliente por su nombre
     * 
     * @param nombre nombre del cliente
     * @return cliente encontrado
     */
    @GetMapping("/buscar")
    public ResponseEntity<ClienteResponseDTO> obtenerPorNombre(@RequestParam String nombre) {
        log.info("GET /api/v1/clientes/buscar?nombre={} - Buscar cliente", nombre);
        
        ClienteResponseDTO response = service.obtenerPorNombre(nombre);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista todos los clientes activos
     * 
     * @return lista de clientes
     */
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listar() {
        log.info("GET /api/v1/clientes - Listar todos los clientes");
        
        List<ClienteResponseDTO> response = service.listar();
        return ResponseEntity.ok(response);
    }

    /**
     * Actualiza un cliente existente
     * 
     * @param id ID del cliente a actualizar
     * @param requestDTO nuevos datos del cliente
     * @return cliente actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> actualizar(
            @PathVariable Long id, 
            @Valid @RequestBody ClienteRequestDTO requestDTO) {
        log.info("PUT /api/v1/clientes/{} - Actualizar cliente", id);
        
        ClienteResponseDTO response = service.actualizar(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Elimina (desactiva) un cliente
     * 
     * @param id ID del cliente a eliminar
     * @return respuesta sin contenido
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/v1/clientes/{} - Eliminar cliente", id);
        
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint de salud para verificar que el servicio está activo
     * 
     * @return mensaje de estado
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Cliente Service is UP");
    }
}