package com.example.segproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class branchListAdapter extends RecyclerView.Adapter<branchListAdapter.customerBranchViewHolder> {



    private Context context;
    private ArrayList<Branch> branchList;
    private ArrayList<String> ids;
    private String customerID;
    private String address;
    private int firstHour;
    private int secondHour;
    private String serviceName;
    private boolean rated;

    public branchListAdapter(Context c, ArrayList<Branch> branches, ArrayList<String> ids, String customerID,  int firstHour, int secondHour, String address, String serviceName){
        this.context = c;
        this.branchList = branches;
        this.ids = ids;
        this.customerID = customerID;
        this.address = address;
        this.firstHour = firstHour;
        this.secondHour = secondHour;
        this.serviceName = serviceName;
        this.rated = false;

    }


    public class customerBranchViewHolder extends RecyclerView.ViewHolder{

        TextView nameOfBranch;
        ConstraintLayout branchLayoutCustomer;
        Button book;
        RatingBar ratingBar;
        Button goToView;
        public customerBranchViewHolder(@NonNull View itemView) {
            super(itemView);
            nameOfBranch = (TextView) itemView.findViewById(R.id.textViewBranchNameCustomer);
            branchLayoutCustomer = itemView.findViewById(R.id.branchLayoutCustomer);
            book = (Button) itemView.findViewById(R.id.buttonBook);
            ratingBar = (RatingBar) itemView.findViewById(R.id.branchRatingBar);
            goToView = (Button) itemView.findViewById(R.id.buttonViewBranchCustomer);
        }
    }


    @NonNull
    @Override
    public branchListAdapter.customerBranchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.branch_row_customer,parent,false);
        return new branchListAdapter.customerBranchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull customerBranchViewHolder holder, int position) {
        Branch branch = branchList.get(position);
        holder.nameOfBranch.setText(branch.getBranchName());

        String id = ids.get(position); // employee ID

        float f = (float) branch.getBranchRating();
        holder.ratingBar.setRating(f);
        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                float selectedRating = rating;
                //Toast.makeText(context,customerID + selectedRating, Toast.LENGTH_SHORT).show();
                boolean alreadyRated = false;
                try{
                    for(customerRatings r: branch.getRatedBy()){
                        if(r.getCustomerID().equals(customerID)){


                            r.setCustomerRating(selectedRating);
                            alreadyRated = true;
                            //Toast.makeText(context,"(already) rated: " + selectedRating, Toast.LENGTH_SHORT).show();

                        }
                    }

                    if(!alreadyRated){
                        //Toast.makeText(context,"Not already rated " + selectedRating, Toast.LENGTH_SHORT).show();
                        customerRatings newRating = new customerRatings(customerID, selectedRating);
                        ArrayList<customerRatings> currentRatings = branch.getRatedBy();
                        currentRatings.add(newRating);
                        branch.setRatedBy(currentRatings);

                    }

                    updateBranchRating(alreadyRated, id, branch);

                }
                catch(Exception e){ // first rating when branch.getRatedBy() null object reference

                    //Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                    customerRatings newRating = new customerRatings(customerID, selectedRating);
                    ArrayList<customerRatings> currentRatings = new ArrayList<customerRatings>();
                    currentRatings.add(newRating);
                    branch.setRatedBy(currentRatings);
                    updateBranchRating(false, id, branch);


                }


            }
        });


        holder.book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, fillForm.class);
                intent.putExtra("serviceName", serviceName);
                intent.putExtra("uID", id); // employeeID
                intent.putExtra("customerID", customerID);
                intent.putExtra("firstHour", firstHour);
                intent.putExtra("secondHour", secondHour);
                intent.putExtra("address", address);
                intent.putExtra("isCustomer", "YES");
                context.startActivity(intent);
            }
        });

        holder.goToView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)context).finish(); // always Activity in this case so typecasting is fine
                Intent intent = new Intent(context, branchView.class);
                intent.putExtra("firstHour", firstHour);
                intent.putExtra("secondHour", secondHour);
                intent.putExtra("address", address);
                intent.putExtra("serviceName", serviceName);
                intent.putExtra("isCustomer", "YES");
                intent.putExtra("uID", id); // employee ID
                intent.putExtra("customerID", customerID);
                context.startActivity(intent);
            }


        });




    }

    private void updateBranchRating(boolean alreadyRated, String id, Branch branch){
        DatabaseReference rootNode = FirebaseDatabase.getInstance().getReference("Users");

        rootNode.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(alreadyRated){
                        Toast.makeText(context, "Thanks for updating your rating", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(context, "Thank you for rating", Toast.LENGTH_SHORT).show();
                    }
                    Employee employeeToUpdateRatings = snapshot.getValue(Employee.class);

                    int sum = 0;
                    double count = 0.0;
                    for(customerRatings r: branch.getRatedBy()){
                        sum+= r.getCustomerRating();
                        count += 1;
                    }
                    double overallRating = (sum / count);
                    Log.i("RATING",":sum: " + sum + " count: " + count + " overallRating: " + overallRating);
                    branch.setBranchRating(overallRating);
                    //employeeToUpdateRatings.setBranch(branch);
                    rootNode.child(id).child("branch").setValue(branch);
                    //Toast.makeText(context, "Reaches here", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(context,"Unable to update branch rating", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return branchList.size();
    }
}
