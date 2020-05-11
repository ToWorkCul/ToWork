package com.culproject.towork;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class tabServicesList extends Fragment {

    Service[] services = {new Service(), new Service(), new Service(), new Service(), new Service(), new Service()};

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_services_list, container, false);

        ListView serviceListView =  (ListView) view.findViewById(R.id.servicesListView);

        CustomAdapter customAdapter = new CustomAdapter();
        serviceListView.setAdapter(customAdapter);

        return view;
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return services.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("ResourceType")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.service_card_layout, null);
            TextView textViewServiceName = (TextView) convertView.findViewById(R.id.textViewServiceName);
            TextView textViewDescripcion = (TextView) convertView.findViewById(R.id.textViewServiceDescription);

            textViewServiceName.setText("Hardware(Fisico)");
            textViewDescripcion.setText(services[position].getDescription());

            return convertView;
        }
    }

}
