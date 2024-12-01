package university.Turmas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/turmas")
public class TurmaController {

    @Autowired
    private TurmaRepository repository;

    @GetMapping
    public ResponseEntity<List<TurmaEntity>> findAll() {
        var turmas = repository.findAll();
        return turmas.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(turmas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TurmaEntity> findById(@PathVariable Integer id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TurmaEntity> create(@RequestBody TurmaEntity turma) {
        TurmaEntity saved = repository.save(turma);
        return ResponseEntity.status(201).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TurmaEntity> update(@PathVariable Integer id, @RequestBody TurmaEntity turma) {
        return repository.findById(id).map(existing -> {
            existing.setAno(turma.getAno());
            existing.setSemestre(turma.getSemestre());
            existing.setCurso(turma.getCurso());
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
