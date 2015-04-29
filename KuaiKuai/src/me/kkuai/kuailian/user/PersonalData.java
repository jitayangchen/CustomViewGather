package me.kkuai.kuailian.user;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import me.kkuai.kuailian.R;
import me.kkuai.kuailian.bean.OptionCell;

public class PersonalData {

	public static List<OptionCell> getPersonalSex(Context context) {
		String[] arr = context.getResources().getStringArray(R.array.personal_sex);
		List<OptionCell> datas = new ArrayList<OptionCell>();
		for (int i = 0; i < arr.length; i++) {
			OptionCell cell = new OptionCell();
			cell.setId(i + "");
			cell.setName(arr[i]);
			datas.add(cell);
		}
		return datas;
	}
	
	public static String getSexById(Context context, String id) {
		String[] arr = context.getResources().getStringArray(R.array.personal_sex);
		int index = Integer.parseInt(id);
		if (arr.length > index) {
			return arr[index];
		}
		return "";
	}
	
	public static List<OptionCell> getPersonalEducation(Context context) {
		String[] arr = context.getResources().getStringArray(R.array.personal_education);
		List<OptionCell> datas = new ArrayList<OptionCell>();
		for (int i = 0; i < arr.length; i++) {
			OptionCell cell = new OptionCell();
			cell.setId(i + "");
			cell.setName(arr[i]);
			datas.add(cell);
		}
		return datas;
	}
	
	public static String getEducationById(Context context, String id) {
		String[] arr = context.getResources().getStringArray(R.array.personal_education);
		int index = Integer.parseInt(id);
		if (arr.length > index) {
			return arr[index];
		}
		return "";
	}
	
	public static List<OptionCell> getPersonalIncome(Context context) {
		String[] arr = context.getResources().getStringArray(R.array.personal_income);
		List<OptionCell> datas = new ArrayList<OptionCell>();
		for (int i = 0; i < arr.length; i++) {
			OptionCell cell = new OptionCell();
			cell.setId(i + "");
			cell.setName(arr[i]);
			datas.add(cell);
		}
		return datas;
	}
	
	public static String getIncomeById(Context context, String id) {
		String[] arr = context.getResources().getStringArray(R.array.personal_income);
		int index = Integer.parseInt(id);
		if (arr.length > index) {
			return arr[index];
		}
		return "";
	}
	
	public static List<OptionCell> getPersonalWork(Context context) {
		String[] arr = context.getResources().getStringArray(R.array.personal_work);
		List<OptionCell> datas = new ArrayList<OptionCell>();
		for (int i = 0; i < arr.length; i++) {
			OptionCell cell = new OptionCell();
			cell.setId(i + "");
			cell.setName(arr[i]);
			datas.add(cell);
		}
		return datas;
	}
	
	public static String getWorkById(Context context, String id) {
		String[] arr = context.getResources().getStringArray(R.array.personal_work);
		int index = Integer.parseInt(id);
		if (arr.length > index) {
			return arr[index];
		}
		return "";
	}
	
	public static List<OptionCell> getPersonalJob(Context context) {
		String[] arr = context.getResources().getStringArray(R.array.personal_job);
		List<OptionCell> datas = new ArrayList<OptionCell>();
		for (int i = 0; i < arr.length; i++) {
			OptionCell cell = new OptionCell();
			cell.setId(i + "");
			cell.setName(arr[i]);
			datas.add(cell);
		}
		return datas;
	}
	
	public static String getJobById(Context context, String id) {
		String[] arr = context.getResources().getStringArray(R.array.personal_job);
		int index = Integer.parseInt(id);
		if (arr.length > index) {
			return arr[index];
		}
		return "";
	}
	
	public static List<OptionCell> getPersonalMarriage(Context context) {
		String[] arr = context.getResources().getStringArray(R.array.personal_marriage);
		List<OptionCell> datas = new ArrayList<OptionCell>();
		for (int i = 0; i < arr.length; i++) {
			OptionCell cell = new OptionCell();
			cell.setId(i + "");
			cell.setName(arr[i]);
			datas.add(cell);
		}
		return datas;
	}
	
