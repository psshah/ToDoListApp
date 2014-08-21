package com.codepath.example.todolist;
import java.io.Serializable;

public class Item implements Serializable {
		private static final long serialVersionUID = 123456789L;
		private int position;
		private String value;
		
		public Item(int position2, String string) {
			position = position2;
			value = string;
		}
		
		public int getPosition() {
			return position;
		}
		public void setPosition(int position) {
			this.position = position;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
}
