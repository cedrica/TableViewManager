package com.tableview.manager;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class Book {

    private SimpleStringProperty title;
	private SimpleIntegerProperty author;
	private SimpleLongProperty id;

    public Book () {
    }

	public Book (long id, String s1, int s2) {
        title = new SimpleStringProperty(s1);
        author = new SimpleIntegerProperty(s2);
        this.id = new SimpleLongProperty(id);
    }


    public long getId() {
		return id.get();
	}

	public void setId(long id) {
		this.id.set(id);
	}

	public void setTitle(SimpleStringProperty title) {
		this.title = title;
	}

	public String getTitle() {

        return title.get();
    }
    public void setTitle(String s) {

        title.set(s);
    }

	public int getAuthor() {
		return author.get();
	}

	public void setAuthor(int author) {
		this.author.set(author);
	}




	@Override
	public String toString() {
		return "Book [title=" + title + ", author=" + author + ", id=" + id + "]";
	}



}