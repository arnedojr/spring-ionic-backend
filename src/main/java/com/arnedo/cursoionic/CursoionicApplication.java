package com.arnedo.cursoionic;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.arnedo.cursoionic.domain.Categoria;
import com.arnedo.cursoionic.domain.Cidade;
import com.arnedo.cursoionic.domain.Cliente;
import com.arnedo.cursoionic.domain.Endereco;
import com.arnedo.cursoionic.domain.Estado;
import com.arnedo.cursoionic.domain.Pagamento;
import com.arnedo.cursoionic.domain.PagamentoComBoleto;
import com.arnedo.cursoionic.domain.PagamentoComCartao;
import com.arnedo.cursoionic.domain.Pedido;
import com.arnedo.cursoionic.domain.Produto;
import com.arnedo.cursoionic.domain.enums.EstadoPagamento;
import com.arnedo.cursoionic.domain.enums.TipoCliente;
import com.arnedo.cursoionic.repositories.CategoriaRepository;
import com.arnedo.cursoionic.repositories.CidadeRepository;
import com.arnedo.cursoionic.repositories.ClienteRepository;
import com.arnedo.cursoionic.repositories.EnderecoRepository;
import com.arnedo.cursoionic.repositories.EstadoRepository;
import com.arnedo.cursoionic.repositories.PagamentoRepository;
import com.arnedo.cursoionic.repositories.PedidoRepository;
import com.arnedo.cursoionic.repositories.ProdutoRepository;


@SpringBootApplication
public class CursoionicApplication implements CommandLineRunner {

	@Autowired
	private CategoriaRepository categoriaRepository;
	@Autowired
	private ProdutoRepository produtoRepository;
	@Autowired
	private EstadoRepository estadoRepository;
	@Autowired
	private CidadeRepository cidadeRepository;
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private PedidoRepository pedidoRepository;
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	
	public static void main(String[] args) {
		SpringApplication.run(CursoionicApplication.class, args); 
	}

	@Override
	public void run(String... args) throws Exception {
		Categoria cat1 = new Categoria(null, "Informática X");
		Categoria cat2 = new Categoria(null, "Escritório");
		
		Produto p1 = new Produto(null, "Computador", 2000.00);
		Produto p2 = new Produto(null, "Impressora", 800.00);
		Produto p3 = new Produto(null, "Mouse", 80.00);
		
		cat1.getProdutos().addAll(Arrays.asList(p1, p2, p3));
		cat2.getProdutos().addAll(Arrays.asList(p2));
		
		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1, cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));
		
		categoriaRepository.saveAll(Arrays.asList(cat1, cat2));
		produtoRepository.saveAll(Arrays.asList(p1, p2, p3));
		
		Estado est1 = new Estado(null, "Minas Gerais");
		Estado est2 = new Estado(null, "São Paulo");
		
		Cidade c1 = new Cidade(null, "Uberlândia", est1);
		Cidade c2 = new Cidade(null, "São Paulo", est2);
		Cidade c3 = new Cidade(null, "Campinas", est2);
		
		est1.setCidades(Arrays.asList(c1));
		est2.setCidades(Arrays.asList(c2, c3));
		
		// o estado pode ter varias cidades ou nenhuma... deve ser salvo primeiro
		// a cidade é dependente de um Estado criado, logo deverá ser salva depois 
		estadoRepository.saveAll(Arrays.asList(est1, est2));
		cidadeRepository.saveAll(Arrays.asList(c1, c2, c3));
		
		//como o endereço depende de Cliente e Cidade, aquela entidade deve ser instanciada por último
		//
		Cliente cli1 = new Cliente(null, "Maria Silva", "maria@gmail.com", "36378912377", TipoCliente.PESSOAFISICA);
		cli1.getTelefones().addAll(Arrays.asList("273663323", "93838393"));
		
		Endereco e1 = new Endereco(null, "Rua Flores", "300", "Apto 203", "Jardim", "38220834", cli1, c1);
		
		Endereco e2 = new Endereco(null, "Av Matos", "105", "Sala 800", "Centro", "38777012", cli1, c2);
		
		cli1.getEnderecos().addAll(Arrays.asList(e1, e2));
		
		//primeiro salvar a entidade independente
		clienteRepository.saveAll(Arrays.asList(cli1));
		enderecoRepository.saveAll(Arrays.asList(e1, e2));
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		// por razoes práticas, será desvinculada a instancia de pedido com pagamento, para 
		// facilitar a construção do Pedido
		Pedido ped1 = new Pedido (null, sdf.parse("30/09/2017 10:32"), cli1, e1);
		Pedido ped2 = new Pedido (null, sdf.parse("10/10/2017 19:35"), cli1, e2);
		
		Pagamento pagto1 = new PagamentoComCartao(null, EstadoPagamento.QUITADO, ped1, 6);
		ped1.setPagamento(pagto1);
		
		Pagamento pagto2 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, ped2, 
				sdf.parse("20/10/2017 00:00"), null);
		ped2.setPagamento(pagto2);
		
		cli1.getPedidos().addAll(Arrays.asList(ped1, ped2));
		
		//salvar primeiro o pedido
		pedidoRepository.saveAll(Arrays.asList(ped1, ped2));
		
		pagamentoRepository.saveAll(Arrays.asList(pagto1, pagto2));
		
		
	}

}
