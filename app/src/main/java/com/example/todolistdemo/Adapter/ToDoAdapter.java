package com.example.todolistdemo.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistdemo.AddNewTask;
import com.example.todolistdemo.MainActivity;
import com.example.todolistdemo.R;
import com.example.todolistdemo.Utils.DataBaseHelper;
import com.example.todolistdemo.model.ToDoModel;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {

    private List<ToDoModel> mList;
    private MainActivity activity;
    private DataBaseHelper myDB;

    public ToDoAdapter(DataBaseHelper myDB,MainActivity activity){
        this.activity=activity;
        this.myDB=myDB;
    }

    @NonNull
    @Override
    public ToDoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoAdapter.MyViewHolder holder, int position) {
         final ToDoModel item = mList.get(position);
         holder.mCheckBox.setText(item.getTask());
         holder.mCheckBox.setChecked(toBoolean(item.getStatus()));
         holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                 if (isChecked){
                     myDB.updateStatus(item.getId(),1);
                 }
                 else{
                     myDB.updateStatus(item.getId(),0);
                 }
             }
         });
    }

    public boolean toBoolean(int num) {
        return num!=0;
    }

    public Context getContext()
    {
        return activity;
    }

    public void setTasks(List<ToDoModel> mList){
        this.mList=mList;
        notifyDataSetChanged();
    }

    public void deleteTasks(int position){
        ToDoModel item = mList.get(position);
        myDB.deleteTask(item.getId());
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position){
        ToDoModel item = mList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id",item.getId());
        bundle.putString("task",item.getTask());

        AddNewTask task = new AddNewTask();
        task.setArguments(bundle);
        task.show(activity.getSupportFragmentManager(),task.getTag());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox mCheckBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.mcheckbox);
        }
    }
}
