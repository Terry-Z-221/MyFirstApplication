package com.jnu.student;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jnu.student.data.Book;
import com.jnu.student.data.DataBank;

import java.util.ArrayList;

public class BookListFragment extends Fragment {
    ArrayList<Book> books = new ArrayList<>();
    RecycleViewBookAdapter recycleViewBookAdapter;
    DataBank dataBank = new DataBank();

    FloatingActionButton floatingActionButton;
    ActivityResultLauncher<Intent> addItemLauncher;   // 用于启动添加窗口
    ActivityResultLauncher<Intent> updateItemLauncher;   // 用于启动修改窗口

    public BookListFragment() {
        // Required empty public constructor
    }

    public static BookListFragment newInstance() {
        BookListFragment fragment = new BookListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_book_list, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerview_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));   //设置布局管理器

        floatingActionButton = rootView.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(view -> {
            Intent intent;
            intent = new Intent(requireActivity(), BookItemDetailsActivity.class);
            intent.putExtra("title", "Name");
            intent.putExtra("position", -1);
            addItemLauncher.launch(intent);
        });

        books = dataBank.LoadBookItems(requireActivity());
        if(0 == books.size()) {
            books.add(new Book("软件项目管理案例教程（第4版）", R.drawable.book_2));
            books.add(new Book("创新工程实践", R.drawable.book_no_name));
            books.add(new Book("信息安全数学基础（第2版）", R.drawable.book_1));
            dataBank.SaveBookItems(requireActivity(), books);
        }
        if(books.size() > 0) {
            floatingActionButton.setVisibility(View.GONE);
        }

        recycleViewBookAdapter = new RecycleViewBookAdapter(books);
        recyclerView.setAdapter(recycleViewBookAdapter);    //设置适配器

        registerForContextMenu(recyclerView);   //注册上下文菜单

        addItemLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // 处理返回数据
                        Intent data = result.getData();
                        String name = null;
                        int position = 0;
                        if (data != null) {
                            name = data.getStringExtra("name"); // 获取返回数据，key要一样
                            position = data.getIntExtra("position", 0);
                        }
                        // 在这里可以根据需要进行进一步处理
                        if(-1 == position)
                            books.add(new Book(name, R.drawable.book_no_name));
                        else
                            books.add(new Book(name, books.get(position).getCoverResourceId()));
                        if(books.size() > 0) {
                            floatingActionButton.setVisibility(View.GONE);
                        }
                        recycleViewBookAdapter.notifyItemInserted(books.size());    // 更新数据后刷新界面
                        dataBank.SaveBookItems(requireActivity(), books);
                        Toast.makeText(requireActivity(), "已添加！", Toast.LENGTH_SHORT).show();
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        // 处理取消操作
                        Toast.makeText(requireActivity(), "已取消该操作！", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        updateItemLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // 处理返回数据
                        Intent data = result.getData();
                        String name = null;
                        int position = 0;
                        if (data != null) {
                            name = data.getStringExtra("name"); // 获取返回数据，key要一样
                            position = data.getIntExtra("position", 0);
                        }
                        // 在这里可以根据需要进行进一步处理
                        books.get(position).setTitle(name);
                        books.get(position).setCoverResourceId(R.drawable.book_no_name);
                        recycleViewBookAdapter.notifyItemChanged(position);    // 更新数据后刷新界面
                        dataBank.SaveBookItems(requireActivity(), books);
                        Toast.makeText(requireActivity(), "已修改！", Toast.LENGTH_SHORT).show();
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        // 处理取消操作
                        Toast.makeText(requireActivity(), "已取消该操作！", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        return rootView;
    }

    @Override
    // 重载响应菜单的函数
    public boolean onContextItemSelected(MenuItem menuItem) {
        Intent intent;
        Book book = books.get(menuItem.getOrder());
        switch (menuItem.getItemId()) {
            case 0: // 添加
                intent = new Intent(requireActivity(), BookItemDetailsActivity.class);
                intent.putExtra("title", book.getTitle());
                intent.putExtra("position", menuItem.getOrder());
                addItemLauncher.launch(intent);
                break;
            case 1: // 修改
                intent = new Intent(requireActivity(), BookItemDetailsActivity.class);
                intent.putExtra("title", book.getTitle());
                intent.putExtra("position", menuItem.getOrder());
                updateItemLauncher.launch(intent);
                break;
            case 2: // 删除
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle("删除操作");
                builder.setMessage("确定要删除这一条数据吗？");
                builder.setPositiveButton("确定", (dialogInterface, i) -> {
                    books.remove(menuItem.getOrder());
                    if(books.size() == 0) {
                        floatingActionButton.setVisibility(View.VISIBLE);
                    }
                    recycleViewBookAdapter.notifyItemRemoved(menuItem.getOrder()); // 更新数据后刷新界面
                    dataBank.SaveBookItems(requireActivity(), books);
                    Toast.makeText(requireActivity(), "已删除！", Toast.LENGTH_SHORT).show();
                });
                builder.setNegativeButton("取消", (dialogInterface, i) ->
                        Toast.makeText(requireActivity(), "已取消该操作！", Toast.LENGTH_SHORT).show());
                builder.create().show();
                break;
            default:
                break;
        }
        return super.onContextItemSelected(menuItem);
    }

    // 关于book_item的适配器
    public static class RecycleViewBookAdapter extends RecyclerView.Adapter<RecycleViewBookAdapter.ViewHolder> {

        private final ArrayList<Book> localDataSet;

        // ViewHolder类
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
                menu.add(0, 0, this.getAdapterPosition(), "添加" + this.getAdapterPosition());
                menu.add(0, 1, this.getAdapterPosition(), "修改" + this.getAdapterPosition());
                menu.add(0, 2, this.getAdapterPosition(), "删除" + this.getAdapterPosition());
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

        // 替换view中的内容
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            // 绑定数据
            viewHolder.getBookTitle().setText(localDataSet.get(position).getTitle());
            viewHolder.getBookCover().setImageResource(localDataSet.get(position).getCoverResourceId());
        }

        // 返回数据库的大小
        @Override
        public int getItemCount() {
            return localDataSet.size();
        }
    }
}