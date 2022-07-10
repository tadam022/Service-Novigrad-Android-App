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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class servicelistadapter extends RecyclerView.Adapter<servicelistadapter.viewHolder> {
    Context c;

    private ArrayList<Services> servicesList;

    public servicelistadapter(Context c, ArrayList<Services> services){
        this.c = c;
        this.servicesList = services;

    }
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.servicerow,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Services service = servicesList.get(position);
        holder.serviceNameDisplay.setText(service.getServiceName());


        holder.serviceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, ServiceInfoDisplay.class);
                intent.putExtra("name", service.getServiceName());
                intent.putExtra("form", service.getStringForm());
                intent.putExtra("documents", service.getStringDocuments());
                c.startActivity(intent);
            }


        });

        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = servicesList.get(position).getId();
                deleteService(id,servicesList.get(position).getServiceName());
                deleteServiceFromBranch(servicesList.get(position).getServiceName());

            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase firebaseData = FirebaseDatabase.getInstance();

                String id = servicesList.get(position).getId();

                Intent intent = new Intent(c, EditService.class);
                intent.putExtra("ID", id);

                c.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return servicesList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        TextView serviceNameDisplay;
        TextView serviceFormDisplay;
        TextView serviceDocumentsDisplay;
        Button del;
        Button edit;

        ConstraintLayout serviceLayout;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            serviceNameDisplay = (TextView) itemView.findViewById(R.id.textViewServiceName);
            edit = (Button) itemView.findViewById(R.id.buttoneditservice);
            del = (Button) itemView.findViewById(R.id.buttonDelete);
            //serviceFormDisplay = itemView.findViewById(R.id.textViewForm);
            //serviceDocumentsDisplay = itemView.findViewById(R.id.textViewDocuments);
            serviceLayout = itemView.findViewById(R.id.serviceLayout);



        }


    }

    private void deleteService(String id, String name){
        DatabaseReference referenceService;
        FirebaseDatabase firebaseData = FirebaseDatabase.getInstance();
        referenceService = firebaseData.getReference("Services");
        referenceService.child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(c,"Service deleted successfully", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(c,"Unable to delete service", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    private void deleteServiceFromBranch(String name){
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();

            //name = "d";
            //.orderByChild("/MMOPi5pAOhr2Nx5SHAg/branch/branchServices/serviceName")
            //Query query = root.child("Users").child("branch").child("branchServices").child("2").orderByChild("serviceName").equalTo(name);
            //root.child("Users").child("branch").child("branchServices").child("2").child("serviceName").setValue("aaaaaaaaa"); /// WORKSsssssssssssssssssssssss
            //root.child("Users").child("-MMOPi5pAOhr2Nx5SHAg").child("branch").child("branchServices").child("2").child("serviceName").setValue("aaaaaaaa");
            //Query query = root.child("Users").child("branch").child("branchServices").child(String.valueOf(2)).child("serviceName").orderByValue().equalTo("aaaaaaaaa");
            Query query = root.child("Users").orderByChild("accountType").equalTo("employee");
            query.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for(DataSnapshot snap: snapshot.getChildren()){
                            Employee e = snap.getValue(Employee.class);

                            if(e.getBranch()!= null && e.getBranch().getBranchServices()!= null && e.getBranch().getBranchServices().size() > 0){
                                for(int i = 0; i < e.getBranch().getBranchServices().size(); i++){
                                    try{
                                        if(e.getBranch().getBranchServices().get(i).getServiceName().equals(name)){
                                            //Toast.makeText(c, e.getBranch().getBranchName() + " was found.....", Toast.LENGTH_SHORT).show();
                                            e.getBranch().getBranchServices().remove(i);
                                            root.child("Users").child(e.getUID()).child("branch").child("branchServices").setValue(e.getBranch().getBranchServices());
                                            //root.child("Users").child(e.getUID()).child("branch").child("branchServices").child(String.valueOf(i)).setValue(null);
                                        }
                                    }
                                    catch(IndexOutOfBoundsException exception){

                                    }
                                }

                            }


                        }

                    }
                    else{
                        Toast.makeText(c,"No employee accounts to delete branch from.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(c,"Query cancelled", Toast.LENGTH_SHORT).show();
                }
            });



    }
}
