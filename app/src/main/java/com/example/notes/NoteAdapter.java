package com.example.notes;

import android.content.Context;
import android.content.Intent;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.viewHolder> {

    LayoutInflater layoutInflater;
    List<Note> notes;

    public NoteAdapter(Context context, List<Note> note) {

        this.layoutInflater = LayoutInflater.from(context);
        this.notes = note;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.custom_list_view,parent, false);


        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        String title = notes.get(position).getTitle();
        holder.title.setText(title);

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        TextView title;
        public viewHolder(@NonNull final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), NoteDetail.class);
                    intent.putExtra("ID", notes.get(getAdapterPosition()).getID());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
