package com.example.demo.Service;

import com.example.demo.Model.Cliente;
import com.example.demo.Model.Endereco;
import com.example.demo.Repository.ClienteRepository;
import com.example.demo.Repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class ClienteSevice {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private ViaCepService viaCepService;

    public Iterable<Cliente> buscarTodos(){
        return clienteRepository.findAll();
    }

    public Cliente buscarPorId(Integer id) {
        // Buscar Cliente por ID.
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.get();
    }


    public void inserir(Cliente cliente){
        //verificar se endereço existe
        String auxiliar = cliente.getEndereco().getCep();
        Endereco endereco = enderecoRepository.findById(auxiliar).orElseGet(()->{
            //caso não exista ( .orElseGet(()->{}) ) integrar com viaCep e persistir o retorno
            Endereco novoEnd = viaCepService.consultarCep(auxiliar);
           return novoEnd;
        });
        cliente.setEndereco(endereco);
        //inserir cliente vinculando endereço (novo ou existente)
        clienteRepository.save(cliente);
    }

    public void atualizar(Integer id, Cliente cliente) {
        //busca cliente por id caso exista
        Optional<Cliente> cliente1 = clienteRepository.findById(id);
        if (cliente1.isPresent()) {
            //verificar se endereço ja existe pelo cep
            String aux = cliente.getEndereco().getCep();
            Endereco end = enderecoRepository.findById(aux).orElseGet(() -> {
                //caso não exista integrar com viaCep e persistir o retorno
                Endereco novoEnd = viaCepService.consultarCep(aux);
                return novoEnd;
            });
            cliente.setEndereco(end);
            //inserir cliente vinculando endereço (novo ou existente)
            clienteRepository.save(cliente);
        }
    }

    public void deletar(Integer id){
        clienteRepository.deleteById(id);
    }


}
