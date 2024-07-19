package com.example.jogodavelha;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jogodavelha.entites.JogadorEntity;
import com.example.jogodavelha.entites.NovoJogoEntity;

import java.util.Objects;

public class JogoDaVelha extends AppCompatActivity implements SensorEventListener {

    private String simboloPrimeiroJogador, simboloSegundoJogador;
    private boolean existeVencedor = false;
    private Integer rodada = 0;
    private Integer pontosPrimeiroJogador = 0;
    private Integer pontosSegundoJogador = 0;
    private String[] jogoVelha = new String[9];
    TextView quadradoA, quadradoB, quadradoC, quadradoD, quadradoE, quadradoF, quadradoG, quadradoH, quadradoI, nomePrimeiroJogador, nomeSegundoJogador;
    EditText edPontosPrimeiroJogador, edPontosSegundoJogador;
    private SensorManager sensorManager;
    private Sensor sensorMovimento;
    private Intent intent;
    MediaPlayer somVitoria;
    Button btReiniciar;
    CharSequence nomeCanal = "MeuCanal";
    String descricao = "Descriçao";
    int importancia = NotificationManager.IMPORTANCE_DEFAULT;

    private JogadorEntity primeiroJogador, segundoJogador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_jogo_da_velha);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        NotificationChannel canal = new NotificationChannel("ID_CANAL", nomeCanal, importancia);
        canal.setDescription(descricao);

        NotificationManager gerenciador = getSystemService(NotificationManager.class);
        gerenciador.createNotificationChannel(canal);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setComponents();
        setSensors();
        setListeners();
        getDataIntent();
        setMediaPlayer();
    }

    private void setComponents() {
        quadradoA = (TextView) findViewById(R.id.quadradoA);
        quadradoB = (TextView) findViewById(R.id.quadradoB);
        quadradoC = (TextView) findViewById(R.id.quadradoC);
        quadradoD = (TextView) findViewById(R.id.quadradoD);
        quadradoE = (TextView) findViewById(R.id.quadradoE);
        quadradoF = (TextView) findViewById(R.id.quadradoF);
        quadradoG = (TextView) findViewById(R.id.quadradoG);
        quadradoH = (TextView) findViewById(R.id.quadradoH);
        quadradoI = (TextView) findViewById(R.id.quadradoI);

        btReiniciar = (Button) findViewById(R.id.btReiniciar);

        nomePrimeiroJogador = (TextView) findViewById(R.id.primeiroJogador);
        nomeSegundoJogador = (TextView) findViewById(R.id.segundoJogador);

        edPontosPrimeiroJogador = (EditText) findViewById(R.id.pontosPrimeiroJogador);
        edPontosSegundoJogador = (EditText) findViewById(R.id.pontosSegundoJogador);
    }

    private void setSensors() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorMovimento = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(this, sensorMovimento, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void setListeners() {
        quadradoA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jogar(quadradoA, 0);
            }
        });

        quadradoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jogar(quadradoB, 1);
            }
        });

        quadradoC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jogar(quadradoC, 2);
            }
        });

        quadradoD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jogar(quadradoD, 3);
            }
        });

        quadradoE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jogar(quadradoE, 4);
            }
        });

        quadradoF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jogar(quadradoF, 5);
            }
        });

        quadradoG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jogar(quadradoG, 6);
            }
        });

        quadradoH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jogar(quadradoH, 7);
            }
        });

        quadradoI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jogar(quadradoI, 8);
            }
        });

        btReiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zerarJogo();
            }
        });
    }

    private void getDataIntent() {
        intent = getIntent();

        if (intent.hasExtra("jogoMultiplayer")) {
            NovoJogoEntity novoJogoEntity = (NovoJogoEntity) intent.getSerializableExtra("jogoMultiplayer");

            primeiroJogador = novoJogoEntity.getPrimeiroJogador();
            segundoJogador = novoJogoEntity.getSegundoJogador();
            simboloPrimeiroJogador = novoJogoEntity.getSimboloPrimeiroJogador();
            simboloSegundoJogador = novoJogoEntity.getSimboloSegundoJogador();

            setNomeJogadores();
        }
    }

    private void setMediaPlayer() {
        somVitoria = MediaPlayer.create(JogoDaVelha.this, R.raw.somvitoria);
    }

    private void setNomeJogadores() {
        nomePrimeiroJogador.setText(primeiroJogador.getNome());
        nomeSegundoJogador.setText(segundoJogador.getNome());

        edPontosPrimeiroJogador.setText(String.valueOf(primeiroJogador.getPontos()));
        edPontosSegundoJogador.setText(String.valueOf(segundoJogador.getPontos()));
    }

    private void jogar(TextView quadrado, Integer posicao) {
        if (quadrado.getText().equals("") && !existeVencedor) {
            if (rodada % 2 == 0) {
                quadrado.setText(simboloPrimeiroJogador);
            } else {
                quadrado.setText(simboloSegundoJogador);
            }
            jogoVelha[posicao] = quadrado.getText().toString();
            verificarJogo();
        }
    }

    private void verificarJogo() {
        verificarHorizontais();
        verificarVerticais();
        verificarDiagonais();

        if (!existeVencedor) {
            rodada++;
        } else {
            somVitoria.start();
        }
    }

    private void verificarHorizontais() {
        if (verificarSeLinhaEstaPreenchida(jogoVelha[0], jogoVelha[1], jogoVelha[2])) {
            if (verificarSeLinhaEhIgual(jogoVelha[0], jogoVelha[1], jogoVelha[2])) {
                declararVencedor(jogoVelha[0]);
            }
        }

        if (verificarSeLinhaEstaPreenchida(jogoVelha[3], jogoVelha[4], jogoVelha[5])) {
            if (verificarSeLinhaEhIgual(jogoVelha[3], jogoVelha[4], jogoVelha[5])) {
                declararVencedor(jogoVelha[3]);
            }
        }

        if (verificarSeLinhaEstaPreenchida(jogoVelha[6], jogoVelha[7], jogoVelha[8])) {
            if (verificarSeLinhaEhIgual(jogoVelha[6], jogoVelha[7], jogoVelha[8])) {
                declararVencedor(jogoVelha[6]);
            }
        }
    }

    private void verificarVerticais() {
        if (verificarSeLinhaEstaPreenchida(jogoVelha[0], jogoVelha[3], jogoVelha[6])) {
            if (verificarSeLinhaEhIgual(jogoVelha[0], jogoVelha[3], jogoVelha[6])) {
                declararVencedor(jogoVelha[0]);
            }
        }

        if (verificarSeLinhaEstaPreenchida(jogoVelha[1], jogoVelha[4], jogoVelha[7])) {
            if (verificarSeLinhaEhIgual(jogoVelha[1], jogoVelha[4], jogoVelha[7])) {
                declararVencedor(jogoVelha[1]);
            }
        }

        if (verificarSeLinhaEstaPreenchida(jogoVelha[2], jogoVelha[5], jogoVelha[8])) {
            if (verificarSeLinhaEhIgual(jogoVelha[2], jogoVelha[5], jogoVelha[8])) {
                declararVencedor(jogoVelha[2]);
            }
        }
    }

    private void verificarDiagonais() {
        if (verificarSeLinhaEstaPreenchida(jogoVelha[0], jogoVelha[4], jogoVelha[8])) {
            if (verificarSeLinhaEhIgual(jogoVelha[0], jogoVelha[4], jogoVelha[8])) {
                declararVencedor(jogoVelha[0]);
            }
        }

        if (verificarSeLinhaEstaPreenchida(jogoVelha[2], jogoVelha[4], jogoVelha[6])) {
            if (verificarSeLinhaEhIgual(jogoVelha[2], jogoVelha[4], jogoVelha[6])) {
                declararVencedor(jogoVelha[2]);
            }
        }
    }

    private void zerarJogo() {
        mostrarNotificacao();
        for (int i = 0; i < jogoVelha.length; i++) {
            jogoVelha[i] = null;
        }
        rodada = 0;
        existeVencedor = false;
        quadradoA.setText("");
        quadradoB.setText("");
        quadradoC.setText("");
        quadradoD.setText("");
        quadradoE.setText("");
        quadradoF.setText("");
        quadradoG.setText("");
        quadradoH.setText("");
        quadradoI.setText("");
    }

    private Boolean verificarSeLinhaEstaPreenchida(String primeiroValor, String segundoValor, String terceiroValor) {
        return Objects.nonNull(primeiroValor) && Objects.nonNull(segundoValor) && Objects.nonNull(terceiroValor);
    }

    private Boolean verificarSeLinhaEhIgual(String primeiroValor, String segundoValor, String terceiroValor) {
        return primeiroValor.equals(segundoValor) && segundoValor.equals(terceiroValor);
    }

    private void declararVencedor(String simboloVencedor) {
        existeVencedor = true;
        setPontos(simboloVencedor);
        Toast.makeText(this, getNomeVencedor(simboloVencedor) + " ganhou!!", Toast.LENGTH_LONG).show();
    }

    private void setPontos(String simboloVencedor) {
        if (simboloVencedor.equals(simboloPrimeiroJogador)) {
            pontosPrimeiroJogador++;
            edPontosPrimeiroJogador.setText(String.format("%d pontos", pontosPrimeiroJogador));
        } else {
            pontosSegundoJogador++;
            edPontosSegundoJogador.setText(String.format("%d pontos", pontosSegundoJogador));
        }
    }

    private String getNomeVencedor(String simboloVencedor) {
        return simboloVencedor.equals(simboloPrimeiroJogador) ? primeiroJogador.getNome() : segundoJogador.getNome();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            if (event.values[0] > 4 || event.values[1] > 4 || event.values[2] > 4) {
                Log.i("SENSOR", String.format("MOVIMENTO => %s", Float.toString(event.values[0])));
                zerarJogo();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @SuppressLint("MissingPermission")
    private void mostrarNotificacao() {
        NotificationCompat.Builder contruidor = new NotificationCompat.Builder(this, "ID_CANAL")
                .setSmallIcon(android.R.drawable.star_on)
                .setContentTitle("TITULO AQUI")
                .setContentText("COnteudo")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat gerenciadorNotificacao = NotificationManagerCompat.from(this);
        gerenciadorNotificacao.notify(1, contruidor.build());
    }
}