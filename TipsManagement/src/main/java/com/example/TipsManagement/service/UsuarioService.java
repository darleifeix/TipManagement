package com.example.TipsManagement.service;

import com.example.TipsManagement.Exception.BadRequestException;
import com.example.TipsManagement.Exception.BusinessException;
import com.example.TipsManagement.Exception.NotFoundException;
import com.example.TipsManagement.model.dto.Request.ChangePasswordRequest;
import com.example.TipsManagement.model.dto.Request.EditUsuarioRequest;
import com.example.TipsManagement.model.dto.Request.UsuarioRequest;
import com.example.TipsManagement.model.dto.Response.UsuarioResponse;
import com.example.TipsManagement.model.Usuario;
import com.example.TipsManagement.repository.IUsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class UsuarioService {

    private final IUsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper mapper;

    public UsuarioService(IUsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, ObjectMapper mapper) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    public UsuarioResponse save(UsuarioRequest usuarioRequest){
        if(usuarioRequest.getName().length()<3){
            throw new BadRequestException("Número de caracteres insuficiente (min. 3)");
        }
        if(usuarioRepository.existsByEmail(usuarioRequest.getEmail())) {
            throw new BusinessException("Email já cadastrado.");
        }
        Usuario usuario = new Usuario();
        usuario.setName(usuarioRequest.getName());
        usuario.setEmail(usuarioRequest.getEmail());
        usuario.setPassword(passwordEncoder.encode(usuarioRequest.getPassword()));

        return mapper.convertValue(usuarioRepository.save(usuario), UsuarioResponse.class);
    }

    public List<UsuarioResponse> listAll(){
        return usuarioRepository.findAll()
                .stream()
                .map(usuario -> mapper.convertValue(usuario, UsuarioResponse.class))
                .toList();
    }

    //valida o usuario e realiza alteração de nome e email
    public UsuarioResponse editUsuario(Long userId, EditUsuarioRequest editUsuarioRequest){
        if(editUsuarioRequest.getName().length()<3){
            throw new BadRequestException("Número de caracteres insuficiente (min. 3)");
        }
        Usuario usuario = usuarioRepository.findById(userId).orElseThrow(()-> new NotFoundException("Usuario não encontrado."));
        usuario.setName(editUsuarioRequest.getName());
        usuario.setEmail(editUsuarioRequest.getEmail());
        return mapper.convertValue(usuarioRepository.save(usuario), UsuarioResponse.class);
    }


    //Recebe um usuario, senha atual e nova, valida senha atual recebida com a atual salva em banco e realiza a alteração
    public void changePassword(Long userId, ChangePasswordRequest changePasswordRequest){
        Usuario usuario = usuarioRepository.findById(userId).orElseThrow(()-> new NotFoundException("Usuario não encontrado."));


        boolean correctPassword = passwordEncoder.matches(
                changePasswordRequest.getCurrentPassword(),
                usuario.getPassword()
        );

        if (!correctPassword) {
            throw new BadRequestException("Senha atual incorreta.");
        }

        usuario.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        usuarioRepository.save(usuario);
    }
}
