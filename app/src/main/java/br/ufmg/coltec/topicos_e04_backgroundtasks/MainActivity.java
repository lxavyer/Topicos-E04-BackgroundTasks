package br.ufmg.coltec.topicos_e04_backgroundtasks;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;


import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button downloadBtn = findViewById(R.id.btn_download);
        EditText txtLink = findViewById(R.id.txt_img_link);
        ImageView imgView = findViewById(R.id.img_picture);
        ProgressBar progressBar = findViewById(R.id.progress_bar);

        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // mostra a barra de progresso
                progressBar.setVisibility(View.VISIBLE);

                // tira a imagem durante o download
                imgView.setVisibility(View.GONE);
                String imageUrl = txtLink.getText().toString();

                // cria a nova thread para o download
                new Thread(new Runnable() {
                @Override
                public void run(){
                    try {
                        // tenta fazer o  download
                        Bitmap imagem = ImageDownloader.download(imageUrl);
                        if(imagem != null) {
                            // seta a imagem no layout e mostra
                            imgView.setImageBitmap(imagem);
                            imgView.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        } else {
                            Log.e("MainActivity", "Nao foi possivel baixar a imagem");
                        }
                    } catch (IOException e) {
                        Log.e("MainActivity", "Nao foi possivel baixar a imagem: " + e.toString());

                        // esconde a barra de progresso se nao der certo
                        runOnUiThread(new Runnable() {
                        @Override
                            public void run() {
                            progressBar.setVisibility(View.GONE);
                        }
                        });
                    }
                }
                }).start();
            }
        });
    }

    private Bitmap downloadImage(String imgLink) {
        try {
            return ImageDownloader.download(imgLink);
        } catch (IOException e) {
            Log.e("MainActivity", e.toString());
            return null;
        }
    }
}