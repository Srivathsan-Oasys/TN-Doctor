package com.oasys.digihealth.doctor;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiSelectionSpinner extends AppCompatSpinner implements
    DialogInterface.OnMultiChoiceClickListener {

  String[] _items = null;
  boolean[] mSelection = null;
  String dropdownName = "";
  Context mcontext;


  ArrayAdapter<String> simple_adapter;
  ArrayList<String> selectedITem = new ArrayList<String>();
  private int sbLength;

  public MultiSelectionSpinner(Context context) {
    super(context);
    mcontext=context;
    simple_adapter = new ArrayAdapter<String>(context,
        android.R.layout.simple_spinner_item);
    super.setAdapter(simple_adapter);
  }

  public MultiSelectionSpinner(Context context, AttributeSet attrs) {
    super(context, attrs);
    mcontext=context;
    simple_adapter = new ArrayAdapter<String>(context,
        android.R.layout.simple_spinner_item);
    super.setAdapter(simple_adapter);
  }

  public void onClick(DialogInterface dialog, int which, boolean isChecked) {
    if (mSelection != null && which < mSelection.length) {
      mSelection[which] = isChecked;
      simple_adapter.clear();
      if (buildSelectedItemString().length() > 0) {
        simple_adapter.add(buildSelectedItemString());
      } else {
        simple_adapter.add("Select "+dropdownName);
      }
    }
  }

  public void setChecked(int position){
    mSelection[position-1] = true;
    simple_adapter.clear();
    if (buildSelectedItemString().length() > 0) {
      simple_adapter.add(buildSelectedItemString());
    } else {
      simple_adapter.add("Select "+dropdownName);
    }
  }

  public void clear(){
    simple_adapter.clear();
    for (int i = 0; i < _items.length; ++i) {
      if (mSelection[i]) {
        mSelection[i] = false;
      }
    }

    if (buildSelectedItemString().length() > 0) {
      simple_adapter.add(buildSelectedItemString());
    } else {
      simple_adapter.add("Select "+dropdownName);
    }
  }

  @Override
  public boolean performClick() {
    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
    builder.setMultiChoiceItems(_items, mSelection, this);

    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface arg0, int arg1) {
      }

    });

    builder.show();
    return true;
  }

  @Override
  public void setAdapter(SpinnerAdapter adapter) {
    throw new RuntimeException(
        "setAdapter is not supported by MultiSelectSpinner.");
  }

  public void setItems(List<String> items, String hintName) {
    dropdownName =hintName;
    _items = items.toArray(new String[items.size()]);
    mSelection = new boolean[_items.length];
    simple_adapter.clear();
    simple_adapter.add("Select "+dropdownName);
    Arrays.fill(mSelection, false);
  }

  public void setSelection(String[] selection) {
    for (String cell : selection) {
      for (int j = 0; j < _items.length; ++j) {
        if (_items[j].equals(cell)) {
          mSelection[j] = true;
        }
      }
    }
  }

  public void setSelection(List<String> selection) {
    for (int i = 0; i < mSelection.length; i++) {
      mSelection[i] = false;
    }
    for (String sel : selection) {
      for (int j = 0; j < _items.length; ++j) {
        if (_items[j].equals(sel)) {
          mSelection[j] = true;
        }
      }
    }
    simple_adapter.clear();
    simple_adapter.add(buildSelectedItemString());
    simple_adapter.notifyDataSetChanged();
  }

  public void setSelection(int index) {
    for (int i = 0; i < mSelection.length; i++) {
      mSelection[i] = false;
    }
    if (index >= 0 && index < mSelection.length) {
      mSelection[index] = true;
    }
    simple_adapter.clear();
    simple_adapter.add(buildSelectedItemString());
    simple_adapter.notifyDataSetChanged();
  }

  public void setSelection(int[] selectedIndicies) {
    for (int i = 0; i < mSelection.length; i++) {
      mSelection[i] = false;
    }
    for (int index : selectedIndicies) {
      if (index >= 0 && index < mSelection.length) {
        mSelection[index] = true;
      }
    }
    simple_adapter.clear();
    simple_adapter.add(buildSelectedItemString());
    simple_adapter.notifyDataSetChanged();
  }

  public String buildSelectedItemString() {
    StringBuilder sb = new StringBuilder();
    boolean foundOne = false;

    for (int i = 0; i < _items.length; ++i) {
      if (mSelection[i]) {

        if (foundOne) {
          sb.append(", ");
        }
        foundOne = true;

        sb.append(_items[i]);
      }
    }
    sbLength = sb.length();


    return sb.toString();
  }

  public ArrayList<String> getSelectedITem()
  {
    selectedITem.clear();
    for (int i = 0; i < _items.length; ++i) {
      if (mSelection[i]) {
        selectedITem.add(_items[i]);
      }
    }

    return selectedITem;
  }
}