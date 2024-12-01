package university.Matricula;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    @Autowired
    private MatriculaRepository repository;

    @GetMapping
    public ResponseEntity<List<MatriculaEntity>> findAll() {
        var matriculas = repository.findAll();
        return matriculas.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(matriculas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatriculaEntity> findById(@PathVariable Integer id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MatriculaEntity> create(@RequestBody MatriculaEntity matricula) {
        MatriculaEntity saved = repository.save(matricula);
        return ResponseEntity.status(201).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MatriculaEntity> update(@PathVariable Integer id, @RequestBody MatriculaEntity matricula) {
        return repository.findById(id).map(existing -> {
            existing.setAluno(matricula.getAluno());
            existing.setTurma(matricula.getTurma());
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
