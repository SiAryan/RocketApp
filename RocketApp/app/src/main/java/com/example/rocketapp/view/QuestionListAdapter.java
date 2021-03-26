package com.example.rocketapp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rocketapp.R;
import com.example.rocketapp.controller.ForumManager;
import com.example.rocketapp.controller.UserManager;
import com.example.rocketapp.model.comments.Question;

import java.util.ArrayList;

public class QuestionListAdapter extends RecyclerView.Adapter<QuestionListAdapter.ViewHolder>  {
    private final ArrayList<Question> questions;
    private final ForumManager.QuestionCallback onClickRespond;
    private final UserManager.UserCallback onClickUser;
    private final Context context;
    /**
     * QuestionListAdapter is the custom adapter for the recyclerView that displays questions and answers
     * @param questions the initial questions list
     */
    public QuestionListAdapter(Context context, ArrayList<Question> questions, ForumManager.QuestionCallback onClickRespond, UserManager.UserCallback onClickUser) {
        this.context = context;
        this.questions = questions;
        this.onClickRespond = onClickRespond;
        this.onClickUser = onClickUser;
    }

    /**
     * @param questions new list of questions
     */
    public void updateList(ArrayList<Question> questions) {
        this.questions.clear();
        this.questions.addAll(questions);
        notifyDataSetChanged();
    }

    /**
     * Viewholder for experiment_recycler_view_item.
     * Multiple views can be populated into the holder.
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public QuestionListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_recyclerview_item, parent, false);
        return new QuestionListAdapter.ViewHolder(view);
    }

    /**
     * OnClick for the items in the holder implemented
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull QuestionListAdapter.ViewHolder holder, int position) {
        Question question = questions.get(position);

//        https://www.geeksforgeeks.org/how-to-create-a-nested-recyclerview-in-android/
        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.answerRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setInitialPrefetchItemCount(question.getAnswers().size());

        holder.answerRecyclerView.setLayoutManager(layoutManager);
        holder.answerRecyclerView.setAdapter(new AnswersListAdapter(question.getAnswers(), answer -> {
            // TODO not implemented for anything, needs more setup to work
//            Intent userProfileIntent = new Intent(context, UserProfileActivity.class);
//            context.startActivity(userProfileIntent);
        }, onClickUser));
        holder.answerRecyclerView.setRecycledViewPool(holder.viewPool);
        holder.answerRecyclerView.addItemDecoration(new DividerItemDecoration(holder.answerRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        holder.set(question, view -> onClickRespond.callBack(question), view -> onClickUser.callBack(question.getOwner()));
    }

    /**
     * @return number of experiments in the list
     */
    @Override
    public int getItemCount() {
        return questions.size();
    }

    /**
     * The ViewHolder to populate with experiment information for each experiment.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView commentTitleTextView;
        private final TextView ownerUsernameTextView;
        private final TextView dialogueTextView;
        private final TextView respondButton;
        private final RecyclerView answerRecyclerView;
        private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

        /**
         * Sets all fields according to the experiment input
         * @param question The question to populate the viewholder with
         * @param onClickRespond The click behaviour
         */
        public void set(Question question, View.OnClickListener onClickRespond, View.OnClickListener onClickUser) {
            commentTitleTextView.setText(question.getType());
            ownerUsernameTextView.setText(question.getOwner().getName());
            dialogueTextView.setText(question.getText());
            respondButton.setOnClickListener(onClickRespond);
            ownerUsernameTextView.setOnClickListener(onClickUser);
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            commentTitleTextView = itemView.findViewById(R.id.typeTextView);
            ownerUsernameTextView = itemView.findViewById(R.id.userTextView);
            dialogueTextView = itemView.findViewById(R.id.dialogueTextView);
            respondButton = itemView.findViewById(R.id.buttonRespond);
            answerRecyclerView = itemView.findViewById(R.id.answersRecyclerView);

        }
    }

}
