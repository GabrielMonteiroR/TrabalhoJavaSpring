package university.Notas.dto;

import java.time.LocalDate;

public record NotaResponseDTO(Integer id, Integer matriculaId, Integer disciplinaId, Double nota, LocalDate dataLancamento) {}
