package com.crypto.wallet.model;

import java.sql.Timestamp;
import java.util.ArrayList;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Carteira {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nome;
	private int usuario;
	private boolean ativo;
	private Timestamp dataCadastro;
}
