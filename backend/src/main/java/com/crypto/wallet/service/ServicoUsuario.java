package com.crypto.wallet.service;

import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.crypto.wallet.model.Usuario;
import com.crypto.wallet.exception.BadResourceException;
import com.crypto.wallet.exception.ResourceAlreadyExistsException;
import com.crypto.wallet.exception.ResourceNotFoundException;
import com.crypto.wallet.repository.RepositorioUsuario;

@Service
public class ServicoUsuario {
	
	@Autowired
	private RepositorioUsuario repositorioUsuario;
	
	private boolean existsById(Long id) {
		return repositorioUsuario.existsById(id);
	}
	
	public Usuario findById(Long id) throws ResourceNotFoundException{
		Usuario usuario = repositorioUsuario.findById(id).orElse(null);
		if(usuario==null) {
			throw new ResourceNotFoundException("Usuário não encontrado com o id: "+id);
		}else return usuario;
	}
	
	public Page<Usuario> findAll(Pageable pageable){
		return repositorioUsuario.findAll(pageable);
	}
	
	public Page<Usuario> findAllByCpf(String cpf, Pageable page){
		Page<Usuario>usuarios = repositorioUsuario.findByCpf(cpf,page);
		return usuarios;
	}
	
	public Usuario save(Usuario usuario) throws BadResourceException,ResourceAlreadyExistsException{
		if(!StringUtils.isEmpty(usuario.getCpf())) {
			if(usuario.getId() != null && existsById(usuario.getId())) {
				throw new ResourceAlreadyExistsException("Usuario com id: "+ usuario.getId()+"já existe.");
			}
			return repositorioUsuario.save(usuario);
		}
		else {
			BadResourceException exc =  new BadResourceException("Erro ao salvar usuário");
			exc.addErrorMessages("Usuário está vazio ou é nulo");
			throw exc;
		}
	}
	
	public void update(Usuario usuario) throws BadResourceException, ResourceNotFoundException{
		if(!StringUtils.isEmpty(usuario.getCpf())) {
			if(!existsById(usuario.getId())) {
				throw new ResourceNotFoundException("Usuário não encontrado com o id: "+usuario.getId());
			}
			repositorioUsuario.save(usuario);
		}
		else {
			BadResourceException  exc = new BadResourceException ("Falha ao salvar usuário");
			exc.addErrorMessages("Usuário está nulo ou em branco");
			throw exc;
		}
	}
	public void deleteById(Long id) throws ResourceNotFoundException{
		if(!existsById(id)) {
			throw new  ResourceNotFoundException("usuario não encontrado com o id: "+id);
		}
		else {
			repositorioUsuario.deleteById(id);
		}
	}
	
	public Long count(){
		return repositorioUsuario.count();
	}
}
