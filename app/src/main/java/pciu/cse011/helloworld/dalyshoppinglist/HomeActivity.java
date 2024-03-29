package pciu.cse011.helloworld.dalyshoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;
import java.util.zip.Inflater;

import pciu.cse011.helloworld.dalyshoppinglist.Model.Data;

public class HomeActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FloatingActionButton fab_btn;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Daly Shopping List");

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        String uId = mUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("shopping List").child(uId);
        mDatabase.keepSynced(true);


        recyclerView = findViewById(R.id.recycler_home);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);



        fab_btn = findViewById(R.id.fab);
        fab_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog();
            }
        });
    }
    private void customDialog(){
        AlertDialog.Builder mydialog = new AlertDialog.Builder(HomeActivity.this);
        LayoutInflater inflater= LayoutInflater.from(HomeActivity.this);
        final View myview = inflater.inflate(R.layout.input_data,null);
        final AlertDialog dialog = mydialog.create();

        final EditText type = myview.findViewById(R.id.edt_type);
        final EditText amount = myview.findViewById(R.id.edt_amount);
        final EditText note = myview.findViewById(R.id.edt_note);
        Button btnsave = myview.findViewById(R.id.btn_save);

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mType = type.getText().toString().trim();
                String mAmount = amount.getText().toString().trim();
                String mNote = note.getText().toString().trim();
                Button btnsave = myview.findViewById(R.id.btn_save);

                int ammint = Integer.parseInt(mAmount);


                if (TextUtils.isEmpty(mType)){
                    type.setError("Required Field..");
                    return;
                }
                if (TextUtils.isEmpty(mAmount)){
                    amount.setError("Required Field..");
                    return;
                }
                if (TextUtils.isEmpty(mNote)){
                    note.setError("Required Field..");
                    return;
                }

                String id= mDatabase.push().getKey();
                String mdate = DateFormat.getDateInstance().format(new Date());
                Data data = new Data(mType,ammint,mNote,mdate,id);
                mDatabase.child(id).setValue(data);
                Toast.makeText(getApplicationContext(),"Data Add",Toast.LENGTH_SHORT).show();


                dialog.dismiss();
            }
        });

        dialog.setView(myview);

        dialog.show();


    }
    public static class MyviewHolder extends RecyclerView.ViewHolder{

        View myview;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            myview=itemView;
        }
        public void setType (String type){
            TextView mtype = myview.findViewById(R.id.type);
            mtype.setText(type);
        }
        public void setNote (String note){
            TextView mNote = myview.findViewById(R.id.note);
            mNote.setText(note);
        }
        public void setDate (String date){
            TextView mDate = myview.findViewById(R.id.date);
            mDate.setText(date);
        }
        public void setAmount(String amount){
            TextView mAmount= myview.findViewById(R.id.amount);
            String stm = String.valueOf(amount);
            mAmount.setText(stm);

        }
    }




}
