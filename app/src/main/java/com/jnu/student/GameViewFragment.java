package com.jnu.student;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.jnu.student.view.GameView;

public class GameViewFragment extends Fragment {

    public GameViewFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
//    public static GameViewFragment newInstance() {
//        GameViewFragment fragment = new GameViewFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//
//        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_game_view, container, false);

        GameView gameView = rootView.findViewById(R.id.book_gameView);
        TextView scoreTextView = rootView.findViewById(R.id.game_score_textView);
        TextView timeTextView = rootView.findViewById(R.id.game_time_textView);
        Button startButton = rootView.findViewById(R.id.start_game_button);

        startButton.setOnClickListener(view -> {
            AlertDialog.Builder Builder = new AlertDialog.Builder(getContext());
            Builder.setTitle("游戏规则");
            Builder.setMessage("这是一款学习知识的游戏，游戏中图书会随机出现，每点击到一本书，你就能得到一分。注意，你的时间只有30秒，准备好了吗？");
            Builder.setCancelable(false);
            Builder.setPositiveButton("我准备好了，开始吧", (dialogInterface_, i_) -> {
                startButton.setVisibility(View.GONE);
                gameView.setVisibility(View.VISIBLE);
                scoreTextView.setVisibility(View.VISIBLE);
                timeTextView.setVisibility(View.VISIBLE);
            });
            Builder.create().show();
        });

        gameView.setGameListener(new GameView.gameListener() {
            @Override
            public void update() {
                requireActivity().runOnUiThread(() -> {
                    drawScore(scoreTextView, gameView.getScore());
                    drawTime(timeTextView, gameView.getTimeLeft());
                });
            }

            @Override
            public void end() {
                gameView.setVisibility(View.GONE);
                scoreTextView.setVisibility(View.GONE);
                timeTextView.setVisibility(View.GONE);
                startButton.setVisibility(View.VISIBLE);
            }
        });

        return rootView;
    }

    @SuppressLint("DefaultLocale")
    public void drawScore(TextView textView, int score) {
        // 显示得分
        textView.setTextColor(Color.argb(155, 80, 40, 10));
        textView.setText(String.format("%s%d", getResources().getString(R.string.score), score));
    }

    @SuppressLint("DefaultLocale")
    public void drawTime(TextView textView, int time) {
        // 显示时间
        textView.setText(String.format("%d", time));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}