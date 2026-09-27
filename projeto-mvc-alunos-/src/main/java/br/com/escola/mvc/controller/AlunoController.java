package br.com.escola.mvc.controller;

import br.com.escola.mvc.model.Aluno;
import br.com.escola.mvc.service.AlunoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoService service;

    public AlunoController(AlunoService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("alunos", service.listarTodos());
        return "alunos/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        if (!model.containsAttribute("aluno")) {
            model.addAttribute("aluno", new Aluno());
        }
        return "alunos/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        if (!model.containsAttribute("aluno")) {
            model.addAttribute("aluno", service.buscarPorId(id));
        }
        return "alunos/formulario";
    }

    @PostMapping("/salvar")
    public String salvar(Aluno aluno, RedirectAttributes redirectAttributes) {
        try {
            service.salvar(aluno);
            redirectAttributes.addFlashAttribute("sucesso", "Aluno salvo com sucesso.");
            return "redirect:/alunos";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            redirectAttributes.addFlashAttribute("aluno", aluno);
            String destino = aluno.getId() == null
                    ? "/alunos/novo"
                    : "/alunos/editar/" + aluno.getId();
            return "redirect:" + destino;
        }
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            service.excluir(id);
            redirectAttributes.addFlashAttribute("sucesso", "Aluno excluído com sucesso.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/alunos";
    }
}
