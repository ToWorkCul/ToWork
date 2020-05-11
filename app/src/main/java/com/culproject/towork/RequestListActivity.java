package com.culproject.towork;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class RequestListActivity extends AppCompatActivity {

    Request[] requests = {new Request(), new Request(), new Request(), new Request(), new Request(), new Request()};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);

        ListView requestListView = (ListView)findViewById(R.id.requestListView);

        CustomAdapter customAdapter = new CustomAdapter();
        requestListView.setAdapter(customAdapter);


    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return requests.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.request_card_layout, null);
            TextView textViewSector = (TextView) convertView.findViewById(R.id.textViewSector);
            TextView textViewTipo = (TextView) convertView.findViewById(R.id.textViewTipo);
            TextView textViewDescripcion = (TextView) convertView.findViewById(R.id.textViewServiceDescripcion);
            TextView textViewPrecio = (TextView) convertView.findViewById(R.id.textViewPrecio);

            textViewSector.setText("Equipos de Computos");
            textViewTipo.setText("Hardware(Fisico)");
            textViewDescripcion.setText(requests[position].getDescription());
            textViewPrecio.setText(requests[position].getPrice());

            return convertView;
        }
    }
}
