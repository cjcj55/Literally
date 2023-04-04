//package com.cjcj55.literallynot.db;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.cjcj55.literallynot.R;
//
//import java.util.List;
//
//public class AudioFileAdapter extends RecyclerView.Adapter<AudioFileAdapter.ViewHolder> {
//
//    private List<AudioFile> audioFiles;
//
//    public AudioFileAdapter(List<AudioFile> audioFiles) {
//        this.audioFiles = audioFiles;
//    }
//
//    public void setAudioFiles(List<AudioFile> audioFiles) {
//        this.audioFiles = audioFiles;
//        notifyDataSetChanged();
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_file_item, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        AudioFile audioFile = audioFiles.get(position);
//        holder.playButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Handle play button click
//            }
//        });
//        holder.timeSaidTextView.setText(audioFile.getTimeSaid());
//    }
//
//    @Override
//    public int getItemCount() {
//        return audioFiles.size();
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//
//        public Button playButton;
//        public TextView timeSaidTextView;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            playButton = itemView.findViewById(R.id.playButton);
//            timeSaidTextView = itemView.findViewById(R.id.timeSaidTextView);
//        }
//    }
//}
//
