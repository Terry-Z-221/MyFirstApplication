package com.jnu.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.jnu.student.data.Book;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_6);

        RecyclerView recyclerView = findViewById(R.id.recycle_view_books);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));   //设置布局管理器

        ArrayList<Book> books = new ArrayList<>();
        books.add(new Book("软件项目管理案例教程（第4版）", R.drawable.book_2));
        books.add(new Book("book", R.drawable.book_no_name));
        books.add(new Book("信息安全数学基础（第2版）", R.drawable.book_1));

        RecycleViewBookAdapter recycleViewBookAdapter = new RecycleViewBookAdapter(books);
        recyclerView.setAdapter(recycleViewBookAdapter);
    }
    public static class RecycleViewBookAdapter extends RecyclerView.Adapter<RecycleViewBookAdapter.ViewHolder> {

        private final ArrayList<Book> localDataSet;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView bookTitle;
            private final ImageView bookCover;

            public TextView getBookTitle() {
                return bookTitle;
            }

            public ImageView getBookCover() {
                return bookCover;
            }
            public ViewHolder(View view) {
                super(view);
                // Define click listener for the ViewHolder's View

                bookTitle = (TextView) view.findViewById(R.id.text_view_book_title);
                bookCover = (ImageView) view.findViewById(R.id.image_view_book_cover);
            }

        }

        public RecycleViewBookAdapter(ArrayList<Book> dataSet) {
            localDataSet = dataSet;
        }

        // Create new views (invoked by the layout manager)
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view, which defines the UI of the list item
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.book_item_row, viewGroup, false);

            return new ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {

            // Get element from your dataset at this position and replace the
            // contents of the view with that element
            viewHolder.getBookTitle().setText(localDataSet.get(position).getTitle());
            viewHolder.getBookCover().setImageResource(localDataSet.get(position).getCoverResourceId());
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return localDataSet.size();
        }
    }

}