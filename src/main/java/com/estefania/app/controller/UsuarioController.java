package com.estefania.app.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estefania.app.entity.Usuario;
import com.estefania.app.service.UsuarioService;


@RestController
@RequestMapping("/api/users")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	
	//Creacion de un nuevo usuario
	@PostMapping
	public ResponseEntity<?> create (@RequestBody Usuario user){
		return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.save(user));
	}
	
	//Leer un usuario
	@GetMapping("/{id}")
	public ResponseEntity<?> read (@PathVariable(value ="id") Long userId){
		Optional<Usuario> oUsuario = usuarioService.findById(userId);
		
		if(!oUsuario.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(oUsuario);
	}
	
	//Actualizar usuario
	@PutMapping("/{id}")
	public ResponseEntity<?> update (@RequestBody Usuario userDetails, @PathVariable(value = "id")Long userid){
		Optional<Usuario> user = usuarioService.findById(userid);
		
		if(!user.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		//BeanUtils.copyProperties(userDetails, user.getClass());
		user.get().setNombre(userDetails.getNombre());
		user.get().setClave(userDetails.getClave());
		user.get().setEmail(userDetails.getEmail());
		user.get().setEstado(userDetails.getEstado());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.save(user.get()));
	}
	
	//eliminar usuario
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete (@PathVariable(value = "id") Long userId){
		if(!usuarioService.findById(userId).isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		usuarioService.deleteById(userId);
		return ResponseEntity.ok().build();
	}
	
	//Leer todos los usuario
	@GetMapping
	public List<Usuario> readAll(){
		List<Usuario> users = StreamSupport
				.stream(usuarioService.findAll().spliterator(),false)
				.collect(Collectors.toList());
		
		return users;
	}
	
}
