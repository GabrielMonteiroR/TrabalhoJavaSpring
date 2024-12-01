package university.Professor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/professores")
public class ProfessorController {

    @Autowired
    private ProfessorRepository repository;

    @GetMapping
    public ResponseEntity<List<ProfessorEntity>> findAll() {
        var professores = repository.findAll();
        return professores.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(professores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessorEntity> findById(@PathVariable Integer id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProfessorEntity> create(@RequestBody ProfessorEntity professor) {
        ProfessorEntity saved = repository.save(professor);
        return ResponseEntity.status(201).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessorEntity> update(@PathVariable Integer id, @RequestBody ProfessorEntity professor) {
        return repository.findById(id).map(existing -> {
            existing.setNome(professor.getNome());
            existing.setEmail(professor.getEmail());
            existing.setTelefone(professor.getTelefone());
            existing.setEspecialidade(professor.getEspecialidade());
            repository.save(existing);
            return ResponseEntity.ok(existing);
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
