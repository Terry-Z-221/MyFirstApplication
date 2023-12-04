package com.jnu.student;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.jnu.student.data.Book;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ArrayList<Book> books = new ArrayList<>();
    RecycleViewBookAdapter recycleViewBookAdapter = new RecycleViewBookAdapter(books);
    ActivityResultLauncher<Intent> addItemLauncher;   // 用于启动添加窗口
    ActivityResultLauncher<Intent> updateItemLauncher;   // 用于启动修改窗口

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_6);

        RecyclerView recyclerView = findViewById(R.id.recycle_view_books);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));   //设置布局管理器

        books.add(new Book("软件项目管理案例教程（第4版）", R.drawable.book_2));
        books.add(new Book("创新工程实践", R.drawable.book_no_name));
        books.add(new Book("信息安全数学基础（第2版）", R.drawable.book_1));

        recyclerView.setAdapter(recycleViewBookAdapter);    //设置适配器

        registerForContextMenu(recyclerView);   //注册上下文菜单

        addItemLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        // 处理返回数据
                        String name = null;
                        if (data != null) {
                            name = data.getStringExtra("name");
                        }
                        // 在这里可以根据需要进行进一步处理
                        books.add(new Book(name, R.drawable.book_no_name));
                        recycleViewBookAdapter.notifyItemInserted(books.size());
                        Toast.makeText(this, "已添加！", Toast.LENGTH_SHORT).show();
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        // 处理取消操作
                        Toast.makeText(this, "已取消该操作！", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        updateItemLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        // 处理返回数据
                        String name = null;
                        if (data != null) {
                            name = data.getStringExtra("name"); // 获取返回数据
                        }
                        // 在这里可以根据需要进行进一步处理
                        int index = recycleViewBookAdapter.getPosition();   // 获取要修改的book_item的索引
                        Book book = new Book(name, books.get(index).getCoverResourceId());
                        books.set(index, book);
                        recycleViewBookAdapter.notifyItemChanged(index);    // 更新数据后刷新界面
                        Toast.makeText(this, "已修改！", Toast.LENGTH_SHORT).show();
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        // 处理取消操作
                        Toast.makeText(this, "已取消该操作！", Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }

    @Override
    // 重载响应菜单的函数
    public boolean onContextItemSelected(MenuItem menuItem) {
        Intent intent;
        switch (menuItem.getItemId()) {
            case 0: // 添加
                intent = new Intent(MainActivity.this, BookItemDetailsActivity.class);
                addItemLauncher.launch(intent);
                break;
            case 1: // 修改
                intent = new Intent(MainActivity.this, BookItemDetailsActivity.class);
                updateItemLauncher.launch(intent);
                break;
            case 2: // 删除
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("删除操作");
                builder.setMessage("确定要删除这一条数据吗？");
                builder.setPositiveButton("确定", (dialogInterface, i) -> {
                    books.remove(menuItem.getOrder());
                    recycleViewBookAdapter.notifyItemRemoved(menuItem.getOrder()); // 更新数据后刷新界面
                    Toast.makeText(getApplicationContext(), "已删除！", Toast.LENGTH_SHORT).show();
                });
                builder.setNegativeButton("取消", (dialogInterface, i) ->
                        Toast.makeText(getApplicationContext(), "已取消该操作！", Toast.LENGTH_SHORT).show());
                builder.create().show();
                break;
            default:
                break;
        }
        return super.onContextItemSelected(menuItem);
    }
    public static class RecycleViewBookAdapter extends RecyclerView.Adapter<RecycleViewBookAdapter.ViewHolder> {

        private final ArrayList<Book> localDataSet;
        private int position;   // 用于获取Book对象在recyclerView中的位置

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
            private final TextView bookTitle;
            private final ImageView bookCover;

            public TextView getBookTitle() {
                return bookTitle;
            }

            public ImageView getBookCover() {
                return bookCover;
            }
            public ViewHolder(View bookItemView) {
                super(bookItemView);
                // Define click listener for the ViewHolder's View

                bookTitle = bookItemView.findViewById(R.id.text_view_book_title);
                bookCover = bookItemView.findViewById(R.id.image_view_book_cover);
                bookItemView.setOnCreateContextMenuListener(this);
            }

            @Override
            // 创建菜单
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("具体操作");
                menu.add(0, 0, this.getAdapterPosition(), "添加");
                menu.add(0, 1, this.getAdapterPosition(), "修改");
                menu.add(0, 2, this.getAdapterPosition(), "删除");
            }

        }

        public RecycleViewBookAdapter(ArrayList<Book> dataSet) {
            localDataSet = dataSet;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view, which defines the UI of the list item
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.book_item_row, viewGroup, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {

            viewHolder.getBookTitle().setText(localDataSet.get(position).getTitle());
            viewHolder.getBookCover().setImageResource(localDataSet.get(position).getCoverResourceId());
            viewHolder.itemView.setOnLongClickListener(v -> {
                setPosition(viewHolder.getAdapterPosition());
                return false;
            });
        }

        @Override
        public int getItemCount() {
            return localDataSet.size();
        }
    }

}