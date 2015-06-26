package edu.cmu.pocketsphinx.demo;

import java.util.List;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class deleteCheckList extends ArrayAdapter<MessageDel> {
	
	Context context;
	LayoutInflater inflater;
	List<MessageDel> Mlist;
	private SparseBooleanArray mSelectedItemsIds;
	
	public deleteCheckList(Context context, int resourceId,List<MessageDel> Mlist) {
		super(context, resourceId,Mlist);
		mSelectedItemsIds = new SparseBooleanArray();
		this.context = context;
		this.Mlist = Mlist;
		inflater = LayoutInflater.from(context);
		// TODO Auto-generated constructor stub
	}
	private class ViewHolder
	{
		TextView txtBody;
		TextView txtSender;
		ImageView inboxImage;
		CheckBox checkBox1;
	}
	public View getView(int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		
		if(view==null){
			holder= new ViewHolder();
			view= inflater.inflate(R.layout.listadapter,null);
			
			holder.txtBody=(TextView)view.findViewById(R.id.txtBody);
			holder.txtSender=(TextView)view.findViewById(R.id.txtSender);
			holder.inboxImage=(ImageView)view.findViewById(R.id.inboxImage);
			view.setTag(holder);
		}else{
			holder=(ViewHolder)view.getTag();
		}
		holder.txtBody.setText(Mlist.get(position).getTxtBody());
		return view;
			
	}
			
	@Override
	public void remove(MessageDel object) {
		Mlist.remove(object);
		notifyDataSetChanged();
	}
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
