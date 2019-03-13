package com.example.jojo.obsido.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.jojo.obsido.Partner;
import com.example.jojo.obsido.PartnerAdapter;
import com.example.jojo.obsido.PartnerViewModel;
import com.example.jojo.obsido.R;
import com.example.jojo.obsido.ui.AddEditProfileActivity;
import com.example.jojo.obsido.ui.PartnerProfileActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PartnerListFragment extends Fragment {

    private RecyclerView partnerRecyclerView;
    private RelativeLayout partnerEmptyView;

    private PartnerViewModel partnerViewModel;

    public static final int EDIT_PARTNER_REQUEST = 2;

    public PartnerListFragment() {

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return false;

            case R.id.action_delete_all_entries:
                partnerViewModel.deleteAllNotes();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_partner_list, container, false);

        partnerRecyclerView = rootView.findViewById(R.id.partners_list);
        partnerEmptyView = rootView.findViewById(R.id.empty_view);

        initRecyclerView();

        return rootView;
    }

    private void initRecyclerView() {
        partnerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        partnerRecyclerView.setHasFixedSize(true);
        partnerRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(),
                DividerItemDecoration.VERTICAL));

        final PartnerAdapter partnerAdapter = new PartnerAdapter();
        partnerRecyclerView.setAdapter(partnerAdapter);

        partnerViewModel = ViewModelProviders.of(this).get(PartnerViewModel.class);
        partnerViewModel.getAllPartners().observe(this, new Observer<List<Partner>>() {
            @Override
            public void onChanged(@Nullable List<Partner> partners) {
                partnerAdapter.submitList(partners);

                if (partners.size() <= 0) {
                    partnerRecyclerView.setVisibility(View.GONE);
                    partnerEmptyView.setVisibility(View.VISIBLE);
                } else {
                    partnerRecyclerView.setVisibility(View.VISIBLE);
                    partnerEmptyView.setVisibility(View.GONE);
                }
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                partnerViewModel.delete(partnerAdapter.getPartnerAt(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(partnerRecyclerView);
        partnerAdapter.setOnItemClickListener(new PartnerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Partner partner) {
                Intent intent = new Intent(getActivity().getApplicationContext(),
                        PartnerProfileActivity.class);
                intent.putExtra(AddEditProfileActivity.EXTRA_PARTNER_NAME, partner.getName());
                intent.putExtra(AddEditProfileActivity.EXTRA_PARTNER_DESCRIPTION, partner.getDescription());
                intent.putExtra(AddEditProfileActivity.EXTRA_PARTNER_AGE, partner.getAge());
                intent.putExtra(AddEditProfileActivity.EXTRA_PARTNER_GENDER, partner.getGender());
                intent.putExtra(AddEditProfileActivity.EXTRA_PARTNER_ID, partner.getId());
                startActivityForResult(intent, EDIT_PARTNER_REQUEST);
            }
        });

    }

}
