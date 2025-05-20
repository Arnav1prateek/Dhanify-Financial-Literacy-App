package com.example.dhanify;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.GoalViewHolder> {

    private List<Goal> goals;
    private OnGoalClickListener onGoalClickListener;

    public GoalAdapter(List<Goal> goals, OnGoalClickListener onGoalClickListener) {
        this.goals = goals;
        this.onGoalClickListener = onGoalClickListener;
    }

    @NonNull
    @Override
    public GoalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goal, parent, false);
        return new GoalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoalViewHolder holder, int position) {
        Goal goal = goals.get(position);
        holder.goalText.setText(goal.getDescription()); // Update this line
        holder.deleteButton.setOnClickListener(v -> onGoalClickListener.onDeleteClick(goal));
        holder.goalText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String updatedText = holder.goalText.getText().toString().trim();
                if (!updatedText.equals(goal.getDescription())) { // Update this line
                    goal.setDescription(updatedText); // Update this line
                    onGoalClickListener.onUpdateClick(goal);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return goals.size();
    }

    public interface OnGoalClickListener {
        void onUpdateClick(Goal goal);
        void onDeleteClick(Goal goal);
    }

    static class GoalViewHolder extends RecyclerView.ViewHolder {
        TextView goalText;
        ImageButton deleteButton;

        public GoalViewHolder(@NonNull View itemView) {
            super(itemView);
            goalText = itemView.findViewById(R.id.goal_text);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}
