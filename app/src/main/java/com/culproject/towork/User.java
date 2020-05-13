package com.culproject.towork;

import com.google.firebase.database.DataSnapshot;

import org.json.JSONException;

public class User {

        private String id = "";
        private String name = "";
        private String pass = "";
        private String role = "";


        public User(DataSnapshot data) throws JSONException {
                id = data.child("id").getValue().toString();
                name = data.child("name").getValue().toString();
                pass = data.child("pass").getValue().toString();
                role = data.child("role").getValue().toString();
        }

        public String getId() { return id;}

        public String getName() { return name;}

        public String getPass() { return pass;}

        public String getRole() { return role;}



        }
