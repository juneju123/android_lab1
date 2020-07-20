package com.example.androidlabs;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.net.IDN;
import java.util.ArrayList;
import java.util.List;


public class ChatRoomActivity extends AppCompatActivity {

    private ListView msgListView;

    private EditText userInput;

    private Button send;

    private MsgAdapter adapter;

    private List<Msg> msgList = new ArrayList<Msg>();

    private SQLiteDatabase db;
    public static final String MSG = "MESSAGE";
    public static final String MSG_ID = "POSITION";
    public static final String MSG_SEND_RECIEVE = "SEND_RECIEVE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        msgListView = (ListView)findViewById(R.id.lv) ;
        userInput = findViewById(R.id.userInput);
        boolean isTablet = findViewById(R.id.fragmentLocation) != null;
        adapter = new MsgAdapter();
        msgListView.setAdapter(adapter);
        loadDataFromDatabase();
        Button recievedBtn = findViewById(R.id.receiveBtn);
        recievedBtn.setOnClickListener(btn->{
            String content = userInput.getText().toString();
            ContentValues newRowValue = new ContentValues();
            newRowValue.put(MyOpener.MESSAGE_COL,content);
            newRowValue.put(MyOpener.IS_RECIEVED_COL,1);
            long newId = db.insert(MyOpener.TABLE_NAME,null,newRowValue);
            Msg msg = new Msg(content,true,newId);
            msgList.add(msg);
           adapter.notifyDataSetChanged();
           userInput.setText("");

    });
        Button sendBtn = findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(btn->{
            String content = userInput.getText().toString();
            ContentValues newRowValue = new ContentValues();
            newRowValue.put(MyOpener.MESSAGE_COL,content);
            newRowValue.put(MyOpener.IS_RECIEVED_COL,0);
            long newId = db.insert(MyOpener.TABLE_NAME,null,newRowValue);
            Msg msg = new Msg(content,false,newId);
            msgList.add(msg);
            adapter.notifyDataSetChanged();
            userInput.setText("");
        });


        msgListView.setOnItemClickListener((list, item, position, id) -> {
            //Create a bundle to pass data to the new fragment
            Bundle dataToPass = new Bundle();
            dataToPass.putString(MSG, msgList.get(position).getContent() );
            dataToPass.putLong(MSG_ID, id);
           if(msgList.get(position).isRecieved()){
               dataToPass.putString(MSG_SEND_RECIEVE,getResources().getString(R.string.isRecievedMSG));
           }else {
               dataToPass.putString(MSG_SEND_RECIEVE,"Is a SEND message");
           }

            if(isTablet)
            {
                DetailsFragment dFragment = new DetailsFragment(); //add a DetailFragment
                dFragment.setArguments( dataToPass ); //pass it a bundle for information
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                        .commit(); //actually load the fragment. Calls onCreate() in DetailFragment
            }
            else //isPhone
            {

                Intent nextActivity = new Intent(ChatRoomActivity.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivity(nextActivity); //make the transition
            }
        });
            msgListView.setOnItemLongClickListener((parent, view, position, id) -> {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Do you want to delete this?").setMessage("The select row is "+ position +
                " \n id in the database is "+ id).setPositiveButton("Yes",(click,arg)->{
                   db.delete(MyOpener.TABLE_NAME,MyOpener.COL_ID + "=?",new String[]{Long.toString(msgList.get(position).getId())});
                    msgList.remove(position);
                    adapter.notifyDataSetChanged();
            if (isTablet) {
                if (getSupportFragmentManager().findFragmentById(R.id.fragmentLocation) != null)
                    getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.fragmentLocation)).commit();
            }

        }).setNegativeButton("No",(click,arg)->{

        }).create().show();
        return true;
    });




    }
    private void printCursor( Cursor c, int version){
        String TAG = "printCursor";
        Log.i(TAG, "The database version number: "+Integer.toString(db.getVersion()));
        Log.i(TAG,"The number of columns in the cursor: "+ Integer.toString(c.getColumnCount()));
        for(int i = 0; i < c.getColumnCount(); i++){
        Log.i(TAG,"The name of the columns in the cursor: " + c.getColumnName(i));}
        Log.i(TAG,"The number of rows in the cursor: " + Integer.toString(c.getCount()));
        Log.i(TAG,DatabaseUtils.dumpCursorToString(c));


    }

    private void loadDataFromDatabase() {
        MyOpener dbOpener = new MyOpener(this);
        db = dbOpener.getWritableDatabase();

        String[] columns = {MyOpener.COL_ID, MyOpener.MESSAGE_COL, MyOpener.IS_RECIEVED_COL};
        Cursor results = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        int msgColIndex = results.getColumnIndex(MyOpener.MESSAGE_COL);
        int isRecievedColIndx = results.getColumnIndex(MyOpener.IS_RECIEVED_COL);
        int idColIndx = results.getColumnIndex(MyOpener.COL_ID);

        while (results.moveToNext()){
            String msg = results.getString(msgColIndex);
           boolean isRecievedMsg = results.getInt(isRecievedColIndx) == 1? true : false;
            long id = results.getLong(idColIndx);
            msgList.add(new Msg(msg,isRecievedMsg,id));

//            if (recieveMsg != null)
//            msgList.add(new Msg(recieveMsg,true,id));
//            if(sendMsg != null)
//            msgList.add(new Msg(sendMsg,false,id));
        }
        printCursor( results,1);
    }

    public class Msg{
        private String content;
        private  boolean isRecieved;
        private long id;

        public Msg(String content,boolean isRecieved,long id){
            this.content = content;
            this.isRecieved = isRecieved;
            this.id = id;
        }
        public Msg(String content,boolean isRecieved){
            this(content,isRecieved,0);
        }
        public String getContent(){
            return content;
        }
        public boolean isRecieved(){
            return isRecieved;
        }
        public long getId(){return id;}

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
            return getItem(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View view;
            Msg msg = getItem(position);
            if(msg.isRecieved()){//if type is true,then receive message(left side)
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




