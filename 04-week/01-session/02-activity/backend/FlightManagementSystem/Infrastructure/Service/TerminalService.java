package com.SENA.FlightManagementSystem.Infrastructure.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SENA.FlightManagementSystem.Parameterization.IRepository.IBaseRepository;
import com.SENA.FlightManagementSystem.Parameterization.Service.ABaseService;
import com.SENA.FlightManagementSystem.Infrastructure.Entity.Terminal;
import com.SENA.FlightManagementSystem.Infrastructure.IRepository.ITerminalRepository;
import com.SENA.FlightManagementSystem.Infrastructure.IService.ITerminalService;

@Service
public class TerminalService extends ABaseService<Terminal> implements ITerminalService {

    @Autowired
    private ITerminalRepository repository;

    @Override
    protected IBaseRepository<Terminal, String> getRepository() {
        return repository;
    }

    @Override
    public Optional<Terminal> findByName(String name) throws Exception {
        try {
            if (name == null || name.trim().isEmpty()) {
                throw new Exception("El nombre de la terminal no puede estar vacío");
            }
            return repository.findByName(name.trim());
        } catch (Exception e) {
            throw new Exception("Error al buscar terminal por nombre: " + e.getMessage());
        }
    }

    @Override
    public Optional<Terminal> findByCode(String code) throws Exception {
        try {
            if (code == null || code.trim().isEmpty()) {
                throw new Exception("El código de la terminal no puede estar vacío");
            }
            return repository.findByCode(code.trim().toUpperCase());
        } catch (Exception e) {
            throw new Exception("Error al buscar terminal por código: " + e.getMessage());
        }
    }

    @Override
    public List<Terminal> findByAirportId(String airportId) throws Exception {
        try {
            if (airportId == null || airportId.trim().isEmpty()) {
                throw new Exception("El ID del aeropuerto no puede estar vacío");
            }
            return repository.findByAirportId(airportId);
        } catch (Exception e) {
            throw new Exception("Error al buscar terminales por aeropuerto: " + e.getMessage());
        }
    }

    @Override
    public void validateTerminalData(Terminal terminal) throws Exception {
        if (terminal == null) {
            throw new Exception("Los datos de la terminal no pueden estar vacíos");
        }

        // Validar campos obligatorios heredados de ABaseEntity
        if (terminal.getName() == null || terminal.getName().trim().isEmpty()) {
            throw new Exception("El nombre de la terminal es obligatorio");
        }
        if (terminal.getCode() == null || terminal.getCode().trim().isEmpty()) {
            throw new Exception("El código de la terminal es obligatorio");
        }
        if (terminal.getDescription() == null || terminal.getDescription().trim().isEmpty()) {
            throw new Exception("La descripción de la terminal es obligatoria");
        }

        // Validar longitudes
        if (terminal.getName().length() > 100) {
            throw new Exception("El nombre de la terminal no puede tener más de 100 caracteres");
        }
        if (terminal.getCode().length() > 10) {
            throw new Exception("El código de la terminal no puede tener más de 10 caracteres");
        }
        if (terminal.getDescription().length() > 255) {
            throw new Exception("La descripción de la terminal no puede tener más de 255 caracteres");
        }

        // Validar formato del código (solo letras mayúsculas y números)
        if (!terminal.getCode().matches("^[A-Z0-9]+$")) {
            throw new Exception("El código de la terminal solo puede contener letras mayúsculas y números");
        }
    }

    @Override
    public boolean isNameInUse(String name, String excludeTerminalId) throws Exception {
        return false;
    }

    @Override
    public boolean isCodeInUse(String code, String excludeTerminalId) throws Exception {
        return false;
    }

    @Override
    public boolean isCodeInUseInAirport(String code, String airportId, String excludeTerminalId) throws Exception {
        return false;
    }

    @Override
    public boolean isNameInUseInAirport(String name, String airportId, String excludeTerminalId) throws Exception {
        return false;
    }

    @Override
    public Terminal save(Terminal entity) throws Exception {
        try {
            // Normalizar datos antes de guardar
            if (entity.getName() != null) {
                entity.setName(entity.getName().trim());
            }
            if (entity.getCode() != null) {
                entity.setCode(entity.getCode().trim().toUpperCase());
            }
            if (entity.getDescription() != null) {
                entity.setDescription(entity.getDescription().trim());
            }

            // Validar datos
            validateTerminalData(entity);

            return super.save(entity);
        } catch (Exception e) {
            throw new Exception("Error al guardar la terminal: " + e.getMessage());
        }
    }

    @Override
    public void update(String id, Terminal entity) throws Exception {
        try {
            // Normalizar datos antes de actualizar
            if (entity.getName() != null) {
                entity.setName(entity.getName().trim());
            }
            if (entity.getCode() != null) {
                entity.setCode(entity.getCode().trim().toUpperCase());
            }
            if (entity.getDescription() != null) {
                entity.setDescription(entity.getDescription().trim());
            }

            entity.setId(id);
            validateTerminalData(entity);

            super.update(id, entity);
        } catch (Exception e) {
            throw new Exception("Error al actualizar la terminal: " + e.getMessage());
        }
    }
}