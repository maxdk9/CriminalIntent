package criminalintent.and.mazzy.criminalintent;

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

import java.util.List;

public class CrimeListFragment extends Fragment {
    private RecyclerView mCrimeRecycleView;
    private CrimeAdapter mCrimeAdapter;

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

    private void UpdateUI() {
        CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        mCrimeAdapter = new CrimeAdapter(crimes);
        mCrimeRecycleView.setAdapter(mCrimeAdapter);

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
            mDateTextView.setText(crime.getDate().toString());
            mSolvedImageView.setVisibility(crime.isSolved()?View.VISIBLE:View.GONE);

        }

        @Override
        public void onClick(View view) {
            String toastMessage = "" + mCrime.getTitle() + " is clicked";
            Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_SHORT).show();
        }
    }


    public class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private List<Crime> mCrimeList;

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
