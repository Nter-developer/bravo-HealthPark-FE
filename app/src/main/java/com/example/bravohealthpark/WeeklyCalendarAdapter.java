package com.example.bravohealthpark;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

public class WeeklyCalendarAdapter extends RecyclerView.Adapter<WeeklyCalendarAdapter.SmallCalendarViewHolder>{

    ArrayList<LocalDate> dayList;

    public WeeklyCalendarAdapter(ArrayList<LocalDate> dayList) {
        this.dayList = dayList;
    }

    @NonNull
    @Override
    public SmallCalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_weekly_caldendar_list, parent, false);

        return new SmallCalendarViewHolder(view);
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull SmallCalendarViewHolder holder, int position) {

        //날짜 변수에 담기
        LocalDate day = dayList.get(position);


        if(day == null){
            holder.dayText.setText("");
        }else{
            holder.dayText.setText(String.valueOf(day.getDayOfMonth()));

            /*현재 날짜 색상 칠하기
            if(day.equals(SmallCalendarUtil.selectedDate)){
                holder.parentView_2.setBackgroundColor(Color.LTGRAY);
            }*/
        }

        //텍스트 색상 지정

        /*
        if( (position + 1) % 7 == 0){ //토요일 파랑

            holder.dayText.setTextColor(Color.BLUE);

        }else if( position == 0 || position % 7 == 0){ //일요일 빨강

            holder.dayText.setTextColor(Color.RED);
        }
        */
        //날짜 클릭 이벤트
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                /*
                int iYear = day.getYear();//년
                int iMonth = day.getMonthValue(); //월
                int iDay = day.getDayOfMonth(); //일

                String yearMonDay = iYear + "년 " + iMonth + "월 " + iDay + "일";

                Toast.makeText(holder.itemView.getContext(), yearMonDay, Toast.LENGTH_SHORT).show();
                 */
            }

        });

    }

    @Override
    public int getItemCount() {
        return dayList.size();
    }

    class SmallCalendarViewHolder extends RecyclerView.ViewHolder{

        TextView dayText;

        TextView dateText;

        View parentView_2;

        public SmallCalendarViewHolder(@NonNull View itemView) {
            super(itemView);

            dayText = itemView.findViewById(R.id.day);
            dateText = itemView.findViewById(R.id.date);

            parentView_2 = itemView.findViewById(R.id.week_cardview);
        }
    }
}