package cz.istep.javatest.controller;

import cz.istep.javatest.data.JavaScriptFramework;
import cz.istep.javatest.repository.JavaScriptFrameworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class JavaScriptFrameworkController {

	private final JavaScriptFrameworkRepository repository;

	@Autowired
	public JavaScriptFrameworkController(JavaScriptFrameworkRepository repository) {
		this.repository = repository;
	}

	@GetMapping("/frameworks")
	public Iterable<JavaScriptFramework> frameworks() {
		return repository.findAll();
	}

	@ResponseBody
	@PostMapping("/frameworks")
	public void addFramework(@Valid @RequestBody JavaScriptFramework javaScriptFramework) {
		repository.save(javaScriptFramework);
	}

	@ResponseBody
	@PostMapping("/frameworks/delete")
	public void deleteFramework(@Valid @RequestBody Long idFramework) {

		Optional<JavaScriptFramework> javaScriptFrameworkOpt = repository.findById(idFramework);
        javaScriptFrameworkOpt.ifPresent(repository::delete);
	}

	@ResponseBody
	@PostMapping("/frameworks/edit")
	public void editFramework(@Valid @RequestBody JavaScriptFramework javaScriptFramework) {
		repository.save(javaScriptFramework);
		Optional<JavaScriptFramework> javaScriptFrameworkOpt = repository.findById(javaScriptFramework.getId());
		if (javaScriptFrameworkOpt.isPresent()) {
			JavaScriptFramework entity = javaScriptFrameworkOpt.get();
			entity.setHypeLevel(javaScriptFramework.getHypeLevel());
			entity.setName(javaScriptFramework.getName());
			entity.setVersionNumber(javaScriptFramework.getVersionNumber());
			repository.save(entity);
		}
	}
}
