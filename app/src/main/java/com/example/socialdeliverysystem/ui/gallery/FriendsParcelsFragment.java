package com.example.socialdeliverysystem.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.example.socialdeliverysystem.Entites.Person;
import com.example.socialdeliverysystem.R;
import com.example.socialdeliverysystem.ui.SharedViewModel;

public class FriendsParcelsFragment extends Fragment {

    private SharedViewModel friendsParcelsViewModel;
    private Person user;
    private TextView textView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        textView = root.findViewById(R.id.text_gallery);
        friendsParcelsViewModel = ViewModelProviders.of(this).get(SharedViewModel.class);
        friendsParcelsViewModel.user.observe(getActivity(), new Observer<Person>() {
            @Override
            public void onChanged(Person person) {
                user = person;
                textView.setText(user.getFirstName());
            }
        });
        return root;
    }

  // @Override
  // public void onActivityCreated(@Nullable Bundle savedInstanceState) {
  //     super.onActivityCreated(savedInstanceState);
  //     friendsParcelsViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
  //     friendsParcelsViewModel.user.observe(getViewLifecycleOwner(), new Observer<Person>() {
  //         @Override
  //         public void onChanged(Person person) {
  //             user = person;
  //             textView.setText(user.getFirstName());
  //         }
  //     });
  // }
}
