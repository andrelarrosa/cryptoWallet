package com.crypto.wallet.model;

import java.sql.Timestamp;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Moeda {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nome;
	private double valor;
	private String alias;
	private boolean ativo;
	private Timestamp dataCadastro;

	
}
