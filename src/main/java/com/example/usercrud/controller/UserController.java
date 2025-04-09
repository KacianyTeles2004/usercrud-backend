@PostMapping("/register")
public ResponseEntity<?> registrarCliente(@RequestBody @Valid UserDTO userDTO) {


        if (userRepository.existsByEmail(userDTO.getEmail())) {
        return ResponseEntity.badRequest().body("Email já cadastrado.");
        }


        if (userRepository.existsByCpf(userDTO.getCpf())) {
        return ResponseEntity.badRequest().body("CPF já cadastrado.");
        }


        if (!viaCepService.isCepValido(userDTO.getCep())) {
        return ResponseEntity.badRequest().body("CEP inválido.");
        }


        String senhaEncriptada = passwordEncoder.encode(userDTO.getSenha());


        User novoUsuario = new User();
        novoUsuario.setNome(userDTO.getNomeCompleto());
        novoUsuario.setEmail(userDTO.getEmail());
        novoUsuario.setCpf(userDTO.getCpf());
        novoUsuario.setDataNascimento(userDTO.getDataNascimento());
        novoUsuario.setGenero(userDTO.getGenero());
        novoUsuario.setSenha(senhaEncriptada);


        userRepository.save(novoUsuario);

        return ResponseEntity.ok("Cadastro realizado com sucesso!");
        }
@PutMapping("/update")
public ResponseEntity<?> atualizarCliente(@RequestBody @Valid UserDTO userDTO, Principal principal) {

        String email = principal.getName();
        User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));


        user.setNome(userDTO.getNomeCompleto());
        user.setDataNascimento(userDTO.getDataNascimento());
        user.setGenero(userDTO.getGenero());


        if (userDTO.getSenha() != null && !userDTO.getSenha().isBlank()) {
        String novaSenha = passwordEncoder.encode(userDTO.getSenha());
        user.setSenha(novaSenha);
        }



        userRepository.save(user);

        return ResponseEntity.ok("Dados atualizados com sucesso!");
        }

