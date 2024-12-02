package university.Professor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import university.Professor.dto.ProfessorCreateDTO;
import university.Professor.dto.ProfessorResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/professores")
public class ProfessorController {

    @Autowired
    private ProfessorRepository repository;

    @GetMapping
    public ResponseEntity<List<ProfessorResponseDTO>> findAll() {
        var professores = repository.findAll();
        if (professores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<ProfessorResponseDTO> professorDTOs = professores.stream()
                .map(professor -> new ProfessorResponseDTO(
                        professor.getId(),
                        professor.getNome(),
                        professor.getEmail(),
                        professor.getTelefone(),
                        professor.getEspecialidade()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(professorDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessorResponseDTO> findById(@PathVariable Integer id) {
        return repository.findById(id)
                .map(professor -> new ProfessorResponseDTO(
                        professor.getId(),
                        professor.getNome(),
                        professor.getEmail(),
                        professor.getTelefone(),
                        professor.getEspecialidade()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProfessorResponseDTO> create(@RequestBody ProfessorCreateDTO professorDTO) {
        if (repository.existsByEmail(professorDTO.email())) {
            return ResponseEntity.status(409).build();
        }

        ProfessorEntity professor = new ProfessorEntity();
        professor.setNome(professorDTO.nome());
        professor.setEmail(professorDTO.email());
        professor.setTelefone(professorDTO.telefone());
        professor.setEspecialidade(professorDTO.especialidade());

        ProfessorEntity saved = repository.save(professor);

        ProfessorResponseDTO responseDTO = new ProfessorResponseDTO(
                saved.getId(),
                saved.getNome(),
                saved.getEmail(),
                saved.getTelefone(),
                saved.getEspecialidade()
        );

        return ResponseEntity.status(201).body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessorResponseDTO> update(@PathVariable Integer id, @RequestBody ProfessorCreateDTO professorDTO) {
        return repository.findById(id).map(existing -> {
            existing.setNome(professorDTO.nome());
            existing.setEmail(professorDTO.email());
            existing.setTelefone(professorDTO.telefone());
            existing.setEspecialidade(professorDTO.especialidade());
            ProfessorEntity updated = repository.save(existing);

            ProfessorResponseDTO responseDTO = new ProfessorResponseDTO(
                    updated.getId(),
                    updated.getNome(),
                    updated.getEmail(),
                    updated.getTelefone(),
                    updated.getEspecialidade()
            );

            return ResponseEntity.ok(responseDTO);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
