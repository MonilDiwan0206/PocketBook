package com.example.pocketbook;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.MyViewHolder>{

    private List<Settings> settingsList;
    private Context context;
    private FirebaseAuth mAuth;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView headings;
        private LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            headings = itemView.findViewById(R.id.rViewText);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            mAuth = FirebaseAuth.getInstance();
        }
    }

    public SettingsAdapter(List<Settings> settingsList, Context context) {
        this.settingsList = settingsList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_row, viewGroup, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {

        final Settings settings = settingsList.get(i);
        myViewHolder.headings.setText(settings.getHeading());

        myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "You Clicked"+settings.getHeading(), Toast.LENGTH_SHORT).show();
                String action = settings.getHeading();
                if(action == "View your usage"){

                } else if(action == "View your family members"){

                } else if(action == "View Monthly Spendings"){
                    Toast.makeText(context, "Monthly Spendings", Toast.LENGTH_SHORT).show();
                } else if(action == "Log out"){
                    mAuth.signOut();
                    Intent mainIntent = new Intent(context, LoginActivity.class);
                    context.startActivity(mainIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return settingsList.size();
    }
}

