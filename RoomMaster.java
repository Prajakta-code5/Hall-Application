package com.example.hallapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hallapplication.model.MessageModel;
import com.example.hallapplication.model.buildingTypeModel;
import com.example.hallapplication.model.floorTypeModel;
import com.example.hallapplication.model.roomMasterModel;
import com.example.hallapplication.model.roomTypeModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomMaster extends AppCompatActivity {
    int admin_id = 1;
    String str_buildingtype,str_floor, str_roomtype, str_title, str_ac, str_size,room_rate,room_capacity;

    EditText et_title, et_capacity, et_size, et_rate;
    Spinner sp_building, sp_floor, sp_roomtype, sp_ac;
    Button btn_save, btn_view;
    Context context;
    ArrayAdapter arrayAdapter;
    ListView lst;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_master);
        context = RoomMaster.this;
        init();
    }

    public void init() {

        et_title = findViewById(R.id.et_title);
        et_capacity = findViewById(R.id.et_capacity);
        et_size = findViewById(R.id.et_size);
        et_rate = findViewById(R.id.et_rate);

        sp_ac = findViewById(R.id.sp_ac);
        sp_building = findViewById(R.id.sp_building);
        sp_floor = findViewById(R.id.sp_floor);
        sp_roomtype = findViewById(R.id.sp_roomtype);

        btn_save = findViewById(R.id.btn_save);
        btn_view = findViewById(R.id.btn_show);
        lst = findViewById(R.id.lst);
        selectionFloor();
        selectionRoom();
        selectionBuilding();
        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showRoom();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    insertRoom();
                }

            }
        });
    }
    boolean validation()
    {

        str_buildingtype = sp_building.getSelectedItem().toString().trim();
        str_floor = sp_floor.getSelectedItem().toString().trim();
        str_roomtype = sp_roomtype.getSelectedItem().toString().trim();
        str_ac=sp_ac.getSelectedItem().toString().trim();

        str_title=et_title.getText().toString().trim();
        str_size=et_size.getText().toString().trim();
        room_rate=et_rate.getText().toString().trim();
        room_capacity=et_capacity.getText().toString().trim();

        if(str_buildingtype.equals(""))
        {
            Toast.makeText(context, "Please select the status.", Toast.LENGTH_SHORT).show();
            return false;

        }
        else if (str_floor.equals("")) {

            Toast.makeText(context, "Please select the status.", Toast.LENGTH_SHORT).show();
            return false;

        } else if (str_roomtype.equals("")) {

            Toast.makeText(context, "Please select the status.", Toast.LENGTH_SHORT).show();
            return false;

        } else if (str_title.equals("")) {
            et_title.setError("REQUIRED");
            return false;

        } else if (str_ac.equals("") || str_ac.equals("select")) {

            Toast.makeText(context, "Please select the status.", Toast.LENGTH_SHORT).show();
            return false;

        } else if (room_capacity.equals("")) {
            et_capacity.setError("REQUIRED");
            return false;

        } else if (room_rate.equals("")) {
            et_rate.setError("REQUIRED");
            return false;

        }else if (str_size.equals("")){
            et_size.setError("REQUIRED");
            return false;
        }
        else {
            return true;
        }

    }

    public void insertRoom(){
        try {
            Call<List<MessageModel>> call = RetrofitClient.getInstance().getMyApi().createRoomMaster(str_buildingtype,str_floor,str_roomtype,str_title,str_ac,room_capacity,str_size,room_rate,admin_id);
            call.enqueue(new Callback<List<MessageModel>>() {
                @Override
                public void onResponse(Call<List<MessageModel>> call, Response<List<MessageModel>> response) {
                    List<MessageModel> result=response.body();
                    Toast.makeText(RoomMaster.this,"Saved",Toast.LENGTH_LONG).show();
                    Log.i("Response from",""+result.size());
                    for(MessageModel messageModel:result){
                        Log.i("Data",messageModel.getComment());
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<List<MessageModel>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "An error has occured" + t.getMessage(), Toast.LENGTH_LONG).show();
                }

            });
        }
        catch(Exception e){

        }
    }

    public void showRoom(){

        try {
            retrofit2.Call<List<roomMasterModel>> call = RetrofitClient.getInstance().getMyApi().viewRoomMaster();
            call.enqueue(new Callback<List<roomMasterModel>>() {

                @Override
                public void onResponse(Call<List<roomMasterModel>> call, Response<List<roomMasterModel>> response) {
                    List<roomMasterModel> result=response.body();

                    String[] rooms= new String[result.size()];

                   for(int i=0;i<result.size();i++)
                    {

                        rooms[i]=result.get(i).getRoom_title();
                    }

                    lst.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,rooms));

                }


                @Override
                public void onFailure(Call<List<roomMasterModel>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
                }

            });


        }catch (Exception e)
        {

        }

    }
    public void selectionRoom(){
        try {
            retrofit2.Call<List<roomTypeModel>> call = RetrofitClient.getInstance().getMyApi().viewRoomType();
            call.enqueue(new Callback<List<roomTypeModel>>() {
                @Override
                public void onResponse(Call<List<roomTypeModel>> call, Response<List<roomTypeModel>> response) {
                    List<roomTypeModel> result=response.body();
                    String[] buildings=new String[result.size()];
                    for(int i=0;i<result.size();i++)
                    {
                        buildings[i]=result.get(i).getRoom_type();
                    }
                    sp_roomtype.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,buildings));
                }
                @Override
                public void onFailure(Call<List<roomTypeModel>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){}
    }
    public void selectionBuilding(){
        try {
            retrofit2.Call<List<buildingTypeModel>> call = RetrofitClient.getInstance().getMyApi().viewBuildingType();
            call.enqueue(new Callback<List<buildingTypeModel>>() {
                @Override
                public void onResponse(Call<List<buildingTypeModel>> call, Response<List<buildingTypeModel>> response) {
                    List<buildingTypeModel> result=response.body();
                    String[] buildings=new String[result.size()];
                    for(int i=0;i<result.size();i++)
                    {
                        buildings[i]=result.get(i).getBuilding_type();
                    }
                    sp_building.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,buildings));
                }
                @Override
                public void onFailure(Call<List<buildingTypeModel>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){}
    }
    public void selectionFloor(){
        try {
            retrofit2.Call<List<floorTypeModel>> call = RetrofitClient.getInstance().getMyApi().viewFloorType();
            call.enqueue(new Callback<List<floorTypeModel>>() {
                @Override
                public void onResponse(Call<List<floorTypeModel>> call, Response<List<floorTypeModel>> response) {
                    List<floorTypeModel> result=response.body();
                    String[] buildings=new String[result.size()];
                    for(int i=0;i<result.size();i++)
                    {
                        buildings[i]=result.get(i).getTitle();
                    }
                    sp_floor.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,buildings));
                }
                @Override
                public void onFailure(Call<List<floorTypeModel>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){}
    }

}