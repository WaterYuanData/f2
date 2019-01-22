package com.example.yuan.myview;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int size = 20;
        String[] stringArray = new String[size];
        List<String> stringList;
        for (int i = 0; i < stringArray.length; i++) {
            stringArray[i] = "item_" + i;
        }
        stringList = Arrays.asList(stringArray);
        String[] objects = ((String[]) stringList.toArray());
        for (int i = 0; i < stringList.size(); i++) {
            Log.d(TAG, "onCreate: stringList=" + stringList.get(i));
            Log.d(TAG, "onCreate: stringArray=" + objects[i]);
        }

        ListView listView = findViewById(R.id.lv);
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, stringArray));

        RecyclerView recyclerView = findViewById(R.id.rv);
        recyclerView.setAdapter(new MyAdapter(stringList));
        recyclerView.setLayoutManager(new ResolutionLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new MyItemDecoration()); // 调整间距

        // 滑动冲突、滑动不流畅
        // recyclerView.setHasFixedSize(true);
        // recyclerView.setNestedScrollingEnabled(false);

        RecyclerView recyclerView2 = findViewById(R.id.rv2);
        recyclerView2.setAdapter(new MyAdapter(stringList));
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));

        recyclerView2 = findViewById(R.id.rv3);
        recyclerView2.setAdapter(new MyAdapter(stringList));
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));

        recyclerView2 = findViewById(R.id.rv4);
        recyclerView2.setAdapter(new MyAdapter(stringList));
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
    }

    private class ResolutionLayoutManager extends LinearLayoutManager {

        public ResolutionLayoutManager(Context context) {
            super(context);
        }

        public ResolutionLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public ResolutionLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public boolean canScrollHorizontally() {
            return false;
        }

        @Override
        public boolean canScrollVertically() {
            return false;
        }
    }

    private class MyItemDecoration extends RecyclerView.ItemDecoration {

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            Log.d(TAG, "getItemOffsets: " + (parent.getChildAdapterPosition(view) != state.getItemCount() - 1));
            if (parent.getChildAdapterPosition(view) != state.getItemCount() - 1) {
                outRect.bottom = 20;
            }
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        class MyViewHolder extends RecyclerView.ViewHolder {
            public final TextView title;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.tv);
            }
        }

        private List<String> mDatas;

        public MyAdapter(List<String> data) {
            this.mDatas = data;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder vh, final int position) {
            Log.d(TAG, "onBindViewHolder: position=" + position);
            vh.title.setText(mDatas.get(position));
            vh.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "position_" + position, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

    }
}
