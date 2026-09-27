package br.com.escola.mvc.repository;

import br.com.escola.mvc.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {
}
