package com.example.jogodavelha;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jogodavelha.entites.JogadorEntity;
import com.example.jogodavelha.entites.NovoJogoEntity;

public class Jogador extends AppCompatActivity {

    private EditText edPrimeiroJogador, edSegundoJogador;
    private Button btIniciar;
    private Intent telaJogoVelha;
    private NovoJogoEntity novoJogoEntity;
    RadioGroup selecaoSimboloInicial;
    String simboloPrimeiroJogador, simboloSegundoJogador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_jogador);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        setIntents();
        setComponents();
        setListeners();
    }

    private void setIntents() {
        telaJogoVelha = new Intent(this, JogoDaVelha.class);
    }

    private void setComponents() {
        edPrimeiroJogador = (EditText) findViewById(R.id.edPrimeiroJogador);
        edSegundoJogador = (EditText) findViewById(R.id.edSegundoJogador);
        btIniciar = (Button) findViewById(R.id.btIniciar);
        selecaoSimboloInicial = (RadioGroup) findViewById(R.id.selecaoSimbolo);
    }

    private void setListeners() {
        btIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDadosNovoJogo();
                iniciarJogo();
            }
        });

        selecaoSimboloInicial.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.opcaoX) {
                    simboloPrimeiroJogador = "X";
                    simboloSegundoJogador = "O";
                } else {
                    simboloPrimeiroJogador = "O";
                    simboloSegundoJogador = "X";
                }
            }
        });
    }

    private void setDadosNovoJogo() {
        String nomePrimeiroJogador = edPrimeiroJogador.getText().toString();
        String nomeSegundoJogador = edSegundoJogador.getText().toString();

        JogadorEntity primeiroJogador = new JogadorEntity(nomePrimeiroJogador, 0);
        JogadorEntity segundoJogador = new JogadorEntity(nomeSegundoJogador, 0);

        novoJogoEntity = new NovoJogoEntity(primeiroJogador, segundoJogador, simboloPrimeiroJogador, simboloSegundoJogador);

    }

    private void iniciarJogo() {
        telaJogoVelha.putExtra("jogoMultiplayer", novoJogoEntity);
        startActivity(telaJogoVelha);
    }
}