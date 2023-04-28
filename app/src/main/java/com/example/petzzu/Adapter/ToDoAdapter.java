package com.example.petzzu.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petzzu.AddNewTask;
import com.example.petzzu.Medication;
import com.example.petzzu.Model.ToDoModel;
import com.example.petzzu.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {

    private List<ToDoModel> todolist;
    private Medication medication;
    private FirebaseFirestore firestore;

    public ToDoAdapter(Medication medication1, List<ToDoModel> todolist){
        this.todolist=todolist;
        medication=medication1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(medication).inflate(R.layout.eachtask,parent,false);
        firestore=FirebaseFirestore.getInstance();
        return new MyViewHolder(view);
    }

    public void deleteTask(int position){
        ToDoModel toDoModel=todolist.get(position);
        firestore.collection("task").document(toDoModel.TaskId).delete();
        todolist.remove(position);
        notifyItemRemoved(position);
    }
    public Context getContext(){
        return medication;
    }
    public void editTask(int position){
        ToDoModel toDoModel=todolist.get(position);
        Bundle bundle=new Bundle();
        bundle.putString("task",toDoModel.getTask());
        bundle.putString("due",toDoModel.getDue());
        bundle.putString("id",toDoModel.TaskId);
        AddNewTask addNewTask=new AddNewTask();
        addNewTask.setArguments(bundle);
        addNewTask.show(medication.getSupportFragmentManager(), addNewTask.getTag());
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ToDoModel toDoModel=todolist.get(position);
        holder.mCheckbox.setText(toDoModel.getTask());
        holder.mDueDateTv.setText("Due on" + toDoModel.getDue());

        holder.mCheckbox.setChecked(toBoolean(toDoModel.getStatus()));

        holder.mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    firestore.collection("task").document(toDoModel.TaskId).update("status",1);
                }
                else{
                    firestore.collection("task").document(toDoModel.TaskId).update("status",0);
                }
            }
        });
    }

    private boolean toBoolean(int status){
        return status!=0;
    }

    @Override
    public int getItemCount() {
        return todolist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mDueDateTv;
        CheckBox mCheckbox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mDueDateTv=itemView.findViewById(R.id.due_date_tv);
            mCheckbox=itemView.findViewById(R.id.mcheckbox);

        }
    }
}
