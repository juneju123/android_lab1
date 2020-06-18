package com.example.androidlabs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class ChatRoomActivity extends AppCompatActivity {

    private ListView msgListView;

    private EditText userInput;

    private Button send;

    private MsgAdapter adapter;

    private List<Msg> msgList = new ArrayList<Msg>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        msgListView = (ListView)findViewById(R.id.lv) ;
        userInput = findViewById(R.id.userInput);
        adapter = new MsgAdapter();
        msgListView.setAdapter(adapter);
        Button recievedBtn = findViewById(R.id.receiveBtn);
        recievedBtn.setOnClickListener(btn->{
            String content = userInput.getText().toString();
            Msg msg = new Msg(content,true);
            msgList.add(msg);
           adapter.notifyDataSetChanged();
           userInput.setText("");

    });
        Button sendBtn = findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(btn->{
            String content = userInput.getText().toString();
            Msg msg = new Msg(content,false);
            msgList.add(msg);
            adapter.notifyDataSetChanged();
            userInput.setText("");
        });




    }
public class Msg{
        private String content;
        private  boolean type;

        public Msg(String content,boolean type){
            this.content = content;
            this.type = type;
        }
        public String getContent(){
            return content;
        }
        public boolean getType(){
            return type;
        }

}

    class MsgAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return msgList.size();
        }

        @Override
        public Msg getItem(int position) {
            return msgList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View view;
            Msg msg = (Msg)getItem(position);
            if(msg.getType()){//if type is true,then receive message(left side)
                view = inflater.inflate(R.layout.receive_row_layout,parent,false);
                TextView textView = view.findViewById(R.id.receive_msg);
                textView.setText(((Msg) getItem(position)).getContent());
            }else{
                view = inflater.inflate(R.layout.send_row_layout,parent,false);
                TextView textView = view.findViewById(R.id.send_msg);
                textView.setText(((Msg) getItem(position)).getContent());
            }
            return view;
        }
    }
    }




