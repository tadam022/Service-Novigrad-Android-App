package com.example.segproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class requestListAdapter extends RecyclerView.Adapter<requestListAdapter.employeeRequestViewHolder>{

    private Context context;
    private ArrayList<customerServiceRequests> requestList;
    private String employeeID;

    public requestListAdapter(Context c, ArrayList<customerServiceRequests> requestList, String employeeID){
        this.context = c;
        this.requestList = requestList;
        this.employeeID = employeeID;
    }

    public class employeeRequestViewHolder extends RecyclerView.ViewHolder{

        TextView serviceNameDisplayer;
        TextView appointmentDate;
        TextView statusDisplay;
        ConstraintLayout requestLayoutEmployee;
        Button acceptRequest;
        Button denyRequest;
        public employeeRequestViewHolder(@NonNull View itemView){
            super(itemView);
            serviceNameDisplayer = (TextView) itemView.findViewById(R.id.textViewRequestName);
            appointmentDate = (TextView) itemView.findViewById(R.id.textViewAppointmentDate);
            requestLayoutEmployee = itemView.findViewById(R.id.requestLayoutEmployee);
            acceptRequest = (Button) itemView.findViewById(R.id.buttonAcceptRequest);
            denyRequest = (Button) itemView.findViewById(R.id.buttonDenyRequest);
            statusDisplay = (TextView) itemView.findViewById(R.id.textViewStatus);
        }

    }

    @NonNull
    @Override
    public requestListAdapter.employeeRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.requests_layout_row_employee,parent,false);
        return new requestListAdapter.employeeRequestViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull requestListAdapter.employeeRequestViewHolder holder, int position) {
        customerServiceRequests request = requestList.get(position);
        String customerID = request.getCustomerID();
        holder.serviceNameDisplayer.setText(request.getServiceName());
        holder.appointmentDate.setText("For : " + request.getChosenDate());
        holder.statusDisplay.setText("Status: " + request.getStatus());

        if(request.getStatus() == serviceRequestStatus.DENIED){
            holder.acceptRequest.setEnabled(true);
            holder.denyRequest.setEnabled(false);
        }
        else if(request.getStatus() == serviceRequestStatus.ACCEPTED){
            holder.acceptRequest.setEnabled(false);
            holder.denyRequest.setEnabled(true);
        }
        else{ // if PENDING
            holder.acceptRequest.setEnabled(true);
            holder.denyRequest.setEnabled(true);
        }

        holder.requestLayoutEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, serviceRequestInfoDisplay.class);
                intent.putExtra("firstName", request.getFirstName());
                intent.putExtra("lastName", request.getLastName());
                intent.putExtra("licenseType", request.getLicenseType());
                intent.putExtra("address", request.getAddress());
                intent.putExtra("chosenDate", request.getChosenDate());
                intent.putExtra("dob", request.getDOB());
                intent.putExtra("status",request.getStatus().name());
                intent.putExtra("uID", employeeID);
                context.startActivity(intent);
            }


        });


        holder.acceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Request Accepted", Toast.LENGTH_SHORT).show();
                updateRequest(true, customerID, request);

            }
        });

        holder.denyRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Request Rejected", Toast.LENGTH_SHORT).show();
                updateRequest(false, customerID, request);

            }
        });

    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public void updateRequest(boolean accepted, String customerID, customerServiceRequests request){
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        root.child("Users").child(employeeID).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    Employee e = snapshot.getValue(Employee.class);
                    Branch b = e.getBranch();
                    try{
                        for(int i = 0; i < b.getServiceRequests().size(); i++){
                            if(b.getServiceRequests().get(i).equals(request)){
                                if(accepted){
                                    root.child("Users").child(employeeID).child("branch").child("serviceRequests").child(String.valueOf(i)).child("status").
                                            setValue(serviceRequestStatus.ACCEPTED);
                                }
                                else{
                                    root.child("Users").child(employeeID).child("branch").child("serviceRequests").child(String.valueOf(i)).child("status").
                                            setValue(serviceRequestStatus.DENIED);
                                }
                                break; // dont update multiple customer requests
                            }
                        }
                    }
                    catch(NullPointerException exception){
                        Toast.makeText(context,"Could not find service request", Toast.LENGTH_SHORT).show();
                     // shouldnt reach here otherwise the request wouldnt display
                    }


                }
                else{
                    Toast.makeText(context,"Could not update service request status", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context,"Query cancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
