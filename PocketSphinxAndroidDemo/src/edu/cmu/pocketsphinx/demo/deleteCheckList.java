package edu.cmu.pocketsphinx.demo;

import java.util.List;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

public class deleteCheckList extends ArrayAdapter<Message> {
	
	Context context;
	LayoutInflater inflater;
	List<Message> MessageList;
	private SparseBooleanArray mSelectedItemsIds;
	
	public deleteCheckList(Context context, int resourceId,List<Message> MessageList) {
		super(context, resourceId,MessageList);
		mSelectedItemsIds = new SparseBooleanArray();
		this.context = context;
		this.MessageList = MessageList;
		inflater = LayoutInflater.from(context);
		// TODO Auto-generated constructor stub
	}
		
	@Override
	public void remove(Message object) {
		MessageList.remove(object);
		notifyDataSetChanged();
	}
 
//	public List<Message> getWorldPopulation() {
//		return MessageList;
//	}
// 
//	public void toggleSelection(int position) {
//		selectView(position, !mSelectedItemsIds.get(position));
//	}
 
	public void removeSelection() {
		mSelectedItemsIds = new SparseBooleanArray();
		notifyDataSetChanged();
	}
 
	public void selectView(int position, boolean value) {
		if (value)
			mSelectedItemsIds.put(position, value);
		else
			mSelectedItemsIds.delete(position);
		notifyDataSetChanged();
	}
 
	public int getSelectedCount() {
		return mSelectedItemsIds.size();
	}
 
	public SparseBooleanArray getSelectedIds() {
		return mSelectedItemsIds;
	}
}
