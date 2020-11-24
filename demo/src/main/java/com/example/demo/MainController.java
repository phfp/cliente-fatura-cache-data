package com.example.demo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;
import com.google.gson.Gson;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping(path="/demo")
public class MainController {
  @Autowired
  private RepositoryCliente clienteRepository;

  @Autowired
  private RepositoryFatura faturaRepository;


  //--------------- Cliente-------------

  @PostMapping(path="/add/cliente")
  public @ResponseBody Cliente addNovoCliente (@RequestParam String nome
      , @RequestParam String cpf) {

    Cliente cliente = new Cliente();
    cliente.setNome(nome);
    cliente.setCpf(cpf);
    clienteRepository.save(cliente);
    return cliente;
  }


  @GetMapping(path="/busca/cliente")
  public @ResponseBody Optional<Cliente> getCliente(@RequestParam Integer id) {
    return clienteRepository.findById(id);
  }

  @GetMapping(path="/clientes")
  public @ResponseBody Iterable<Cliente> getTodosClientes() {
    return clienteRepository.findAll();
  }

  @PutMapping(path="/update/cliente")
  public @ResponseBody Cliente updateCliente(@RequestParam Integer id, @RequestParam String nome){
    Optional<Cliente> buscaCliente = clienteRepository.findById(id);
		if (!buscaCliente.isPresent())
      throw new IllegalArgumentException();
      
    Cliente cliente = buscaCliente.get();
    cliente.setNome(nome);
    clienteRepository.save(cliente);
    return cliente;
  }

  @DeleteMapping(path="/delete/cliente")
  @CacheEvict(value = "getTodasFaturas", allEntries = true)
	public @ResponseBody Boolean deleteCliente(@RequestParam Integer id) {
		clienteRepository.deleteById(id);
		return true;
	}

  //--------- Fatura ------------

  @PostMapping(path="/add/fatura")
  @CacheEvict(value = "getTodasFaturas", allEntries = true)
  public @ResponseBody Fatura addNovaFatura (
    @RequestParam Integer cliente_id,
    @RequestParam String mesFaturamento,
    @RequestParam Integer leituraAtual) {
   
    Calendar dataFatura = Calendar.getInstance();    

    SimpleDateFormat dataFormato = new SimpleDateFormat("MM-yyyy");
    
    try {         
      dataFatura.setTime(dataFormato.parse(mesFaturamento));
    } catch (ParseException e) {
      e.printStackTrace();
    }
       
    Fatura fatura = new Fatura();

    Optional<Cliente> buscaCliente = clienteRepository.findById(cliente_id);
    if (!buscaCliente.isPresent())
      throw new IllegalArgumentException();
    
    Calendar dataFaturaAnterior = null;
    dataFaturaAnterior = Calendar.getInstance();
    
    dataFaturaAnterior.set(dataFatura.get(Calendar.YEAR), dataFatura.get(Calendar.MONTH)-1 ,1);

    Optional<Fatura> leituraFatAnterior =  faturaRepository.findByMesFaturamento(dataFaturaAnterior);
    if (!leituraFatAnterior.isPresent()){
      fatura.setLeituraAnterior(0);
    }else
      fatura.setLeituraAnterior(leituraFatAnterior.get().getLeituraAtual());

    Cliente cliente = buscaCliente.get();   

    fatura.setCliente(cliente);
    fatura.setMesFaturamento(dataFatura);
    fatura.setLeituraAtual(leituraAtual);
    fatura.setConsumo();
    faturaRepository.save(fatura);
    return fatura;
  }

  @GetMapping(path="/busca/fatura")
  public @ResponseBody Optional<Fatura> getFatura(@RequestParam Integer id) {
    return faturaRepository.findById(id);
  }

  @GetMapping(path="/faturas")
  public @ResponseBody Iterable<Fatura> getFaturasClinte(@RequestParam Integer idCliente) {

    Optional<Cliente> buscaCliente = clienteRepository.findById(idCliente);
    if (!buscaCliente.isPresent()) {
      return null;
    } else {
      Cliente cliente = buscaCliente.get();
      return faturaRepository.findAllByCliente(cliente);
    }
  }

  @PutMapping(path="/update/fatura")
  @CacheEvict(value = "getTodasFaturas", allEntries = true)
  public @ResponseBody Fatura updateFatura(@RequestParam Integer id, @RequestParam Integer leituraAtual ){
    
    Optional<Fatura> buscaFatura = faturaRepository.findById(id);
    if (!buscaFatura.isPresent())
        throw new IllegalArgumentException();

    Fatura fatura = buscaFatura.get();
    Fatura faturaPos;

    Calendar dataFaturaPosterior = null;
    dataFaturaPosterior = Calendar.getInstance();

    dataFaturaPosterior.set(fatura.getMesFaturamento().get(Calendar.YEAR), fatura.getMesFaturamento().get(Calendar.MONTH)+1,1);

    Optional<Fatura> buscaFaturaPosterior = faturaRepository.findByMesFaturamento(dataFaturaPosterior);
    
    if (!buscaFaturaPosterior.isPresent())
      throw new IllegalArgumentException();

      faturaPos = buscaFaturaPosterior.get();
      fatura.setLeituraAtual(leituraAtual);
      fatura.setConsumo();
      faturaPos.setLeituraAnterior(fatura.getLeituraAtual());
      faturaPos.setConsumo();
      faturaRepository.save(fatura);
      return fatura;     
  }

  @DeleteMapping(path="/delete/fatura")
  @CacheEvict(value = "getTodasFaturas", allEntries = true)
	public @ResponseBody Boolean deleteFatura(@RequestParam Integer id) {
    faturaRepository.deleteById(id);
		return true;
  }

  @GetMapping(path="/all/faturas")
  @Cacheable(value = "getTodasFaturas")
  public @ResponseBody String getTodasFaturas() {
    List<Fatura> listaFaturas = new ArrayList<Fatura>();

    faturaRepository.findAll().forEach(listaFaturas::add);

    Gson gson = new Gson();

    String resultado = gson.toJson(listaFaturas);

    System.out.println("Teste caching...");

    return resultado;
  }
}