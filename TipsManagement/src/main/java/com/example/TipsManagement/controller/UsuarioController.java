package com.example.TipsManagement.controller;

import com.example.TipsManagement.model.LoggedUser;
import com.example.TipsManagement.model.dto.Request.ChangePasswordRequest;
import com.example.TipsManagement.model.dto.Request.UsuarioRequest;
import com.example.TipsManagement.model.dto.Response.UsuarioResponse;
import com.example.TipsManagement.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> saveUsuario(@RequestBody UsuarioRequest usuarioRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.save(usuarioRequest));
    }

    @GetMapping
    public ResponseEntity<Object> listUsuario(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(usuarioService.listAll());
    }
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> editUsuario(@PathVariable Long id,@RequestBody UsuarioRequest usuarioRequest){
        return ResponseEntity.status(HttpStatus.OK)
                .body(usuarioService.edit(id, usuarioRequest));
    }

    @PatchMapping("/password")
    public ResponseEntity<String> editUserPassword(@AuthenticationPrincipal LoggedUser loggedUser, @RequestBody ChangePasswordRequest changePasswordRequest){
        usuarioService.changePassword(loggedUser, changePasswordRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Senha alterada com sucesso.");
    }
}
