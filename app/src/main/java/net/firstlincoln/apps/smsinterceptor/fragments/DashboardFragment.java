package net.firstlincoln.apps.smsinterceptor.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.firstlincoln.apps.smsinterceptor.*;
import net.firstlincoln.apps.smsinterceptor.adapters.SmsAdapter;
import net.firstlincoln.apps.smsinterceptor.db.database.AppDatabase;
import net.firstlincoln.apps.smsinterceptor.db.entity.SmsEntity;
import net.firstlincoln.apps.smsinterceptor.models.Sms;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DashboardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private FloatingActionButton deleteFab;

    private RecyclerView rView;

    private LiveData<List<SmsEntity>> smsList;

    private TextView tvEmpty;


    private final String TAG = "Dashboard Fragment";

    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        rView = view.findViewById(R.id.sms_recycler_view);

        tvEmpty = view.findViewById(R.id.empty_view);

        deleteFab = view.findViewById(R.id.fabDeleteSuccess);

        deleteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(false);
                builder.setMessage("Delete all successful sms?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                List<SmsEntity> smsList = AppDatabase.getAppDatabase(getActivity()).smsDao().getByStatus(1);
                                AppDatabase.getAppDatabase(getContext()).smsDao().deleteAll(smsList);
                            }
                        }).start();

                        Toast.makeText(getContext(), "Messages Deleted", Toast.LENGTH_SHORT).show();

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //if user select "No", just cancel this dialog and continue with app
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {

                smsList = AppDatabase.getAppDatabase(getActivity()).smsDao().getSuccessful();
                //populateList(sms);
                final Observer<List<SmsEntity>> smsListObserver = new Observer<List<SmsEntity>>() {
                    @Override
                    public void onChanged(@Nullable final List<SmsEntity> smsList) {
                        if(smsList.isEmpty()){
                            tvEmpty.setVisibility(View.VISIBLE);
                            rView.setVisibility(View.GONE);
                            deleteFab.setVisibility(View.GONE);

                        }else{
                            tvEmpty.setVisibility(View.GONE);
                            rView.setVisibility(View.VISIBLE);
                            deleteFab.setVisibility(View.VISIBLE);

                            rView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            rView.setAdapter(new SmsAdapter(smsList, new SmsAdapter.OnItemClickListener() {
                                @Override public void onItemClick(SmsEntity sms) {
                                    Intent intent = new Intent(getActivity(), SmsDetail.class);
                                    intent.putExtra("sms_id", sms.getId());
                                    startActivity(intent);
                                }
                            }));
                        }

                    }
                };
                smsList.observe(DashboardFragment.this, smsListObserver);
            }
        }).start();





        return view;


    }

    public void populateList(final List<SmsEntity> arrayOfSms){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rView.setLayoutManager(new LinearLayoutManager(getActivity()));
                rView.setAdapter(new SmsAdapter(arrayOfSms, new SmsAdapter.OnItemClickListener() {
                    @Override public void onItemClick(SmsEntity sms) {
                        Toast.makeText(getActivity().getApplicationContext(), "Item Clicked", Toast.LENGTH_SHORT).show();
                    }
                }));
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
