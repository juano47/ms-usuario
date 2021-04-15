package ms.usuario.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ms.usuario.dao.ClienteRepository;
import ms.usuario.domain.Cliente;
import ms.usuario.service.ClienteService;

@Service
public class ClienteServiceImpl implements ClienteService{
	@Autowired
	ClienteRepository clienteRepo;
	public Cliente guardarCliente(Cliente c) {
		return this. clienteRepo.save(c);
	}
	@Override
	public Optional<Cliente> buscarPorId(Integer id) {
		return this. clienteRepo.findById(id);
	}
}
