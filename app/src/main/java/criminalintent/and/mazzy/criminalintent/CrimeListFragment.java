package criminalintent.and.mazzy.criminalintent;

import android.content.Context;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import criminalintent.and.mazzy.criminalintent.model.Crime;
import criminalintent.and.mazzy.criminalintent.model.CrimeLab;

public class CrimeListFragment extends Fragment {
    private static final String SAVED_SUBTITLE_VISIBLE = "SAVED_SUBTITLE_VISIBLE";
    private final int POSITION_TAG=1001;

    private View emptyView;
    private Button addCrime_Button;
    private RecyclerView mCrimeRecycleView;
    private CrimeAdapter mCrimeAdapter;
    private boolean mSubtitleVisible;
    private List<Integer> changedList=new ArrayList<>();

    public Callbacks mCallbacks;




    public interface Callbacks{
            void onCrimeSelected(Crime crime);
        }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks=(Callbacks) context;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks=null;
    }

    ItemTouchHelper recycleViewRemoveHelper=new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT,ItemTouchHelper.RIGHT){


        public  boolean onMove(RecyclerView recyclerView,
                               RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            final int fromPos = viewHolder.getAdapterPosition();
            final int toPos = target.getAdapterPosition();
            // move item in `fromPos` to `toPos` in adapter.
            Log.i("ItemTouchHelper","Moved");
            return true;// true if moved, false otherwise
        }
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            Log.i("ItemTouchHelper","SWIPED");
            Toast.makeText(getActivity(),"Item swiped",Toast.LENGTH_SHORT).show();
        }
    });


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);


        mCrimeRecycleView = (RecyclerView) view.findViewById(R.id.crime_recycler_listview);
        mCrimeRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleViewRemoveHelper.attachToRecyclerView(mCrimeRecycleView);


        emptyView = view.findViewById(R.id.emty_view);
        addCrime_Button = view.findViewById(R.id.emty_view_addcrimebutton);
        addCrime_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewCrime();
            }
        });

        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        UpdateUI();

        setHasOptionsMenu(true);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE,mSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list,menu);
        MenuItem subtitleItem = menu.findItem(R.id.ic_show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
        else{
            subtitleItem.setTitle(R.string.hide_subtitle);
        }



    }

    @Override
    public void onResume() {

        super.onResume();
        mCrimeAdapter.notifyDataSetChanged();
        UpdateUI();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.new_crime:
                AddNewCrime();
                return true;
            case R.id.ic_show_subtitle:
                mSubtitleVisible=!mSubtitleVisible;
                getActivity().invalidateOptionsMenu();

                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void AddNewCrime() {
        Crime crime = new Crime();
        CrimeLab.getInstance(getActivity()).AddCrime(crime);
        UpdateUI();
        mCallbacks.onCrimeSelected(crime);

    }

    private void updateSubtitle() {
        int crimeCount=CrimeLab.getInstance(getActivity()).getCrimes().size();
        String subtitle = getString(R.string.subtitle_format, crimeCount);
        if (mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();


        activity.getSupportActionBar().setSubtitle(subtitle);

    }


    public void UpdateUI() {



        if (mCrimeAdapter != null) {

            if (mCrimeAdapter.getItemCount() > 0) {
                emptyView.setVisibility(View.GONE);
            }
            else{
                emptyView.setVisibility(View.VISIBLE);
            }
            mCrimeAdapter.notifyDataSetChanged();
        }


        CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        if (mCrimeAdapter == null) {
            mCrimeAdapter = new CrimeAdapter(crimes);
            mCrimeRecycleView.setAdapter(mCrimeAdapter);
        }
        else{
            mCrimeAdapter.mCrimeList=crimes;
            mCrimeAdapter.notifyDataSetChanged();
            //updateChangedList();

        }

        updateSubtitle();

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
            changedList.add(getAdapterPosition());
            //Intent intent = CrimeActivity.newIntent(getActivity(),mCrime.getUid());


            mCallbacks.onCrimeSelected(mCrime);
        }
    }


    public class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>  {

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
