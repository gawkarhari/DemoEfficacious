package com.mobi.efficacious.demoefficacious.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.mobi.efficacious.demoefficacious.R;

import java.util.ArrayList;
import java.util.HashMap;

public class StudentResultFragment extends Fragment {
    View myview;
    Toolbar toolbar;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    private GoogleApiClient client;
    String Standard_id,role_id,Year_id,Division_id,Student_id,Stud_name,Exam_selected_name,Exam_id,StandatdName,DivisionName;;
    int std_,div_;
    String Subject_id,Subject_name,Subject_marks;
    TextView standardname,divisionname;
    ArrayAdapter adapter;

    ListView listview;
    String value;
    HashMap<Object, Object> map;

    private ArrayList<HashMap<Object, Object>> dataList;
    private ArrayList<HashMap<Object, Object>> dataList3;
    Spinner spinnerexamname,STudentName;
    int resultStatus=1,total_marks=0,OutOfFinalMarks;
    int marks,Total;
    String selected_studentName,school_id;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview=inflater.inflate(R.layout.student_result_fragment,null);
        value = "StudentResult";
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        role_id = settings.getString("TAG_USERTYPEID", "");
        Year_id = settings.getString("TAG_ACADEMIC_ID", "");
        school_id=settings.getString("TAG_SCHOOL_ID", "");
        dataList = new ArrayList<HashMap<Object, Object>>();
        spinnerexamname=(Spinner)myview.findViewById(R.id.spinnerexamname) ;
        listview = (ListView) myview.findViewById(R.id.resultListview);
        standardname = (TextView) myview.findViewById(R.id.stdtv);
        STudentName = (Spinner) myview.findViewById(R.id.nametv);
        divisionname = (TextView) myview.findViewById(R.id.divtv);
        dataList3 = new ArrayList<HashMap<Object, Object>>();
        if(role_id.contentEquals("1")||role_id.contentEquals("2"))
        {
            DivisionName=settings.getString("TAG_DivisionName", "");
            StandatdName=settings.getString("TAG_StandardName", "");
            Standard_id = settings.getString("TAG_STANDERDID", "");
            Division_id=settings.getString("TAG_DIVISIONID", "");
            Student_id=settings.getString("TAG_USERID", "");
            Stud_name=settings.getString("TAG_NAME", "");
            standardname.setText(StandatdName);
            divisionname.setText(DivisionName);
        }
        STudentName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selected_studentName= String.valueOf(dataList3.get(position).get("Name"));
                Student_id= String.valueOf(dataList3.get(position).get("Student_id"));

//                ExamAsync
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerexamname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Exam_selected_name= String.valueOf(dataList.get(position).get("Exam_Name"));
                Exam_id= String.valueOf(dataList.get(position).get("Exam_id"));
                if(Exam_id.contentEquals("--Select Exam--")||Student_id.contentEquals("--Select Name--"))
                {
                    Toast.makeText(getActivity(),"Please Select Proper Data",Toast.LENGTH_SHORT).show();
                }else
                {
//                    ResultDeclareAsync
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        StudentNameAsync ();
        return myview;
    }


}