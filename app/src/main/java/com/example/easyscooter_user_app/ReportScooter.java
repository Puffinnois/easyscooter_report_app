package com.example.easyscooter_user_app;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReportScooter extends AppCompatActivity {

    private Connection connection;
    private final String host = "practicephoenixazure.postgres.database.azure.com";
    private final String database = "easy_scooter";
    private final int port = 5432;
    private final String user = "practiceadmin%40practicephoenixazure";
    private final String pwd = "Respons11";
    private String url = "jdbc:postgresql://%s:%d:%s";
    private boolean status;

    public void Database(){
        this.url = String.format(this.url, this.host, this.port, this.database);
        connect();

    }

    private Connection connect()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    Class.forName("org.postgresql.Driver");
                    connection = DriverManager.getConnection(url, user, pwd);
                    status = true;
                }
                catch (Exception e)
                {
                    status = false;
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try
        {
            thread.join();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            this.status = false;
        }
        return null;
    }

    public int updateScooterStatus(String id) {
        String SQL = "UPDATE Scooter "
                + "SET scooter_status = BROKEN "
                + "WHERE id = ?";

        int affectedrows = 0;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(2, id);

            affectedrows = pstmt.executeUpdate();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return affectedrows;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_scooter);

        TextView scooter_id =(TextView) findViewById(R.id.scooter_id);

        MaterialButton submitbtn = (MaterialButton) findViewById(R.id.submitbtn);

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (scooter_id.getText().toString().equals("id")){
                    updateScooterStatus(scooter_id.getText().toString());
                    Toast.makeText(ReportScooter.this, "SUBMIT SUCCESSFUL", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(ReportScooter.this, "WRONG SCOOTER ID",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
