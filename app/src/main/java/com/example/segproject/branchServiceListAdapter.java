package com.example.segproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CheckBox;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class branchServiceListAdapter extends RecyclerView.Adapter<branchServiceListAdapter.branchViewHolder>{

    private Context context;

    private ArrayList<Services> branchServiceList;
    private String employeeID;
    ArrayList<Services> desiredServices = new ArrayList<Services>();

    public branchServiceListAdapter(Context c, ArrayList<Services> services, String id, ArrayList<Services> desiredServices){
        this.context = c;
        this.branchServiceList = services;
        employeeID = id;
        this.desiredServices = desiredServices;

    }

    public class branchViewHolder extends RecyclerView.ViewHolder{

        private TextView nameOfService;
        ConstraintLayout branchServiceLayout;
        CheckBox addService;
        public branchViewHolder(@NonNull View itemView) {
            super(itemView);
            addService = (CheckBox) itemView.findViewById(R.id.checkBoxaddservice);

            nameOfService = (TextView) itemView.findViewById(R.id.textViewServiceName2);
            branchServiceLayout = itemView.findViewById(R.id.branchServiceLayout);
        }
    }


    @NonNull
    @Override
    public branchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.branchservicerow,parent,false);
        return new branchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull branchViewHolder holder, int position) {
        Services service = branchServiceList.get(position);
        holder.nameOfService.setText(service.getServiceName());

        // check services already added later



        holder.branchServiceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ServiceInfoDisplay.class);
                intent.putExtra("name", service.getServiceName());
                intent.putExtra("form", service.getStringForm());
                intent.putExtra("documents", service.getStringDocuments());
                context.startActivity(intent);
            }
        });

        holder.addService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.addService.isChecked()){
                    desiredServices.add(branchServiceList.get(position));
                }
                else{
                    desiredServices.remove(branchServiceList.get(position));
                }

               // Toast.makeText(context, "Service Removed", Toast.LENGTH_SHORT).show();

                //Toast.makeText(context, "Service Added", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return branchServiceList.size();
    }

    public ArrayList<Services> getDesiredServices(){
        return desiredServices;


    }




}
