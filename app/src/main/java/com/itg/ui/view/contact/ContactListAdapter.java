package com.itg.ui.view.contact;

import java.util.Collections;
import java.util.List;

import com.itg.bean.City;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ContactListAdapter extends ArrayAdapter<City>
{

	private int resource; // store the resource layout id for 1 row
	private boolean inSearchMode = false;
   private List<City> itemsCities;
	private ContactsSectionIndexer indexer = null;
	private boolean isInitIndex=false;

	public ContactListAdapter(Context _context, int _resource,
			List<City> _items)
	{
		super(_context, _resource, _items);
		resource = _resource;
		itemsCities=_items;
		// need to sort the items array first, then pass it to the indexer
		Collections.sort(_items, new ContactItemComparator());
		setIndexer(new ContactsSectionIndexer(_items));
	
	
	}
	

	// get the section textview from row view
	// the section view will only be shown for the first item


	public void showSectionViewIfFirstItem(View rowView,
			City item, int position)
	{
		TextView sectionTextView = (TextView) rowView
				.findViewById(com.itg.iguide.R.id.sectionTextView);

			// if first item then show the header
			if (indexer.isFirstItemInSection(position))
			{
				String sectionTitle = indexer.getSectionTitle(item
						.getCIndex());
				sectionTextView.setText(item.getCIndex());
				sectionTextView.setVisibility(View.VISIBLE);
			} 
			else 
			{
				sectionTextView.setVisibility(View.GONE);
			}
	}

	// do all the data population for the row here
	// subclass overwrite this to draw more items
	public void populateDataForRow(View parentView, City item,
			int position)
	{
		// default just draw the item only
		View infoView = parentView.findViewById(com.itg.iguide.R.id.infoRowContainer);
		TextView nameView = (TextView) infoView.findViewById(com.itg.iguide.R.id.cityName);
		nameView.setText(item.getCityName());
	}

	// this should be override by subclass if necessary
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if(!isInitIndex)
		{
			Collections.sort(itemsCities, new ContactItemComparator());
			setIndexer(new ContactsSectionIndexer(itemsCities));
			isInitIndex=true;
		}
		ViewGroup parentView;

		City item = getItem(position);

		if (convertView == null)
		{
			parentView = new RelativeLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					inflater);
			vi.inflate(resource, parentView, true);
		} else
		{
			parentView = (RelativeLayout) convertView;
		}
		
//		parentView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Toast.makeText(getContext(), "sdfsdf", 3).show();
//			}
//		});
		
		showSectionViewIfFirstItem(parentView, item, position);

		populateDataForRow(parentView, item, position);

		return parentView;

	}

	public boolean isInSearchMode()
	{
		return inSearchMode;
	}

	public void setInSearchMode(boolean inSearchMode)
	{
		this.inSearchMode = inSearchMode;
	}

	public ContactsSectionIndexer getIndexer()
	{
		return indexer;
	}

	public void setIndexer(ContactsSectionIndexer indexer)
	{
		this.indexer = indexer;
	}

}
