package com.example.segproject;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class customerViewRequestsAdapter extends RecyclerView.Adapter<customerViewRequestsAdapter.customerRequestViewHolder>{

    private Context context;
    private ArrayList<customerServiceRequests> requestsList;

    public customerViewRequestsAdapter(Context c, ArrayList<customerServiceRequests> requestList){
        this.context = c;
        this.requestsList = requestList;

    }

    public class customerRequestViewHolder extends RecyclerView.ViewHolder{

        TextView serviceNameDisplayCustomer;
        TextView appointmentDateCustomer;
        TextView statusDisplayCustomer;
        TextView branchNameDisplayCustomer;
        ConstraintLayout requestLayoutCustomer;

        public customerRequestViewHolder(@NonNull View itemView){
            super(itemView);

            serviceNameDisplayCustomer = (TextView) itemView.findViewById(R.id.textViewRequestName3);
            appointmentDateCustomer = (TextView) itemView.findViewById(R.id.textViewAppointmentDate3);
            branchNameDisplayCustomer = (TextView) itemView.findViewById(R.id.textViewBranchCustomerDisplay);
            requestLayoutCustomer = itemView.findViewById(R.id.requestLayoutCustomer);
            statusDisplayCustomer = (TextView) itemView.findViewById(R.id.textViewCustomerStatus);
        }



    }

    @NonNull
    @Override
    public customerViewRequestsAdapter.customerRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.requests_layout_row_customer,parent,false);
        return new customerViewRequestsAdapter.customerRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull customerViewRequestsAdapter.customerRequestViewHolder holder, int position) {
        customerServiceRequests request = requestsList.get(position);
        String employeeID = request.getEmployeeID();
        String branchName = request.getBranchName();
        holder.serviceNameDisplayCustomer.setText(request.getServiceName());
        holder.appointmentDateCustomer.setText("For : " + request.getChosenDate());
        holder.statusDisplayCustomer.setText("Status: " + request.getStatus());

        holder.branchNameDisplayCustomer.setText("At branch: " + branchName);


        holder. requestLayoutCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }


        });

    }

    @Override
    public int getItemCount() {
        return requestsList.size();
    }
    

}
