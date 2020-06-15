package com.example.androidlabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class ChatRoomActivity extends AppCompatActivity {

    ArrayList<String> userInputFiled = new ArrayList<>();
    MsgAdapter msgAdapter = new MsgAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        EditText userInput = findViewById(R.id.userInput);


        Button recievedBtn = findViewById(R.id.receiveBtn);
        recievedBtn.setOnClickListener(btn->{
            userInputFiled.add(userInput.getText().toString());
            msgAdapter.notifyDataSetChanged();

    });
        ListView myListView = findViewById(R.id.lv);
        myListView.setAdapter(msgAdapter);


    }


    class MsgAdapter extends BaseAdapter {

        @Override
        public int getCount() {

            return userInputFiled.size();
        }

        @Override
        public Object getItem(int position) {


            return userInputFiled.get(position) ;
        }

        @Override
        public long getItemId(int position) {
            return (long)position;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View newView = findViewById(R.id.goToChat);
            if(newView.getId() == R.id.receiveBtn){
            inflater.inflate(R.layout.receive_row_layout, parent, false );
            TextView theText = newView.findViewById(R.id.receiveTV);
            theText.setText((String)getItem(position));
            return newView;
        }else{
                inflater.inflate(R.layout.send_row_layout, parent, false );
                TextView theText = newView.findViewById(R.id.sendTV);
                theText.setText((String)getItem(position));
                return newView;
            }

        }
    }
}