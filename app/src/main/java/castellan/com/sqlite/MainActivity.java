package castellan.com.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayAdapter<String> adaptador;
    ArrayList<String> itensTarefa;
    ArrayList<Integer> idTarefa;
    private ListView lista_tarefas;
    private EditText tarefa;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tarefa = findViewById(R.id.etTarefa);
        //btAtualizar = findViewById(R.id.btAtualizar);
        lista_tarefas = findViewById(R.id.lwTarefas);
        db = openOrCreateDatabase("tarefas", MODE_PRIVATE, null);
        itensTarefa = new ArrayList<>();
        idTarefa = new ArrayList<>();

        try {
            //db.execSQL("DROP TABLE TAREFA;");
            db.execSQL("CREATE TABLE IF NOT EXISTS TAREFA(ID INTEGER PRIMARY KEY AUTOINCREMENT, NOME VARCHAR(50));");

        } catch (Exception e) {
            System.out.println(e.getMessage() + "\nOnCreate");
        } finally {
            get();
        }

        lista_tarefas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(MainActivity.this, String.valueOf(idTarefa.get(i)), Toast.LENGTH_SHORT).show();
                remover(idTarefa.get(i));
                get();
            }
        });

    }

    public void save(View v) {
        String tf;
        try {
            if (tarefa.getText().toString().isEmpty())
                Toast.makeText(this, "Campo vazio", Toast.LENGTH_SHORT).show();

            else {
                tf = tarefa.getText().toString();
                db.execSQL("INSERT INTO TAREFA VALUES(NULL,'" + tf + "') ");
                Toast.makeText(this, tf + " adicionado", Toast.LENGTH_SHORT).show();
                get();
                tarefa.setText("");
            }
        } catch (SQLiteException e) {
            System.out.println(e.getMessage() + "\nOnSave");
        }
    }

    private void get() {
        try {

            //itensTarefa.set(0,"");

            Cursor c = db.rawQuery("SELECT * FROM TAREFA", null);

            int indiceColumn = c.getColumnIndex("NOME");
            int idColumn = c.getColumnIndex("ID");

            adaptador = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_2,
                    android.R.id.text1, itensTarefa);

            c.moveToFirst();

            while (c != null) {
                String nome = c.getString(indiceColumn);
                String id = c.getString(idColumn);
                //System.out.println(nome);
                itensTarefa.add(nome);
                idTarefa.add(Integer.parseInt(id));
                lista_tarefas.setAdapter(adaptador);
                c.moveToNext();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\nOnGet");
        }
    }

    public void refresh(View view) {
        get();
    }

    private void remover(Integer i) {
        try {
            db.execSQL("DELETE FROM TAREFA WHERE ID = " + i);
            get();
        } catch (Exception e) {

            System.out.println(e.getMessage() + "\nOnDelete");
        }
    }

}
