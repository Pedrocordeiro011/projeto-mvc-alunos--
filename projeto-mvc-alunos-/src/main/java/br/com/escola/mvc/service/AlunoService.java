package br.com.escola.mvc.service;

import br.com.escola.mvc.model.Aluno;
import br.com.escola.mvc.repository.AlunoRepository;
import br.com.escola.mvc.repository.MatriculaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {

    private final AlunoRepository repository;
    private final MatriculaRepository matriculaRepository;

    public AlunoService(AlunoRepository repository, MatriculaRepository matriculaRepository) {
        this.repository = repository;
        this.matriculaRepository = matriculaRepository;
    }

    public List<Aluno> listarTodos() {
        return repository.findAll();
    }

    public Aluno buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado: " + id));
    }

    public Aluno salvar(Aluno aluno) {
        validar(aluno);
        return repository.save(aluno);
    }

    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Aluno não encontrado: " + id);
        }

        if (matriculaRepository.existsByAlunoId(id)) {
            throw new IllegalArgumentException(
                    "Não é possível excluir este aluno, pois ele possui matrículas registradas.");
        }

        repository.deleteById(id);
    }

    private void validar(Aluno aluno) {
        if (aluno.getNome() == null || aluno.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome é obrigatório.");
        }

        if (aluno.getEmail() == null || aluno.getEmail().isBlank()) {
            throw new IllegalArgumentException("O e-mail é obrigatório.");
        }

        if (aluno.getRg() == null || aluno.getRg().isBlank()) {
            throw new IllegalArgumentException("O RG é obrigatório.");
        }

        Optional<Aluno> alunoComMesmoRg = repository.findByRg(aluno.getRg());

        if (alunoComMesmoRg.isPresent()
                && (aluno.getId() == null || !alunoComMesmoRg.get().getId().equals(aluno.getId()))) {
            throw new IllegalArgumentException("Já existe um aluno cadastrado com este RG.");
        }
    }
}