	public static String getMarriageById(Context context, String id) {
		String[] arr = context.getResources().getStringArray(R.array.personal_marriage);
		int index = Integer.parseInt(id);
		if (arr.length > index) {
			return arr[index];
		}
		return "";
	}
	
	public static List<OptionCell> getPersonalHousing(Context context) {
		String[] arr = context.getResources().getStringArray(R.array.personal_housing);
		List<OptionCell> datas = new ArrayList<OptionCell>();
		for (int i = 0; i < arr.length; i++) {
			OptionCell cell = new OptionCell();
			cell.setId(i + "");
			cell.setName(arr[i]);
			datas.add(cell);
		}
		return datas;
	}
	
	public static String getHousingById(Context context, String id) {
		String[] arr = context.getResources().getStringArray(R.array.personal_housing);
		int index = Integer.parseInt(id);
		if (arr.length > index) {
			return arr[index];
		}
		return "";
	}
	
	public static List<OptionCell> getPersonalCar(Context context) {
		String[] arr = context.getResources().getStringArray(R.array.personal_car);
		List<OptionCell> datas = new ArrayList<OptionCell>();
		for (int i = 0; i < arr.length; i++) {
			OptionCell cell = new OptionCell();
			cell.setId(i + "");
			cell.setName(arr[i]);
			datas.add(cell);
		}
		return datas;
	}
	
	public static String getCarById(Context context, String id) {
		String[] arr = context.getResources().getStringArray(R.array.personal_car);
		int index = Integer.parseInt(id);
		if (arr.length > index) {
			return arr[index];
		}
		return "";
	}
	
	public static List<OptionCell> getPersonalWeight() {
		List<OptionCell> datas = new ArrayList<OptionCell>();
		for (int i = 30; i < 200; i++) {
			OptionCell cell = new OptionCell();
			cell.setId(i + "");
			cell.setName(i + "kg");
			datas.add(cell);
		}
		return datas;
	}
	
	public static List<OptionCell> getPersonalNation(Context context) {
		String[] arr = context.getResources().getStringArray(R.array.personal_nation);
		List<OptionCell> datas = new ArrayList<OptionCell>();
		for (int i = 0; i < arr.length; i++) {
			OptionCell cell = new OptionCell();
			cell.setId(i + "");
			cell.setName(arr[i]);
			datas.add(cell);
		}
		return datas;
	}
	
	public static String getNationById(Context context, String id) {
		String[] arr = context.getResources().getStringArray(R.array.personal_nation);
		int index = Integer.parseInt(id);
		if (arr.length > index) {
			return arr[index];
		}
		return "";
	}
	
	public static List<OptionCell> getPersonalConstellation(Context context) {
		String[] arr = context.getResources().getStringArray(R.array.personal_constellation);
		List<OptionCell> datas = new ArrayList<OptionCell>();
		for (int i = 0; i < arr.length; i++) {
			OptionCell cell = new OptionCell();
			cell.setId(i + "");
			cell.setName(arr[i]);
			datas.add(cell);
		}
		return datas;
	}
	
	public static String getConstellationById(Context context, String id) {
		String[] arr = context.getResources().getStringArray(R.array.personal_constellation);
		int index = Integer.parseInt(id);
		if (arr.length > index) {
			return arr[index];
		}
		return "";
	}
	
	public static List<OptionCell> getPersonalBloodType(Context context) {
		String[] arr = context.getResources().getStringArray(R.array.personal_blood_type);
		List<OptionCell> datas = new ArrayList<OptionCell>();
		for (int i = 0; i < arr.length; i++) {
			OptionCell cell = new OptionCell();
			cell.setId(i + "");
			cell.setName(arr[i]);
			datas.add(cell);
		}
		return datas;
	}
	
	public static String getBloodTypeById(Context context, String id) {
		String[] arr = context.getResources().getStringArray(R.array.personal_blood_type);
		int index = Integer.parseInt(id);
		if (arr.length > index) {
			return arr[index];
		}
		return "";
	}
	
}
