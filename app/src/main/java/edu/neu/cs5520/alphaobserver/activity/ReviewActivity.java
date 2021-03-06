package edu.neu.cs5520.alphaobserver.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import edu.neu.cs5520.alphaobserver.R;
import edu.neu.cs5520.alphaobserver.adapter.ReviewAdapter;
import edu.neu.cs5520.alphaobserver.adapter.StockAdaptor;
import edu.neu.cs5520.alphaobserver.fragment.NoticeDialogFragment;
import edu.neu.cs5520.alphaobserver.model.StockCard;
import edu.neu.cs5520.alphaobserver.model.StockReview;
import edu.neu.cs5520.alphaobserver.model.StockSave;


public class ReviewActivity extends AppCompatActivity implements NoticeDialogFragment.NoticeDialogListener{
    private DatabaseReference mDatabase;
    private String currentUser;
    private String stockSymbol;
    //private String stockName;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ReviewAdapter stockAdaptor;
    private List<StockReview> reviewList;

    FloatingActionButton addButton;

    @Override
    public void onConfirmPositiveClick(DialogFragment dialog, String comments) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Review").child(stockSymbol).push();
        Task t = dbRef.setValue(new StockReview(currentUser, stockSymbol, comments, System.currentTimeMillis()));
        if (t.isSuccessful()) {
            Toast.makeText(ReviewActivity.this, "ADD_STOCK_COMMENT_SUCCESS", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onConfirmNegativeClick(DialogFragment dialog) {

    }
    public void AddButtonClicked(View v) {
        DialogFragment dialog = new NoticeDialogFragment();
        dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Bundle data = getIntent().getExtras();
        currentUser = data.getString("USER_NAME");
        stockSymbol = data.getString("STOCK_SYMBOL");
        //stockName = data.getString("STOCK_NAME");

        TextView stockSymbolText = findViewById(R.id.stockNSymbol);
        stockSymbolText.setText(stockSymbol);

        addButton = findViewById(R.id.addButton);

        mDatabase = FirebaseDatabase.getInstance().getReference();
       // DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Review").child(stockSymbol).push();
       // Task t = myRef.setValue(new StockReview(currentUser, stockSymbol, "This stock is good! \nThis stock is good!", System.currentTimeMillis()));
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Review").child(stockSymbol);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reviewList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    StockReview stockReview = snapshot.getValue(StockReview.class);
                    reviewList.add(stockReview);
                }
                createRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
    private void createRecyclerView() {
        layoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.review_recycler_view);
        recyclerView.setHasFixedSize(true);
        stockAdaptor = new ReviewAdapter(reviewList);
        recyclerView.setAdapter(stockAdaptor);
        recyclerView.setLayoutManager(layoutManager);
    }


}