package com.culproject.towork;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class CreateRequestActivity extends AppCompatActivity {

    private Spinner spnProblemType;
    private Spinner spnProblemSector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);

        spnProblemType = (Spinner) findViewById(R.id.problemTypeRequestSpinner);
        spnProblemSector = (Spinner) findViewById(R.id.problemSectorRequestSpinner);

        ArrayAdapter<CharSequence> problemSectorAdapter = ArrayAdapter.createFromResource(this, R.array.proplem_sector_array, android.R.layout.simple_spinner_item);
        problemSectorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> problemSectorTypeAdapter = ArrayAdapter.createFromResource(this, R.array.problem_sector_compute_array, android.R.layout.simple_spinner_item);
        problemSectorTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnProblemType.setAdapter(problemSectorAdapter);
        spnProblemSector.setAdapter(problemSectorTypeAdapter);
    }
}
