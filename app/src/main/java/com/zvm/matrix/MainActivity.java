package com.zvm.matrix;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity {

    public String path = "";
    private int counter = 0;

    private TextView tvPath;
    private TextView tvData;
    private TextView tvData2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(v -> addToPath(0));
        findViewById(R.id.button2).setOnClickListener(v -> addToPath(1));
        findViewById(R.id.button3).setOnClickListener(v -> addToPath(2));
        findViewById(R.id.button4).setOnClickListener(v -> addToPath(3));

        tvPath = findViewById(R.id.tvPath);
        tvData = findViewById(R.id.tvData);
        tvData2 = findViewById(R.id.tvData2);
    }

    private void addToPath(int i) {
        if (path.equals("")) {
            path += ""+i;
        } else {
            path += "-"+i;
        }
        tvPath.setText(path);
        counter++;
        if (counter == 5) {
            findViewById(R.id.button).setClickable(false);
            findViewById(R.id.button2).setClickable(false);
            findViewById(R.id.button3).setClickable(false);
            findViewById(R.id.button4).setClickable(false);
            new Thread(new Loader(path, this)).start();
        }
    }

    public void display(Matrix matrix) {

        findViewById(R.id.button).setClickable(true);
        findViewById(R.id.button2).setClickable(true);
        findViewById(R.id.button3).setClickable(true);
        findViewById(R.id.button4).setClickable(true);

        StringBuilder builder = new StringBuilder();
        StringBuilder builder2 = new StringBuilder();
        Map<String, Integer[]> map = matrix.getRows();

        for (Map.Entry<String, Integer[]> entry : map.entrySet()) {
            builder.append(entry.getKey())
                    .append(" ")
                    .append(Arrays.toString(entry.getValue()).replaceAll("\\[", "(").replaceAll("]", ")"))
                    .append("\n");

            Integer[] ints = entry.getValue();
            int x = (ints[0] + ints[3] + ints[7] + ints [11])/4;
            builder2.append(entry.getKey())
                    .append(": через рік ")
                    .append(x)
                    .append("\n");
        }

        tvData.setText(builder.toString());
        tvData2.setText(builder2.toString());
    }

    static class Loader implements Runnable {

        private final String path;
        private final MainActivity mainActivity;

        public Loader(String path, MainActivity mainActivity) {
            this.path = path;
            this.mainActivity = mainActivity;
        }

        @Override
        public void run() {

            try {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("https://matrix-method.herokuapp.com/matrix?path="+path)
                        .build();

                String response = client.newCall(request).execute().body().string();
                Matrix matrix = new Gson().fromJson(response, Matrix.class);

                mainActivity.runOnUiThread(() -> {
                    mainActivity.display(matrix);
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}