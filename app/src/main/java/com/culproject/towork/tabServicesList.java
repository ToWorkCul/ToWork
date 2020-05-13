package com.culproject.towork;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class tabServicesList extends Fragment {

    private FirebaseAuth firebaseAuth;
    private Map<String, Object> usersData;


    Service[] services = {};

    ArrayList<Service> serviceList = new ArrayList<Service>();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_services_list, container, false);

        ListView serviceListView =  (ListView) view.findViewById(R.id.servicesListView);

        CustomAdapter customAdapter = new CustomAdapter();
        serviceListView.setAdapter(customAdapter);

        firebaseAuth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("services");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot data) {
                Log.w("TAG", "services ---->" + data.toString());

                for (DataSnapshot childDataSnapshot : data.getChildren()) {
                    //Service services = childDataSnapshot.getValue(Service.class);
                    //JSONObject jo = new JSONObject();
                    Log.w("TAG", "services ---->" + childDataSnapshot.getKey());
                    Log.w("TAG", "services ---->" + childDataSnapshot.child("name").getValue());
                    Log.w("TAG", "services ---->" + childDataSnapshot.getChildren().toString());

                    try {
                        serviceList.add(new Service(childDataSnapshot));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "Cancelado", databaseError.toException());
                // TODO: Manejar escenario cuando falle
            }
        };
        myRef.addValueEventListener(postListener);
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return serviceList.toArray().length;
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

            Service[] ts = {};
            services = serviceList.toArray(ts);
            textViewServiceName.setText(services[position].getName());
            textViewDescripcion.setText(services[position].getDescription());

            return convertView;
        }
    }

}
