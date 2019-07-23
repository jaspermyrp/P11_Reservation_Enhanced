package sg.edu.rp.c346.reservation;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText etName;
    EditText etTelephone;
    EditText etSize;
    EditText datePicker, timePicker;
    CheckBox checkBox;
    Button btReserve;
    Button btReset;

    Button btnRetrieveData, btnDeleteData;

    String date = "";
    String time = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setTitle("Reservation");

        etName = findViewById(R.id.editTextName);
        etTelephone = findViewById(R.id.editTextTelephone);
        etSize = findViewById(R.id.editTextSize);
        datePicker = findViewById(R.id.etDatePicker);
        timePicker = findViewById(R.id.etTimePicker);
        checkBox = findViewById(R.id.checkBox);
        btReserve = findViewById(R.id.buttonReserve);
        btReset = findViewById(R.id.buttonReset);
        btnRetrieveData = findViewById(R.id.btnRetrieveData);
        btnDeleteData = findViewById(R.id.btnDeleteData);

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        date = dayOfMonth + "/" + (month + 1) + "/" + year;
                        datePicker.setText(date);
                    }
                };

                Calendar date = Calendar.getInstance();
                DatePickerDialog dateDialog = new DatePickerDialog(MainActivity.this, dateListener, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
                dateDialog.show();
            }
        });

        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time = hourOfDay + ":" + minute;
                        timePicker.setText(time);
                    }
                };

                Calendar time = Calendar.getInstance();
                TimePickerDialog timeDialog = new TimePickerDialog(MainActivity.this, timeListener, time.get(Calendar.HOUR), time.get(Calendar.MINUTE), true);
                timeDialog.show();
            }
        });

        btReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkIfNull() == false) {
                    Toast.makeText(MainActivity.this, "You need to fill up all fields.", Toast.LENGTH_SHORT).show();
                }else{
                    String isSmoke = "";
                    if (checkBox.isChecked()) {
                        isSmoke = "smoking";
                    } else {
                        isSmoke = "non-smoking";
                    }

                    String message = "New Reservation \n" +
                            "Name: " + etName.getText().toString() + "\n" +
                            "Telephone: " + etTelephone.getText().toString() + "\n" +
                            "Smoking: " + isSmoke + "\n" +
                            "Size: " + etSize.getText().toString() + "\n" +
                            "Date: " + date + "\n" +
                            "Time: " + time;

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Confirm your Reservation");
                    builder.setMessage(message);
                    builder.setNeutralButton("Cancel", null);
                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            saveData();
                            Toast.makeText(MainActivity.this, "Reservation saved.", Toast.LENGTH_SHORT).show();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }
        });

        btnRetrieveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                String name = prefs.getString("Name", "");
                String telephone = prefs.getString("Telephone", "");
                String isSmoking = prefs.getString("Smoking", "");
                String size = prefs.getString("Size", "");
                String date = prefs.getString("Date", "");
                String time = prefs.getString("Time", "");
                String saved = prefs.getString("UserSaved", "");

                Toast.makeText(MainActivity.this, "Displaying last Reservation...", Toast.LENGTH_SHORT).show();

                etName.setText(name);
                etTelephone.setText(telephone);
                etSize.setText(size);

                if (saved.equals("Yes")) {
                    if (isSmoking.equals("smoking")) {
                        checkBox.setChecked(true);
                    } else {
                        checkBox.setChecked(false);
                    }

                    datePicker.setText(date);
                    timePicker.setText(time);
                } else {
                    Toast.makeText(MainActivity.this, "No records to show.", Toast.LENGTH_SHORT).show();
                }


            }
        });

        btnDeleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData();
            }
        });

        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etName.setText(null);
                etTelephone.setText(null);
                etSize.setText(null);
                checkBox.setChecked(false);
                datePicker.setText("");
                timePicker.setText("");
            }
        });
    }

    private void saveData() {
        String isSmoking = "";
        if (checkBox.isChecked()) {
            isSmoking = "smoking";
        } else {
            isSmoking = "non-smoking";
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString("Name", etName.getText().toString()).putString("Telephone", etTelephone.getText().toString()).putString("Smoking", isSmoking).putString("Size", etSize.getText().toString()).putString("Date", date).putString("Time", time).putString("UserSaved", "Yes");
        prefsEditor.commit();
    }

    private void clearData() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString("Name", "").putString("Telephone", "").putString("Smoking", "").putString("Size", "").putString("Date", "").putString("Time", "").putString("UserSaved", "Deleted");
        prefsEditor.commit();
    }

    public boolean checkIfNull() {
        if (etName.getText().toString().trim().isEmpty()) {
            return false;
        }

        if (etTelephone.getText().toString().trim().isEmpty()) {
            return false;
        }

        if (etSize.getText().toString().trim().isEmpty()) {
            return false;
        }

        if (date.equals("")) {
            return false;
        }

        if (time.equals("")) {
            return false;
        }

        return true;
    }
}