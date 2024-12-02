package university.Notas.dto;

import java.time.LocalDate;

public record NotaCreateDTO(Integer matriculaId, Integer disciplinaId, Double nota, LocalDate dataLancamento) {}
