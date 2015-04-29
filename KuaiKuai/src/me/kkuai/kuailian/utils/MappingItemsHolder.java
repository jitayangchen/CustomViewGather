package me.kkuai.kuailian.utils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  to hold key item mapping.
 *  This is THREAD-SAFE implementation
 * @author ice
 *
 */
public class MappingItemsHolder<T, J>
{
	// holds all listeners. Key: command ID; value: listeners
	protected ConcurrentHashMap<T, LinkedList<J>> mapping = new ConcurrentHashMap<T, LinkedList<J>>();
	
	/**
	 * check listener is contained
	 * @param key
	 * @param item
	 */
	public boolean isContained(T key, J item)
	{
		boolean rtn = false;
		LinkedList<J> lst = mapping.get(key);
		if(lst != null)
		{
			rtn = lst.contains(item);
		}
		return rtn;
	}
	/**
	 * clear all listener
	 */
	public void clear()
	{
		mapping.clear();
	}
	
	/**
	 * map an item to key
	 * @param key
	 * @param item
	 */
	public void addMapping(T key, J item)
	{
		LinkedList<J> lst = mapping.get(key);
		if(lst == null)
		{
			synchronized (mapping)
			{
				lst = new LinkedList<J>();
				mapping.put(key, lst);	
			}
		}
		
		lst.add(item);
	}
	
	/**
	 * remove a mapped item
	 * @param item
	 */
	public void removeMapping(J item)
	{
		synchronized (mapping)
		{
			for(T tmp : mapping.keySet())
			{
				LinkedList<J> lst = mapping.get(tmp);
				if(lst.contains(lst))
				{
					lst.remove(lst);
					if(lst.size() == 0)
						mapping.remove(tmp);
					break;
				}
			}
		}
	}
	
	/**
	 * get all mapped items in Set
	 * @param key
	 * @return
	 */
	public Set<J> getMappedItems(T key)
	{
		LinkedList<J> l = mapping.get(key);
		if(l != null)
			return new HashSet<J>(l);
		else
			return null;
	}
	
	/**
	 * get key set of all items
	 * @return
	 */
	public Set<T> keySet()
	{
		Set<T> s = mapping.keySet();
		return new HashSet<T>(s);
	}
	
	/**
	 * get all mapped values. items for each key in one sub set
	 * @return
	 */
	public Set<Set<J>> values()
	{
		Set<Set<J>> s = new HashSet<Set<J>>();
		for(List<J> l : mapping.values())
			s.add(new HashSet<J>(l));
		
		return s;
	}
}
