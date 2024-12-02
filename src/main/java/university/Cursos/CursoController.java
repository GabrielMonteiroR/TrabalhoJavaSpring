package university.Cursos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import university.Cursos.dto.CursoCreateDTO;
import university.Cursos.dto.CursoResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    @Autowired
    private CursoRepository repository;

    @GetMapping
    public ResponseEntity<List<CursoResponseDTO>> findAll() {
        var cursos = repository.findAll();
        if (cursos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<CursoResponseDTO> cursoDTOs = cursos.stream()
                .map(curso -> new CursoResponseDTO(
                        curso.getId(),
                        curso.getNome(),
                        curso.getCodigo(),
                        curso.getCargaHoraria()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(cursoDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoResponseDTO> findById(@PathVariable Integer id) {
        return repository.findById(id)
                .map(curso -> new CursoResponseDTO(
                        curso.getId(),
                        curso.getNome(),
                        curso.getCodigo(),
                        curso.getCargaHoraria()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CursoResponseDTO> create(@RequestBody CursoCreateDTO cursoDTO) {
        CursoEntity curso = new CursoEntity();
        curso.setNome(cursoDTO.nome());
        curso.setCodigo(cursoDTO.codigo());
        curso.setCargaHoraria(cursoDTO.cargaHoraria());

        CursoEntity saved = repository.save(curso);

        CursoResponseDTO responseDTO = new CursoResponseDTO(
                saved.getId(),
                saved.getNome(),
                saved.getCodigo(),
                saved.getCargaHoraria()
        );

        return ResponseEntity.status(201).body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CursoResponseDTO> update(@PathVariable Integer id, @RequestBody CursoCreateDTO cursoDTO) {
        return repository.findById(id).map(existing -> {
            existing.setNome(cursoDTO.nome());
            existing.setCodigo(cursoDTO.codigo());
            existing.setCargaHoraria(cursoDTO.cargaHoraria());
            CursoEntity updated = repository.save(existing);

            CursoResponseDTO responseDTO = new CursoResponseDTO(
                    updated.getId(),
                    updated.getNome(),
                    updated.getCodigo(),
                    updated.getCargaHoraria()
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
