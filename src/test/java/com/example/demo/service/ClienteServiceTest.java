package com.example.demo.service;

import com.example.demo.exception.ClienteAlreadyExistsException;
import com.example.demo.exception.ClienteNotFoundException;
import com.example.demo.mapper.ClienteMapper;
import com.example.demo.model.dto.ClienteRequestDTO;
import com.example.demo.model.dto.ClienteResponseDTO;
import com.example.demo.model.entity.Cliente;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.service.impl.ClienteServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Tests - ClienteService (Business Logic)")
class ClienteServiceTest {

    @Mock
    private ClienteRepository repository;

    @Mock
    private ClienteMapper mapper;

    @InjectMocks
    private ClienteServiceImpl serviceUnderTest;

    private ClienteRequestDTO validRequestDTO;
    private Cliente validCliente;
    private ClienteResponseDTO validResponseDTO;

    @BeforeEach
    void givenSetup() {
        validRequestDTO = new ClienteRequestDTO();
        validRequestDTO.setNombre("Juan Perez");
        validRequestDTO.setEmail("juan.perez@example.com");
        validRequestDTO.setTelefono("1234567890");

        validCliente = new Cliente();
        validCliente.setId(1L);
        validCliente.setNombre("Juan Perez");
        validCliente.setEmail("juan.perez@example.com");
        validCliente.setTelefono("1234567890");
        validCliente.setFechaRegistro(LocalDateTime.now());
        validCliente.setActivo(true);

        validResponseDTO = new ClienteResponseDTO();
        validResponseDTO.setId(1L);
        validResponseDTO.setNombre("Juan Perez");
        validResponseDTO.setEmail("juan.perez@example.com");
        validResponseDTO.setTelefono("1234567890");
        validResponseDTO.setFechaRegistro(LocalDateTime.now());
        validResponseDTO.setActivo(true);
    }

    @Test
    @DisplayName("givenValidClienteRequest_whenCrear_thenReturnsCreatedCliente")
    void givenValidClienteRequest_whenCrear_thenReturnsCreatedCliente() {
        Mockito.when(repository.existsByNombre(validRequestDTO.getNombre())).thenReturn(false);
        Mockito.when(mapper.toEntity(validRequestDTO)).thenReturn(validCliente);
        Mockito.when(repository.save(validCliente)).thenReturn(validCliente);
        Mockito.when(mapper.toResponseDTO(validCliente)).thenReturn(validResponseDTO);

        var result = serviceUnderTest.crear(validRequestDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(validResponseDTO.getId(), result.getId());
        Assertions.assertEquals(validResponseDTO.getNombre(), result.getNombre());
        Assertions.assertEquals(validResponseDTO.getEmail(), result.getEmail());
        Assertions.assertEquals(validResponseDTO.getTelefono(), result.getTelefono());

        Mockito.verify(repository).existsByNombre(validRequestDTO.getNombre());
        Mockito.verify(mapper).toEntity(validRequestDTO);
        Mockito.verify(repository).save(validCliente);
        Mockito.verify(mapper).toResponseDTO(validCliente);
    }

    @Test
    @DisplayName("givenExistingClienteName_whenCrear_thenThrowsClienteAlreadyExistsException")
    void givenExistingClienteName_whenCrear_thenThrowsClienteAlreadyExistsException() {
        Mockito.when(repository.existsByNombre(validRequestDTO.getNombre())).thenReturn(true);

        var exception = Assertions.assertThrows(
                ClienteAlreadyExistsException.class,
                () -> serviceUnderTest.crear(validRequestDTO)
        );

        Assertions.assertTrue(exception.getMessage().contains("Ya existe un cliente con el nombre"));
        Assertions.assertTrue(exception.getMessage().contains(validRequestDTO.getNombre()));

        Mockito.verify(repository).existsByNombre(validRequestDTO.getNombre());
        Mockito.verify(mapper, Mockito.never()).toEntity(Mockito.any());
        Mockito.verify(repository, Mockito.never()).save(Mockito.any());
    }

    @Test
    @DisplayName("givenExistingClienteId_whenObtenerPorId_thenReturnsCliente")
    void givenExistingClienteId_whenObtenerPorId_thenReturnsCliente() {
        var clienteId = 1L;
        Mockito.when(repository.findById(clienteId)).thenReturn(Optional.of(validCliente));
        Mockito.when(mapper.toResponseDTO(validCliente)).thenReturn(validResponseDTO);

        var result = serviceUnderTest.obtenerPorId(clienteId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(validResponseDTO.getId(), result.getId());
        Assertions.assertEquals(validResponseDTO.getNombre(), result.getNombre());

        Mockito.verify(repository).findById(clienteId);
        Mockito.verify(mapper).toResponseDTO(validCliente);
    }

    @Test
    @DisplayName("givenNonExistingClienteId_whenObtenerPorId_thenThrowsClienteNotFoundException")
    void givenNonExistingClienteId_whenObtenerPorId_thenThrowsClienteNotFoundException() {
        var clienteId = 999L;
        Mockito.when(repository.findById(clienteId)).thenReturn(Optional.empty());

        var exception = Assertions.assertThrows(
                ClienteNotFoundException.class,
                () -> serviceUnderTest.obtenerPorId(clienteId)
        );

        Assertions.assertTrue(exception.getMessage().contains("999"));

        Mockito.verify(repository).findById(clienteId);
        Mockito.verify(mapper, Mockito.never()).toResponseDTO(Mockito.any());
    }

    @Test
    @DisplayName("givenValidUpdateRequest_whenActualizar_thenReturnsUpdatedCliente")
    void givenValidUpdateRequest_whenActualizar_thenReturnsUpdatedCliente() {
        var clienteId = 1L;
        var updateRequest = new ClienteRequestDTO();
        updateRequest.setNombre("Juan Perez Updated");
        updateRequest.setEmail("juan.updated@example.com");
        updateRequest.setTelefono("9876543210");

        var existingCliente = new Cliente();
        existingCliente.setId(clienteId);
        existingCliente.setNombre("Juan Perez");
        existingCliente.setEmail("juan.perez@example.com");
        existingCliente.setTelefono("1234567890");
        existingCliente.setActivo(true);

        var updatedCliente = new Cliente();
        updatedCliente.setId(clienteId);
        updatedCliente.setNombre("Juan Perez Updated");
        updatedCliente.setEmail("juan.updated@example.com");
        updatedCliente.setTelefono("9876543210");
        updatedCliente.setActivo(true);

        var updatedResponse = new ClienteResponseDTO();
        updatedResponse.setId(clienteId);
        updatedResponse.setNombre("Juan Perez Updated");
        updatedResponse.setEmail("juan.updated@example.com");
        updatedResponse.setTelefono("9876543210");

        Mockito.when(repository.findById(clienteId)).thenReturn(Optional.of(existingCliente));
        Mockito.when(repository.existsByNombre(updateRequest.getNombre())).thenReturn(false);
        Mockito.when(repository.save(existingCliente)).thenReturn(updatedCliente);
        Mockito.when(mapper.toResponseDTO(updatedCliente)).thenReturn(updatedResponse);

        var result = serviceUnderTest.actualizar(clienteId, updateRequest);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Juan Perez Updated", result.getNombre());
        Assertions.assertEquals("juan.updated@example.com", result.getEmail());
        Assertions.assertEquals("9876543210", result.getTelefono());

        Mockito.verify(repository).findById(clienteId);
        Mockito.verify(repository).existsByNombre(updateRequest.getNombre());
        Mockito.verify(repository).save(existingCliente);
        Mockito.verify(mapper).toResponseDTO(updatedCliente);
    }
}

