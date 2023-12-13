package com.jnu.student.data;

import static org.junit.Assert.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.test.platform.app.InstrumentationRegistry;

import com.jnu.student.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class DataBankTest {
    DataBank dataSaverBackup;
    ArrayList<Book> bookItemsBackup;

    @Before
    public void setUp() throws Exception {
        dataSaverBackup=new DataBank();
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        bookItemsBackup=dataSaverBackup.LoadBookItems(targetContext);
    }

    @After
    public void tearDown() throws Exception {
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        dataSaverBackup.SaveBookItems(targetContext, bookItemsBackup);
    }

    @Test
    public void saveAndLoadBookItems() {
        DataBank dataSaver = new DataBank();
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ArrayList<Book> books = new ArrayList<>();
        Book book = new Book("测试", R.drawable.book_1);
        books.add(book);
        book = new Book("正常", R.drawable.book_2);
        books.add(book);
        dataSaver.SaveBookItems(targetContext, books);

        DataBank dataLoader = new DataBank();
        ArrayList<Book> bookItemsRead = dataLoader.LoadBookItems(targetContext);
        assertNotSame(books,bookItemsRead);
        assertEquals(books.size(),bookItemsRead.size());
        for(int index = 0; index<books.size(); ++index)
        {
            assertNotSame(books.get(index),bookItemsRead.get(index));
            assertEquals(books.get(index).getTitle(),bookItemsRead.get(index).getTitle());
            assertEquals(books.get(index).getCoverResourceId(),bookItemsRead.get(index).getCoverResourceId());
            // 测试图片是否与预期图片一致
            Bitmap expectedBitmap = BitmapFactory.decodeResource(targetContext.getResources(),
                    books.get(index).getCoverResourceId());
            Bitmap actualBitmap = BitmapFactory.decodeResource(targetContext.getResources(),
                    bookItemsRead.get(index).getCoverResourceId());
            assertTrue(actualBitmap.sameAs(expectedBitmap));
        }

    }
}