package castellan.com.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayAdapter<String> adaptador;
    ArrayList<String> itensTarefa;
    private ListView lista_tarefas;
    private EditText tarefa;
    private SQLiteDatabase db;
    private Button btAtualizar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tarefa = findViewById(R.id.etTarefa);
        btAtualizar = findViewById(R.id.btAtualizar);
        db = openOrCreateDatabase("tarefas", MODE_PRIVATE, null);

        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS TAREFA(NOME VARCHAR(50));");

        } catch (Exception e) {
            System.out.println(e.getMessage() + "\nOnCreate");
        }

        btAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get();
            }
        });

    }

    public void save(View v) {
        String tf = "";
        try {
            if (tarefa.getText().toString().isEmpty())
                Toast.makeText(this, "Campo vazio", Toast.LENGTH_SHORT).show();

            else {
                tf = tarefa.getText().toString();
                db.execSQL("INSERT INTO TAREFA VALUES('" + tf + "') ");
                Toast.makeText(this, "Deu certo\n" + tf + " adicionado", Toast.LENGTH_SHORT).show();
                get();
                tarefa.setText("");
            }
        } catch (SQLiteException e) {
            System.out.println(e.getMessage() + "\nOnSave");
        }
    }

    private void get() {
        try {
            itensTarefa = new ArrayList<>();
            lista_tarefas = findViewById(R.id.lwTarefas);
            Cursor c = db.rawQuery("SELECT * FROM TAREFA", null);

            int indiceColumn = c.getColumnIndex("NOME");

            adaptador = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,
                    android.R.id.text1, itensTarefa);

            c.moveToFirst();

            while (c != null) {
                String nome = c.getString(indiceColumn);
                System.out.println(nome);
                itensTarefa.add(nome);
                c.moveToNext();
                lista_tarefas.setAdapter(adaptador);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\nOnGet");
        }
    }

}
