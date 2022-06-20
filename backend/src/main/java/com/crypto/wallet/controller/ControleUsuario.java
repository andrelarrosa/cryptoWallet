package com.crypto.wallet.controller;

import java.net.URI;
import java.net.URISyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.crypto.wallet.model.Usuario;
import com.crypto.wallet.exception.BadResourceException;
import com.crypto.wallet.exception.ResourceAlreadyExistsException;
import com.crypto.wallet.exception.ResourceNotFoundException;
import com.crypto.wallet.service.ServicoUsuario;

@RestController
@RequestMapping("/api")
public class ControleUsuario {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ServicoUsuario servicoUsuario;

	@GetMapping(value = "/usuario", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<Usuario>> findAll(
			@RequestBody(required = false) String nome, String cpf, 
			Pageable pageable) {
		if (StringUtils.isEmpty(nome)) {
			return ResponseEntity.ok(servicoUsuario.findAll(pageable));
		} else {
			return ResponseEntity.ok(servicoUsuario.findAllByNome(nome, pageable));
		}
	}
	
	@GetMapping(value = "/usuario/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Usuario> findUsuarioById(@PathVariable long id) {
		try {
			Usuario usuario = servicoUsuario.findById(id);
			return ResponseEntity.ok(usuario);
		} catch (ResourceNotFoundException ex) {
			logger.error(ex.getMessage());

			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
		}
	}

	@PostMapping(value = "/usuario")
	public ResponseEntity<Usuario> addUsuario(@RequestBody Usuario usuario) throws URISyntaxException {
		try {
			Usuario novoUsuario = servicoUsuario.save(usuario);
			return ResponseEntity.created(new URI("/api/usuario/" + novoUsuario.getId())).body(usuario);
		} catch (ResourceAlreadyExistsException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		} catch (BadResourceException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	@PutMapping(value = "/usuario/{id}")
	public ResponseEntity<Usuario> updateUsuario (@RequestBody Usuario usuario, @PathVariable long id) {
		try {
			usuario.setId(id);
			servicoUsuario.update(usuario);
			return ResponseEntity.ok().build();
		} catch (ResourceNotFoundException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.notFound().build();
		} catch (BadResourceException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	@DeleteMapping(path = "/usuario/{id}")
	public ResponseEntity<Void> deleteUsuarioById(@PathVariable long id) {
		try {
			servicoUsuario.deleteById(id);
			return ResponseEntity.ok().build();
		} catch (ResourceNotFoundException ex) {
			logger.error(ex.getMessage());
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
		}
	}
}