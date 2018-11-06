package criminalintent.and.mazzy.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeListFragment extends Fragment {
    private final int POSITION_TAG=1001;

    private RecyclerView mCrimeRecycleView;
    private CrimeAdapter mCrimeAdapter;
    private List<Integer> changedList=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /*View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mRecyclerView = view.findViewById(R.id.);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));*/


        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeRecycleView = (RecyclerView) view.findViewById(R.id.crime_recycler_listview);
        mCrimeRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));

        UpdateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        UpdateUI();
    }

    private void UpdateUI() {
        CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        if (mCrimeAdapter == null) {
            mCrimeAdapter = new CrimeAdapter(crimes);
            mCrimeRecycleView.setAdapter(mCrimeAdapter);
        }
        else{

            updateChangedList();

        }


    }

    private void updateChangedList() {
        for (int position : changedList) {
            mCrimeAdapter.notifyItemChanged(position);
        }
        changedList.clear();
    }


    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTitleTextView;
        TextView mDateTextView;
        ImageView mSolvedImageView;
        Crime mCrime;
        public CrimeHolder(LayoutInflater inflater,ViewGroup parent,int viewType) {
            super(inflater.inflate(viewType,parent,false));
            itemView.setOnClickListener(this);
            mTitleTextView = itemView.findViewById(R.id.crime_title);
            mDateTextView = itemView.findViewById(R.id.crime_date);
            mSolvedImageView = itemView.findViewById(R.id.crime_solved);


        }

        public void bind(Crime crime) {
            mCrime=crime;
            mTitleTextView.setText(crime.getTitle());
            mDateTextView.setText(crime.getDateString());
            mSolvedImageView.setVisibility(crime.isSolved()?View.VISIBLE:View.GONE);

        }

        @Override
        public void onClick(View view) {
//            String toastMessage = "" + mCrime.getTitle() + " is clicked";
//            Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_SHORT).show();


            changedList.add(getAdapterPosition());
            Intent intent = CrimeActivity.newIntent(getActivity(),mCrime.getUid());
            startActivity(intent);
        }
    }


    public class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private List<Crime> mCrimeList;
        private List<Integer> changedList;

        public CrimeAdapter(List<Crime> mCrimeList) {
            this.mCrimeList = mCrimeList;
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new CrimeHolder(inflater,parent,viewType);


        }


        @Override
        public int getItemViewType(int position) {
            Crime crime = mCrimeList.get(position);
//            if (crime.isRequiredPolice()) {
//                return R.layout.list_item_policecrime;
//            }
            return R.layout.list_item_crime;
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {
            Crime crime = mCrimeList.get(position);

            holder.bind(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimeList.size();
        }
    }
}
