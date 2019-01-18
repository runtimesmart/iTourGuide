package com.itg.ui.view.contact;

import java.util.Comparator;

import com.itg.bean.City;

public class ContactItemComparator implements Comparator<City>
{

	@Override
	public int compare(City lhs, City rhs)
	{
		if (lhs.getCIndex() == null || rhs.getCIndex() == null)
			return -1;

		return (lhs.getCIndex().compareTo(rhs.getCIndex()));

	}

}
