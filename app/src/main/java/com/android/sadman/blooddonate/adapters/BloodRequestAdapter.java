package com.android.sadman.blooddonate.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.sadman.blooddonate.R;
import com.android.sadman.blooddonate.viewmodels.CustomUserData;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;


public class BloodRequestAdapter extends RecyclerView.Adapter<BloodRequestAdapter.PostHolder>{


    private final Activity activity;
    private List<CustomUserData> postLists;
    private String contactNumber;


    public class PostHolder extends RecyclerView.ViewHolder
    {
        TextView Name, bloodgroup, Address, contact, posted;

        public PostHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.reqstUser);
            contact = itemView.findViewById(R.id.targetCN);
            bloodgroup = itemView.findViewById(R.id.targetBG);
            Address = itemView.findViewById(R.id.reqstLocation);
            posted = itemView.findViewById(R.id.posted);

        }
    }

    public BloodRequestAdapter(Activity activity, List<CustomUserData> postLists)
    {
        this.activity = activity;
        this.postLists = postLists;
    }

    @Override
    public PostHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View listitem = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.request_list_item, viewGroup, false);

        return new PostHolder(listitem);
    }

    @Override
    public void onBindViewHolder(PostHolder postHolder, int i) {

        if(i%2==0)
        {
            postHolder.itemView.setBackgroundColor(Color.parseColor("#C13F31"));
        }
        else
        {
            postHolder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        final CustomUserData customUserData = postLists.get(i);
        postHolder.Name.setText("Posted by: "+customUserData.getName());
        postHolder.Address.setText("From: "+customUserData.getAddress()+", "+customUserData.getDivision());
        postHolder.bloodgroup.setText("Needs "+customUserData.getBloodGroup());
        postHolder.posted.setText("Posted on:"+customUserData.getTime()+", "+customUserData.getDate());
        postHolder.contact.setText(customUserData.getContact());

        postHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dexter.withActivity(activity)
                        .withPermission(Manifest.permission.CALL_PHONE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:"+customUserData.getContact()));
                                activity.startActivity(callIntent);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                            }
                        })
                        .check();
            }
        });

    }

    @Override
    public int getItemCount() {
        return postLists.size();
    }
}
