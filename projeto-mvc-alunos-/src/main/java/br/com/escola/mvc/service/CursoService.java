package br.com.escola.mvc.service;

import br.com.escola.mvc.model.Curso;
import br.com.escola.mvc.repository.CursoRepository;
import br.com.escola.mvc.repository.MatriculaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CursoService {

    private final CursoRepository repository;
    private final MatriculaRepository matriculaRepository;

    public CursoService(CursoRepository repository, MatriculaRepository matriculaRepository) {
        this.repository = repository;
        this.matriculaRepository = matriculaRepository;
    }

    public List<Curso> listarTodos() {
        return repository.findAll();
    }

    public Curso buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Curso não encontrado: " + id));
    }

    public Curso salvar(Curso curso) {
        validar(curso);
        return repository.save(curso);
    }

    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Curso não encontrado: " + id);
        }

        if (matriculaRepository.existsByCursoId(id)) {
            throw new IllegalArgumentException(
                    "Não é possível excluir este curso, pois existem matrículas vinculadas a ele.");
        }

        repository.deleteById(id);
    }

    private void validar(Curso curso) {
        if (curso.getNome() == null || curso.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome do curso é obrigatório.");
        }

        if (curso.getCargaHoraria() == null || curso.getCargaHoraria() <= 0) {
            throw new IllegalArgumentException("A carga horária deve ser maior que zero.");
        }
    }
}
