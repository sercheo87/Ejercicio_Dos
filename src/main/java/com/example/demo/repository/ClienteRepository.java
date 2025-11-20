package com.example.demo.repository;

import com.example.demo.model.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestionar las operaciones de persistencia de Cliente
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    /**
     * Busca un cliente por su nombre
     * 
     * @param nombre nombre del cliente
     * @return Optional con el cliente si existe
     */
    Optional<Cliente> findByNombre(String nombre);
    
    /**
     * Verifica si existe un cliente con el nombre dado
     * 
     * @param nombre nombre del cliente
     * @return true si existe, false en caso contrario
     */
    boolean existsByNombre(String nombre);
    
    /**
     * Busca clientes activos
     * 
     * @param activo estado del cliente
     * @return lista de clientes activos
     */
    List<Cliente> findByActivo(Boolean activo);
    
    /**
     * Busca un cliente por email
     * 
     * @param email email del cliente
     * @return Optional con el cliente si existe
     */
    Optional<Cliente> findByEmail(String email);
}
